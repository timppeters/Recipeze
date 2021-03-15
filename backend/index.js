const express = require('express');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const compress = require('compression');
const routes = require('./api/routes');
const neo4j = require('./neo4j');
const marked = require('marked');
const fs = require('fs');

const app = express()
const port = process.env.PORT || 44338
const host = '0.0.0.0'

// request logging. dev: console | production: file
app.use(morgan('dev'));

// parse body params and attach them to req.body
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// gzip compression
app.use(compress());

app.use('/api', routes);

marked.options({
  breaks: true
})

app.get('/', (req, res) => {
  let path = __dirname + '/README.md';
  let file = fs.readFileSync(path, 'utf8');
  res.send("<style>body { font-family: Arial;} pre { background: #f8f8f8;padding: 6px; overflow-wrap: break-word;}</style>\n" +
   marked(file.toString()));
})

app.listen(port, host, () => {
  console.info(`API listening at http://localhost:${port}`)
})