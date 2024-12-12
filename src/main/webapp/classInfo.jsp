<%@ page import="models.Classroom" %>
<%@ page import="models.User" %>
<%@ page import="models.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="dtos.StudentDto" %><%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/8/2024
  Time: 11:13 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int pageNumber = (Integer) request.getAttribute("pageNumber");
    int elementsPerPage = (Integer) request.getAttribute("elementsPerPage");
    Long noOfRecord = (Long) request.getAttribute("noOfRecord");
    int classId = (Integer) request.getAttribute("classID");
    StudentDto studentDto = (StudentDto) request.getAttribute("studentDto");

    String selectedGender;
    String selectedName;

    if(studentDto!= null) {
        selectedName = studentDto.getStudentLastName() != null ? studentDto.getStudentLastName() : "";
        selectedGender = studentDto.getStudentGender() != null ? studentDto.getStudentGender() : "";
    }else {
        selectedGender = "";
        selectedName = "";
    }


    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();

    User user = (User) session.getAttribute("user");
    Classroom classroom = (Classroom) request.getAttribute("classroom");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");

    if(user == null || classroom == null){
        response.sendRedirect("index.jsp");
        return;
    }
    String nextPageUrl ;
    String prevPageUrl ;
    if ((pageNumber - 1) <= 0){
        prevPageUrl = "";
    }else {
        prevPageUrl = url + "/classInfo?classID="+classId+"&page=" + (pageNumber-1) +"&selectedName="+selectedName + "&selectedGender="+selectedGender;
    }
    if ((pageNumber + 1) > noOfRecord){
        nextPageUrl = "";
    }else {
        nextPageUrl = url + "/classInfo?classID="+classId+"&page=" + (pageNumber+1)+"&selectedName="+selectedName + "&selectedGender="+selectedGender;
    }
%>

<jsp:include page="includes/header.jsp"/>

<div class="container my-4">
    <div class="card mb-4 shadow">
        <div class="card-body">
            <div class="row">
                <div class="col-md-9">
                    <h4 class="mb-3"><%= classroom.getCrs().getCourseName()%></h4>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Course Code:</strong>  <%= classroom.getCrs().getCourseCode()%></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Lecturer:</strong>  <%= classroom.getLec().getLecLname()%></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Class Course:</strong>  <%= classroom.getClassName()%></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Credit:</strong>  <%= classroom.getCrs().getCourseCredit()%></p>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Start Date:</strong>  <%=classroom.getStartDate()%></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>End Date:</strong>  <%= classroom.getEndDate()%></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container-fluid p-2">
        <form action = "classInfo" method="GET">
            <input type="hidden" name="classID" value="<%=classId%>">
            <div class="card shadow-sm mb-3">
                <div class="card-header bg-white">
                    <h4 class="mb-1">Search for Students</h4>
                </div>
                <div class="row g-2 p-3">
                    <div class="col-md-3">
                        <select class="form-select form-select" name="selectedGender" aria-label="Year">
                            <option selected value="">Select Gender:</option>
                            <option value="Male" <%= selectedGender.equalsIgnoreCase("Male") ? "selected" : "" %> >Male</option>
                            <option value="Female" <%= selectedGender.equalsIgnoreCase("Female") ? "selected" : "" %> >Female</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label>
                            <input type="text" class="form-control form-control " name="selectedName" placeholder="Name" value="<%=selectedName%>">
                        </label>
                    </div>
                    <div class="col-md-3"></div>
                    <div class="col-md-2 ">
                        <button type="submit" class="btn btn-warning btn w-75 float-end">Search</button>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <div class="table-responsive">
        <table class="table table-hover">
            <thead class="bg-primary text-white">
            <tr>
                <th>Student Code</th>
                <th>Student First Name</th>
                <th>Student Last Name</th>
                <th>Class</th>
                <th>Gender</th>
                <th>Date of Birth</th>
                <th>Email</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
            <%for (Student student : studentList) {
            %>

            <tr>
                <td><%=student.getUser().getUserName()%></td>
                <td><%=student.getStudentFname()%></td>
                <td><%=student.getStudentLname()%></td>
                <td><%=student.getStudentClass()%></td>
                <td><%=student.getStudentGender()?"Male":"Female"%></td>
                <td><%=student.getStudentDob()%></td>
                <td><%=student.getStudentEmail()%></td>
                <td><%=student.getStatus()%></td>
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
</div>

<jsp:include page="createClass.jsp"/>
<jsp:include page="includes/footer.jsp"/>