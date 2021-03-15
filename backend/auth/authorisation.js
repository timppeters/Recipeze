const jwt = require('jsonwebtoken');

module.exports = function (admin=False) {

    return function(req, res, next) {
        let token = req.headers['x-access-token'];
        if (!token) {
          if (req.path == "/tokensignin" || req.path == "/requesttokenemail") {
            next();
            return;
          }
          return res.status(403).send({ auth: false, message: 'No token provided.' });
        }
        token = token.replace(/"/g,"");
        jwt.verify(token, process.env.JWTSecret, function(err, decoded) {
          if (err) {
              return res.status(500).send({ auth: false, message: 'Failed to authenticate token.' });
          }
          
          // If route require admin persmissions
          if (admin == decoded.admin) {
            // if everything good, save to request for use in other routes
            req.username = decoded.username;
            req.email = decoded.email;
            next();
            return;
          } else {
              return res.status(401).send({auth: false, message: 'Unauthorised. '});
          }
          
        });
      }
}

