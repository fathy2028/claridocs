<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt"
uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Department Management" scope="request" />
<jsp:include page="../layout/header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4">
  <h2>Department Management</h2>
  <button
    type="button"
    class="btn btn-primary"
    data-bs-toggle="modal"
    data-bs-target="#addDepartmentModal"
  >
    <i class="fas fa-plus"></i> Add Department
  </button>
</div>

<!-- Departments Table -->
<div class="card">
  <div class="card-header">
    <h5 class="mb-0">All Departments</h5>
  </div>
  <div class="card-body">
    <div class="table-responsive">
      <table class="table table-striped table-hover">
        <thead class="table-dark">
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Created Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="dept" items="${departments}">
            <tr>
              <td><strong>${dept.name}</strong></td>
              <td>${dept.description}</td>
              <td>${dept.createdAt.toString().substring(0,10)}</td>
              <td>
                <button
                  type="button"
                  class="btn btn-sm btn-outline-primary"
                  onclick="editDepartment('${dept.id}', '${dept.name}', '${dept.description}')"
                >
                  <i class="fas fa-edit"></i> Edit
                </button>
                <form
                  method="post"
                  action="/admin/departments/${dept.id}/delete"
                  style="display: inline"
                  onsubmit="return confirm('Are you sure you want to delete this department?')"
                >
                  <button type="submit" class="btn btn-sm btn-outline-danger">
                    <i class="fas fa-trash"></i> Delete
                  </button>
                </form>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty departments}">
            <tr>
              <td colspan="4" class="text-center text-muted">
                No departments found
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- Add Department Modal -->
<div class="modal fade" id="addDepartmentModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add New Department</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
        ></button>
      </div>
      <form method="post" action="/admin/departments">
        <div class="modal-body">
          <div class="mb-3">
            <label for="name" class="form-label">Department Name *</label>
            <input
              type="text"
              class="form-control"
              id="name"
              name="name"
              required
            />
          </div>
          <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea
              class="form-control"
              id="description"
              name="description"
              rows="3"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button type="submit" class="btn btn-primary">Add Department</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Edit Department Modal -->
<div class="modal fade" id="editDepartmentModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Department</h5>
        <button
          type="button"
          class="btn-close"
          data-bs-dismiss="modal"
        ></button>
      </div>
      <form method="post" id="editDepartmentForm">
        <div class="modal-body">
          <div class="mb-3">
            <label for="editName" class="form-label">Department Name *</label>
            <input
              type="text"
              class="form-control"
              id="editName"
              name="name"
              required
            />
          </div>
          <div class="mb-3">
            <label for="editDescription" class="form-label">Description</label>
            <textarea
              class="form-control"
              id="editDescription"
              name="description"
              rows="3"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            data-bs-dismiss="modal"
          >
            Cancel
          </button>
          <button type="submit" class="btn btn-primary">
            Update Department
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  function editDepartment(id, name, description) {
    document.getElementById("editName").value = name;
    document.getElementById("editDescription").value = description || "";
    document.getElementById("editDepartmentForm").action =
      "/admin/departments/" + id + "/edit";

    var editModal = new bootstrap.Modal(
      document.getElementById("editDepartmentModal")
    );
    editModal.show();
  }
</script>

<jsp:include page="../layout/footer.jsp" />
