<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Profile" scope="request" />
<jsp:include page="../layout/header.jsp" />

<div class="row">
  <div class="col-md-8">
    <div class="card">
      <div class="card-header">
        <h5 class="mb-0">Personal Information</h5>
      </div>
      <div class="card-body">
        <form method="post" action="/employee/profile">
          <div class="row">
            <div class="col-md-6">
              <div class="mb-3">
                <label for="name" class="form-label">Full Name *</label>
                <input
                  type="text"
                  class="form-control"
                  id="name"
                  name="name"
                  value="${employee.user.name}"
                  required
                />
              </div>
            </div>
            <div class="col-md-6">
              <div class="mb-3">
                <label for="email" class="form-label">Email Address *</label>
                <input
                  type="email"
                  class="form-control"
                  id="email"
                  name="email"
                  value="${employee.user.email}"
                  required
                />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="mb-3">
                <label for="phone" class="form-label">Phone Number</label>
                <input
                  type="tel"
                  class="form-control"
                  id="phone"
                  name="phone"
                  value="${employee.phone}"
                />
              </div>
            </div>
            <div class="col-md-6">
              <div class="mb-3">
                <label class="form-label">Department</label>
                <input
                  type="text"
                  class="form-control"
                  value="${employee.department.name}"
                  readonly
                />
                <div class="form-text">Contact HR to change department</div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-6">
              <div class="mb-3">
                <label class="form-label">Date Joined</label>
                <input
                  type="text"
                  class="form-control"
                  value="${employee.dateJoined}"
                  readonly
                />
              </div>
            </div>
            <div class="col-md-6">
              <div class="mb-3">
                <label class="form-label">Account Status</label>
                <div class="form-control-plaintext">
                  <c:choose>
                    <c:when test="${employee.user.active}">
                      <span class="badge bg-success">Active</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge bg-danger">Inactive</span>
                    </c:otherwise>
                  </c:choose>
                </div>
              </div>
            </div>
          </div>

          <div class="d-flex justify-content-between">
            <a href="/employee/change-password" class="btn btn-outline-warning">
              <i class="fas fa-key"></i> Change Password
            </a>
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Update Profile
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <div class="col-md-4">
    <div class="card">
      <div class="card-header">
        <h5 class="mb-0">Quick Stats</h5>
      </div>
      <div class="card-body">
        <div class="text-center mb-3">
          <i class="fas fa-user-circle fa-5x text-muted"></i>
          <h5 class="mt-2">${employee.user.name}</h5>
          <p class="text-muted">${employee.department.name}</p>
        </div>

        <hr />

        <div class="row text-center">
          <div class="col-6">
            <h4 class="text-primary">${totalDocuments}</h4>
            <small class="text-muted">Documents</small>
          </div>
          <div class="col-6">
            <h4 class="text-success">
              <c:choose>
                <c:when test="${employee.dateJoined != null}">
                  <jsp:useBean id="now" class="java.util.Date" />
                  <jsp:useBean id="dateJoined" class="java.util.Date" />
                  <jsp:setProperty
                    name="dateJoined"
                    property="time"
                    value="${employee.dateJoined.toEpochDay() * 86400000}"
                  />
                  <fmt:formatNumber
                    value="${(now.time - dateJoined.time) / (1000 * 60 * 60 * 24)}"
                    maxFractionDigits="0"
                  />
                </c:when>
                <c:otherwise>0</c:otherwise>
              </c:choose>
            </h4>
            <small class="text-muted">Days</small>
          </div>
        </div>
      </div>
    </div>

    <div class="card mt-3">
      <div class="card-header">
        <h5 class="mb-0">Contact Information</h5>
      </div>
      <div class="card-body">
        <p>
          <i class="fas fa-envelope text-muted me-2"></i> ${employee.user.email}
        </p>
        <p>
          <i class="fas fa-phone text-muted me-2"></i>
          <c:choose>
            <c:when test="${not empty employee.phone}">
              ${employee.phone}
            </c:when>
            <c:otherwise>
              <span class="text-muted">Not provided</span>
            </c:otherwise>
          </c:choose>
        </p>
        <p>
          <i class="fas fa-building text-muted me-2"></i>
          ${employee.department.name}
        </p>
      </div>
    </div>
  </div>
</div>

<jsp:include page="../layout/footer.jsp" />
