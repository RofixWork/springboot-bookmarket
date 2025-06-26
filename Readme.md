# 📚 Spring Boot Book Market

A simple shopping cart system for managing books — built using Java, Spring Boot, and H2. This project simulates a basic book store backend with cart functionality — ideal for educational purposes and beginner practice in Spring Boot.

---

## 🚀 Features

- 📘 Manage Books (Add, List, Delete, Update)
- 🛒 Shopping Cart (Add/Remove Books)
- 💸 Dynamic cart subtotal updates
- 🧪 Built-in validations and error handling
- 💡 Clear separation of concerns (DTOs, Services, Repositories)
- 🛢️ In-memory H2 database

---

## 📦 Tech Stack

| Layer        | Technology     |
|--------------|----------------|
| Language     | Java 17        |
| Backend      | Spring Boot 3  |
| Database     | H2             |
| Build Tool   | Maven          |
| Mapping      | ModelMapper    |
| API Tool     | Postman/Curl   |

---

## 🛠️ How to Run the Project

1. **Clone the repo**
   ```bash
   git clone https://github.com/RofixWork/springboot-bookmarket.git
   cd springboot-bookmarket
````

2. **Run the app**

   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access H2 DB Console**

   ```
   URL: http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:testdb
   ```

---

## 🔗 API Endpoints

### 📘 Book

| Method | Endpoint          | Description    |
| ------ | ----------------- | -------------- |
| GET    | `/api/books`      | Get all books  |
| POST   | `/api/books`      | Add a new book |
| DELETE | `/api/books/{id}` | Delete a book  |

### 🛒 Cart

| Method | Endpoint                             | Description                   |
| ------ | ------------------------------------ | ----------------------------- |
| POST   | `/api/cart/{cartId}/add/{bookId}`    | Add book to cart              |
| DELETE | `/api/cart/{cartId}/remove/{bookId}` | Remove book from cart         |
| GET    | `/api/cart/{cartId}`                 | View cart contents & subtotal |

---

## ✅ Example Request (Postman / Curl)

```http
POST /api/books
Content-Type: application/json

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "price": 39.99
}
```

---

## 📂 Project Structure

```
springboot-bookmarket/
├── controller
├── dto
├── model
├── repository
├── service
└── config
```

---

## 🧪 Future Improvements

* Add authentication with Spring Security
* Create user-specific carts
* Pagination and sorting
* Unit and integration testing

---

## 👨‍💻 Author

Made with ❤️ by [RofixWork](https://github.com/RofixWork)

---

## 📄 License

This project is for learning purposes and is not licensed for commercial use.

