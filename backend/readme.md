# API Documentation & Formats

## Endpoints
#### All parameters should be included in the request body as JSON

- ### Create a user 
    - POST /api/user 
    - ```
      {
        "username": "example",
        "email": "example@example.com"
      }
      ```
- ### Read a user 
    - GET /api/user 
    - ```
      {
        "username": "example",
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
- ### Delete a user 
    - DELETE /api/user 
    - ```
      {
        "username": "example",
      }
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
        }"

      }
      ```

- ### Read a recipe 
    - GET /api/recipe 
    - ```
      {
        "recipeId": "1234",
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
            }"
        }
      }
      ```

- ### Delete a recipe 
    - DELETE /api/recipe 
    - ```
      {
        "recipeId": "1234",
      }
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

- ### Read a Forum Post  
    - GET /api/forum 
    - ```
      {
        "postId": "1234",
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

- ### Delete a Forum Post  
    - DELETE /api/forum 
    - ```
      {
        "postId": "1234",
      }
      ```