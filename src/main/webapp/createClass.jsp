<%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/7/2024
  Time: 4:57 PM
  To change this template use File | Settings | File Templates.
--%>

<div class="modal fade" id="addClassModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Class</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="studentForm" class="needs-validation" novalidate>
                    <div class="row mb-3">
                            <label class="form-label">Class Name</label>
                            <input type="text" class="form-control" required>
                    </div>
                    <div class="row mb-3">
                        <label class="form-label">Lecturer Name</label>
                        <input type="text" class="form-control" required>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Major</label>
                            <select class="form-select" required>
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Course Code</label>
                            <select class="form-select" required>
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Start Date</label>
                            <input type="date" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">End Date</label>
                            <input type="date" class="form-control" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <span></span>
                        </div>
                        <div class="col-md-6">
                            <span></span>
                        </div>
                    </div>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="submit" class="btn btn-primary">Save Change</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="includes/footer.jsp"/>
