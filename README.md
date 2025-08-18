# ClariDocs 📄
A comprehensive, enterprise-grade HR Document Management System designed to streamline employee document handling, departmental organization, and administrative workflows. Built with modern Spring Boot architecture and featuring role-based access control, secure file management, and intuitive user interfaces.

## 🌟 Key Features

- **Role-Based Access Control**: Secure admin and employee portals with distinct permissions
- **Document Management**: Upload, organize, and manage employee documents with version control
- **Department Management**: Comprehensive departmental structure and employee assignment
- **Advanced Search**: Powerful search capabilities across employees, departments, and documents
- **File Security**: SHA256 hashing, file integrity checks, and secure storage
- **Responsive Design**: Modern, mobile-friendly interface with Bootstrap integration
- **Audit Trail**: Complete tracking of document uploads, modifications, and access
- **Soft Delete**: Data preservation with logical deletion for compliance

## 🏗️ Architecture Overview

ClariDocs follows a layered architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │   Admin Portal  │  │ Employee Portal │  │  REST APIs   │ │
│  │     (JSP)       │  │     (JSP)       │  │    (JSON)    │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                    Business Layer                           │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ User Service    │  │Document Service │  │Dept Service  │ │
│  │Employee Service │  │Security Service │  │Search Service│ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                   Data Access Layer                         │
│  ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐ │
│  │ JPA Repositories│  │   Custom Queries│  │  Flyway      │ │
│  │   (Spring Data) │  │     (@Query)    │  │ Migrations   │ │
│  └─────────────────┘  └─────────────────┘  └──────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                    Database Layer                           │
│              PostgreSQL with Optimized Indexes             │
└─────────────────────────────────────────────────────────────┘
```

## 🛠️ Technology Stack

### Backend

- **Framework**: Spring Boot 3.3.2
- **Language**: Java 17
- **Security**: Spring Security with BCrypt password encoding
- **Data Access**: Spring Data JPA with Hibernate
- **Database**: PostgreSQL (Production), H2 (Testing)
- **Migration**: Flyway for database versioning
- **Build Tool**: Maven
- **Authentication**: Session-based with role-based access control

### Frontend

- **Template Engine**: JSP with JSTL
- **CSS Framework**: Bootstrap 5
- **JavaScript**: Vanilla JS with modern ES6+ features
- **Icons**: Font Awesome
- **Responsive Design**: Mobile-first approach

### Development & Testing

- **Testing**: JUnit 5, Spring Boot Test
- **Code Quality**: Lombok for boilerplate reduction
- **API Documentation**: Comprehensive REST API documentation
- **Development Tools**: Spring Boot DevTools, Maven Wrapper

## 🗄️ Database Schema

The system uses a well-normalized PostgreSQL database with the following core entities:

### Entity Relationship Diagram

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│      Users      │       │   Departments   │       │    Employees    │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ id (UUID) PK    │       │ id (UUID) PK    │       │ id (UUID) PK    │
│ name            │       │ name            │       │ user_id FK      │
│ email (unique)  │       │ description     │       │ department_id FK│
│ password        │       │ created_at      │       │ date_joined     │
│ role (enum)     │◄──────┤                 │◄──────┤ phone           │
│ created_at      │ 1:1   │                 │ 1:N   │                 │
│ active          │       │                 │       │                 │
└─────────────────┘       └─────────────────┘       └─────────────────┘
                                                              │
                                                              │ 1:N
                                                              ▼
                                                    ┌─────────────────┐
                                                    │    Documents    │
                                                    ├─────────────────┤
                                                    │ id (UUID) PK    │
                                                    │ employee_id FK  │
                                                    │ title           │
                                                    │ type (enum)     │
                                                    │ original_filename│
                                                    │ mime_type       │
                                                    │ size_bytes      │
                                                    │ storage_key     │
                                                    │ storage_provider│
                                                    │ sha256          │
                                                    │ version         │
                                                    │ uploaded_at     │
                                                    │ deleted_at      │
                                                    │ created_at      │
                                                    │ updated_at      │
                                                    └─────────────────┘
```

### Core Entities

#### 👤 Users

- **Primary Key**: UUID for enhanced security
- **Authentication**: BCrypt encrypted passwords
- **Roles**: ADMIN (full access) | EMPLOYEE (restricted access)
- **Status**: Active/inactive flag for user management
- **Audit**: Creation timestamp tracking

#### 🏢 Departments

- **Structure**: Hierarchical organization units
- **Metadata**: Name, description, creation tracking
- **Relationships**: One-to-many with employees
- **Constraints**: Unique department names

#### 👥 Employees

- **Profile**: Links users to organizational structure
- **Department Assignment**: Foreign key relationship
- **Contact Info**: Phone numbers and joining dates
- **Document Association**: One-to-many with documents

#### 📄 Documents

- **File Management**: Secure storage with integrity checks
- **Metadata**: Title, type, size, MIME type tracking
- **Versioning**: Document version control
- **Security**: SHA256 hashing for file integrity
- **Soft Delete**: Logical deletion with timestamp
- **Types**: CONTRACT, PAYSLIP, ID, OTHER

## 🚀 Quick Start

### Prerequisites

- **Java 17** or higher
- **PostgreSQL 12+**
- **Maven 3.6+**
- **Git**

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/fathy2028/claridocs.git
   cd claridocs/demo
   ```

2. **Setup PostgreSQL Database**

   ```sql
   CREATE DATABASE claridocs;
   ```



4. **Build and Run the Application**

   ```bash
   # Using Maven Wrapper (Recommended)
   ./mvnw clean install
   ./mvnw spring-boot:run

   # Or using system Maven
   mvn clean install
   mvn spring-boot:run
   ```

   The application will automatically run database migrations on startup.

5. **Initialize Database with Admin User**

   After the application starts successfully, run the following SQL script in your PostgreSQL tool (pgAdmin, DBeaver, or psql) to create initial data:

   ```sql
   -- First, let's create departments with created_at
   INSERT INTO departments (id, name, created_at)
   VALUES
       (gen_random_uuid(), 'Administration', NOW()),
       (gen_random_uuid(), 'Human Resources', NOW());

   -- Insert Admin User
   INSERT INTO users (id, name, email, password, role, created_at, active)
   VALUES (
       gen_random_uuid(),
       'System Administrator',
       'admin@claridocs.com',
       '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
       'ADMIN',
       NOW(),
       true
   );

   -- Insert Employee User
   INSERT INTO users (id, name, email, password, role, created_at, active)
   VALUES (
       gen_random_uuid(),
       'John Doe',
       'john.doe@claridocs.com',
       '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
       'EMPLOYEE',
       NOW(),
       true
   );

   -- Create Employee record for the employee user
   INSERT INTO employees (id, user_id, department_id, date_joined, phone)
   SELECT
       gen_random_uuid(),
       u.id,
       d.id,
       CURRENT_DATE,
       '+1-555-0123'
   FROM users u
   CROSS JOIN departments d
   WHERE u.email = 'john.doe@claridocs.com'
   AND d.name = 'Human Resources';
   ```

6. **Access the Application**
   - **URL**: http://localhost:8080
   - **Admin Credentials**:
     - Email: `admin@claridocs.com`
     - Password: `password`
   - **Employee Credentials**:
     - Email: `john.doe@claridocs.com`
     - Password: `password`
   - **Port**: 8080 (configurable in application.yml)

## ⚙️ Configuration

### Application Configuration

The application uses `application.yml` for configuration:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:claridocs}
    username: ${DB_USER:your_username}
    password: ${DB_PASSWORD:your_password}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update # Use 'validate' in production
    properties:
      hibernate:
        format_sql: true
        jdbc.lob.non_contextual_creation: true

  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

app:
  upload:
    dir: uploads

server:
  port: 8080
```

### Environment-Specific Configurations

- **Development**: `application.yml` (default)
- **Testing**: `application-test.yml` (H2 in-memory database)
- **Production**: Override with environment variables

### File Upload Configuration

- **Maximum File Size**: 10MB per file
- **Storage Location**: `uploads/` directory (configurable)
- **Supported Formats**: PDF, DOCX, Images, and more
- **Security**: SHA256 integrity verification

## 📚 API Documentation

ClariDocs provides comprehensive REST APIs for integration:

### Authentication Endpoints

```http
POST /auth/login          # User authentication
POST /auth/logout         # User logout
```

### Admin Endpoints

```http
# Department Management
GET    /admin/departments           # List all departments
POST   /admin/departments           # Create department
PUT    /admin/departments/{id}      # Update department
DELETE /admin/departments/{id}      # Delete department

# Employee Management
GET    /admin/employees             # List all employees
POST   /admin/employees             # Create employee
PUT    /admin/employees/{id}        # Update employee
DELETE /admin/employees/{id}        # Delete employee

# Document Management
GET    /admin/documents             # List all documents
POST   /admin/documents             # Upload document
GET    /admin/documents/{id}        # Get document details
DELETE /admin/documents/{id}        # Delete document

# Reports & Analytics
GET    /admin/reports/dashboard     # Dashboard statistics
GET    /admin/reports/departments   # Department analytics
```

### Employee Endpoints

```http
# Profile Management
GET    /employee/profile            # Get employee profile
PUT    /employee/profile            # Update profile

# Document Access
GET    /employee/documents          # List own documents
POST   /employee/documents          # Upload document
GET    /employee/documents/{id}     # Download own document
```

### Search & Filter APIs

```http
GET /api/search/employees?name={name}&department={dept}
GET /api/search/documents?type={type}&employee={id}
GET /api/search/departments?name={name}
```

For detailed API documentation, see [API_DOCUMENTATION.md](demo/API_DOCUMENTATION.md)

## 👨‍💼 Usage Guide

### Admin Portal Features

1. **Dashboard Overview**

   - Employee count and statistics
   - Department analytics
   - Document management metrics
   - Recent activity tracking

2. **Employee Management**

   - Create, update, and deactivate employees
   - Assign employees to departments
   - Bulk operations and data export
   - Advanced search and filtering

3. **Department Management**

   - Create and manage organizational structure
   - Department-wise employee allocation
   - Performance analytics per department

4. **Document Administration**
   - View all employee documents
   - Document type categorization
   - File integrity monitoring
   - Bulk document operations

### Employee Portal Features

1. **Personal Dashboard**

   - Document overview and quick access
   - Profile information display
   - Recent uploads and activities

2. **Document Management**

   - Upload personal documents (contracts, IDs, etc.)
   - Download and view own documents
   - Document version tracking

3. **Profile Management**
   - Update contact information
   - View department assignment
   - Employment history

## 🔒 Security Features

### Authentication & Authorization

- **Session-Based Authentication**: Secure session management
- **Role-Based Access Control (RBAC)**: Admin vs Employee permissions
- **Password Security**: BCrypt encryption with salt
- **Session Timeout**: Automatic logout for security

### Data Protection

- **Input Validation**: Comprehensive server-side validation
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **File Security**: SHA256 integrity verification
- **Soft Delete**: Data preservation for audit trails

### Access Control

- **Resource Protection**: URL-based access restrictions
- **File Access Control**: Employee-specific document access
- **Admin Privileges**: Elevated permissions for administrative tasks
- **Cross-Site Protection**: CSRF token implementation

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=EntityTest

# Run with coverage
./mvnw test jacoco:report
```

### Test Configuration

- **Unit Tests**: JUnit 5 with Mockito
- **Integration Tests**: Spring Boot Test with H2
- **Test Database**: In-memory H2 for isolation
- **Test Profiles**: Separate configuration for testing

### Test Coverage

The project includes comprehensive tests for:

- Entity relationships and constraints
- Service layer business logic
- Repository data access operations
- Controller endpoint functionality

## 🛠️ Development

### Project Structure

```
claridocs/
├── demo/                           # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/claridocs/
│   │   │   │       ├── config/     # Configuration classes
│   │   │   │       ├── controller/ # Web controllers
│   │   │   │       ├── domain/     # Entity models
│   │   │   │       ├── dto/        # Data transfer objects
│   │   │   │       ├── repository/ # Data access layer
│   │   │   │       └── service/    # Business logic
│   │   │   └── resources/
│   │   │       ├── META-INF/resources/WEB-INF/jsp/ # JSP templates
│   │   │       ├── static/         # CSS, JS, images
│   │   │       ├── db/migration/   # Flyway migrations
│   │   │       └── application.yml # Configuration
│   │   └── test/                   # Test classes
│   ├── uploads/                    # File storage directory
│   └── pom.xml                     # Maven dependencies
├── API_DOCUMENTATION.md            # Detailed API docs
└── README.md                       # This file
```

### Development Setup

1. **IDE Configuration**

   - Import as Maven project
   - Enable annotation processing for Lombok
   - Configure code formatting (Google Java Style recommended)

2. **Database Setup**

   ```bash
   # Start PostgreSQL with Docker
   docker run --name claridocs-db \
     -e POSTGRES_DB=claridocs \
     -e POSTGRES_USER=fathy \
     -e POSTGRES_PASSWORD=2028 \
     -p 5432:5432 -d postgres:15
   ```

3. **Development Commands**

   ```bash
   # Hot reload development
   ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"

   # Debug mode
   ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
   ```

### Code Quality

- **Lombok**: Reduces boilerplate code
- **Spring Boot DevTools**: Hot reload during development
- **Validation**: Bean validation with custom constraints
- **Error Handling**: Global exception handling
- **Logging**: Structured logging with SLF4J

## 🚀 Deployment

### Production Deployment

1. **Build Production JAR**

   ```bash
   ./mvnw clean package -Dmaven.test.skip=true
   ```

2. **Environment Variables**

   ```bash
   export SPRING_PROFILES_ACTIVE=prod
   export DB_HOST=your-db-host
   export DB_NAME=claridocs_prod
   export DB_USER=prod_user
   export DB_PASSWORD=secure_password
   ```

3. **Run Application**
   ```bash
   java -jar target/claridocs-0.0.1-SNAPSHOT.jar
   ```

### Docker Deployment

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/claridocs-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Production Considerations

- **Database**: Use connection pooling (HikariCP included)
- **File Storage**: Consider cloud storage (S3, Azure Blob)
- **Security**: Enable HTTPS, update security headers
- **Monitoring**: Add application monitoring (Micrometer, Actuator)
- **Backup**: Regular database and file backups

## 📈 Performance & Scalability

### Database Optimization

- **Indexes**: Strategic indexing on frequently queried columns
- **Connection Pooling**: HikariCP for efficient connection management
- **Query Optimization**: Custom JPQL queries for complex operations
- **Lazy Loading**: Optimized entity relationships

### File Management

- **Storage Strategy**: Local storage with cloud migration path
- **File Validation**: Size and type restrictions
- **Integrity Checks**: SHA256 verification
- **Cleanup**: Automated cleanup of orphaned files

### Caching Strategy

- **Session Management**: Efficient session storage
- **Query Caching**: Hibernate second-level cache ready
- **Static Resources**: CDN-ready static asset serving

## 🔧 Troubleshooting

### Common Issues

#### Database Connection Issues

```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Verify database exists
psql -U fathy -d claridocs -c "\dt"

# Reset database (development only)
./mvnw flyway:clean flyway:migrate
```

#### File Upload Problems

- **Check upload directory permissions**: `chmod 755 uploads/`
- **Verify file size limits**: Max 10MB per file
- **Supported formats**: PDF, DOCX, images
- **Storage space**: Ensure adequate disk space

#### Authentication Issues

- **Clear browser cache and cookies**
- **Check user active status in database**
- **Verify password encryption matches**

#### Performance Issues

```bash
# Enable SQL logging for debugging
logging.level.org.hibernate.SQL=DEBUG

# Monitor JVM memory usage
java -XX:+PrintGCDetails -jar app.jar

# Database query analysis
EXPLAIN ANALYZE SELECT * FROM employees;
```

### Logging Configuration

```yaml
logging:
  level:
    com.claridocs: DEBUG
    org.hibernate.SQL: WARN
    org.springframework.security: DEBUG
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/claridocs.log
```

## 🤝 Contributing

We welcome contributions to ClariDocs! Please follow these guidelines:

### Development Workflow

1. **Fork the repository**

   ```bash
   git clone https://github.com/fathy2028/claridocs.git
   cd claridocs
   ```

2. **Create a feature branch**

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make your changes**

   - Follow existing code style and conventions
   - Add tests for new functionality
   - Update documentation as needed

4. **Test your changes**

   ```bash
   ./mvnw test
   ./mvnw spring-boot:run # Manual testing
   ```

5. **Submit a pull request**
   - Provide clear description of changes
   - Reference any related issues
   - Ensure all tests pass

### Code Standards

- **Java Style**: Follow Google Java Style Guide
- **Commit Messages**: Use conventional commit format
- **Documentation**: Update README and API docs
- **Testing**: Maintain test coverage above 80%

### Reporting Issues

When reporting bugs, please include:

- Java and Spring Boot versions
- Database type and version
- Steps to reproduce the issue
- Expected vs actual behavior
- Relevant log outputs

---

**ClariDocs** - Streamlining HR document management with modern technology and intuitive design.

_Built with ❤️ using Spring Boot, PostgreSQL, and modern web technologies._
