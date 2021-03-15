const jwt = require('jsonwebtoken');
const db = require('../neo4j');
const {OAuth2Client} = require('google-auth-library');
const googleOAuthClient = new OAuth2Client(process.env.GOOGLE_CLIENT_ID);
const got = require('got');
const sgMail = require('@sendgrid/mail')
const CryptoJS = require("crypto-js");

sgMail.setApiKey(process.env.SENDGRID_API_KEY);

async function createToken(email, res) {
    console.log("creatingtoken for ", email)
    newAccount = false;

    // Check if user with email exists
    user = await db.readUserByEmail(email);

    if (Object.keys(user) == 0) { // register new user without username
        newAccount = true;
        db.createUser(email);
        user.admin = false;
        user.username = "";
    } else if (user.email && !user.username) {
        newAccount = true;
    }

    // create a token
    let token = jwt.sign({ username: user.username, email: email, admin: user.admin }, process.env.JWTSecret, {
        expiresIn: 86400 // expires in 24 hours
      });
    res.status(200).send({ auth: true, token: token, newAccount: newAccount });
}


async function googleVerifyToken(token, res) {
    try {
        const ticket = await googleOAuthClient.verifyIdToken({
            idToken: token,
            audience: process.env.GOOGLE_CLIENT_ID,  // Specify the CLIENT_ID of the app that accesses the backend
        });
        let payload = ticket.getPayload();
        let email = payload['email'];
        createToken(email, res);
    } catch(e) {
        res.status(401).send({auth: false, message: 'Login failed.'});
    }
}

async function facebookVerifyToken(token, res) {
    try {
        let response = await got(`https://graph.facebook.com/me?fields=email&access_token=${token}`);
        let email = JSON.parse(response.body)['email'];
        if (!email) {
            throw new Error("Invalid token")
        }
        createToken(email, res);
    } catch(e) {
        console.log(e)
        res.status(401).send({auth: false, message: 'Login failed.'});
    }
}

async function emailVerifyToken(token, res) {
    try {
        let buffer = Buffer.from(token, 'hex');
        let emailTokenBase64 = buffer.toString('base64');
        let bytes  = CryptoJS.AES.decrypt(emailTokenBase64, process.env.JWTSecret);
        let emailToken = JSON.parse(bytes.toString(CryptoJS.enc.Utf8));
        let email = emailToken['email'];
        let timestamp = emailToken['timestamp'];
        // CHECK TIMESTAMP IS LESS THAN 1 HOUR OLD
        if (!email || Date.now() - int(timestamp) > (60 * 60 * 1000)) {
            throw new Error("Invalid token")
        }
        createToken(email, res);
    } catch(e) {
        console.log(e)
        res.status(401).send({auth: false, message: 'Login failed.'});
    }
}

async function sendEmail(email, res) {

    let emailToken = CryptoJS.AES.encrypt(JSON.stringify({email, "timestamp": Date.now()}), process.env.JWTSecret).toString();
    let buffer = Buffer.from(emailToken, 'base64');
    let emailTokenHex = buffer.toString('hex');

    let msg = {
        to: email,
        from: 'login@recipeze.tk',
        templateId: 'd-913d224aa33f41afbf5e18d9926688a7',
    
        dynamic_template_data: {
          link: 'https://recipeze.tk/emailsignin/'+encodeURI(emailTokenHex)
        },
      };

    console.log('https://recipeze.tk/emailsignin/'+encodeURI(emailTokenHex));
    
    sgMail
      .send(msg)
      .then(() => {
        res.status(200).send({emailSent: true})
      })
      .catch((error) => {
        res.status(500).send({emailSent: false});
        console.error(error)
      })

}

exports.emailVerifyToken = emailVerifyToken;
exports.facebookVerifyToken = facebookVerifyToken;
exports.googleVerifyToken = googleVerifyToken;
exports.sendEmail = sendEmail;
exports.createToken = createToken;