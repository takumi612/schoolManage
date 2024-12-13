package controller;

import dtos.ClassDto;
import models.Classroom;
import models.Course;
import models.User;
import services.implement.ClassService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.implement.CourseService;
import utils.ResultList;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@WebServlet("/classAdmin")
public class ClassAdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/classAdmin.jsp";
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        HttpSession sessionHttp = req.getSession();

        Long noOfRecord;
        int pageNumber;
        int elementsPerPage = 5;
        int start;
        ClassService service = new ClassService();

        if(req.getParameter("page") != null) {
            pageNumber = Integer.parseInt(req.getParameter("page"));
        }else {
            pageNumber = 1;
        }
        start = (pageNumber - 1) * elementsPerPage;

        User user = (User) sessionHttp.getAttribute("user");
        if(user == null ){
            getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(req, resp);
            return;
        }

        //Kiểm tra và lấy các param

        String selectedYear;
        String selectedLec;
        String selectedCourse;
        String selectedMajor;

        if( req.getParameter("selectedYear") == null || req.getParameter("selectedYear").equalsIgnoreCase("Select Year")) {
            selectedYear = "";
        }else{
            selectedYear = req.getParameter("selectedYear");
        }
        if( req.getParameter("selectedMajor") == null || req.getParameter("selectedMajor").equalsIgnoreCase("Select Major")) {
            selectedMajor = "";
        }else{
            selectedMajor = req.getParameter("selectedMajor");
        }

        if( req.getParameter("selectedLec") == null ) {
            selectedLec = "";
        }else{
            selectedLec = req.getParameter("selectedLec");
        }

        if( req.getParameter("selectedCourse") == null ) {

            selectedCourse = "";
        }else{
            selectedCourse = req.getParameter("selectedCourse");
        }

        ResultList<Classroom> classroomList;
        ClassDto classDto = null;
        Course course;
        ClassService classService = new ClassService();
        CourseService courseService = new CourseService();

        if(Stream.of(selectedYear, selectedLec, selectedCourse,selectedMajor)
                .allMatch(s -> s == null || s.isEmpty() || s.equalsIgnoreCase("0"))
        ){
            course = new Course();
            classroomList = classService.selectAll(start,elementsPerPage);
        }else{
            if(selectedCourse == null || selectedCourse.isEmpty() || selectedCourse.equalsIgnoreCase("0")){
                course = new Course();
            }else {
                course = courseService.selectedByName(selectedCourse);
            }
            classDto = new ClassDto(selectedYear,course.getId(),selectedLec,selectedMajor);
            if(course.getMajorIn()!=null){
                classDto.setSelectedMajor(course.getMajorIn().getMajorName());
            }
            classroomList = classService.findByClassCondition(classDto,start,elementsPerPage);
        }

        List<Integer> yearList  = classService.listYear();

        // Lấy tất cả các class
        if(classroomList.getCount() == null){
            noOfRecord = 0L;
        }else {
            noOfRecord = classroomList.getCount();
        }

        req.setAttribute("classroomList", classroomList.getResultList());
        req.setAttribute("course", course);
        req.setAttribute("yearList", yearList);
        req.setAttribute("classDto",classDto);

        req.setAttribute("pageNumber", pageNumber);
        req.setAttribute("noOfRecord", noOfRecord);
        req.setAttribute("elementsPerPage", elementsPerPage);

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        getServletContext().getRequestDispatcher(url).forward(req, resp);
    }

}

