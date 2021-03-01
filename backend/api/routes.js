const express = require('express');
const db = require('../neo4j');

const router = express.Router();

router.route('/user')
    // Create
    .post(async (req, res) => {
      result = await db.createUser(req.body.username, req.body.email)
      res.send(result)
    })
    // Read
    .get(async (req, res) => {
      result = await db.readUser(req.body.username)
      res.send(result)
    })
    // Update
    .put(async (req, res) => {
      result = await db.updateUser(req.body.username, req.body.updates)
      res.send(result)
    })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteUser(req.body.username)
      res.send(result)
    })

router.route('/publicUser')
    // Read
    .get(async (req, res) => {
      result = await db.readUserPublic(req.body.username)
      res.send(result)
    })

router.route('/recipe')
    // Create
    .post(async (req, res) => {
      result = await db.createRecipe(req.body.username, req.body.title, req.body.description, req.body.ingredients, req.body.instructions, req.body.images, req.body.tags)
      res.send(result)
    })
    // Read
    .get(async (req, res) => {
      result = await db.readRecipe(req.body.recipeId)
      res.send(result)
    })
    // Update
    .put(async (req, res) => {
      result = await db.updateRecipe(req.body.recipeId, req.body.updates)
      res.send(result)
    })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteRecipe(req.body.recipeId)
      res.send(result)
    })

router.route('/forum')
    // Create
    .post(async (req, res) => {
      result = await db.createForumPost(req.body.username, req.body.title, req.body.body, req.body.tag)
      res.send(result)
      })
    // Read
    .get(async (req, res) => {
      result = await db.readForumPost(req.body.postId)
      res.send(result)
      })
    // Update
    .put(async (req, res) => {
      result = await db.updateForumPost(req.body.postId, req.body.updates)
      res.send(result)
      })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteForumPost(req.body.postId)
      res.send(result)
      })

router.route('/like')
    // Create
    .post(async (req, res) => {
      if (req.recipeId) { // like a recipe
        result = await db.likeRecipe(req.body.username, req.body.recipeId)
        res.send(result)
      } else { // like a forum post
        result = await db.likePost(req.body.username, req.body.postId)
        res.send(result)
      }
      })
    // Delete
    .delete(async (req, res) => {
      if (req.recipeId) { // unlike a recipe
        result = await db.unlikeRecipe(req.body.username, req.body.recipeId)
        res.send(result)
      } else { // unlike a forum post
        result = await db.unlikePost(req.body.username, req.body.postId)
        res.send(result)
      }
      })

router.route('/comment')
    // Create
    .post(async (req, res) => {
      result = await db.createComment(req.body.username, req.body.postId, req.body.body)
      res.send(result)
      })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteComment(req.body.commentId)
      res.send(result)
      })

router.route('/recipes')
    // Read
    .get(async (req, res) => {
        res.send('Get recipes for feed, explore, recipe book, profile')
      })

router.route('/follow')
    // follow someone
    .post(async (req, res) => {
      result = await db.follow(req.body.username, req.body.followUser)
      res.send(result)
      })

    // unfollow someone
    .delete(async (req, res) => {
      result = await db.unfollow(req.body.username, req.body.unfollowUser)
      res.send(result)
      })

router.route('/tag')
    // Create a tag
    .post(async (req, res) => {
      result = await db.createTag(req.body.tagName)
      res.send(result)
      })

    // Delete a tag
    .delete(async (req, res) => {
      result = await db.deleteTag(req.body.tagName)
      res.send(result)
      })

router.route('/rate')
    // Create
    .post(async (req, res) => {
      result = await db.rate(req.body.username, req.body.recipeId, req.body.rating, req.body.review)
      res.send(result)
    })
    // Get recipe reviews
    .get(async (req, res) => {
      result = await db.getRatings(req.body.recipeId)
      res.send(result)
    })
    // Delete
    .delete(async (req, res) => {
      result = await db.deleteRating(req.body.username, req.body.recipeId)
      res.send(result)
    })


module.exports = router;