package controller;

import dtos.StudentDto;
import models.Classroom;
import models.Student;
import models.User;
import services.implement.ClassService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.ResultList;

import java.io.IOException;
import java.util.stream.Stream;

@WebServlet("/classInfo")
public class ClassInfoController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String url = "/classInfo.jsp";
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        Long noOfRecord;
        int pageNumber;
        int elementsPerPage = 5;
        int start;

        String searchString;
        ClassService service = new ClassService();
        Classroom classroom;
        StudentDto studentDto;
        ResultList<Student> studentList;
        int classID;

        // Kiểm tra và lấy các param từ request

        if(req.getParameter("classID") != null){
            classID = Integer.parseInt (req.getParameter("classID"));
        }else{
            classID = 0;
        }

        String selectedName;
        if(req.getParameter("selectedName") != null) {
            selectedName = req.getParameter("selectedName");
        }else {
            selectedName = "";
        }

        String selectedGender;
        if(req.getParameter("selectedGender") != null) {
            selectedGender = req.getParameter("selectedGender");
        }else {
            selectedGender = "";
        }

        User user = (User) session.getAttribute("user");
        if(user == null ){
            getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(req, resp);
            return;
        }

        if(req.getParameter("page") != null) {
            pageNumber = Integer.parseInt(req.getParameter("page"));
        }else {
            pageNumber = 1;
        }

        start = (pageNumber - 1) * elementsPerPage;

        // Lấy ClassRoom dựa trên ClassID
        classroom = service.selectedById(classID);
        studentDto = new StudentDto(selectedName,selectedGender);

        // Lấy List student dựa trên class
        if(Stream.of(selectedName, selectedGender)
                .allMatch(s -> s == null || s.isEmpty())
        ) {
            studentList= service.getStudentList(classroom,start,elementsPerPage);
        }else{
            studentList = service.findByStudentCondition(classID,studentDto,start,elementsPerPage);
        }
        if(studentList.getCount() != null  ){
            noOfRecord = studentList.getCount();
        }else{
            noOfRecord=0L;
        }

        req.setAttribute("classID", classID);
        req.setAttribute("studentDto", studentDto);
        req.setAttribute("studentList", studentList.getResultList());

        req.setAttribute("classroom", classroom);
        req.setAttribute("pageNumber", pageNumber);
        req.setAttribute("noOfRecord", noOfRecord);
        req.setAttribute("elementsPerPage", elementsPerPage);

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        getServletContext()
                .getRequestDispatcher(url)
                .forward(req, resp);
    }
}
