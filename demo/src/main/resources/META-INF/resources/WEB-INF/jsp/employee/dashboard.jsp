<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Employee Dashboard" scope="request" />
<jsp:include page="../layout/header.jsp" />

<div class="row">
  <div class="col-md-12">
    <div class="alert alert-info">
      <h5><i class="fas fa-user"></i> Welcome, ${employee.user.name}!</h5>
      <p class="mb-0">
        Department: <strong>${employee.department.name}</strong> | Date Joined:
        <strong>${employee.dateJoined}</strong>
      </p>
    </div>
  </div>
</div>

<div class="row">
  <div class="col-xl-3 col-md-6 mb-4">
    <div class="card border-left-primary shadow h-100 py-2">
      <div class="card-body">
        <div class="row no-gutters align-items-center">
          <div class="col mr-2">
            <div
              class="text-xs font-weight-bold text-primary text-uppercase mb-1"
            >
              Total Documents
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${totalDocuments}
            </div>
          </div>
          <div class="col-auto">
            <i class="fas fa-file-alt fa-2x text-gray-300"></i>
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
            <div
              class="text-xs font-weight-bold text-success text-uppercase mb-1"
            >
              Contracts
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${contractCount}
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
              Payslips
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">
              ${payslipCount}
            </div>
          </div>
          <div class="col-auto">
            <i class="fas fa-money-check fa-2x text-gray-300"></i>
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
              ID Documents
            </div>
            <div class="h5 mb-0 font-weight-bold text-gray-800">${idCount}</div>
          </div>
          <div class="col-auto">
            <i class="fas fa-id-card fa-2x text-gray-300"></i>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Recent Documents -->
<div class="row">
  <div class="col-12">
    <div class="card shadow mb-4">
      <div
        class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
      >
        <h6 class="m-0 font-weight-bold text-primary">Recent Documents</h6>
        <a href="/employee/documents" class="btn btn-sm btn-primary">
          <i class="fas fa-eye"></i> View All
        </a>
      </div>
      <div class="card-body">
        <div class="table-responsive">
          <table class="table table-striped">
            <thead>
              <tr>
                <th>Title</th>
                <th>Type</th>
                <th>Upload Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="doc" items="${documents}" end="4">
                <tr>
                  <td><strong>${doc.title}</strong></td>
                  <td>
                    <span class="badge bg-info">${doc.type}</span>
                  </td>
                  <td>${doc.uploadedAt.toString().substring(0,10)}</td>
                  <td>
                    <a
                      href="/documents/${doc.id}/download"
                      class="btn btn-sm btn-outline-success"
                    >
                      <i class="fas fa-download"></i> Download
                    </a>
                    <a
                      href="/documents/${doc.id}/view"
                      class="btn btn-sm btn-outline-primary"
                      target="_blank"
                    >
                      <i class="fas fa-eye"></i> View
                    </a>
                  </td>
                </tr>
              </c:forEach>
              <c:if test="${empty documents}">
                <tr>
                  <td colspan="4" class="text-center text-muted">
                    No documents available. Contact your HR administrator.
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
