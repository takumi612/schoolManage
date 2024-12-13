<%@ page import="models.Major" %>
<%@ page import="java.util.List" %>
<%@ page import="services.implement.MajorService" %><%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/7/2024
  Time: 4:57 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    List<Major> majorList = (List<Major>) session.getAttribute("mayorList");
%>
<div class="modal fade" id="addUserModal" tabindex="-1">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form id="studentForm" enctype="multipart/form-data" action="${pageContext.request.contextPath}/studentAdmin" method="POST" class="needs-validation" novalidate>
                <div class="modal-header">
                    <h5 class="modal-title" id="modalTitle">Add New Student</h5>

                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                        <input type="hidden" id="studentId" name="studentId">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">First Name</label>

                                <input type="text" class="form-control" id="studentFName" name="studentFName" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Last Name</label>
                                <input type="text" class="form-control" id="studentLName" name="studentLName" required>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Date of Birth</label>
                                <input class="form-control " id="studentDob" name="studentDob" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Gender</label>
                                <select class="form-select" id="studentGender" name="studentGender" required>
                                    <option value="">Select Gender</option>
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                </select>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Entry Year</label>
                                <input class="form-control " id="studentEntryYear" name="studentEntryYear" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Major</label>
                                <select class="form-select form-select " name = "studentMajor" id="studentMajor" aria-label="Major">
                                    <option selected>Select Major</option>
                                    <%for(Major major : majorList){%>
                                        <option value="<%=major.getMajorName()%>"><%=major.getMajorName()%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control" id="studentEmail" name="studentEmail" placeholder="example@email.com" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Phone</label>
                            <input type="tel" class="form-control" id="studentPhone" name="studentPhone" minlength="10" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Profile Image</label>
                            <input type="file" class="form-control" id="imageInput" name="imageInput" accept="image/*" required>
                        </div>
                        <div class="preview-image-container text-center mb-4 ">
                            <div class="preview-image d-flex justify-content-center align-items-center border rounded">
                                <img id="imagePreview" src="" alt="Profile Preview" class="rounded-circle">
                            </div>
                        </div>

                        <script>
                            document.getElementById('imageInput').addEventListener('change', function(event) {
                                const file = event.target.files[0]; // Get the selected file
                                const preview = document.getElementById('imagePreview');

                                if (file) {
                                    const reader = new FileReader();
                                    reader.onload = function(e) {
                                        preview.src = e.target.result;  // Set the image source to the file's content
                                        preview.classList.remove('d-none'); // Show the image
                                    };
                                    reader.readAsDataURL(file); // Read the file as a data URL
                                } else {
                                    preview.classList.add('d-none'); // Hide preview if no file is selected
                                }
                            });
                            function setStudentImage(imageBase64) {
                                const preview = document.getElementById('imagePreview');
                                if (imageBase64) {
                                    preview.src = 'data:image/png;base64,' + imageBase64;
                                } else {
                                    preview.src = '/images/download.png'; // Ảnh default
                                }
                            }
                        </script>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save Change</button>
                </div>
         </form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#studentEntryYear').datepicker({
            format: 'yyyy-mm-dd', // Định dạng ngày
            autoclose: true,     // Tự động đóng sau khi chọn
            todayHighlight: true // Tô sáng ngày hiện tại
        });
        $('#studentDob').datepicker({
            format: 'yyyy-mm-dd', // Định dạng ngày
            autoclose: true,     // Tự động đóng sau khi chọn
            todayHighlight: true // Tô sáng ngày hiện tại
        });
    });
</script>
<jsp:include page="includes/footer.jsp"/>
