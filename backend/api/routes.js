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

router.route('/recipe')
    // Create
    .post(async (req, res) => {
      result = await db.createRecipe(req.body.username, req.body.title, req.body.description, req.body.ingredients, req.body.instructions, req.body.images)
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
        res.send('Add like to recipe/comment')
      })
    // Delete
    .delete(async (req, res) => {
        res.send('Remove like from recipe/comment')
      })

router.route('/comment')
    // Create
    .post(async (req, res) => {
        res.send('Add comment to a post')
      })
    // Read
    .get(async (req, res) => {
        res.send('Read comments of a post')
      })
    // Delete
    .delete(async (req, res) => {
        res.send('Delete comment')
      })

router.route('/recipes')
    // Read
    .get(async (req, res) => {
        res.send('Get recipes for feed, explore, recipe book, profile')
      })

router.route('/follow')
    // follow someone
    .post(async (req, res) => {
        res.send('Follow a user')
      })

    // unfollow someone
    .delete(async (req, res) => {
      res.send('Unfollow a user')
      })

router.route('/tag')
    // Create a tag
    .post(async (req, res) => {
        res.send('Create a tag')
      })

    // Delete a tag
    .delete(async (req, res) => {
      res.send('Delete a tag')
      })

router.route('/rate')
    // Create
    .post(async (req, res) => {
      res.send('Add comment to a post')
    })
    .get(async (req, res) => {
      res.send('Get recipe reviews')
    })
    // Delete
    .delete(async (req, res) => {
      res.send('Delete comment')
    })


module.exports = router;