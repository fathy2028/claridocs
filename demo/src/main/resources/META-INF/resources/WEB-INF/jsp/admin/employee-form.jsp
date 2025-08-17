<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Add New Employee" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Add New Employee</h5>
            </div>
            <div class="card-body">
                <form method="post" action="/admin/employees">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="name" class="form-label">Full Name *</label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email Address *</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="password" class="form-label">Password *</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" id="phone" name="phone">
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="departmentId" class="form-label">Department *</label>
                                <select class="form-select" id="departmentId" name="departmentId" required>
                                    <option value="">Select Department</option>
                                    <c:forEach var="dept" items="${departments}">
                                        <option value="${dept.id}">${dept.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="dateJoined" class="form-label">Date Joined *</label>
                                <input type="date" class="form-control" id="dateJoined" name="dateJoined" required>
                            </div>
                        </div>
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="/admin/employees" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Employees
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Create Employee
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
// Set default date to today
document.getElementById('dateJoined').valueAsDate = new Date();
</script>

<jsp:include page="../layout/footer.jsp"/>
