# API Documentation & Formats

## Endpoints
#### All parameters should be included in the request body as JSON

- ### Create a user 
    - POST /api/user 
    - Body parameters  
    ```
      {
        "username": "example",
        "email": "example@example.com"
      }
      ```
    - Returns  
    ```
    {}
    ```
- ### Read a user's private profile
    - GET /api/user 
    - ```
      {
        "username": "example",
      }
      ```
    - Returns  
    ```
      {
        "username": "example",
        "bio": "This is their bio.",
        "email": "example@example.com",
        "settings": {}
      }
    ```

- ### Read a user's public profile
    - GET /api/publicUser 
    - ```
      {
        "username": "example",
      }
      ```
    - Returns  
    ```
      {
        "username": "example",
        "bio": "This is their bio."
      }
    ```

- ### Update a user 
    - PUT /api/user 
    - ```
      {
        "username": "example",
        "updates": { // Each entry is optional
            "username": "newusername",
            "email": "newemail@example.com",
            "bio": "this is my bio",
            "settings": "{
                "darkMode": False,
                etc..
            }"
        }
      }
      ```
    - Returns  
    ```
      {}
    ```
- ### Delete a user 
    - DELETE /api/user 
    - ```
      {
        "username": "example",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Create a recipe 
    - POST /api/recipe 
    - ```
      {
        "username": "test1",
        "title": "Tomato Soup",
        "description": "This is my lovely tomato soup...",
        "ingredients": ["200g tomatoes", "500ml water"],
        "instructions": "{
            "1": "First step",
            "2": "Second step"
        }",
        "images": "{
            // instructionNumber : base64EncodedImage
            "1": "base64://jsdfur8924y8fhhjhasdasduhfJKHASDFJK"
        }",
        "tags": ["Gluten Free"]

      }
      ```
    - Returns  
    ```
      {
        "recipeId": "1234"
      }
    ```

- ### Read a recipe 
    - GET /api/recipe 
    - ```
      {
        "recipeId": "1234",
      }
      ```
    - Returns  
    ```
      {
        "rating": 4.25
        "author": "test1"
        "title": "Tomato Soup",
        "description": "This is my lovely tomato soup...",
        "ingredients": ["200g tomatoes", "500ml water"],
        "instructions": "{
            "1": "First step",
            "2": "Second step"
        }",
        "images": "{
            // instructionNumber : base64EncodedImage
            "1": "base64://jsdfur8924y8fhhjhasdasduhfJKHASDFJK"
        }",
        "tags": ["Gluten Free"]
      }
    ```

- ### Update a recipe
    - PUT /api/recipe 
    - ```
      {
        "recipeId": "1234",
        "updates": { // Each entry is optional
            "title": "Tomato Soup",
            "description": "This is my lovely tomato soup...",
            "ingredients": ["200g tomatoes", "500ml water"],
            "instructions": "{
                "1": "First step",
                "2": "Second step"
            }",
            "images": "{
                // instructionNumber : base64EncodedImage
                "1": "base64://jsdfur8924y8fhhjhasdasduhfJKHASDFJK"
            }",
            "tags": ["Gluten Free", "Dairy Free"]
        }
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Delete a recipe 
    - DELETE /api/recipe 
    - ```
      {
        "recipeId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Create a Forum Post 
    - POST /api/forum 
    - ```
      {
        "username": "test1"
        "title": "Test",
        "body": "This is my forum post",
        "tag": "Gluten Free"
      }
      ```
    - Returns  
    ```
      {
        "postId": "1234"
      }
    ```

- ### Read a Forum Post  
    - GET /api/forum 
    - ```
      {
        "postId": "1234",
      }
      ```
    - Returns  
    ```
      {
        "author": "test1",
        "comments": [
          {
            "author": "test2",
            "id": 9,
            "body": "Test comment"
          },
          {
            "author": "test1",
            "id": 8,
            "body": "Great place to have discussions!"
          }
        ],
        "title": "Gluten Free test",
        "body": "this is a forum for the gluten free tag"
      }
    ```

- ### Update a Forum Post 
    - PUT /api/forum 
    - ```
      {
        "postId": "1234",
        "updates": { // Each entry is optional
            "title": "Test",
            "body": "This is my forum post",
        }
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Delete a Forum Post  
    - DELETE /api/forum 
    - ```
      {
        "postId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Like a recipe  
    - POST /api/like 
    - ```
      {
        "username": "test"
        "recipeId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Unlike a Recipe  
    - DELETE /api/like 
    - ```
      {
        "username": "test"
        "recipeId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Like a forum post  
    - POST /api/like 
    - ```
      {
        "username": "test"
        "postId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Unlike a forum post  
    - DELETE /api/like 
    - ```
      {
        "username": "test"
        "postId": "1234",
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Add a comment to a forum post  
    - POST /api/comment 
    - ```
      {
        "username": "test"
        "postId": "1234",
        "body": "comment text"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Delete a comment from a forum post  
    - DELETE /api/comment 
    - ```
      {
        "commentId": "1234"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Follow a user  
    - POST /api/follow 
    - ```
      {
        "username": "test",
        "followUser": "test2"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Unfollow a user  
    - DELETE /api/follow 
    - ```
      {
        "username": "test",
        "unfollowUser": "test2"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Create a tag 
    - POST /api/tag 
    - ```
      {
        "tagName": "Dairy Free"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Delete a tag 
    - DELETE /api/tag 
    - ```
      {
        "tagName": "Dairy Free"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Rate a recipe
    - POST /api/rate 
    - ```
      {
        "username": "test",
        "recipeId": "1234",
        "rating": "4"
        "review": "This is a great recipe!" //Optional
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Get ratings/reviews of a recipe (only ones with review text)
    - GET /api/rate 
    - ```
      {
        "recipeId": "1234"
      }
      ```
    - Returns  
    ```
      {}
    ```

- ### Delete a rating
    - DELETE /api/rate 
    - ```
      {
        "username": "test",
        "recipeId": "1234"
      }
      ```
    - Returns  
    ```
      {}
    ```