<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Dashboard" scope="request" />
<jsp:include page="../layout/header.jsp" />

<!-- 2x2 Grid Dashboard Cards -->
<div class="container-fluid h-100">
  <!-- First Row - Two Cards -->
  <div class="row mb-4" style="height: 35vh">
    <div class="col-6">
      <div class="card border-left-primary shadow-lg h-100">
        <div class="card-body py-5 px-5 d-flex align-items-center">
          <div class="row no-gutters align-items-center w-100">
            <div class="col mr-4">
              <div
                class="text-lg font-weight-bold text-primary text-uppercase mb-3"
              >
                Total Employees
              </div>
              <div class="display-4 mb-0 font-weight-bold text-gray-800">
                ${totalEmployees}
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-users fa-5x text-primary opacity-75"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="col-6">
      <div class="card border-left-success shadow-lg h-100">
        <div class="card-body py-5 px-5 d-flex align-items-center">
          <div class="row no-gutters align-items-center w-100">
            <div class="col mr-4">
              <div
                class="text-lg font-weight-bold text-success text-uppercase mb-3"
              >
                Total Departments
              </div>
              <div class="display-4 mb-0 font-weight-bold text-gray-800">
                ${totalDepartments}
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-building fa-5x text-success opacity-75"></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Second Row - Two Cards -->
  <div class="row" style="height: 35vh">
    <div class="col-6">
      <div class="card border-left-info shadow-lg h-100">
        <div class="card-body py-5 px-5 d-flex align-items-center">
          <div class="row no-gutters align-items-center w-100">
            <div class="col mr-4">
              <div
                class="text-lg font-weight-bold text-info text-uppercase mb-3"
              >
                Average Tenure (Days)
              </div>
              <div class="display-4 mb-0 font-weight-bold text-gray-800">
                <c:choose>
                  <c:when test="${averageTenure != null}">
                    ${Math.round(averageTenure)}
                  </c:when>
                  <c:otherwise>0</c:otherwise>
                </c:choose>
              </div>
            </div>
            <div class="col-auto">
              <i class="fas fa-calendar fa-5x text-info opacity-75"></i>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="col-6">
      <div class="card border-left-warning shadow-lg h-100">
        <div class="card-body py-5 px-5 d-flex align-items-center">
          <div class="row no-gutters align-items-center w-100">
            <div class="col mr-4">
              <div
                class="text-lg font-weight-bold text-warning text-uppercase mb-3"
              >
                Employees Without Documents
              </div>
              <div class="display-4 mb-0 font-weight-bold text-gray-800">
                <c:choose>
                  <c:when test="${employeesWithoutDocuments != null}">
                    ${employeesWithoutDocuments.size()}
                  </c:when>
                  <c:otherwise>0</c:otherwise>
                </c:choose>
              </div>
            </div>
            <div class="col-auto">
              <i
                class="fas fa-exclamation-triangle fa-5x text-warning opacity-75"
              ></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../layout/footer.jsp" />
