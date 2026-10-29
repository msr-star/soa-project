# 🚀 CraveDash Mobility Services — Teammate Setup Guide
> **Course**: 24SDCS03R - SOA PROGRAMMING AND MICROSERVICES  
> **Team**: HungryDevs  
> **Project**: PS034 Multi-Restaurant Food Ordering & Delivery Orchestration Engine

Welcome! Follow this **2-minute quick-start guide** to run the complete microservices project on your computer without any errors.

---

## 📋 1. Prerequisites on Your Computer
* **Java**: JDK 17 or higher (Installed with STS or standalone).
* **IDE**: Eclipse IDE or Spring Tool Suite (STS) 4.
* **Database**: PostgreSQL (Port 5432).

---

## 🗄️ 2. PostgreSQL Setup (Takes 60 Seconds)

1. Open **pgAdmin** or **psql** command line:
   ```sql
   CREATE DATABASE mydb;
   ```
2. Populate the 3 restaurants and 26 dishes by running the included `populate_menu.sql` script:
   * **In pgAdmin**: Open Query Tool on `mydb` $\rightarrow$ Open file `populate_menu.sql` $\rightarrow$ Click Execute (▶).
   * **OR in Terminal**:
     ```powershell
     psql -U postgres -d mydb -f populate_menu.sql
     ```
3. **Database Password Note**:
   * The project defaults to password: `root`.
   * If your local PostgreSQL password is `postgres` (or something else), simply open `application.yml` in each service and change `password: root` to your password, or set an environment variable `SPRING_DATASOURCE_PASSWORD`.

---

## 📥 3. How to Import into Eclipse / Spring Tool Suite (STS)

1. Open **Eclipse** or **Spring Tool Suite**.
2. Click **File** $\rightarrow$ **Import...**
3. Select **Maven** $\rightarrow$ **Existing Maven Projects** $\rightarrow$ Click **Next**.
4. In **Root Directory**, browse to this cloned folder (`cravedash-engine`).
5. Eclipse will automatically detect 7 projects:
   * `[x] /pom.xml (cravedash-parent)`
   * `[x] eureka-server/pom.xml`
   * `[x] api-gateway/pom.xml`
   * `[x] auth-service/pom.xml`
   * `[x] restaurant-service/pom.xml`
   * `[x] order-service/pom.xml`
   * `[x] payment-service/pom.xml`
6. Click **Finish**. Wait for Eclipse to build the workspace.

---

## 🚀 4. How to Run

### Option A: 1-Click Launch (Easiest)
Simply double-click the file:
```text
run-all.bat
```
This automatically starts Eureka, waits for it to initialize, launches all 5 microservices, and opens the frontend in your browser!

### Option B: Running inside Eclipse / STS
Run the main classes in this exact order:
1. `eureka-server` $\rightarrow$ `EurekaServerApplication.java` (Port 8761)
2. `api-gateway` $\rightarrow$ `ApiGatewayApplication.java` (Port 8080)
3. `auth-service` $\rightarrow$ `AuthServiceApplication.java` (Port 8081)
4. `restaurant-service` $\rightarrow$ `RestaurantServiceApplication.java` (Port 8082)
5. `order-service` $\rightarrow$ `OrderServiceApplication.java` (Port 8083)
6. `payment-service` $\rightarrow$ `PaymentServiceApplication.java` (Port 8084)

---

## 🌐 5. Open the Web Application

1. Double-click `start-frontend.bat` (or double-click `frontend/index.html`).
2. Open your browser to:
   * **Eureka Registry**: `http://localhost:8761` (Verify all 5 services say UP).
   * **Web Application**: `http://localhost:3000` (or `file:///.../frontend/index.html`).

---

## 🛠️ Common Errors & Quick Fixes

| Issue | Cause | Fix |
| :--- | :--- | :--- |
| `FATAL: password authentication failed for user "postgres"` | Your PostgreSQL password is not `root` | Change `password: root` to your password in `application.yml` across services |
| `database "mydb" does not exist` | Database not created | In psql/pgAdmin, run: `CREATE DATABASE mydb;` |
| `Port 8080 is already in use` | Old process or another app using port 8080 | Close the existing process or run `Get-Process -Name java \| Stop-Process -Force` in PowerShell |
