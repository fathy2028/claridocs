# HR Document System - API Documentation

## Overview

This document describes the REST API endpoints for the HR Document System, a web-based HR management system with role-based access control.

## Base URL

```
http://localhost:8080/api
```

## Authentication

All endpoints require authentication except `/login` and `/logout`. The system uses session-based authentication with role-based access control.

### Roles

- **ADMIN**: Full access to all features
- **EMPLOYEE**: Limited access to own documents and profile

## API Endpoints

### 1. Authentication

```
POST /login          - Login user
POST /logout         - Logout user
```

### 2. Department Management (Admin Only)

```
GET    /departments                    - List all departments
GET    /departments/{id}              - Get department by ID
POST   /departments                    - Create new department
PUT    /departments/{id}              - Update department
DELETE /departments/{id}              - Delete department
GET    /departments/{id}/employees    - Get employees in department
```

#### Department DTO

```json
{
  "id": "uuid",
  "name": "string",
  "description": "string",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

### 3. Employee Management (Admin Only)

```
GET    /employees                    - List all employees
GET    /employees/{id}              - Get employee by ID
POST   /employees                    - Create new employee
PUT    /employees/{id}              - Update employee
DELETE /employees/{id}              - Delete employee
```

#### Employee DTO

```json
{
  "id": "uuid",
  "userId": "uuid",
  "departmentId": "uuid",
  "dateJoined": "date",
  "phone": "string"
}
```

### 4. Document Management

```
GET    /documents                    - List documents (filtered by role)
GET    /documents/{id}              - Get document by ID
POST   /documents                    - Upload document (Admin only)
GET    /documents/{id}/download     - Download document
DELETE /documents/{id}              - Delete document (Admin only)
GET    /documents/search             - Search documents
```

#### Document Upload Request

```
POST /documents
Content-Type: multipart/form-data

file: [binary file]
employeeId: uuid
type: "CONTRACT" | "PAYSLIP" | "ID" | "OTHER"
title: string
```

#### Document DTO

```json
{
  "id": "uuid",
  "employeeId": "uuid",
  "employeeName": "string",
  "type": "CONTRACT|PAYSLIP|ID|OTHER",
  "originalFilename": "string",
  "mimeType": "string",
  "sizeBytes": "number",
  "storageKey": "string",
  "sha256": "string",
  "version": "number",
  "uploadedAt": "timestamp",
  "deletedAt": "timestamp"
}
```

### 5. Search and Filtering

```
GET /search/employees                    - Search employees (Admin only)
GET /search/documents                    - Search documents
GET /search/employees/autocomplete      - Employee name autocomplete
GET /search/departments/autocomplete    - Department name autocomplete
GET /search/advanced/employees          - Advanced employee search
```

#### Search Parameters

- **Employee Search**: name, departmentId, dateJoinedFrom, dateJoinedTo, phone
- **Document Search**: title, type, employeeId, uploadedFrom, uploadedTo
- **Advanced Search**: searchQuery, departmentIds[], hireDateFrom, hireDateTo, hasDocuments, sortBy, sortDirection

### 6. Reports & Analytics (Admin Only)

```
GET /admin/reports/department-stats           - Department statistics
GET /admin/reports/document-stats             - Document statistics
GET /admin/reports/employee-stats             - Employee statistics
GET /admin/reports/monthly-uploads            - Monthly upload trends
GET /admin/reports/employees-without-documents - Employees missing documents
GET /admin/reports/document-type-distribution - Document type distribution
GET /admin/reports/average-tenure             - Average employee tenure
```

#### Report DTOs

**Department Stats**

```json
{
  "departmentId": "uuid",
  "departmentName": "string",
  "employeeCount": "number",
  "documentCount": "number"
}
```

**Document Stats**

```json
{
  "totalDocuments": "number",
  "totalSizeBytes": "number",
  "documentsByType": "object",
  "documentsByDate": "object",
  "documentsThisMonth": "number",
  "documentsLastMonth": "number"
}
```

**Employee Stats**

```json
{
  "totalEmployees": "number",
  "activeEmployees": "number",
  "newEmployeesThisMonth": "number",
  "averageTenureDays": "number",
  "oldestEmployeeDate": "date",
  "newestEmployeeDate": "date"
}
```

## File Upload Configuration

- Maximum file size: 10MB
- Supported formats: PDF, DOCX, Images, etc.
- Files are stored locally in the `uploads/` directory
- SHA256 hash is calculated for file integrity
- Soft delete is implemented for documents

## Security Features

- Role-based access control (RBAC)
- Session-based authentication
- File access restricted by employee ownership
- Admin can access all data, employees only their own
- Input validation and sanitization
- SQL injection protection through JPA

## Error Handling

All endpoints return appropriate HTTP status codes:

- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Example Usage

### Create a Department

```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Engineering",
    "description": "Software Engineering Department"
  }'
```

### Upload a Document

```bash
curl -X POST http://localhost:8080/api/documents \
  -F "file=@contract.pdf" \
  -F "employeeId=123e4567-e89b-12d3-a456-426614174000" \
  -F "type=CONTRACT" \
  -F "title=Employment Contract"
```

### Search Employees

```bash
curl "http://localhost:8080/api/search/employees?name=John&departmentId=123e4567-e89b-12d3-a456-426614174000"
```

### Get Department Statistics

```bash
curl http://localhost:8080/api/admin/reports/department-stats
```

## Database Schema

The system uses PostgreSQL with the following main entities:

- **Users**: Authentication and role management
- **Departments**: Organizational structure
- **Employees**: Employee information and department assignment
- **Documents**: File metadata and storage information

## Performance Considerations

- Pagination implemented for large result sets
- Database indexes on frequently queried fields
- In-memory filtering for complex search operations
- File storage with unique naming to prevent conflicts

## Development Notes

- Built with Spring Boot 3.x
- Uses JPA/Hibernate for data persistence
- Implements soft delete for data integrity
- File uploads are stored locally (configurable for cloud storage)
- All timestamps are in UTC
