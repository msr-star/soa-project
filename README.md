# CraveDash Mobility Services 🍔🍕

> **Distributed Multi-Restaurant Food Ordering & Delivery Orchestration Mesh**  
> **Course / Review Project**: SOA & Microservices Architecture (`PS034` / `24SDCS03R`)  
> **Development Team**: Team HungryDevs

---

## 📌 Project Overview

**CraveDash Mobility Services** is an enterprise-grade food ordering and delivery orchestration engine designed using a **Service-Oriented Architecture (SOA)** and **Spring Cloud Microservices Mesh**. It decouples complex food delivery workflows into autonomous services with centralized perimeter security, dynamic service discovery, inter-service load balancing, and atomic transactional persistence in PostgreSQL.

---

## 🏗️ System Architecture

```
                                 [ Browser Client (Single Page App) ]
                                                  │
                                                  │ HTTP + Bearer JWT
                                                  ▼
                                   ┌─────────────────────────────┐
                                   │  Spring Cloud API Gateway   │
                                   │         (Port 8080)         │
                                   │    • JwtGatewayFilter       │
                                   │    • Header Enrichment      │
                                   │    • Dynamic Route Predicate│
                                   └──────────────┬──────────────┘
                                                  │
                      ┌───────────────────────────┼───────────────────────────┐
                      │ lb://                     │ lb://                     │ lb://
                      ▼                           ▼                           ▼
       ┌────────────────────────────┐ ┌───────────────────────────┐ ┌───────────────────────────┐
       │        Auth Service        │ │    Restaurant Service     │ │       Order Service       │
       │        (Port 8081)         │ │        (Port 8082)        │ │        (Port 8083)        │
       │ • User Accounts & RBAC     │ │ • Menus, Dishes, Stock    │ │ • Order State Machine     │
       │ • BCrypt & JWT Minting     │ │ • SQL Insert & Delete     │ │ • OpenFeign Coordinator   │
       └──────────────┬─────────────┘ └─────────────┬─────────────┘ └─────────────┬─────────────┘
                      │                             │                             │
                      │                             │                             │ OpenFeign
                      │                             │                             ▼
                      │                             │               ┌───────────────────────────┐
                      │                             │               │      Payment Service      │
                      │                             │               │        (Port 8084)        │
                      │                             │               │ • Transaction Ledger      │
                      │                             │               │ • Authorize Charges       │
                      │                             │               └─────────────┬─────────────┘
                      │                             │                             │
                      └─────────────────────────────┼─────────────────────────────┘
                                                    │
                                                    ▼
                                    ┌─────────────────────────────┐
                                    │    PostgreSQL 16 (mydb)     │
                                    │         (Port 5432)         │
                                    └─────────────────────────────┘

       📡 Discovery Registry: Netflix Eureka Server (Port 8761) manages dynamic leases for all services.
```

---

## 🧩 Microservices Breakdown

| Service | Port | Description |
|---|:---:|---|
| **`eureka-server`** | `8761` | Dynamic service registry with heartbeat lease management. Eliminates hardcoded IP/port endpoints. |
| **`api-gateway`** | `8080` | Unified perimeter reverse proxy. Validates JWT HMAC-SHA256 signatures, enriches downstream headers, and enforces RBAC. |
| **`auth-service`** | `8081` | Handles user registration, BCrypt password hashing, and role-based cryptographic JWT token generation. |
| **`restaurant-service`**| `8082` | Manages restaurant profiles, multi-category digital menus, real-time stock toggling (In Stock / Sold Out), and SQL CRUD. |
| **`order-service`** | `8083` | Manages cart checkout, order state lifecycle (`PENDING` $\rightarrow$ `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `DELIVERED`), and Feign inter-service calls. |
| **`payment-service`** | `8084` | Simulated financial ledger, transaction authorization, and refund management. |

---

## 🛡️ Security & Authentication

- **Stateless Tokens**: Signed using `HMAC-SHA256` containing claims (`sub`, `role`, `userId`, `fullName`, `exp`).
- **BCrypt Hashing**: Zero plaintext passwords in PostgreSQL.
- **Role-Based Access Control (RBAC)**:
  - `CUSTOMER`: Read menus, place orders, track personal orders.
  - `RESTAURANT_OWNER`: Restricted to managing inventory for their assigned kitchen (`INSERT` & `DELETE`).
  - `ADMIN`: Global kitchen switcher, PostgreSQL `mydb` monitor, and system mesh governance.

---

## 🗄️ Database Architecture (PostgreSQL `mydb`)

The system uses 6 relational tables with enforced foreign key cascades:
- `users`: User identity, roles, and password hashes.
- `restaurants`: Kitchen profiles, cuisine types, ratings, and owner email mappings.
- `menu_items`: Dish catalog with Veg 🟢 / Non-Veg 🔴 tags, price, and real-time stock status.
- `orders`: Order header, delivery address, payment method, and state machine status.
- `order_items`: Order line items capturing snapshot price and quantity.
- `payments`: Immutable payment transaction ledger and reference tokens.

---

## 💻 Tech Stack

- **Backend**: Java 17 LTS, Spring Boot 3.2.x, Spring Cloud 2023.0.x (Netflix Eureka, Spring Cloud Gateway, OpenFeign, Spring Data JPA)
- **Database**: PostgreSQL 16 (`mydb`)
- **Frontend**: Single-Page Application (HTML5, Modern CSS3 Custom Tokens, Pure Vanilla ES6)
- **Build Tool**: Apache Maven (Parent Multi-Module POM)

---

## 🚀 How to Run the Project

### Prerequisites
- Java JDK 17+
- PostgreSQL 16 running on Port 5432 (Database: `mydb`, User: `postgres`, Password: `root`)

### Option 1: 1-Click Launch (Windows)
Double-click:
```bash
run-all.bat
```
*This starts Eureka first, initializes Auth, Restaurant, Payment, and Order services, launches the Gateway on port 8080, and opens the frontend at `http://localhost:3000`.*

### Option 2: Run via Spring Tool Suite (STS) / Eclipse
Import as **Existing Maven Projects** and run the `*Application.java` classes in this order:
1. `eureka-server` (Port `8761`)
2. `auth-service` (Port `8081`)
3. `restaurant-service` (Port `8082`)
4. `payment-service` (Port `8084`)
5. `order-service` (Port `8083`)
6. `api-gateway` (Port `8080`)
7. Open `frontend/index.html` in browser.

---

## 👥 Default Demo Credentials

| Role | Email | Password |
|---|---|---|
| **Customer** | `customer@cravedash.com` | `customer123` |
| **Restaurant Partner** | `owner@cravedash.com` | `owner123` |
| **System Administrator** | `admin@cravedash.com` | `admin123` |
