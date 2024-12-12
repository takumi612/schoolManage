
<%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/6/2024
  Time: 5:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page import="models.User"%>
<%@ page import="models.Student"%>
<%@ page import="java.util.List" %>
<%@ page import="models.Classroom" %>


<%
    String selectedYear = (String) request.getAttribute("selectedYear");
    int pageNumber = (Integer) request.getAttribute("pageNumber");
    int elementsPerPage = (Integer) request.getAttribute("elementsPerPage");
    List<String> years = (List<String>) request.getAttribute("yearList");
    Long noOfRecord = (Long) request.getAttribute("noOfRecord");

    if(selectedYear == null ){
        selectedYear = "";
    }

    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
    String nextPageUrl = url + "/studentInfo?page=" + (pageNumber+1)+"&selectedYear=" + selectedYear;
    String prevPageUrl = url + "/studentInfo?page=" + (pageNumber-1)+"&selectedYear=" + selectedYear;

    String studentImage ;
    String imageURL;

    if (request.getAttribute("studentImage") == null) {
        imageURL ="/images/download.png";
    }else{
        imageURL = "data:image/png;base64," + request.getAttribute("studentImage");
    }
    if ((pageNumber - 1) <= 0){
        prevPageUrl = "";
    }
    if ((pageNumber + 1) > noOfRecord){
        nextPageUrl = "";
    }

    User user = (User) session.getAttribute("user");
    Student student = (Student) request.getAttribute("student");
    List<Classroom> classList = (List<Classroom>) request.getAttribute("classList");
    if(user == null || student == null){
        response.sendRedirect("index.jsp");
        return;
    }
%>

<jsp:include page="includes/header.jsp"/>



<div class="container my-4">
    <div class="card mb-4 shadow">
        <div class="card-body">
            <div class="row">
                <div class="col-md-3 text-center">
                    <img src="<%=imageURL%>" alt="Student Profile" class="student-profile rounded-circle mb-3">
                </div>
                <div class="col-md-9">
                    <h2 class="mb-3">  <%= student.getStudentFname() %> <%= student.getStudentLname()%></h2>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>StudentCode:</strong>  <%= student.getUser().getUserName()%></p>
                            <p><strong>StudentClass:</strong>  <%= student.getStudentClass()%></p>
                            <p><strong>Entry Year:</strong>  <%= student.getEntryYear()%></p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Gender:</strong>  <%= student.getStudentGender()? "Male": "Female"%></p>
                            <p><strong>Phone:</strong>  <%= student.getStudentPhone()%></p>
                            <p><strong>Email:</strong>  <%= student.getStudentEmail()%></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <form action="studentInfo" method="GET" >
            <input type="hidden" name="studentCode" value="<%=student.getUser().getUserName()%>">
            <div class="col-md-4">
                <label>
                    <select name="selectedYear" class="form-select">
                        <%for (String year : years) {
                        %>
                        <option  value="<%=year%>" <%if(!selectedYear.isEmpty() && year.equalsIgnoreCase(selectedYear)){%> disabled selected <% }%> ><%=year%></option>
                        <%
                            }
                        %>
                    </select>
                </label>
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">Search</button>
            </div>
        </form>
    </div>

    <div class="table-responsive">
        <table class="table table-hover">
            <thead class="bg-primary text-white">
            <tr>
                <th>Course</th>
                <th>Class Course</th>
                <th>Course Name</th>
                <th>Quantity</th>
                <th>Start</th>
                <th>End</th>
                <th>Lecturer</th>
                <th>Status</th>
            </tr>
            </thead>
            <tbody>
                <%for (Classroom classroom : classList) {
                %>

                <tr class="student-row" data-class-id =<%=classroom.getId()%>>
                    <td><%=classroom.getCrs().getCourseCode()%></td>
                    <td><%=classroom.getClassCode()%></td>
                    <td><%=classroom.getCrs().getCourseName()%></td>
                    <td><%=classroom.getClassQuantity()%></td>
                    <td><%=classroom.getStartDate()%></td>
                    <td><%=classroom.getEndDate()%></td>
                    <td><%=classroom.getLec().getLecLname()%></td>
                    <td><%=classroom.getStatus()%></td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>

    <div class="float-end align-items-center mt-2">
        <a href="<%=prevPageUrl%>" >
            <button  class="btn btn-outline-primary">&lt;&lt; Prev. Page</button>
        </a>
            <span class="p-2"><%= pageNumber%> / <%= noOfRecord%></span>
        <a href="<%=nextPageUrl%>" >
            <button  class="btn btn-outline-primary" >Next Page &gt;&gt;</button>
        </a>
    </div>

</div>

<script>
    $(document).ready(function() {
        // Xử lý sự kiện double click trên hàng của bảng
        $('.student-row').dblclick(function() {
            var classId = $(this).attr('data-class-id');
            window.location.href = 'classInfo?classID=' + classId;
        });
    });
</script>



<jsp:include page="includes/footer.jsp"/>


