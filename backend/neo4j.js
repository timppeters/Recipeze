const { json } = require('body-parser');
const neo4j = require('neo4j-driver')

const driver = neo4j.driver(
    'neo4j://18.217.24.212',
    neo4j.auth.basic('neo4j', process.env.NEO4J_PASSWORD)
  )

var session;

function startSession() {
    session = driver.session({database: 'neo4j'})
    console.log('Database session created')
}


function cypher(query, params) {
    
    return session
    .run(query, params)
    .then(result => {
      return result.records
    })
    .catch(error => {
      console.log(error)
    })
}


async function createUser(username, email) {
    let result = {}
    let records = await cypher(`CREATE (n:User {username: $username, email: $email})`,
    {
        username,
        email
    });
    return result
    
}

async function readUser(username) {
    let result = {}
    let records = await cypher(`MATCH (n:User {username: $username}) RETURN properties(n)`,
    {
        username
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties(n)')))
          })
    }
    return result
    
}

async function updateUser(username, updates) {
    let result = {}
    let records = await cypher(`MATCH (n:User {username: $username}) SET n += $updates`,
    {
        username,
        updates: JSON.parse(updates)
    });
    return result
    
}
async function deleteUser(username) {
    let result = {}
    let records = await cypher(`MATCH (n:User {username: $username}) DETACH DELETE n`,
    {
        username
    });
    return result  
}

async function createRecipe(username, title, description, ingredients, instructions, rating, images) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}) CREATE (n:Recipe {title: $title, description: $description, ingredients: $ingredients, instructions: $instructions, rating: $rating, images: $images, creationTime: datetime()})<-[:AUTHOR_OF]-(u) RETURN ID(n)`,
    {
        username,
        title,
        description,
        ingredients,
        instructions,
        rating,
        images
    });
    if (records) {
        records.forEach(record => {
            result['id'] = record.get('ID(n)')
          })
    }
    return result 
}

async function readRecipe(recipeId) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe), (:User)-[r:RATED]->(n) WHERE ID(n)=$recipeID RETURN properties(n), avg(r.rating) as rating`,
    {
        recipeId
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties(n)')))
            result['rating'] = record.get('rating')
          })
    }
    return result
    
}

async function updateRecipe(recipeId, updates) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe) WHERE ID(n)=$recipeId SET n += $updates`,
    {
        recipeId,
        updates: JSON.parse(updates)
    });
    return result
}

async function deleteRecipe(recipeId) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe) WHERE ID(n)=$recipeId DETACH DELETE n`,
    {
        recipeId
    });
    return result  
}

async function createForumPost(username, title, body, tag) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (t:Tag {name: $tag}) CREATE (u)-[:AUTHOR_OF]->(f:Forum_Post {title: $title, body: $body})-[:IN]->(t)`,
    {
        username,
        title,
        body,
        tag
    });
    return result
    
}

async function readForumPost(postId) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post), (n)<-[:IN]-(c:Comment), (c)<-[:AUTHOR_OF]-(u:User) WHERE ID(n)=$postId RETURN properties(n), collect({body: c.body, author: u.username}) as comments`,
    {
        postId
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties(n)')))
            result['comments'] = record.get('comments')
          })
    }
    return result
    
}

async function updateForumPost(postId, updates) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post) WHERE ID(n)=$postId SET n += $updates`,
    {
        postId,
        updates: JSON.parse(updates)
    });
    return result
}

async function deleteForumPost(postId) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post) WHERE ID(n)=$postId DETACH DELETE n`,
    {
        postId
    });
    return result  
}


// CREATE CONVERSATION
// MATCH (n:User {username: "test1"}), (n2:User {username: "test2"}) CREATE (n)-[:PART_OF]->(:Conversation)<-[:PART_OF]-(n2)

// FOLLOW A USER
// MATCH (n:User {username: "test1"}), (n2:User {username: "test2"}) CREATE (n)-[:FOLLOWS]->(n2)

// SEND A MESSAGE
// MATCH (n:User {username: "test1"})-[:PART_OF]->(c:Conversation)<-[:PART_OF]-(User {username: "test2"}) CREATE (n)-[:SENT]->(m:Message {content: "Hey"})-[:IN]->(c)

// Like a recipe
// MATCH (u:User {username: "test2"}), (r:Recipe) WHERE ID(r)=5 CREATE (u)-[:LIKED]->(r)

// Rate a recipe
// MATCH (u:User {username: "test2"}), (r:Recipe) WHERE ID(r)=5 CREATE (u)-[:RATED {rating: 4.5, review: "Good soup!"}]->(r)

// Create a forum post
// MATCH (u:User {username: "test1"}), (t:Tag {name: "Gluten Free"}) CREATE (u)-[:AUTHOR_OF]->(f:Forum_Post {title: "Gluten Free test", body: "this is a forum for the gluten free tag"})-[:IN]->(t)

// Like a forum post
// MATCH (u:User {username: "test1"}), (f:Forum_Post) WHERE ID(f)=7 CREATE (u)-[:LIKED]->(f)

// Comment on a forum post
// MATCH (u:User {username: "test1"}), (f:Forum_Post) WHERE ID(f)=7 CREATE (u)-[:AUTHOR_OF]->(c:Comment {body: "Great place to have discussions!"})-[:IN]->(f)

async function exitHandler() {
    console.log('handling exit')
    await session.close()
    await driver.close()
    process.exit();
}


//catches ctrl+c event
process.on('SIGINT', exitHandler.bind());
// catches "kill pid"
process.on('SIGUSR1', exitHandler.bind());
process.on('SIGUSR2', exitHandler.bind());
//catches uncaught exceptions
process.on('uncaughtException', exitHandler.bind());

exports.startSession = startSession;
exports.createUser = createUser;
exports.readUser = readUser;
exports.updateUser = updateUser;
exports.deleteUser = deleteUser;
exports.createRecipe = createRecipe;
exports.readRecipe = readRecipe;
exports.updateRecipe = updateRecipe;
exports.deleteRecipe = deleteRecipe;
exports.createForumPost = createForumPost;
exports.readForumPost = readForumPost;
exports.updateForumPost = updateForumPost;
exports.deleteForumPost = deleteForumPost;
