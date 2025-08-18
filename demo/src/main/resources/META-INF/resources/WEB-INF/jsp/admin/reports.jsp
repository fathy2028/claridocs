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

<jsp:include page="../layout/footer.jsp" />
