const express = require('express')
const morgan = require('morgan');
const bodyParser = require('body-parser');
const compress = require('compression');
const passport = require('passport');
const routes = require('./api/routes');
const neo4j = require('./neo4j')
const marked = require('marked')
const fs = require('fs')
//const strategies = require('./passport');

const app = express()
const port = process.env.PORT | 8080

// request logging. dev: console | production: file
app.use(morgan('dev'));

// parse body params and attach them to req.body
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// gzip compression
app.use(compress());

// enable authentication
app.use(passport.initialize());
//passport.use('jwt', strategies.jwt);
//passport.use('facebook', strategies.facebook);
//passport.use('google', strategies.google);

app.use('/api', routes);

marked.options({
  breaks: true
})

app.get('/', (req, res) => {
  let path = __dirname + '/readme.md';
  let file = fs.readFileSync(path, 'utf8');
  res.send("<style>body { font-family: Arial;} pre { background: #f8f8f8;padding: 6px; overflow-wrap: break-word;}</style>\n" +
   marked(file.toString()));
})

neo4j.startSession();

app.listen(port, () => {
  console.info(`Example app listening at http://localhost:${port}`)
})