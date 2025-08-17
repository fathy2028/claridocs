<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Reports & Analytics" scope="request" />
<jsp:include page="../layout/header.jsp" />

<!-- Summary Cards -->
<div class="row mb-4">
  <div class="col-xl-3 col-md-6 mb-4">
    <div class="card border-left-primary shadow h-100 py-2">
      <div class="card-body">
        <div class="row no-gutters align-items-center">
          <div class="col mr-2">
            <div
              class="text-xs font-weight-bold text-primary text-uppercase mb-1"
            >
              Total Employees
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${totalEmployees}
            </div>
          </div>
          <div class="col-auto">
            <i class="fas fa-users fa-2x text-gray-300"></i>
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
            <div
              class="text-xs font-weight-bold text-warning text-uppercase mb-1"
            >
              Employees Without Documents
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${employeesWithoutDocuments.size()}
            </div>
          </div>
          <div class="col-auto">
            <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="col-xl-3 col-md-6 mb-4">
    <div class="card border-left-danger shadow h-100 py-2">
      <div class="card-body">
        <div class="row no-gutters align-items-center">
          <div class="col mr-2">
            <div
              class="text-xs font-weight-bold text-danger text-uppercase mb-1"
            >
              Employees Without Contracts
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${employeesWithoutContracts.size()}
            </div>
          </div>
          <div class="col-auto">
            <i class="fas fa-file-contract fa-2x text-gray-300"></i>
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
</div>

<!-- Charts Row -->
<div class="row">
  <!-- Employees by Department Chart -->
  <div class="col-xl-6 col-lg-6">
    <div class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
          Employees by Department
        </h6>
      </div>
      <div class="card-body">
        <canvas id="employeesByDepartmentChart"></canvas>
      </div>
    </div>
  </div>

  <!-- Document Uploads by Month Chart -->
  <div class="col-xl-6 col-lg-6">
    <div class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-primary">
          Document Uploads by Month
        </h6>
      </div>
      <div class="card-body">
        <canvas id="documentUploadsByMonthChart"></canvas>
      </div>
    </div>
  </div>
</div>

<!-- Detailed Tables -->
<div class="row">
  <!-- Employees Without Documents -->
  <div class="col-xl-6 col-lg-6">
    <div class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-warning">
          Employees Without Documents
        </h6>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-sm">
            <thead>
              <tr>
                <th>Name</th>
                <th>Department</th>
                <th>Date Joined</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="emp" items="${employeesWithoutDocuments}">
                <tr>
                  <td>${emp.user.name}</td>
                  <td>${emp.department.name}</td>
                  <td>${emp.dateJoined}</td>
                </tr>
              </c:forEach>
              <c:if test="${empty employeesWithoutDocuments}">
                <tr>
                  <td colspan="3" class="text-center text-muted">
                    All employees have documents
                  </td>
                </tr>
              </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

  <!-- Employees Without Contracts -->
  <div class="col-xl-6 col-lg-6">
    <div class="card shadow mb-4">
      <div class="card-header py-3">
        <h6 class="m-0 font-weight-bold text-danger">
          Employees Without Contracts
        </h6>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-sm">
            <thead>
              <tr>
                <th>Name</th>
                <th>Department</th>
                <th>Date Joined</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="emp" items="${employeesWithoutContracts}">
                <tr>
                  <td>${emp.user.name}</td>
                  <td>${emp.department.name}</td>
                  <td>${emp.dateJoined}</td>
                </tr>
              </c:forEach>
              <c:if test="${empty employeesWithoutContracts}">
                <tr>
                  <td colspan="3" class="text-center text-muted">
                    All employees have contracts
                  </td>
                </tr>
              </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  // Employees by Department Chart
  const empDeptCtx = document
    .getElementById("employeesByDepartmentChart")
    .getContext("2d");
  const empDeptChart = new Chart(empDeptCtx, {
    type: "bar",
    data: {
      labels: [
        <c:forEach
          var="item"
          items="${employeeCountByDepartment}"
          varStatus="status"
        >
          '${item[0]}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>,
      ],
      datasets: [
        {
          label: "Number of Employees",
          data: [
            <c:forEach
              var="item"
              items="${employeeCountByDepartment}"
              varStatus="status"
            >
              ${item[1]}
              <c:if test="${!status.last}">,</c:if>
            </c:forEach>,
          ],
          backgroundColor: "rgba(54, 162, 235, 0.2)",
          borderColor: "rgba(54, 162, 235, 1)",
          borderWidth: 1,
        },
      ],
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });

  // Document Uploads by Month Chart
  const docUploadCtx = document
    .getElementById("documentUploadsByMonthChart")
    .getContext("2d");
  const docUploadChart = new Chart(docUploadCtx, {
    type: "line",
    data: {
      labels: [
        <c:forEach
          var="item"
          items="${documentUploadsByMonth}"
          varStatus="status"
        >
          '${item[0]}/${item[1]}'<c:if test="${!status.last}">,</c:if>
        </c:forEach>,
      ],
      datasets: [
        {
          label: "Documents Uploaded",
          data: [
            <c:forEach
              var="item"
              items="${documentUploadsByMonth}"
              varStatus="status"
            >
              ${item[2]}
              <c:if test="${!status.last}">,</c:if>
            </c:forEach>,
          ],
          backgroundColor: "rgba(75, 192, 192, 0.2)",
          borderColor: "rgba(75, 192, 192, 1)",
          borderWidth: 2,
          fill: true,
        },
      ],
    },
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  });
</script>

<jsp:include page="../layout/footer.jsp" />
