# Order Management System

Order Management System built with **Spring Boot**, **Spring Security**, and **Stripe** for real‑time payment processing.

---

## 🚀 Features

- User authentication & authorization (JWT + Spring Security)
- Role‑based access control (ADMIN / USER)
- Product management
- Order lifecycle management (CREATED → PAID)
- Stripe real‑time payment integration
- Secure REST APIs
- Docker & Docker Compose support

---

## 🛠 Tech Stack

- **Backend**: Java, Spring Boot
- **Security**: Spring Security, JWT
- **Database**: PostgreSql (configurable)
- **Payments**: Stripe API
- **Build Tool**: Maven
- **Deployment**: Docker

---

## 📁 Project Structure

```
src/main/java/com/vodica/order_system
├── config        # Security & application config
├── controller    # REST controllers
├── dto           # Request / Response DTOs
├── entity        # JPA entities
├── repository    # Spring Data JPA repositories
├── security      # JWT filter & security logic
├── service       # Business logic
└── exceptions    # Global exception handling
```

---

## ⚙️ Configuration

### application.yml / application.properties

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/order_db
    username: root
    password: password

stripe:
  secret-key: sk_test_xxxxxxxxxxxxxxxxx
```

> ⚠️ Never commit your real Stripe secret key

---

---

## ⚙️ Configuration .env

### Create .env file in root

```.env
DATABASE_USERNAME=database_username
DATABASE_PASSWORD=database_password
JWT_ACCESS_SECRET=base64_32bit_secret_key
JWT_REFRESH_SECRET=base64_32bit_secret_key
JWT_EXP=3600000 // 1hr example
JWT_REFRESH_EXP=604800000 // 7days example
STRIPE_SECRET_KEY=sk_test_xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
STRIPE_WEBHOOK_SECRET=whsec_xxxxxxxxxx
```

---

## ▶️ Running the Application

### Local

```bash
mvn clean install
mvn spring-boot:run
```

Application will start at:

```
http://localhost:8080
```

---

### Docker

```bash
docker compose up --build
```

---

## 🔐 Authentication APIs

### Register

`POST /api/auth/sign-up`

```json
{
  "email": "user@example.com",
  "password": "password123",
  "name": "Your_FULL_NAME",
  "address": "YOUR_ADDRESS"
 
}
```

---

### Login

`POST /api/auth/sign-in`

`For test Admin login`

```json
{
  "email": "admin@example.com",
  "password": "123456"
}
```

**Response**

```json
{
  "status": "success",
  "message": "Login Successfully",
  "data": {
    "user": {
      "email": "user@gmail.com",
      "id": 1021,
      "name": "user",
      "role": "USER"
    },
    "tokens": {
      "accessToken": "access_token_xxxxxxxxxxxx",
      "refreshToken": "refresh_token_xxxxxxxxxxxxx"
    }
  }
}
```

Use token as:

```
Authorization: Bearer <accessTOKEN>
```

---

## 📦 Product APIs

| Method | Endpoint                   | Description | Role |
|------|----------------------------|------------|------|
| GET | /api/products/lists        | Get all products | USER |
| GET | /api/products/{id}/product | Get product by ID | USER |
| POST | /api/products/create       | Create product | ADMIN |
| PUT | /api/products/{id}/update  | Update product | ADMIN |
| DELETE | /api/products/{id}/delete  | Delete product | ADMIN |

---

## 🧾 Order APIs

| Method | Endpoint                | Description |
|--------|-------------------------|------------|
| POST   | /api/orders             | Create new order |
| GET    | /api/orders/my          | Get all orders |
| GET    | /api/orders/{id}        | Get order by ID |
| PATCH  | /api/orders/{id}/status | Update order status |

---

## 💳 Payment APIs (Stripe)

### Create Payment Intent

`POST /api/orders/{id}/pay`



---

### Stripe Webhook

`POST /api/orders/stripe/webhook`

Handles Stripe payment events and updates order status to **PAID**.

---

## 🧪 Testing

You can test APIs using:

- Postman
- Insomnia
- curl

---

## 🔒 Security Notes

- All protected routes require JWT token
- Admin‑only endpoints are role‑restricted
- Stripe webhook signature verification enabled

---

## 📜 License

This project is open‑source.

---

## 👤 Author

**Kyaw San Oo**  
GitHub: https://github.com/kyawsanoo2364

