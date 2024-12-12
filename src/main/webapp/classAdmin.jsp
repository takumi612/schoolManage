<%@ page import="models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Classroom" %>
<%@ page import="dtos.ClassDto" %>
<%@ page import="models.Major" %>
<%@ page import="services.implement.StudentService" %>
<%@ page import="services.implement.MajorService" %>
<%@ page import="services.implement.ClassService" %>
<%@ page import="services.implement.LecturerService" %>
<%@ page import="models.Lecturer" %>
<%@ page import="services.implement.CourseService" %>
<%@ page import="javax.print.CancelablePrintJob" %>
<%@ page import="models.Course" %>
<%@ page import="java.util.stream.Stream" %>
<%--
  Created by IntelliJ IDEA.
  User: zenof
  Date: 11/6/2024
  Time: 5:39 PM
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="includes/header.jsp"/>

<%
    ClassDto classDto = (ClassDto) request.getAttribute("classDto");
    String selectedMajor = "";

    String selectedLec;
    String selectedYear;
    String selectedCourse;
    if(classDto!=null){
        try{
            selectedCourse= String.valueOf(classDto.getCourseID());
        }catch (Exception e){
            selectedCourse= "";
        }

        try{
            selectedLec= String.valueOf(classDto.getLecID());
        }catch (Exception e){
            selectedLec= "";
        }
        if(selectedLec.equalsIgnoreCase("0")){
            selectedLec="";
        }
        selectedYear= classDto.getStartDate()!=null?classDto.getStartDate():"";
        selectedMajor= classDto.getSelectedMajor()!=null?classDto.getSelectedMajor():"";
    }else {
        selectedCourse = "";
        selectedLec ="";
        selectedYear = "";
        selectedMajor = "";
    }

    ClassService classService = new ClassService();
    MajorService majorService = new MajorService();
    LecturerService lecturerService = new LecturerService();
    CourseService courseService = new CourseService();

    Course course;
    if(Stream.of(selectedCourse)
            .noneMatch(String::isEmpty) && !selectedCourse.equalsIgnoreCase("0"))
    {
        course = courseService.selectedById(Integer.parseInt(selectedCourse));
    }else{
        course = new Course();
    }
    if(course.getMajorIn()!=null){
        selectedMajor = course.getMajorIn().getMajorName();
    }
    List<Major> majorList = majorService.selectAll();
    List<Integer> yearList  = classService.listYear();


    int pageNumber = (Integer) request.getAttribute("pageNumber");
    int elementsPerPage = (Integer) request.getAttribute("elementsPerPage");
    Long noOfRecord = (Long) request.getAttribute("noOfRecord");

    String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath();
    String nextPageUrl = url + "/classAdmin?page=" + (pageNumber+1) + "&selectedCourse=" +selectedCourse + "&selectedLec=" +selectedLec + "&selectedYear=" + selectedYear+ "&selectedMajor=" + selectedMajor;
    String prevPageUrl = url + "/classAdmin?page=" + (pageNumber-1)+ "&selectedCourse=" +selectedCourse + "&selectedLec=" +selectedLec + "&selectedYear=" + selectedYear+ "&selectedMajor=" + selectedMajor;

    if ((pageNumber - 1) <= 0){
        prevPageUrl = "";
    }
    if ((pageNumber + 1) > noOfRecord){
        nextPageUrl = "";
    }

    User user = (User) session.getAttribute("user");
    List<Classroom> classroomList = (List<Classroom>) request.getAttribute("classroomList");
    if(user == null){
        response.sendRedirect("index.jsp");
        return;
    }
    if(!user.getUserRole()){
        response.sendRedirect("authen_error.jsp");
        return;
    }

%>

<div class="container text-center">
    <h4>Class Infomation</h4>
</div>

<div class="container-md p-2 my-4">
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-white">
            <h5 class="mb-0">Search for Classes</h5>
        </div>
        <form action = "classAdmin" method="GET">
        <div class="card-body">
            <div class="mb-4">
                <h6 class="text-muted mb-3">Search by List</h6>
                <div class="row g-3">
                    <div class="col-md-3">
                        <select class="form-select form-select" name="selectedYear" aria-label="Year">
                            <option value="">Select Year</option>
                            <%for( Integer years: yearList){%>
                            <option value="<%=years%>" <%=( !selectedYear.isEmpty() && String.valueOf(years).equals(selectedYear)) ? "selected" : "" %>><%=years%></option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select form-select" name="selectedMajor" aria-label="Major">
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
                            <input type="text" class="form-control form-control" name="selectedLec" placeholder="Lecturer Name" value="<%=selectedLec%>">
                        </div>
                        <div class="col-md-3">
                                <input type="text" class="form-control form-control" name="selectedCourse" placeholder="Course Code" value="<%=course.getCourseName()%>">
                        </div>
                        <div class="col-md-3"></div>
                        <div class="col-md-3">
                            <button class="btn btn-warning btn  w-50">Search</button>
                        </div>
                    </div>
            </div>
        </div>
        </form>
    </div>

    <div class="table-responsive">
        <table class="table table-hover">
            <thead class="bg-primary text-white">
            <tr>
                <th>Course Code</th>
                <th>Course Name</th>
                <th>Class Name</th>
                <th>Quantity</th>
                <th>Credit</th>
                <th>Start</th>
                <th>End</th>
                <th>Status</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <%for (Classroom classroom : classroomList) {
            %>
                <tr>
                    <td><%= classroom.getCrs().getCourseCode()%></td>
                    <td><%= classroom.getCrs().getCourseName()%></td>
                    <td><%= classroom.getClassName()%></td>
                    <td><%= classroom.getClassQuantity()%></td>
                    <td><%= classroom.getCrs().getCourseCredit()%></td>
                    <td><%= classroom.getStartDate()%></td>
                    <td><%= classroom.getEndDate()%></td>
                    <td><%= classroom.getStatus()%></td>

                    <td>
                        <div class="btn-group">
                            <!-- Nút Xem -->
    <%--                        <form action="classAdmin" method="POST" style="display:inline;">--%>
                                <a href="classInfo?classID=<%=classroom.getId()%>">
                                <button type="submit" class="btn btn-info text-white" title="View">
                                    <i class="bi bi-eye fs-5"></i>
                                </button>
                                </a>
    <%--                        </form>--%>

                            <!-- Nút Sửa -->
<%--                            <form action="classAdmin" method="POST" style="display:inline;">--%>
                                <input type="hidden" name="action" value="edit">
                                <button type="submit" class="btn btn-warning" title="Edit">
                                    <i class="bi bi-pencil fs-5"></i>
                                </button>
<%--                            </form>--%>
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
</div>


<jsp:include page="createStudent.jsp"/>
<jsp:include page="includes/footer.jsp"/>


