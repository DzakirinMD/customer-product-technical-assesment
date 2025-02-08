# Customer Order Management System

A **Spring Boot-based microservices application** that enables customers to place orders, receive email notifications, and earn loyalty points. It follows **event-driven architecture** using **Kafka** and **WebFlux** for communication.

## Architecture Overview
This project consists of **three microservices**:
1. **Order Management Service**  
   - Handles order creation and retrieval.
   - Publishes an event to Kafka after an order is placed.

2. **Email Service**  
   - Listens for order events from Kafka.
   - Sends confirmation emails to customers.

3. **Loyalty Service**  
   - Listens for order events from Kafka.
   - Processes loyalty points based on order amount.
   - Calls Email Service (via WebFlux) if points are awarded.

---

## Tech Stack
| Technology              | Purpose |
|-------------------------|---------|
| **Java 17+**            | Programming Language |
| **Spring Boot 3.3.x**   | Application Framework |
| **Spring WebFlux**      | Reactive Programming (Loyalty Service) |
| **Spring Data JPA**     | Database Interaction |
| **Kafka**               | Event Streaming |
| **OpenAPI (SpringDoc)** | API Documentation |
| **PostgreSQL**          | Relational Database |
| **Groovy & Spock**      | Unit Testing |
| **Maven**               | Dependency Management |
| **GitHub Actions**      | CI/CD Pipeline |

---

## Microservices and Data Flow
### 1️⃣ Order Management Service
✅ **Handles orders**  
✅ **Publishes events to Kafka**  
✅ **Stores orders in PostgreSQL**  

🚀 **Flow**:
- Customer places an order.
- Order details are **saved** in the database.
- A **Kafka event is published** (`ORDER_CREATED` topic).
- Email Service and Loyalty Service consume this event.

### 2️⃣ Email Service
✅ **Consumes Kafka events**  
✅ **Sends confirmation emails to customers**  

🚀 **Flow**:
- Listens for `ORDER_CREATED` event.
- Sends an **email confirmation** to the customer.

### 3️⃣ Loyalty Service
✅ **Processes loyalty points**  
✅ **Calls Email Service (via WebFlux) if points are awarded**  

🚀 **Flow**:
- Listens for `ORDER_CREATED` event.
- Checks **loyalty rules**:
  - If the **order amount meets the threshold**, points are awarded.
  - Calls Email Service via **WebFlux** to notify the customer.
  - If no points are awarded, logs the event.

---

## Database Schema
### Order Management Service
| Table Name | Description |
|------------|------------|
| `customers` | Stores customer details. |
| `products` | Stores product information. |
| `orders` | Stores orders linked to customers. |
| `order_products` | Many-to-many mapping between orders and products. |

### Loyalty Service
| Table Name | Description |
|------------|------------|
| `loyalty_points` | Stores customer points balance. |
| `loyalty_transactions` | Tracks loyalty transactions (earned/redeemed points). |
| `loyalty_rules` | Defines rules for awarding points. |

---

## Kafka Topics
| Topic Name | Description |
|------------|-------------|
| `ORDER_CREATED` | Published by Order Management Service when an order is created. |
| `LOYALTY_POINTS_AWARDED` | Published when loyalty points are granted (for future enhancements). |

---

## Setup and Running the Services
### 1️⃣ Clone the Repository
```sh
git clone https://github.com/DzakirinMD/template-be.git
cd template-be
```

### 2️⃣ Start the Services
Run all services using **Docker Compose**:
```sh
mvn clean package
docker-compose up --build -d
```

### 3️⃣ Stop the Services
```sh
docker-compose down
```

### 4️⃣ Reset Dev Environment
```sh
docker-compose down -v  # Removes volumes
rm -rf docker-data      # Deletes all stored data
```

---

## API Documentation
- **Kafka UI**: [http://localhost:18080](http://localhost:18080)    

---

## Kafka UI Setup
1. Open [Kafka UI](http://localhost:18080).
2. Click **Configure New Cluster**.
3. Set **Cluster Name** = `Localhost`.
4. Set **Bootstrap Servers**:
   - `template-be-kafka:9092`
5. Click **Validate**, then **Submit**.

---

## Testing
### 1️⃣ Run Unit Tests
```sh
mvn test
```

### 2️⃣ Run Integration Tests
```sh
mvn verify
```

### 3️⃣ GitHub Actions
✅ Runs unit tests automatically on **Pull Requests (PRs)**.

---

## Expected Behavior
### 📩 Email Notifications
- When an order is placed, an email is sent:  
  **"Your order has been created successfully!"**
- If loyalty points are awarded, another email is sent:  
  **"You've earned X loyalty points!"**

### 📝 Logging (If No Points Are Awarded)
```
[INFO] LoyaltyService: No points awarded for Order ID: XYZ
```

---

## Services & URLs
| Service | Swagger URL |
|---------|------------|
| Order Management | [http://localhost:10001/swagger-ui.html](http://localhost:10001/swagger-ui.html) |
| Email Service | [http://localhost:10002/swagger-ui.html](http://localhost:10002/swagger-ui.html) |
