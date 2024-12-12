<%@ page import="models.User" %>
<%@ page import="models.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Major" %>
<%@ page import="services.implement.StudentService" %>
<%@ page import="services.implement.MajorService" %>
<%@ page import="dtos.StudentDto" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDate" %>

<%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/6/2024
  Time: 5:39 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="includes/header.jsp"/>

<%
    StudentDto studentDto = (StudentDto) request.getAttribute("searchDto");

    String selectedYear;
    String selectedCode;
    String selectedMajor;
    if(studentDto!= null) {
         selectedYear = studentDto.getStudentEntryYear() != null ? studentDto.getStudentEntryYear() : "";
         selectedMajor = studentDto.getMajor() != null ? studentDto.getMajor() : "";
         selectedCode = studentDto.getStudentCode() != null ? studentDto.getStudentCode() : "";
    }else {
         selectedYear = "";
         selectedCode = "";
         selectedMajor = "";
    }
    User user = (User) session.getAttribute("user");
    List<StudentDto> studentList = (List<StudentDto>) request.getAttribute("studentList");

    StudentService studentService = new StudentService();
    MajorService majorService = new MajorService();

    List<Major> majorList = majorService.selectAll();
    List<Integer> yearList  = studentService.listYear();

    if(user == null){
        response.sendRedirect("index.jsp");
        return;
    }
    if(!user.getUserRole()){
        response.sendRedirect("authen_error.jsp");
        return;
    }

    int pageNumber = (Integer) request.getAttribute("pageNumber");
    int elementsPerPage = (Integer) request.getAttribute("elementsPerPage");
    Long noOfRecord = (Long) request.getAttribute("noOfRecord");

    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();

    String nextPageUrl = url + "/studentAdmin?page=" + (pageNumber+1) +"&selectedYear=" + (selectedYear) + "&selectedMajor="+selectedMajor.replace(" ","+") + "&selectedCode=" + selectedCode;
    String prevPageUrl = url + "/studentAdmin?page=" + (pageNumber-1)+"&selectedYear=" + (selectedYear) + "&selectedMajor="+selectedMajor.replace(" ","+") + "&selectedCode=" + selectedCode;

    if ((pageNumber - 1) <= 0){
        prevPageUrl = "";
    }
    if ((pageNumber + 1) > noOfRecord){
        nextPageUrl = "";
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


%>

<div class="container text-center">
    <h4>Student Infomation</h4>
</div>

    <div class="container-md my-4">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-white">
                <h5 class="mb-0">Search for Students</h5>
            </div>
            <form action = "studentAdmin" method="GET">
                <div class="card-body">
                    <div class="mb-4">
                        <h6 class="text-muted mb-3">Search by List</h6>
                        <div class="row g-3">
                            <div class="col-md-3">
                                <select class="form-select form-select" name = "selectedYear" aria-label="Year">
                                    <option value="">Select Year</option>
                                    <%for( Integer years: yearList){%>
                                    <option value="<%=years%>" <%=( !selectedYear.isEmpty() && String.valueOf(years).equals(selectedYear)) ? "selected" : "" %>><%=years%></option>
                                    <%
                                        }
                                    %>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <select class="form-select form-select " name = "selectedMajor" aria-label="Major">
                                    <option value="">Select Major</option>
                                    <%for(Major major : majorList){%>
                                        <option value="<%=major.getMajorName()%>" <%= (!selectedMajor.isEmpty() && major.getMajorName().equalsIgnoreCase(selectedMajor)) ? "selected" : "" %>><%=major.getMajorName()%></option>
                                        <%
                                        }
                                    %>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div>
                        <h6 class="text-muted mb-3">Specific Search</h6>
                        <div class="row g-3">
                            <div class="col-md-3">
                                <input type="text" class="form-control form-control " name="selectedCode" placeholder="Enter Student Code" value= "<%= selectedCode%>">
                            </div>
                            <div class="col-md-3"></div>
                            <div class="col-md-3"></div>
                            <div class="col-md-3">
                                <button class="btn btn-warning btn w-50" type="submit">Search</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    <div class="row g-3">
            <div class="col-sm-3">
                <button class="btn btn-primary" onclick="openAddModel()">
                    <i class="bi bi-plus-circle me-2"></i>Add New Student
                </button>
            </div>
    </div>

    <div class="table-responsive">
        <table class="table table-hover">
            <thead class="bg-primary text-white">
            <tr>
                <th>Student Code</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Entry year</th>
                <th>DOB</th>
                <th>Gender</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Status</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%for (StudentDto student : studentList) {
            %>
            <tr>
                <td><%= student.getStudentCode()%></td>
                <td><%= student.getStudentFirstName()%></td>
                <td><%= student.getStudentLastName()%></td>
                <td><%= student.getStudentEntryYear()%></td>
                <td><%= student.getStudentDoB()%></td>
                <td><%= student.getStudentGender()%></td>
                <td><%= student.getStudentPhone()%></td>
                <td><%= student.getStudentEmail()%></td>
                <td><%= student.getStudentStatus()%></td>

                <td>
                    <div class="btn-group">
                        <!-- Nút Xem -->
<%--                        <form action="studentAdmin" method="POST" style="display:inline;">--%>
                            <a href="<%=url + "/studentInfo?studentCode=" + student.getStudentCode()%>">
                            <button type="submit" class="btn btn-info text-white" title="View">
                                <i class="bi bi-eye fs-5"></i>
                            </button>
                            </a>
<%--                        </form>--%>

                        <!-- Nút Sửa -->
                        <button class="btn btn-warning" title="Edit" onclick="editStudent('<%=student.getStudentID()%>',
                                                                                        '<%=student.getStudentFirstName()%>',
                                                                                        '<%=student.getStudentLastName()%>',
                                                                                        '<%=LocalDate.parse(student.getStudentEntryYear(), formatter)%>',
                                                                                        '<%=LocalDate.parse(student.getStudentDoB(), formatter)%>',
                                                                                        '<%=student.getStudentGender()%>',
                                                                                        '<%=student.getStudentPhone()%>',
                                                                                        '<%=student.getStudentEmail()%>',
                                                                                        '<%=student.getMajor()%>',
                                                                                        '<%=student.getStudentImageBase64()%>')"
                        >
                            <i class="bi bi-pencil fs-5"></i>
                        </button>
                    </div>
                </td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>
        <div class="float-end align-items-center mt-2">
            <a href="<%=prevPageUrl%>" >
                <button type = "submit" class="btn btn-outline-primary">&lt;&lt; Prev. Page</button>
            </a>
            <span class="p-2"><%= pageNumber%> / <%= noOfRecord%></span>
            <a href="<%=nextPageUrl%>" >
                <button type = "submit" class="btn btn-outline-primary" >Next Page &gt;&gt;</button>
            </a>
        </div>
<%--    <div class="d-flex gap-2">--%>
<%--        <button class="btn btn-primary"><i class="bi bi-download"></i> Export</button>--%>
<%--        <button class="btn btn-primary"><i class="bi bi-upload"></i> Import</button>--%>
<%--    </div>--%>
</div>

<script>
    // Mở modal để thêm sinh viên mới
    function openAddModel(){
        $('#studentForm')[0].reset();
        $('#studentId').val('');
        $('#modalTitle').text('Add Student');
        $('#addUserModal').modal('show');
    }

    // Mở modal để chỉnh sửa sinh viên
    function editStudent(id,fName,lName,entryYear,dob,gender,phone,email,major,image) {
        $('#studentId').val(id.toString());
        $('#studentFName').val(fName.toString());
        $('#studentLName').val(lName.toString());
        $('#studentEmail').val(email.toString());
        $('#studentPhone').val(phone.toString());
        $('#studentGender').val(gender.toString());

        // Xem lại đoạn này
        $('#studentEntryYear').datepicker('update', entryYear);

        $('#studentDob').datepicker('update', dob);

        $('#studentMajor').val(major);
        $('#action').val("edit");

        setStudentImage(image);

        // Debug: In ra các giá trị
        console.log('ID:', id);
        console.log('Name:', fName, lName);
        console.log('Date', entryYear, dob);

        $('#modalTitle').text('Edit Student');
        $('#addUserModal').modal('show');
    }

    $(document).ready(function() {
        $('#studentForm').on('submit', function(e) {
            e.preventDefault(); // Ngăn submit form ngay lập tức

            // Lấy giá trị từ form
            var studentId = $('#studentId').val();

            // Hiển thị confirm dialog
            var confirmUpdate = confirm(studentId ?
                "Bạn có chắc chắn muốn cập nhật thông tin sinh viên này không?" :
                "Bạn có chắc chắn muốn thêm sinh viên mới này không?"
            );

            // Nếu người dùng xác nhận thì mới submit form
            if (confirmUpdate) {
                // Submit form theo cách thông thường
                this.submit();
            }
        });
    });
</script>

<jsp:include page="createStudent.jsp"/>
<jsp:include page="includes/footer.jsp"/>


