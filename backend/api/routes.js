const express = require('express');
const db = require('../neo4j');
const authentication = require('../auth/authentication');
const authorisation = require('../auth/authorisation');


const router = express.Router();

router.use(authorisation(false));

router.route('/requesttokenemail')
    // Send an email with a login token
    .post(async (req, res) => {
      authentication.sendEmail(req.body.email, res);
    });

router.route('/tokensignin')
    // Receive token and verify it with self, google, or fb
    .post(async (req, res) => {
      if (req.body.type == "google") { // verify with google
        authentication.googleVerifyToken(req.body.token, res)
      }
      else if (req.body.type == "facebook") { // verify with fb
        authentication.facebookVerifyToken(req.body.token, res)
      }
      else if (req.body.type == "email") { // verify with self (email magic link)
        authentication.emailVerifyToken(req.body.token, res)
      }
      else {
        res.status(401).send("Need to provide body TYPE parameter")
      }

    });

router.route('/setUsername')
    .post( async (req, res) => {
      result = await db.setUsername(req.body.username, req.email);
      if (result['invalid']) {
        res.status(400).send(result);
      } else {
        authentication.createToken(req.email, res);
      }
    });

router.route('/user')
    // Read private profile
    .get(async (req, res) => {
      result = await db.readUser(req.username)
      res.send(result)
    })
    // Update
    .put(async (req, res) => {
      result = await db.updateUser(req.username, req.body.updates)
      res.send(result)
    })

router.route('/publicUser')
    // Read
    .get(async (req, res) => {
      result = await db.readUserPublic(req.query.username)
      res.send(result)
    })

router.route('/recipe')
    // Create
    .post(async (req, res) => {
      result = await db.createRecipe(req.username, req.body.title, req.body.description, req.body.ingredients, req.body.ingredientsAmounts, req.body.instructions, req.body.images, req.body.tags, req.body.prepTime, req.body.cookTime)
      res.send(result)
    })
    // Read
    .get(async (req, res) => {
      result = await db.readRecipe(req.query.recipeId)
      res.send(result)
    })
    // Update
    .put(async (req, res) => {
      result = await db.updateRecipe(req.body.recipeId, req.body.updates, req.username)
      res.send(result)
    })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteRecipe(req.body.recipeId, req.username)
      res.send(result)
    })

router.route('/forum')
    // Create
    .post(async (req, res) => {
      result = await db.createForumPost(req.username, req.body.title, req.body.body, req.body.tag)
      res.send(result)
      })
    // Read
    .get(async (req, res) => {
      result = await db.readForumPost(req.query.postId)
      res.send(result)
      })
    // Update
    .put(async (req, res) => {
      result = await db.updateForumPost(req.body.postId, req.body.updates, req.username)
      res.send(result)
      })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteForumPost(req.body.postId, req.username)
      res.send(result)
      })

router.route('/posts')
      // Get
      .get(async (req, res) => {
        result = await db.getForumPostsByTag(req.query.tag)
        res.send(result)
      })

router.route('/like')
    // Create
    .post(async (req, res) => {
      if (req.recipeId) { // like a recipe
        result = await db.likeRecipe(req.username, req.body.recipeId)
        res.send(result)
      } else { // like a forum post
        result = await db.likePost(req.username, req.body.postId)
        res.send(result)
      }
      })
    // Delete
    .delete(async (req, res) => {
      if (req.recipeId) { // unlike a recipe
        result = await db.unlikeRecipe(req.username, req.body.recipeId)
        res.send(result)
      } else { // unlike a forum post
        result = await db.unlikePost(req.username, req.body.postId)
        res.send(result)
      }
      })

router.route('/comment')
    // Create
    .post(async (req, res) => {
      result = await db.createComment(req.username, req.body.postId, req.body.body)
      res.send(result)
      })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteComment(req.body.commentId, req.username)
      res.send(result)
      })

router.route('/recipes')
    // Read
    .get(async (req, res) => { 
        switch (req.query.for) {
          case "feedUser":
            result = await db.getRecipesForFeedByUsers(req.username, req.query.filters, req.query.sortBy, req.query.skip)
            res.send(result)
            break;
          
          case "feedTag":
            result = await db.getRecipesForFeedByTags(req.username, req.query.filters, req.query.sortBy, req.query.skip)
            res.send(result)
            break;

          case "explore":
            result = await db.getRecipesForExplore(req.query.skip)
            res.send(result)
            break;

          case "recipe book":
            result = await db.getRecipesForRecipeBook(req.username, req.query.filters, req.query.sortBy, req.query.skip)
            res.send(result)
            break;

          case "profile":
            result = await db.getRecipesForProfile(req.query.username, req.query.skip)
            res.send(result)
            break;

          case "tag":
            result = await db.getRecipesForTag(req.query.tagName, req.query.skip)
            res.send(result)
            break;

          default:
            res.send("Must specify FOR query parameter!")
        }
      })

router.route('/follow')
    // follow someone
    .post(async (req, res) => {
      result = await db.follow(req.username, req.body.followUser)
      res.send(result)
      })

    // unfollow someone
    .delete(async (req, res) => {
      result = await db.unfollow(req.username, req.body.unfollowUser)
      res.send(result)
      })

router.route('/tag')
    // Create a tag
    .post(async (req, res) => {
      result = await db.createTag(req.body.tagName)
      res.send(result)
      })

    // Get tags for explore page
    .get(async (req, res) => {
        if (req.query.type == "all") {
          result = await db.getAllTags()
          res.send(result)
        } else {
          result = await db.getTop5Tags()
          res.send(result)
        }
    })

    // Delete a tag
    .delete(async (req, res) => {
      result = await db.deleteTag(req.body.tagName)
      res.send(result)
      })

router.route('/rate')
    // Create
    .post(async (req, res) => {
      result = await db.rate(req.username, req.body.recipeId, req.body.rating, req.body.review)
      res.send(result)
    })
    // Get recipe reviews
    .get(async (req, res) => {
      result = await db.getRatings(req.query.recipeId)
      res.send(result)
    })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteRating(req.username, req.body.recipeId)
      res.send(result)
    })

router.route('/search')
    .get(async (req, res) => {
      result = await db.search(req.query.query)
      res.send(result)
    });
module.exports = router;