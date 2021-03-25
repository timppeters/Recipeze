const neo4j = require('neo4j-driver')

const driver = neo4j.driver(
    'neo4j://18.217.24.212',
    neo4j.auth.basic('neo4j', process.env.NEO4J_PASSWORD)
  )


function cypher(query, params) {
    let session = driver.session({database: 'neo4j'})
    return session
    .run(query, params)
    .then(async result => {
      await session.close()
      return result.records
    })
    .catch(error => {
      console.log(error)
    })
}


async function createUser(email) { // Should not be public. Only used internally when registering
    let result = {}
    let records = await cypher(`MATCH (u:User {username: "Recipeze"}) CREATE (n:User {username: "", email: $email, bio: "", admin: false, settings: ""})-[:FOLLOWS]->(u)`,
    {
        email
    });
    return result
    
}

async function setUsername(username, email) {
    let result = {}
    let usernameConflicts = await cypher(`MATCH (n:User {username: $username}) RETURN n.email`,
    {
        username,
    });
    if (usernameConflicts.length) {
        result['invalid'] = true;
    } else {
        let records = await cypher(`MATCH (n:User {email: $email}) SET n.username=$username`,
        {
            username,
            email
        });
    }
    
    return result
    
}

async function readUserByEmail(email) { // For login/register
    let result = {}
    let records = await cypher(`MATCH (n:User {email: $email}) RETURN {username: n.username, admin: n.admin} as data`,
    {
        email
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('data')))
          })
    }
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

async function createRecipe(username, title, description, ingredients, ingredientsAmounts, instructions, images, tags, prepTime, cookTime) {
    let result = {}
    let records = await cypher(`MATCH (u:User {username: $username}) CREATE (n:Recipe {title: $title, description: $description, ingredients: $ingredients, ingredientsAmounts: $ingredientsAmounts, instructions: $instructions, images: $images, prepTime: $prepTime, cookTime: $cookTime, creationTime: datetime()})<-[:AUTHOR_OF]-(u) RETURN ID(n)`,
    {
        username,
        title,
        description,
        ingredients: JSON.parse(ingredients),
        ingredientsAmounts: JSON.parse(ingredientsAmounts),
        instructions,
        images,
        prepTime,
        cookTime
    });
    if (records) {
        records.forEach(record => {
            result['recipeId'] = record.get('ID(n)')['low']
          })
    }

    // Add tags to recipe
    tags = JSON.parse(tags)
    for (index in tags) {
        await cypher(`MATCH (r:Recipe), (t:Tag {name: $tag}) WHERE ID(r)=$recipeId CREATE (r)-[:HAS]->(t)`,
        {
            tag: tags[index],
            recipeId: parseInt(result['recipeId'])
        });
    }

    return result 
}

async function readRecipe(recipeId, username) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe), (n)-[:HAS]->(t:Tag), (a:User)-[:AUTHOR_OF]->(n) WHERE ID(n)=$recipeId OPTIONAL MATCH (:User)-[r:RATED]->(n) WITH EXISTS((:User {username: $username})-[:LIKED]->(n)) as liked, properties(n) as properties, collect(DISTINCT t.name) as tags, avg(r.rating) as rating, a.username as author, size((n)<-[:LIKED]-(:User)) as likes RETURN properties{.*, tags: tags, rating: rating, author: author, likes: likes, liked: liked}`,
    {
        recipeId: parseInt(recipeId),
        username
    });
    if (records) {
        records.forEach(record => {
            result = record.get('properties')
            result['recipeId'] = recipeId;
            result['likes'] = result['likes']['low']
            result['cookTime'] = result['cookTime']['low']
            result['prepTime'] = result['prepTime']['low']
            result['instructions'] = JSON.parse(result['instructions'])
            result['images'] = JSON.parse(result['images'])
          })
    }
    return result
    
}

async function updateRecipe(recipeId, updates, username) {
    let result = {}
    let tags = updates['tags']
    delete updates['tags']
    let records = await cypher(`MATCH (n:Recipe)<-[:AUTHOR_OF]-(:User {username: $username}) WHERE ID(n)=$recipeId SET n += $updates`,
    {
        recipeId: parseInt(recipeId),
        updates: JSON.parse(updates),
        username
    });
    // Add tags to recipe
    tags = JSON.parse(tags)
    for (index in tags) {
        await cypher(`MATCH (r:Recipe), (t:Tag {name: $tag}) WHERE ID(r)=$recipeID MERGE (r)-[:HAS]->(t)`,
    {
        tag: tags[index],
        recipeId: parseInt(recipeId)
    });
    }
    return result
}

async function deleteRecipe(recipeId, username) {
    let result = {}
    let records = await cypher(`MATCH (n:Recipe)<-[:AUTHOR_OF]-(:User {username: $username}) WHERE ID(n)=$recipeId DETACH DELETE n`,
    {
        recipeId: parseInt(recipeId),
        username
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
            result['postId'] = record.get('ID(f)')['low']
          })
    }
    return result
    
}

async function readForumPost(postId) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post)<-[:AUTHOR_OF]-(a:User) OPTIONAL MATCH (n)<-[:IN]-(c:Comment), (c)<-[:AUTHOR_OF]-(u:User) WHERE ID(n)=$postId WITH properties(n) as properties, collect({body: c.body, author: u.username, commentId: ID(c)}) as comments, a.username as author, size((n)<-[:LIKED]-(:User)) as likes RETURN properties{.*, comments: comments, author: author, likes: likes}`,
    {
        postId: parseInt(postId)
    });
    if (records) {
        records.forEach(record => {
            result = JSON.parse(JSON.stringify(record.get('properties')))
            result['likes'] = result['likes']['low']
            for (index in result['comments']) {
                result['comments'][index]['commentId'] = result['comments'][index]['commentId']['low']
            }
          })
    }
    return result
    
}

async function getForumPostsByTag(tagName) {
    let result = {}
    let records = await cypher(`MATCH (t:Tag {name: $tagName})<-[:IN]-(n:Forum_Post)<-[:AUTHOR_OF]-(a:User) WITH properties(n) as properties, a.username as author, size((n)<-[:LIKED]-(:User)) as likes, ID(n) as postId RETURN collect(properties{.*, author: author, likes: likes, postId: postId}) as posts`,
    {
        tagName: tagName
    });
    if (records) {
        records.forEach(record => {
            result['posts'] = JSON.parse(JSON.stringify(record.get('posts')))
            console.log(result)
            result['posts'].forEach( post => {
                post['likes'] = post['likes']['low']
                post['postId'] = post['postId']['low']
            })
            
          })
    }
    return result
    
}

async function updateForumPost(postId, updates, username) {
    let result = {}
    let records = await cypher(`MATCH (n:Forum_Post)<-[:AUTHOR_OF]-(:User {username: $username}) WHERE ID(n)=$postId SET n += $updates`,
    {
        postId: parseInt(postId),
        updates: JSON.parse(updates),
        username
    });
    return result
}

async function deleteForumPost(postId, username) {
    let result = {}
    let records = await cypher(`MATCH (:User {username: $username})-[:AUTHOR_OF]->(f:Forum_Post)<-[:IN]-[c:Comment] WHERE ID(f)=$postId DETACH DELETE c,f`,
    {
        postId: parseInt(postId),
        username
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

async function deleteComment(commentId, username) {
    let result = {}
    let records = await cypher(`MATCH (c:Comment)<-[:AUTHOR_OF]-(:User {username: $username}) WHERE ID(c)=$commentId DETACH DELETE c`,
    {
        commentId: parseInt(commentId),
        username
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

async function getAllTags() {
    let result = {}
    let records = await cypher(`MATCH (t:Tag) RETURN {tag: t.name, recipes: size((t)<-[:HAS]-(:Recipe))} as tag`,{});
    if (records) {
        records.forEach(record => {
            tag = record.get('tag')
            result[tag["tag"]] = tag["recipes"]["low"] // low because we don't have that many recipes
          })
    }
    return result
}

async function getTop5Tags() {
    let result = {}
    let records = await cypher(`MATCH (t:Tag) WITH t, size((t)<-[:HAS]-(:Recipe)) as num ORDER BY num desc RETURN {tag: t.name, recipes: num} as tag LIMIT 5`,{});
    if (records) {
        records.forEach(record => {
            tag = record.get('tag')
            result[tag["tag"]] = tag["recipes"]["low"] // low because we don't have that many recipes
          })
    }
    return result
}

async function deleteTag(tagName) {
    let result = {}
    let records = await cypher(`MATCH (t:Tag {name: $tagName}) OPTIONAL MATCH (t)<-[:IN]-(f:Forum_Post)<-[:IN]-(c:Comment) DETATCH DELETE c,f,t`,
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
    return {"ratings": result}
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



async function getRecipesForFeedByUsers(username, filters, sortBy, skip) {
    let result = []
    filters = JSON.parse(filters)
    if (sortBy != "likes") {
        sortBy = "r.creationTime"
    }
    let records = await cypher(`MATCH (u:User {username: $username})-[:FOLLOWS]->(a:User)-[:AUTHOR_OF]->(r:Recipe), (r)-[:HAS]->(t:Tag) OPTIONAL MATCH (:User)-[n:RATED]->(r) WHERE r.prepTime+r.cookTime<=$maxTime AND ALL(ingredient IN $ingredients WHERE ingredient IN r.ingredients) AND size(r.ingredients)<=$maxNumberOfIngredients AND ALL(tag IN $tags WHERE exists((r)-[:HAS]->(:Tag {name: tag}))) WITH EXISTS((u)-[:LIKED]->(r)) as liked, r, properties(r) as properties, collect(DISTINCT t.name) as tags, avg(n.rating) as rating, a.username as author, size((r)<-[:LIKED]-(:User)) as likes ORDER BY $sortBy desc SKIP $skip RETURN properties{.*, recipeId: ID(r), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        username,
        maxTime: parseInt(filters.maxTime),
        ingredients: JSON.parse(filters.ingredients),
        maxNumberOfIngredients: parseInt(filters.maxNumberOfIngredients),
        tags: JSON.parse(filters.tags),
        sortBy,
        skip: neo4j.int(skip)
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
    
}

async function getRecipesForFeedByTags(username, filters, sortBy, skip) {
    let result = []
    filters = JSON.parse(filters)
    if (sortBy != "likes") {
        sortBy = "r.creationTime"
    }
    let records = await cypher(`MATCH (u:User {username: $username})-[:FOLLOWS]->(t:Tag)<-[:HAS]-(r:Recipe), (r)<-[:AUTHOR_OF]-(a:User) OPTIONAL MATCH (:User)-[n:RATED]->(r) WHERE r.prepTime+r.cookTime<=$maxTime AND ALL(ingredient IN $ingredients WHERE ingredient IN r.ingredients) AND size(r.ingredients)<=$maxNumberOfIngredients AND ALL(tag IN $tags WHERE exists((r)-[:HAS]->(:Tag {name: tag}))) WITH EXISTS((u)-[:LIKED]->(r)) as liked, r, properties(r) as properties, collect(DISTINCT t.name) as tags, avg(n.rating) as rating, a.username as author, size((r)<-[:LIKED]-(:User)) as likes ORDER BY $sortBy desc SKIP $skip RETURN properties{.*, recipeId: ID(r), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        username,
        maxTime: parseInt(filters.maxTime),
        ingredients: JSON.parse(filters.ingredients),
        maxNumberOfIngredients: parseInt(filters.maxNumberOfIngredients),
        tags: JSON.parse(filters.tags),
        sortBy,
        skip: neo4j.int(skip)
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
    
}

async function getRecipesForExplore(skip, username) {
    let result = []
    let records = await cypher(`MATCH (n:Recipe), (n)-[:HAS]->(t:Tag), (a:User)-[:AUTHOR_OF]->(n) OPTIONAL MATCH (:User)-[r:RATED]->(n) WITH EXISTS((:User {username: $username})-[:LIKED]->(n)) as liked, n, properties(n) as properties, collect(DISTINCT t.name) as tags, avg(r.rating) as rating, a.username as author, size((n)<-[:LIKED]-(:User)) as likes ORDER BY likes SKIP $skip RETURN properties{.*, recipeId: ID(n), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        skip: neo4j.int(skip),
        username
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
}

async function getRecipesForRecipeBook(username, filters, sortBy, skip) {
    let result = []
    filters = JSON.parse(filters)
    if (sortBy != "likes") {
        sortBy = "r.creationTime"
    }
    let records = await cypher(`MATCH (u:User {username: $username})-[:LIKED]->(r:Recipe)<-[:AUTHOR_OF]-(a:User), (r)-[:HAS]->(t:Tag) OPTIONAL MATCH (:User)-[n:RATED]->(r) WHERE r.prepTime+r.cookTime<=$maxTime AND ALL(ingredient IN $ingredients WHERE ingredient IN r.ingredients) AND size(r.ingredients)<=$maxNumberOfIngredients AND ALL(tag IN $tags WHERE exists((r)-[:HAS]->(:Tag {name: tag}))) WITH EXISTS((u)-[:LIKED]->(r)) as liked, r, properties(r) as properties, collect(DISTINCT t.name) as tags, avg(n.rating) as rating, a.username as author, size((r)<-[:LIKED]-(:User)) as likes ORDER BY $sortBy desc SKIP $skip RETURN properties{.*, recipeId: ID(r), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        username,
        maxTime: parseInt(filters.maxTime),
        ingredients: JSON.parse(filters.ingredients),
        maxNumberOfIngredients: parseInt(filters.maxNumberOfIngredients),
        tags: JSON.parse(filters.tags),
        sortBy,
        skip: neo4j.int(skip)
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
    
}

async function getRecipesForProfile(username, skip) {
    let result = []
    let records = await cypher(`MATCH (u:User {username: $username})-[:AUTHOR_OF]->(r:Recipe), (r)-[:HAS]->(t:Tag) OPTIONAL MATCH (:User)-[n:RATED]->(r) WITH EXISTS((u)-[:LIKED]->(r)) as liked, r, properties(r) as properties, collect(DISTINCT t.name) as tags, avg(n.rating) as rating, u.username as author, size((r)<-[:LIKED]-(:User)) as likes ORDER BY r.creationTime desc SKIP $skip RETURN properties{.*, recipeId: ID(r), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        username,
        skip: neo4j.int(skip)
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
    
}

async function getRecipesForTag(tagName, skip, username) {
    let result = []
    let records = await cypher(`MATCH (:Tag {name: $tagName})<-[:HAS]-(r:Recipe)<-[:AUTHOR_OF]-(a:User) OPTIONAL MATCH (:User)-[n:RATED]->(r) MATCH (r)-[:HAS]->(t:Tag) WITH EXISTS((:User {username: $username})-[:LIKED]->(r)) as liked, r, properties(r) as properties, collect(DISTINCT t.name) as tags, avg(n.rating) as rating, a.username as author, size((r)<-[:LIKED]-(:User)) as likes ORDER BY r.creationTime desc SKIP $skip RETURN properties{.*, recipeId: ID(r), tags: tags, rating: rating, author: author, likes: likes, liked: liked} LIMIT 10`,
    {
        tagName,
        skip: neo4j.int(skip),
        username
    });
    if (records) {
        records.forEach(record => {
            recipe = record.get('properties');
            recipe['recipeId'] = recipe['recipeId']['low']
            recipe['likes'] = recipe['likes']['low']
            recipe['cookTime'] = recipe['cookTime']['low']
            recipe['prepTime'] = recipe['prepTime']['low']
            recipe['instructions'] = JSON.parse(recipe['instructions'])
            recipe['images'] = JSON.parse(recipe['images'])
            result.push(recipe);
          })
    }
    return {"recipes": result}
    
}

async function search(query, username) {
    let result = {}
    query = query.toLowerCase()
    let records = await cypher(`OPTIONAL MATCH (u:User) WHERE toLower(u.username) STARTS WITH $query OPTIONAL MATCH (r:Recipe) WHERE toLower(r.title) CONTAINS $query OR toLower(r.description) CONTAINS $query OR $query IN r.ingredients OPTIONAL MATCH (t:Tag) WHERE toLower(t.name) CONTAINS $query WITH collect(DISTINCT t.name) as tags, collect(DISTINCT u.username) as users, properties(r) as p 
    MATCH (r)-[:HAS]->(t2:Tag), (r)<-[:AUTHOR_OF]-(a:User) OPTIONAL MATCH (:User)-[n:RATED]->(r)
    WITH tags, users, p{.*, recipeId: ID(r), tags: collect(DISTINCT t2.name), rating: avg(n.rating), author: a.username, likes: size((r)<-[:LIKED]-(:User)), liked: EXISTS((:User {username: $username})-[:LIKED]->(r)) } as recipe
    RETURN {users: users, tags: tags, recipes: collect(recipe)} as results
    `,
    {
        query,
        username
    });
    if (records) {
        records.forEach(record => {
            result = record.get('results')
            result['recipes'].forEach(recipe => {
                recipe['recipeId'] = recipe['recipeId']['low']
                recipe['likes'] = recipe['likes']['low']
                recipe['cookTime'] = recipe['cookTime']['low']
                recipe['prepTime'] = recipe['prepTime']['low']
                recipe['instructions'] = JSON.parse(recipe['instructions'])
                recipe['images'] = JSON.parse(recipe['images'])
            })
          })
    }
    return result
    
}


// Ensure that the database session and connection are closed
async function exitHandler() {
    console.log('handling exit')        
    if (driver)
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

exports.createUser = createUser;
exports.setUsername = setUsername;
exports.readUser = readUser;
exports.readUserByEmail = readUserByEmail;
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
exports.getAllTags = getAllTags;
exports.getTop5Tags = getTop5Tags;
exports.deleteTag = deleteTag;
exports.rate = rate;
exports.getRatings = getRatings;
exports.deleteRating = deleteRating;
exports.getRecipesForFeedByUsers = getRecipesForFeedByUsers;
exports.getRecipesForFeedByTags = getRecipesForFeedByTags;
exports.getRecipesForExplore = getRecipesForExplore;
exports.getRecipesForRecipeBook = getRecipesForRecipeBook;
exports.getRecipesForProfile = getRecipesForProfile;
exports.getRecipesForTag = getRecipesForTag;
exports.search = search;
exports.getForumPostsByTag = getForumPostsByTag;
