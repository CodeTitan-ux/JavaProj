---

# 🏦 Nova Bank – Spring Boot Banking Application

Live Demo : https://javaproj-fgmk.onrender.com

Nova Bank is a **full-stack banking web application** built using **Spring Boot, Spring Security, JPA/Hibernate, Thymeleaf, MySQL, and Bootstrap**.
It simulates real-world banking operations such as **user registration, authentication, deposits, withdrawals, fund transfers, and transaction history tracking**.

This project focuses on **secure authentication**, **transaction integrity**, and **clean MVC architecture**, making it an excellent learning and portfolio project.

---

## 🚀 Features

### 👤 User Management

* User Registration with password confirmation
* Secure Login & Logout using **Spring Security**
* Password encryption using **BCrypt**
* Role-based user handling (`USER`)

### 🏦 Account Management

* Automatic account creation upon user registration
* Unique account number generation
* Balance tracking with real-time updates

### 💰 Banking Operations

* Deposit funds
* Withdraw funds with balance validation
* Transfer funds between users
* Atomic transactions using `@Transactional`

### 📜 Transaction Management

* Records **every financial operation**
* Separate entries for:

  * Deposit
  * Withdraw
  * Transfer Out (Debit)
  * Transfer In (Credit)
* Recent transactions shown on dashboard
* Full transaction history page
* Correct credit/debit visualization (+ / −)

### 🎨 UI & UX

* Responsive UI using **Bootstrap**
* Clean banking dashboard
* Color-coded transactions:

  * 🟢 Credit (Deposit / Transfer In)
  * 🔴 Debit (Withdraw / Transfer Out)
* Thymeleaf templating engine
* CSRF-protected forms

---

## 🛠 Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring MVC
* Spring Security
* Spring Data JPA
* Hibernate
* MySQL

### Frontend

* Thymeleaf
* HTML5
* CSS3
* Bootstrap 4

### Tools

* Maven
* MySQL Workbench
* Git

---

## 📂 Project Structure

```
bank-app/
│
├── src/main/java/com/example/bankapp
│   ├── controller
│   │   ├── AuthController.java
│   │   ├── RegisterController.java
│   │   ├── DashboardController.java
│   │   └── TransactionController.java
│   │
│   ├── model
│   │   ├── User.java
│   │   ├── Account.java
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   │
│   ├── repository
│   │   ├── UserRepository.java
│   │   ├── AccountRepository.java
│   │   └── TransactionRepository.java
│   │
│   ├── service
│   │   └── UserService.java
│   │
│   └── security
│       └── SecurityConfig.java
│
├── src/main/resources
│   ├── templates
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── dashboard.html
│   │   └── transactions.html
│   │
│   ├── application.properties
│   └── static
│
└── pom.xml
```

---

## 🗄 Database Schema

### `users` table

| Column   | Type    |
| -------- | ------- |
| id       | BIGINT  |
| username | VARCHAR |
| password | VARCHAR |
| role     | VARCHAR |

### `accounts` table

| Column         | Type     |
| -------------- | -------- |
| id             | BIGINT   |
| user_id        | BIGINT   |
| account_number | VARCHAR  |
| balance        | DOUBLE   |
| account_type   | VARCHAR  |
| created_at     | DATETIME |

### `transactions` table

| Column           | Type     |
| ---------------- | -------- |
| id               | BIGINT   |
| account_id       | BIGINT   |
| transaction_type | ENUM     |
| amount           | DOUBLE   |
| description      | VARCHAR  |
| transaction_date | DATETIME |

---

## 🔐 Transaction Types

```java
public enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_OUT,
    TRANSFER_IN
}
```

| Type         | Meaning         |
| ------------ | --------------- |
| DEPOSIT      | Money added     |
| WITHDRAW     | Money withdrawn |
| TRANSFER_OUT | Money sent      |
| TRANSFER_IN  | Money received  |

---

## 🔁 Fund Transfer Logic (Core Feature)

Transfers create **two transactions** atomically:

1. **Sender**

   * Balance decreases
   * `TRANSFER_OUT` entry created

2. **Receiver**

   * Balance increases
   * `TRANSFER_IN` entry created

This guarantees:

* Accurate balances
* Correct transaction history for both users
* No partial updates (ACID compliance)

---

## 🧠 Security Highlights

* BCrypt password hashing
* CSRF protection enabled
* Session-based authentication
* Secure logout
* Unauthorized access protection

---

## ▶️ How to Run the Project

### 1️⃣ Clone Repository

```bash
git clone https://github.com/your-username/nova-bank.git
cd nova-bank
```

### 2️⃣ Configure Database

Create MySQL database:

```sql
CREATE DATABASE bankappdb;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankappdb
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

### 4️⃣ Open Browser

```
http://localhost:8080/login
```

---

## 🧪 Test Flow

1. Register a new user
2. Login
3. Deposit money
4. Withdraw money
5. Transfer money to another user
6. Verify:

   * Sender sees debit
   * Receiver sees credit
7. View full transaction history

---

## 🐞 Common Issues Solved in This Project

* Enum mismatch causing app crash
* Missing receiver transaction entries
* Incorrect credit/debit UI rendering
* Spring Security CSRF errors
* 403 errors on register
* 404 static resource issues
* Whitelabel error debugging

---

## 🌱 Future Enhancements

* Admin dashboard
* Transaction filters (date / type)
* PDF bank statements
* Email notifications
* Scheduled interest calculation
* Tailwind CSS UI
* REST API version
* React / Angular frontend

---

## 👨‍💻 Author

**Aman Jambhulkar**
Built with ❤️ using Spring Boot

---

## 📜 License

This project is for **educational and learning purposes**.
You are free to modify and extend it.

---

##  Screenshots 

LOGIN : 

<img width="1919" height="1013" alt="Screenshot 2026-01-17 210225" src="https://github.com/user-attachments/assets/1db86ccb-7595-4e08-897d-e8968bdb384a" />


REGISTER : 

<img width="1919" height="1016" alt="Screenshot 2026-01-17 210220" src="https://github.com/user-attachments/assets/b8dc66ab-3a7b-4d70-8ab8-ac3201f30a53" />

DASHBOARD :

<img width="1919" height="799" alt="Screenshot 2026-01-17 210025" src="https://github.com/user-attachments/assets/f99221ae-530a-4bc0-a7ae-f54b709a11e4" />

<img width="1915" height="727" alt="Screenshot 2026-01-17 210041" src="https://github.com/user-attachments/assets/0ed14bba-8f33-4b4e-add4-2bf457cbe0d1" />

<img width="1888" height="685" alt="Screenshot 2026-01-17 210046" src="https://github.com/user-attachments/assets/93d0e629-575e-453c-b45b-1a587be9e6c3" />


TRANSACTION : 

<img width="1919" height="1015" alt="Screenshot 2026-01-17 210012" src="https://github.com/user-attachments/assets/5e64bb4e-a4bf-4c51-af74-2bf57ca08fc9" />

---
