<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Change Password" scope="request"/>
<jsp:include page="../layout/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h5 class="mb-0">Change Password</h5>
            </div>
            <div class="card-body">
                <form method="post" action="/employee/change-password" id="changePasswordForm">
                    <div class="mb-3">
                        <label for="currentPassword" class="form-label">Current Password *</label>
                        <input type="password" class="form-control" id="currentPassword" 
                               name="currentPassword" required>
                    </div>
                    
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password *</label>
                        <input type="password" class="form-control" id="newPassword" 
                               name="newPassword" required minlength="6">
                        <div class="form-text">Password must be at least 6 characters long</div>
                    </div>
                    
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm New Password *</label>
                        <input type="password" class="form-control" id="confirmPassword" 
                               name="confirmPassword" required minlength="6">
                        <div id="passwordMismatch" class="text-danger" style="display: none;">
                            Passwords do not match
                        </div>
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="/employee/profile" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Profile
                        </a>
                        <button type="submit" class="btn btn-primary" id="submitBtn">
                            <i class="fas fa-key"></i> Change Password
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const passwordMismatch = document.getElementById('passwordMismatch');
    const submitBtn = document.getElementById('submitBtn');
    const form = document.getElementById('changePasswordForm');
    
    function validatePasswords() {
        if (newPassword.value && confirmPassword.value) {
            if (newPassword.value !== confirmPassword.value) {
                passwordMismatch.style.display = 'block';
                confirmPassword.classList.add('is-invalid');
                submitBtn.disabled = true;
                return false;
            } else {
                passwordMismatch.style.display = 'none';
                confirmPassword.classList.remove('is-invalid');
                confirmPassword.classList.add('is-valid');
                submitBtn.disabled = false;
                return true;
            }
        }
        return true;
    }
    
    newPassword.addEventListener('input', validatePasswords);
    confirmPassword.addEventListener('input', validatePasswords);
    
    form.addEventListener('submit', function(e) {
        if (!validatePasswords()) {
            e.preventDefault();
        }
    });
});
</script>

<jsp:include page="../layout/footer.jsp"/>
