<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Document Management" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>Document Management</h2>
    <a href="/admin/documents/upload" class="btn btn-primary">
        <i class="fas fa-upload"></i> Upload Document
    </a>
</div>

<!-- Search and Filter -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="/admin/documents" class="row g-3">
            <div class="col-md-3">
                <label for="employeeId" class="form-label">Filter by Employee</label>
                <select class="form-select" id="employeeId" name="employeeId">
                    <option value="">All Employees</option>
                    <c:forEach var="emp" items="${employees}">
                        <option value="${emp.id}"
                                <c:if test="${emp.id == selectedEmployeeId}">selected</c:if>>
                            ${emp.user.name} - ${emp.department.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <label for="type" class="form-label">Filter by Type</label>
                <select class="form-select" id="type" name="type">
                    <option value="">All Types</option>
                    <c:forEach var="docType" items="${documentTypes}">
                        <option value="${docType}"
                                <c:if test="${docType == selectedType}">selected</c:if>>
                            ${docType}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <label for="uploadDate" class="form-label">Date Uploaded</label>
                <input type="date" class="form-control" id="uploadDate" name="uploadDate"
                       value="${uploadDate}" title="Filter by upload date (YYYY-MM-DD format)">
                <c:if test="${not empty uploadDate}">
                    <small class="text-muted">Filtering for: ${uploadDate}</small>
                </c:if>
            </div>
            <div class="col-md-3 d-flex align-items-end">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-filter"></i> Filter
                </button>
                <a href="/admin/documents" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Clear
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Documents Table -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">All Documents</h5>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Title</th>
                        <th>Employee</th>
                        <th>Department</th>
                        <th>Type</th>
                        <th>Upload Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="doc" items="${documents}">
                        <tr>
                            <td><strong>${doc.title}</strong></td>
                            <td>${doc.employee.user.name}</td>
                            <td>
                                <span class="badge bg-secondary">${doc.employee.department.name}</span>
                            </td>
                            <td>
                                <span class="badge bg-info">${doc.type}</span>
                            </td>
                            <td>
                                ${doc.uploadedAt.toString().substring(0,10)} ${doc.uploadedAt.toString().substring(11,16)}
                            </td>
                            <td>
                                <a href="/documents/${doc.id}/download" class="btn btn-sm btn-outline-success">
                                    <i class="fas fa-download"></i> Download
                                </a>
                                <form method="post" action="/admin/documents/${doc.id}/delete" 
                                      style="display: inline;" 
                                      onsubmit="return confirm('Are you sure you want to delete this document?')">
                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                        <i class="fas fa-trash"></i> Delete
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty documents}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                <i class="fas fa-file-alt fa-2x mb-2"></i><br>
                                <c:choose>
                                    <c:when test="${not empty uploadDate}">
                                        <strong>No documents found for date: ${uploadDate}</strong><br>
                                        <small>Try selecting a different date or clear the filter to see all documents.</small>
                                    </c:when>
                                    <c:when test="${not empty selectedEmployeeId or not empty selectedType}">
                                        <strong>No documents found matching the selected filters.</strong><br>
                                        <small>Try adjusting your filters or clear them to see all documents.</small>
                                    </c:when>
                                    <c:otherwise>
                                        <strong>No documents have been uploaded yet.</strong>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>
