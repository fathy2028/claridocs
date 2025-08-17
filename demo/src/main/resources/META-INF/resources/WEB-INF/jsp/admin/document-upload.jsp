<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Upload Document" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Upload New Document</h5>
            </div>
            <div class="card-body">
                <form method="post" action="/admin/documents/upload" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="employeeId" class="form-label">Select Employee *</label>
                        <select class="form-select" id="employeeId" name="employeeId" required>
                            <option value="">Choose Employee</option>
                            <c:forEach var="emp" items="${employees}">
                                <option value="${emp.id}">
                                    ${emp.user.name} - ${emp.department.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="title" class="form-label">Document Title *</label>
                        <input type="text" class="form-control" id="title" name="title" 
                               placeholder="Enter document title" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="type" class="form-label">Document Type *</label>
                        <select class="form-select" id="type" name="type" required>
                            <option value="">Select Type</option>
                            <c:forEach var="docType" items="${documentTypes}">
                                <option value="${docType}">${docType}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="file" class="form-label">Select File *</label>
                        <input type="file" class="form-control" id="file" name="file" 
                               accept=".pdf,.doc,.docx,.jpg,.jpeg,.png" required>
                        <div class="form-text">
                            Supported formats: PDF, DOC, DOCX, JPG, JPEG, PNG. Maximum size: 10MB
                        </div>
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="/admin/documents" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Documents
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-upload"></i> Upload Document
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
// File size validation
document.getElementById('file').addEventListener('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const maxSize = 10 * 1024 * 1024; // 10MB in bytes
        if (file.size > maxSize) {
            alert('File size must be less than 10MB');
            e.target.value = '';
        }
    }
});
</script>

<jsp:include page="../layout/footer.jsp"/>
