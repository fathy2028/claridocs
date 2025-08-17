<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="My Documents" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h2>My Documents</h2>
</div>

<!-- Filter -->
<div class="card mb-4">
    <div class="card-body">
        <form method="get" action="/employee/documents" class="row g-3">
            <div class="col-md-6">
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
            <div class="col-md-6 d-flex align-items-end">
                <button type="submit" class="btn btn-outline-primary me-2">
                    <i class="fas fa-filter"></i> Filter
                </button>
                <a href="/employee/documents" class="btn btn-outline-secondary">
                    <i class="fas fa-times"></i> Clear
                </a>
            </div>
        </form>
    </div>
</div>

<!-- Documents Table -->
<div class="card">
    <div class="card-header">
        <h5 class="mb-0">All My Documents</h5>
    </div>
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Title</th>
                        <th>Type</th>
                        <th>Upload Date</th>
                        <th>File Size</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="doc" items="${documents}">
                        <tr>
                            <td><strong>${doc.title}</strong></td>
                            <td>
                                <span class="badge bg-info">${doc.type}</span>
                            </td>
                            <td>
                                ${doc.uploadedAt.toString().substring(0,10)} ${doc.uploadedAt.toString().substring(11,16)}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${doc.sizeBytes < 1024}">
                                        ${doc.sizeBytes} B
                                    </c:when>
                                    <c:when test="${doc.sizeBytes < 1048576}">
                                        <fmt:formatNumber value="${doc.sizeBytes / 1024}" maxFractionDigits="1"/> KB
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${doc.sizeBytes / 1048576}" maxFractionDigits="1"/> MB
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="/documents/${doc.id}/download" class="btn btn-sm btn-outline-success">
                                    <i class="fas fa-download"></i> Download
                                </a>
                                <a href="/documents/${doc.id}/view" class="btn btn-sm btn-outline-primary" target="_blank">
                                    <i class="fas fa-eye"></i> View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty documents}">
                        <tr>
                            <td colspan="5" class="text-center text-muted">
                                <div class="py-4">
                                    <i class="fas fa-folder-open fa-3x text-muted mb-3"></i>
                                    <h5>No documents found</h5>
                                    <p>Contact your HR administrator if you need documents to be uploaded.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp"/>
