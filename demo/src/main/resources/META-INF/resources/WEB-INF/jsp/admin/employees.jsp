<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Employee Management" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Employee Management</h2>
    <a href="/admin/employees/new" class="btn btn-primary">
        <i class="fas fa-plus"></i> Add Employee
    </a>
</div>

<!-- Search and Filter -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="/admin/employees" class="row g-3">
            <div class="col-md-3">
                <label for="search" class="form-label">Search by Name</label>
                <input type="text" class="form-control" id="search" name="search"
                       value="${search}" placeholder="Enter employee name">
            </div>
            <div class="col-md-3">
                <label for="departmentId" class="form-label">Filter by Department</label>
                <select class="form-select" id="departmentId" name="departmentId">
                    <option value="">All Departments</option>
                    <c:forEach var="dept" items="${departments}">
                        <option value="${dept.id}"
                                <c:if test="${dept.id == selectedDepartmentId}">selected</c:if>>
                            ${dept.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <label for="dateJoined" class="form-label">Date of Joining</label>
                <input type="date" class="form-control" id="dateJoined" name="dateJoined"
                       value="${dateJoined}" title="Filter by joining date">
            </div>
            <div class="col-md-3 d-flex align-items-end">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-search"></i> Search
                </button>
                <a href="/admin/employees" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Clear
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Employees Table -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">All Employees</h5>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Department</th>
                        <th>Phone</th>
                        <th>Date Joined</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="emp" items="${employees}">
                        <tr>
                            <td><strong>${emp.user.name}</strong></td>
                            <td>${emp.user.email}</td>
                            <td>
                                <span class="badge bg-secondary">${emp.department.name}</span>
                            </td>
                            <td>${emp.phone}</td>
                            <td>
                                ${emp.dateJoined}
                            </td>
                            <td>
                                <a href="/admin/employees/${emp.id}/edit" class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-edit"></i> Edit
                                </a>
                                <form method="post" action="/admin/employees/${emp.id}/delete" 
                                      style="display: inline;" 
                                      onsubmit="return confirm('Are you sure you want to delete this employee?')">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty employees}">
                        <tr>
                            <td colspan="6" class="text-center text-muted">No employees found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>
