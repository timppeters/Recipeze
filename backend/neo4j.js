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


async function createUser(username, email) { // Should not be public. Only used when registering
    let result = {}
    let records = await cypher(`CREATE (n:User {username: $username, email: $email})`,
    {
        username,
        email
    });
    return result
    
}

async function readUser(username) { // Return private profile.
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

async function readUserPublic(username) { // Return public profile. Need to return followers as well?
    let result = {}
    let records = await cypher(`MATCH (n:User {username: $username}) RETURN {username: n.username, bio: n.bio} as properties`,
    {
        username
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties')))
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

async function createRecipe(username, title, description, ingredients, instructions, rating, images, tags) {
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
            result['recipeId'] = record.get('ID(n)')
          })
    }

    // Add tags to recipe
    for (tag in tags) {
        await cypher(`MATCH (r:Recipe), (t:Tag {name: $tag}) WHERE ID(r)=$recipeID CREATE (r)-[:HAS]->(t)`,
    {
        tag,
        recipeId: parseInt(result['recipeId'])
    });
    }

    return result 
}

async function readRecipe(recipeId) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe), (:User)-[r:RATED]->(n), (n)-[:HAS]->(t:Tag), (a:User)-[:AUTHOR_OF]->(n) WHERE ID(n)=$recipeId WITH properties(n) as properties, collect(DISTINCT t.name) as tags, avg(r.rating) as rating, a.username as author RETURN properties{.*, tags: tags, rating: rating, author: author}`,
    {
        recipeId: parseInt(recipeId)
    });
    if (records) {
        records.forEach(record => {
            result = record.get('properties')
          })
    }
    return result
    
}

async function updateRecipe(recipeId, updates) {
    let result = {}
    let tags = updates['tags']
    delete updates['tags']
    let records = await cypher(`MATCH (n:Recipe) WHERE ID(n)=$recipeId SET n += $updates`,
    {
        recipeId: parseInt(recipeId),
        updates: JSON.parse(updates)
    });
    // Add tags to recipe
    for (tag in tags) {
        await cypher(`MATCH (r:Recipe), (t:Tag {name: $tag}) WHERE ID(r)=$recipeID MERGE (r)-[:HAS]->(t)`,
    {
        tag,
        recipeId: parseInt(recipeId)
    });
    }
    return result
}

async function deleteRecipe(recipeId) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe) WHERE ID(n)=$recipeId DETACH DELETE n`,
    {
        recipeId: parseInt(recipeId)
    });
    return result  
}

async function createForumPost(username, title, body, tag) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (t:Tag {name: $tag}) CREATE (u)-[:AUTHOR_OF]->(f:Forum_Post {title: $title, body: $body})-[:IN]->(t) RETURN ID(f)`,
    {
        username,
        title,
        body,
        tag
    });
    if (records) {
        records.forEach(record => {
            result['postId'] = record.get('ID(f)')
          })
    }
    return result
    
}

async function readForumPost(postId) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post)<-[:AUTHOR_OF]-(a:User), (n)<-[:IN]-(c:Comment), (c)<-[:AUTHOR_OF]-(u:User) WHERE ID(n)=$postId WITH properties(n) as properties, collect({body: c.body, author: u.username, id: ID(c)}) as comments, a.username as author RETURN properties{.*, comments: comments, author: author}`,
    {
        postId: parseInt(postId)
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties')))
          })
    }
    return result
    
}

async function updateForumPost(postId, updates) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post) WHERE ID(n)=$postId SET n += $updates`,
    {
        postId: parseInt(postId),
        updates: JSON.parse(updates)
    });
    return result
}

async function deleteForumPost(postId) {
    let result = {}
    let records = await cypher(`MATCH (f:Forum_Post)<-[:IN]-[c:Comment] WHERE ID(f)=$postId DETACH DELETE c,f`,
    {
        postId: parseInt(postId)
    });
    return result  
}

async function likeRecipe(username, recipeId) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (r:Recipe) WHERE ID(r)=$recipeId CREATE (u)-[:LIKED]->(r)`,
    {
        username,
        recipeId: parseInt(recipeId)
    });
    return result
}

async function unlikeRecipe(username, recipeId) {
    let result = {}
    let records = await cypher(`MATCH (:User {username: $username})-[l:LIKED]->(r:Recipe) WHERE ID(r)=$recipeId DELETE l`,
    {
        username,
        recipeId: parseInt(recipeId)
    });
    return result
}

async function likePost(username, postId) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (f:Forum_Post) WHERE ID(f)=$postId CREATE (u)-[:LIKED]->(f)`,
    {
        username,
        postId: parseInt(postId)
    });
    return result
}

async function unlikePost(username, postId) {
    let result = {}
    let records = await cypher(`MATCH (:User {username: $username})-[l:LIKED]->(f:Forum_Post) WHERE ID(f)=$postId DELETE l`,
    {
        username,
        postId: parseInt(postId)
    });
    return result
}

async function createComment(username, postId, body) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (f:Forum_Post) WHERE ID(f)=$postId CREATE (u)-[:AUTHOR_OF]->(c:Comment {body: $body})-[:IN]->(f)`,
    {
        username,
        postId: parseInt(postId),
        body
    });
    return result
}

async function deleteComment(commentId) {
    let result = {}
    let records = await cypher(`MATCH (c:Comment) WHERE ID(c)=$commentId DETACH DELETE c`,
    {
        commentId: parseInt(commentId)
    });
    return result
}

async function follow(username, followUser) {
    let result = {}
    let records = await cypher(`MATCH (n:User {username: $username}), (n2:User {username: $followUser}) CREATE (n)-[:FOLLOWS]->(n2)`,
    {
        username,
        followUser
    });
    return result
}

async function unfollow(username, unfollowUser) {
    let result = {}
    let records = await cypher(`MATCH (:User {username: $username})-[f:FOLLOWS]->(:User {username: $unfollowUser}) DELETE f`,
    {
        username,
        unfollowUser
    });
    return result
}


async function createTag(tagName) {
    let result = {}
    let records = await cypher(`CREATE (:Tag {name: $tagName})`,
    {
        tagName
    });
    return result
}

async function deleteTag(tagName) {
    let result = {}
    let records = await cypher(`MATCH (t:Tag {name: $tagName})<-[:IN]-(f:Forum_Post)<-[:IN]-(c:Comment) DETATCH DELETE c,f,t`,
    {
        tagName
    });
    return result
}

async function rate(username, recipeId, rating, review) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}), (r:Recipe) WHERE ID(r)=$recipeId MERGE (u)-[m:RATED]->(r) ON CREATE SET m.rating=$rating, m.review=$review`,
    {
        username,
        recipeId: parseInt(recipeId),
        rating: parseFloat(rating),
        review
    });
    return result
}

async function getRatings(recipeId) {
    let result = {}
    let records = await cypher(`MATCH (u:User)-[m:RATED]->(r:Recipe) WHERE ID(r)=$recipeId AND m.review IS NOT NULL RETURN collect({rating: m.rating, review: m.review, user: u.username}) as ratings`,
    {
        recipeId
    });
    return result
}

async function deleteRating(username, recipeId) {
    let result = {}
    let records = await cypher(`MATCH (:User {username: $username})-[m:RATED]->(r:Recipe) WHERE ID(r)=$recipeId DELETE m`,
    {
        username,
        recipeId: parseInt(recipeId)
    });
    return result
}


// CREATE CONVERSATION
// MATCH (n:User {username: "test1"}), (n2:User {username: "test2"}) CREATE (n)-[:PART_OF]->(:Conversation)<-[:PART_OF]-(n2)

// SEND A MESSAGE
// MATCH (n:User {username: "test1"})-[:PART_OF]->(c:Conversation)<-[:PART_OF]-(User {username: "test2"}) CREATE (n)-[:SENT]->(m:Message {content: "Hey"})-[:IN]->(c)

// Rate a recipe
// MATCH (u:User {username: "test2"}), (r:Recipe) WHERE ID(r)=5 CREATE (u)-[:RATED {rating: 4.5, review: "Good soup!"}]->(r)


// Ensure that the database session and connection are closed
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
exports.readUserPublic = readUserPublic;
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
exports.likeRecipe = likeRecipe;
exports.unlikeRecipe = unlikeRecipe;
exports.likePost = likePost;
exports.unlikePost = unlikePost;
exports.createComment = createComment;
exports.deleteComment = deleteComment;
exports.follow = follow;
exports.unfollow = unfollow;
exports.createTag = createTag;
exports.deleteTag = deleteTag;
exports.rate = rate;
exports.getRatings = getRatings;
exports.deleteRating = deleteRating;
