
### **API Documentation**

#### **Base URL**: `/api/v1`

---

### **Endpoints**

#### **1. Borrow a Book**

- **Endpoint**: `POST /api/v1/users/{id}/borrow`
- **Description**: Allows a user to borrow a book. A user cannot borrow more than 3 books.
- **Headers**:
  - Content-Type: `application/json`
- **Request Body**:
  ```json
  {
    "bookId": 1
  }
  ```
- **Response**:
  - `200 OK`: Book borrowed successfully.
  - `400 Bad Request`: If the user has already borrowed 3 books or the book is unavailable.
  - `404 Not Found`: If the user or book is not found.

---

#### **2. Return a Book**

- **Endpoint**: `POST /api/v1/users/{id}/return`
- **Description**: Allows a user to return a borrowed book.
- **Headers**:
  - Content-Type: `application/json`
- **Request Body**:
  ```json
  {
    "bookId": 1
  }
  ```
- **Response**:
  - `200 OK`: Book returned successfully.
  - `400 Bad Request`: If the user has not borrowed the book.
  - `404 Not Found`: If the user or book is not found.

---

#### **3. Create a New Book**

- **Endpoint**: `POST /api/v1/books`
- **Description**: Adds a new book to the library.
- **Headers**:
  - Content-Type: `application/json`
- **Request Body**:
  ```json
  {
    "title": "The Catcher in the Rye",
    "author": "J.D. Salinger",
    "available": true
  }
  ```
- **Response**:
  - `200 OK`: Book created successfully.

---

#### **4. Create a New User**

- **Endpoint**: `POST /api/v1/users`
- **Description**: Adds a new user to the system.
- **Headers**:
  - Content-Type: `application/json`
- **Request Body**:
  ```json
  {
    "username": "JohnDoe",
    "email": "john.doe@example.com"
  }
  ```
- **Response**:
  - `200 OK`: User created successfully.
  - `400 Bad Request`: If email is missing.

---

#### **5. Get Library Statistics**

- **Endpoint**: `GET /api/v1/users/stats`
- **Description**: Retrieves the total number of users, total books, available books, and borrowed books.
- **Headers**: None
- **Response**:
  ```json
  {
    "totalUsers": 10,
    "totalBooks": 50,
    "availableBooks": 30,
    "borrowedBooks": 20
  }
  ```

---

#### **6. Get All Users with Borrowed Books**

- **Endpoint**: `GET /api/v1/users`
- **Description**: Retrieves all users and their borrowed books.
- **Headers**: None
- **Response**:
  ```json
  [
     {
        "id": 1,
        "username": "JohnDoe",
        "email": "john.doe@example.com",
        "borrowedBooks": [
           {
              "id": 1,
              "title": "The Catcher in the Rye",
              "author": "J.D. Salinger",
              "available": false
           }
        ]
     }
  ]
  ```

---

#### **7. Get a Specific Book**

- **Endpoint**: `GET /api/v1/books/{id}`
- **Description**: Retrieves the details of a specific book by its ID.
- **Headers**: None
- **Response**:
  ```json
  {
    "id": 1,
    "title": "The Catcher in the Rye",
    "author": "J.D. Salinger",
    "available": true
  }
  ```
