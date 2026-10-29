# PS034: CraveDash Mobility Services
## Complete Review 1 Evaluation & Defense Guide (Target: 5/5 in All Rubrics)

---

## 📌 Project Overview
- **System**: Multi-Restaurant Food Ordering & Delivery Orchestration Engine
- **Company**: CraveDash Mobility Services
- **Architecture**: Spring Boot 3.2.x Microservices Mesh with Netflix Eureka & Spring Cloud Gateway
- **Security**: JWT Authentication (HMAC-SHA256) with Role-Based Access Control
- **Database**: PostgreSQL (`mydb`, Port: 5432, User: `postgres`, Password: `root`)
- **Operations Focus**: Direct PostgreSQL **Insertion** and **Deletion**
- **IDE Compatibility**: 100% Spring Tool Suite (STS) 4 & Eclipse compatible

---

## 🛠️ Step-by-Step: How to Import into Spring Tool Suite (STS) / Eclipse

1. Launch **Spring Tool Suite 4** (or Eclipse).
2. Go to top menu: **File $\rightarrow$ Import...**
3. Expand **Maven** and click **Existing Maven Projects** $\rightarrow$ Click **Next**.
4. In **Root Directory**, browse to:
   `C:\Users\msroh\.gemini\antigravity\scratch\cravedash-engine`
5. STS will automatically detect the parent `pom.xml` and all 6 microservices:
   - `cravedash-parent`
   - `eureka-server`
   - `api-gateway`
   - `auth-service`
   - `restaurant-service`
   - `order-service`
   - `payment-service`
6. Click **Finish**. STS will import and index all projects cleanly without any compilation or Lombok errors.

---

## 🚀 How to Run the System

### Option A: 1-Click Launch (Recommended)
Simply double-click the file:
`C:\Users\msroh\.gemini\antigravity\scratch\cravedash-engine\run-all.bat`
*This starts Eureka first, waits for initialization, launches Auth, Restaurant, Payment, and Order services, starts the Gateway on port 8080, and opens the frontend in your browser automatically!*

### Option B: Running within STS / Eclipse
Run the applications in this exact sequence:
1. **`eureka-server`** $\rightarrow$ Right-click `EurekaServerApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8761`)
2. **`auth-service`** $\rightarrow$ Right-click `AuthServiceApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8081`)
3. **`restaurant-service`** $\rightarrow$ Right-click `RestaurantServiceApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8082`)
4. **`payment-service`** $\rightarrow$ Right-click `PaymentServiceApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8084`)
5. **`order-service`** $\rightarrow$ Right-click `OrderServiceApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8083`)
6. **`api-gateway`** $\rightarrow$ Right-click `ApiGatewayApplication.java` $\rightarrow$ **Run As $\rightarrow$ Spring Boot App** (Port: `8080`)
7. **Frontend**: Double-click `frontend/index.html` to open the web portal.

---

## 🏆 How to Present in Review 1 to Score 5/5 in All Rubrics

### 1. Rubric 1: Problem Analysis and Requirement Specification (Score: 5/5)
- **What to Explain**:
  *"CraveDash connects three stakeholders: hungry consumers, restaurant partners, and delivery/payment fulfillments. Because mealtime orders cause heavy concurrent surges, we separated the domain into autonomous bounded contexts: Auth, Dynamic Menus, Workflow State Orchestration, and Tokenized Payments."*
- **What to Show**:
  - Show the **Review 1 Rubrics tab** in the web application.
  - Explain the order state transitions: `PENDING` $\rightarrow$ `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED`.

---

### 2. Rubric 2: Microservice Identification and Service Discovery (Score: 5/5)
- **What to Explain**:
  *"We have 6 isolated microservices. Downstream microservices do not hardcode IP addresses or port numbers. Instead, each service dynamically registers with our Netflix Eureka Discovery Registry. When Order Service needs Restaurant or Payment data, it uses Spring Cloud OpenFeign with client-side load balancing (`lb://`)."*
- **What to Show**:
  - Open **[http://localhost:8761](http://localhost:8761)** in the browser to show the live Eureka Dashboard.
  - Point to the table **"Instances currently registered with Eureka"**:
    * `AUTH-SERVICE`
    * `RESTAURANT-SERVICE`
    * `ORDER-SERVICE`
    * `PAYMENT-SERVICE`
    * `API-GATEWAY`

---

### 3. Rubric 3: JWT Authentication (Score: 5/5)
- **What to Explain**:
  *"We use stateless, cryptographically signed JSON Web Tokens (HMAC-SHA256). Passwords are never stored in plain text; they are encrypted using BCrypt. The token payload contains user claims (`email`, `role`, `userId`, `exp`). We enforce Role-Based Access Control distinguishing between `CUSTOMER` and `RESTAURANT_OWNER`."*
- **What to Show**:
  - In the web app header, click **"JWT Claims Inspector"**.
  - Show the encoded token (`header.payload.signature`) and show the decoded claims:
    ```json
    {
      "sub": "owner@cravedash.com",
      "role": "RESTAURANT_OWNER",
      "userId": 2,
      "fullName": "Chef Sanjeev (Partner)",
      "exp": 1727099200
    }
    ```
  - Switch roles between Customer and Restaurant Owner to demonstrate RBAC.

---

### 4. Rubric 4: API Gateway Configuration (Score: 5/5)
- **What to Explain**:
  *"Clients connect exclusively to a single reverse-proxy entrypoint: Spring Cloud API Gateway on port 8080. The Gateway dynamically routes requests using Eureka (`lb://auth-service`, `lb://restaurant-service`, etc.). A global `JwtGatewayFilter` inspects incoming HTTP requests, verifies token signatures, and enriches downstream headers with `X-User-Email` and `X-User-Role`."*
- **What to Show**:
  - Point out that all requests in the Network tab go to `http://localhost:8080/api/...`
  - Show `api-gateway/src/main/resources/application.yml` and `JwtGatewayFilter.java`.

---

## 🗄️ Demonstrating PostgreSQL (mydb) Insertion & Deletion Live

### 1. Showcase INSERTION:
1. In the web portal, switch to the **Partner Portal** tab.
2. Click **"Add New Dish (INSERT)"**.
3. Fill in dish name: `Chicken Tikka Saffron Roll`, price: `₹249`, category: `Starters`.
4. Click **"Execute INSERT INTO menu_items"**.
5. The toast confirms the row was inserted, and the new dish instantly appears on the menu and the table!
6. Go to the **Database Monitor** tab to see the live row added to `public.menu_items`.

### 2. Showcase DELETION:
1. In the **Partner Portal** table, find any dish and click the red **Trash/Delete icon**.
2. A confirmation modal appears displaying the exact SQL:
   `DELETE FROM menu_items WHERE id = ?;`
3. Click **"Yes, Execute DELETE"**.
4. The dish is permanently deleted from the database and vanishes from the menu!
5. In the **Orders & Tracking** tab, click **"Cancel & Delete Order"** on an active order to demonstrate deletion from `orders` and `order_items` tables with payment refunding!

---

## 💻 PostgreSQL Verification via Terminal

You can run this command at any time to verify the live database rows in `mydb`:

```powershell
$env:PGPASSWORD="root"
& "E:\postgres sql\bin\psql.exe" -U postgres -h localhost -p 5432 -d mydb -c "SELECT id, name, price, category, is_available FROM menu_items;"
```
