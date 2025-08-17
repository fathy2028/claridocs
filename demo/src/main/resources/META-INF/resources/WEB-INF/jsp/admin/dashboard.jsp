<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Dashboard" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="row">
    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-primary shadow h-100 py-2">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                            Total Employees
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalEmployees}</div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-users fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-success shadow h-100 py-2">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                            Total Departments
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">${totalDepartments}</div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-building fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-info shadow h-100 py-2">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                            Average Tenure (Days)
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <c:choose>
                                <c:when test="${averageTenure != null}">
                                    ${Math.round(averageTenure)}
                                </c:when>
                                <c:otherwise>0</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-calendar fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="col-xl-3 col-md-6 mb-4">
        <div class="card border-left-warning shadow h-100 py-2">
            <div class="card-body">
                <div class="row no-gutters align-items-center">
                    <div class="col mr-2">
                        <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                            Employees Without Documents
                        </div>
                        <div class="h5 mb-0 font-weight-bold text-gray-800">
                            <c:choose>
                                <c:when test="${employeesWithoutDocuments != null}">
                                    ${employeesWithoutDocuments.size()}
                                </c:when>
                                <c:otherwise>0</c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="col-auto">
                        <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Charts Row -->
<div class="row">
    <!-- Employees by Department Chart -->
    <div class="col-xl-6 col-lg-6">
        <div class="card shadow mb-4">
            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                <h6 class="m-0 font-weight-bold text-primary">Employees by Department</h6>
            </div>
            <div class="card-body">
                <canvas id="employeesByDepartmentChart"></canvas>
            </div>
        </div>
    </div>

    <!-- Documents by Type Chart -->
    <div class="col-xl-6 col-lg-6">
        <div class="card shadow mb-4">
            <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                <h6 class="m-0 font-weight-bold text-primary">Documents by Type</h6>
            </div>
            <div class="card-body">
                <canvas id="documentsByTypeChart"></canvas>
            </div>
        </div>
    </div>
</div>

<script>
// Employees by Department Chart
const empDeptCtx = document.getElementById('employeesByDepartmentChart').getContext('2d');
const empDeptChart = new Chart(empDeptCtx, {
    type: 'bar',
    data: {
        labels: [
            <c:forEach var="item" items="${employeeCountByDepartment}" varStatus="status">
                '${item[0]}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ],
        datasets: [{
            label: 'Number of Employees',
            data: [
                <c:forEach var="item" items="${employeeCountByDepartment}" varStatus="status">
                    ${item[1]}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});

// Documents by Type Chart
const docTypeCtx = document.getElementById('documentsByTypeChart').getContext('2d');
const docTypeChart = new Chart(docTypeCtx, {
    type: 'doughnut',
    data: {
        labels: [
            <c:forEach var="item" items="${documentCountByType}" varStatus="status">
                '${item[0]}'<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ],
        datasets: [{
            data: [
                <c:forEach var="item" items="${documentCountByType}" varStatus="status">
                    ${item[1]}<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],
            backgroundColor: [
                'rgba(255, 99, 132, 0.2)',
                'rgba(54, 162, 235, 0.2)',
                'rgba(255, 205, 86, 0.2)',
                'rgba(75, 192, 192, 0.2)'
            ],
            borderColor: [
                'rgba(255, 99, 132, 1)',
                'rgba(54, 162, 235, 1)',
                'rgba(255, 205, 86, 1)',
                'rgba(75, 192, 192, 1)'
            ],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true
    }
});
</script>

<jsp:include page="../layout/footer.jsp"/>
