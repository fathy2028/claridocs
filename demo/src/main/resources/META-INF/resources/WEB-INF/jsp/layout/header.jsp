<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ClariDocs - HR Document Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .sidebar {
            min-height: 100vh;
            background-color: #343a40;
        }
        .sidebar .nav-link {
            color: #adb5bd;
        }
        .sidebar .nav-link:hover {
            color: #fff;
        }
        .sidebar .nav-link.active {
            color: #fff;
            background-color: #495057;
        }
        .main-content {
            margin-left: 0;
        }
        @media (min-width: 768px) {
            .main-content {
                margin-left: 250px;
            }
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Sidebar -->
            <nav class="col-md-3 col-lg-2 d-md-block sidebar collapse">
                <div class="position-sticky pt-3">
                    <div class="text-center mb-4">
                        <h4 class="text-white">ClariDocs</h4>
                        <small class="text-muted">HR Document System</small>
                    </div>
                    
                    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'ADMIN'}">
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/dashboard">
                                    <i class="fas fa-tachometer-alt"></i> Dashboard
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/departments">
                                    <i class="fas fa-building"></i> Departments
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/employees">
                                    <i class="fas fa-users"></i> Employees
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/documents">
                                    <i class="fas fa-file-alt"></i> Documents
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/admin/reports">
                                    <i class="fas fa-chart-bar"></i> Reports
                                </a>
                            </li>
                        </ul>
                    </c:if>
                    
                    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'EMPLOYEE'}">
                        <ul class="nav flex-column">
                            <li class="nav-item">
                                <a class="nav-link" href="/employee/dashboard">
                                    <i class="fas fa-tachometer-alt"></i> Dashboard
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/employee/documents">
                                    <i class="fas fa-file-alt"></i> My Documents
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="/employee/profile">
                                    <i class="fas fa-user"></i> Profile
                                </a>
                            </li>
                        </ul>
                    </c:if>
                    
                    <hr class="text-muted">
                    <ul class="nav flex-column">
                        <li class="nav-item">
                            <a class="nav-link" href="/auth/logout">
                                <i class="fas fa-sign-out-alt"></i> Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </nav>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">${pageTitle}</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <c:if test="${sessionScope.user != null}">
                            <span class="text-muted me-3">Welcome, ${sessionScope.user.name}</span>
                        </c:if>
                    </div>
                </div>

                <!-- Flash Messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
