# 🚀 Drone Dispatch Service

## 📌 Overview
The **Drone Dispatch Service** is a Spring Boot application that manages a fleet of drones for **medication delivery**. It supports:
- **Registering drones**
- **Loading drones with medication**
- **Checking loaded medication for a drone**
- **Checking available drones for loading**
- **Fetching valid medications**
- **Monitoring battery levels** (via scheduled tasks)
- **Logging battery level history**

---

## ⚙️ Tech Stack
- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA** (with H2 for local, PostgreSQL for production)
- **Liquibase** (for database migrations)
- **JUnit & Testcontainers** (for integration testing)

---

## 🚀 Build, Run & Test Instructions

### **1️⃣ Prerequisites**
Ensure you have the following installed:
- [Java 17](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/install.html)


### **3️⃣ Set Up the Database**
#### **For Local Development (In-Memory H2)**
No setup needed! The application will automatically use **H2 database**.

### **4️⃣ Build the Project**
Run the following command to build the project:
```sh
mvn clean package
```
This will create the JAR file in the `target/` directory.

---

### **5️⃣ Run the Application**
#### **Option 1: Run Locally (Using H2 Database)**
```sh
mvn spring-boot:run
```

#### **Option 2: Run the JAR File**
```sh
java -jar target/drone-dispatcher-0.0.1-SNAPSHOT.jar
```

### **6️⃣ Running Tests**
#### **Unit Tests**
```sh
make test-unit
```
#### **Integration Tests (With Testcontainers)**
```sh
make test
```
This will start a **Testcontainer**, run the integration tests, and shut down the container automatically.

---

## 📄 API Endpoints
| Method | Endpoint                      | Description                         |
|--------|-------------------------------|-------------------------------------|
| `POST` | `/api/drone`                  | Register a new drone                |
| `POST` | `/api/drone/{id}/load`        | Load a drone with medication        |
| `GET` | `/api/drone/{id}/medications` | Check loaded medication for a drone |
| `GET` | `/api/drone/available`        | Get available drones for loading    |
| `GET` | `/api/drone/{id}/battery`     | Check battery level of a drone      |
| `GET` | `api/v1/drone/loaded/{id}`    | Get loading/loaded drone by id      |

---

## 📄 Swagger doc url

http://localhost:8083/swagger-ui/index.html