# EduSphere – Smart Student Information System

EduSphere is a full-stack Smart Student Information System developed using Spring Boot, Spring Data JPA, MySQL, REST APIs, SOAP Web Services, JWT Authentication, AOP, and a responsive HTML/CSS/JavaScript frontend.

The system provides a centralized platform for managing student academic information, career profiles, skills, projects, certifications, achievements, and student results.

---

## 🎯 Project Objective

The objective of EduSphere is to build a centralized student information and career profile platform that allows students to maintain their academic and professional profiles while providing administrators with tools to manage students and analyze student information.

---

## 🚀 Key Features

### 👨‍🎓 Student Features

- Student registration and login
- JWT-based authentication
- Student academic information
- Student profile management
- GitHub profile
- LinkedIn profile
- Portfolio URL
- Resume URL
- LeetCode profile
- CodeChef profile
- Profile image URL
- Skills management
- Project management
- Certification management
- Achievement management
- Public student profile
- Student result information

### 👨‍💼 Admin Features

- Secure admin login
- View all students
- Search students
- View complete student profiles
- Activate student accounts
- Deactivate student accounts
- Student statistics
- Department-wise analytics
- Year-wise analytics
- Active/inactive student statistics

### 🔐 Security

- Spring Security
- JWT authentication
- BCrypt password encryption
- Role-based authorization
- Student and Admin roles
- Protected REST APIs

### 🌐 APIs

- REST APIs for student management
- REST APIs for skills
- REST APIs for projects
- REST APIs for certifications
- REST APIs for achievements
- REST APIs for student profiles
- REST APIs for admin operations
- SOAP Web Service for student results

### 📊 Documentation

- Swagger/OpenAPI documentation
- REST API testing
- SOAP API testing
- MySQL database
- ER diagram

### ⚙️ Advanced Spring Features

- Constructor Dependency Injection
- Spring Data JPA
- Bean Validation
- Global Exception Handling
- AOP activity logging
- Transaction management
- Spring Security
- JWT authentication
- SOAP Web Services

---

## 🏗️ Technology Stack

### Backend

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Spring Validation
- Spring AOP
- Spring Web Services
- Hibernate
- JWT

### Database

- MySQL 8

### Frontend

- HTML5
- CSS3
- JavaScript

### API Documentation

- Swagger / OpenAPI

### Development Tools

- Visual Studio Code
- Maven
- MySQL Workbench
- Git
- GitHub

---

## 🏛️ System Architecture

```text
                   ┌──────────────────────┐
                   │      Frontend        │
                   │ HTML / CSS / JS      │
                   └──────────┬───────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │    Spring Boot      │
                   │    REST Controllers │
                   └──────────┬───────────┘
                              │
                 ┌────────────┴────────────┐
                 ▼                         ▼
        ┌─────────────────┐       ┌─────────────────┐
        │ Service Layer   │       │ Security Layer  │
        │ Business Logic  │       │ JWT + Roles     │
        └────────┬────────┘       └─────────────────┘
                 │
                 ▼
        ┌─────────────────┐
        │ Spring Data JPA │
        │   Repository    │
        └────────┬────────┘
                 │
                 ▼
        ┌─────────────────┐
        │     MySQL       │
        │    Database     │
        └─────────────────┘

              SOAP Client
                   │
                   ▼
        ┌─────────────────┐
        │ SOAP Web Service│
        │ Student Results │
        └─────────────────┘
        🗄️ Database Design

The EduSphere database contains the following major tables:

student
student_profile
users
skill
project
certification
achievement
student_result

The database is managed using MySQL and the entity relationships are handled using JPA/Hibernate.

🔑 Authentication Flow
Student/Admin
      │
      ▼
 Login
      │
      ▼
 Spring Security
      │
      ▼
 Password Verification
      │
      ▼
 JWT Token Generated
      │
      ▼
 Token Sent to Client
      │
      ▼
 Protected API Request
      │
      ▼
 JWT Authentication Filter
      │
      ▼
 Role Verification
      │
      ▼
 API Access
🌐 REST API Modules
Authentication
POST /api/auth/login
POST /api/auth/signup
GET  /api/auth/me
Students
GET    /api/students
GET    /api/students/{id}
POST   /api/students
PUT    /api/students/{id}
DELETE /api/students/{id}
Public Profiles
GET /api/public/students/{studentId}
Skills
GET    /api/skills/me
POST   /api/skills
PUT    /api/skills/{id}
DELETE /api/skills/{id}
Projects
GET    /api/projects/me
POST   /api/projects
PUT    /api/projects/{id}
DELETE /api/projects/{id}
Certifications
GET    /api/certifications/me
POST   /api/certifications
PUT    /api/certifications/{id}
DELETE /api/certifications/{id}
Achievements
GET    /api/achievements/me
POST   /api/achievements
PUT    /api/achievements/{id}
DELETE /api/achievements/{id}
Admin
GET /api/admin/students
GET /api/admin/students/{id}
GET /api/admin/dashboard

PUT /api/admin/students/{id}/activate
PUT /api/admin/students/{id}/deactivate
🧼 Validation and Exception Handling

EduSphere uses Jakarta Bean Validation to validate incoming student data.

Examples include:

Required fields
Valid email format
Valid academic year
Required registration number

A centralized exception handling mechanism using @ControllerAdvice provides structured error responses for invalid requests and missing resources.

📝 AOP Activity Logging

Aspect-Oriented Programming is used to log service-layer method execution.

Example:

AOP LOG: Method executed - StudentService.getAllStudents()

This provides centralized activity logging without adding logging code to every service method.

🌐 SOAP Student Results

EduSphere also provides a SOAP Web Service for retrieving student result information.

WSDL
/ws/student-results.wsdl
SOAP Endpoint
/ws

The service accepts a student ID and returns:

Student ID
Student Name
Semester
Result
Percentage

It also returns a SOAP Fault when result information is unavailable.

📖 Swagger API Documentation

Swagger/OpenAPI is integrated for API documentation and testing.

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI specification:

http://localhost:8080/v3/api-docs
▶️ Running the Project Locally
1. Clone the repository
git clone https://github.com/mahalakshmi-18-04-06/edusphere.git
2. Open the project

Open the project in Visual Studio Code or another Java IDE.

3. Create the MySQL database
CREATE DATABASE edusphere;
4. Configure MySQL

Update the database configuration in:

src/main/resources/application.properties
5. Run the application

Using Maven:

mvnw spring-boot:run

Or on Windows:

mvnw.cmd spring-boot:run
6. Open the application
http://localhost:8080
👤 Demo Accounts
Student
Roll Number: 24B11AI001
Password: Maha@123
Role: STUDENT
Admin
Roll Number: admin
Password: Admin@123
Role: ADMIN

Demo credentials should be changed before production deployment.

📂 Project Structure
edusphere/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/edusphere/
│       │       ├── aspect/
│       │       ├── config/
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── repository/
│       │       ├── security/
│       │       └── service/
│       │
│       └── resources/
│           ├── static/
│           ├── application.properties
│           └── student-results.xsd
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
🔮 Future Enhancements
AI-powered student career recommendations
Resume analysis
Automated skill-gap analysis
Coding-platform API integration
Email notifications
Resume file upload
Advanced student analytics
Placement prediction
Cloud deployment
Mobile application
👩‍💻 Author

Mahalakshmi Repuri

B.Tech – Artificial Intelligence & Machine Learning

Aditya University

GitHub:
https://github.com/mahalakshmi-18-04-06

LinkedIn:
https://www.linkedin.com/in/mahalakshmi-repuri-495177333/

📄 License

This project is developed for academic and educational purposes.