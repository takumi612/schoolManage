package controller;

import jakarta.servlet.annotation.MultipartConfig;
import models.Classroom;
import models.Student;
import models.User;
import services.implement.StudentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import utils.ResultList;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Log4j2
@WebServlet("/studentInfo")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class StudentInfoController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/studentInfo.jsp";
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        StudentService service = new StudentService();
        HttpSession sessionHttp = req.getSession();

        // Xử lý phân trang/ Search ở đây
        Long totalStudent;
        Long noOfRecord;
        int pageNumber;
        int elementsPerPage = 5;
        int start;
        Student student;
        ResultList<Classroom> classList;
        String studentCode;
        String selectedYear;
        List<String> yearList;
        User user = (User) sessionHttp.getAttribute("user");

        // Kiểm tra xem User đã đăng nhập hay chưa
        if (user == null) {
            getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(req, resp);
            return;
        }

        if (req.getParameter("page") != null) {
            pageNumber = Integer.parseInt(req.getParameter("page"));
        } else {
            pageNumber = 1;
        }
        if(req.getParameter("selectedYear") != null) {
            selectedYear = req.getParameter("selectedYear");
        }else{
            selectedYear = "";
        }

        start = (pageNumber - 1) * elementsPerPage;

        studentCode = user.getUserName();

        if (req.getParameter("studentCode") != null) {
            studentCode = (req.getParameter("studentCode"));
        }
        student = service.selectedByCode(studentCode);

        String studentImage = null;
        if(student.getStudentImage()!=null){
            studentImage = Base64.getEncoder().encodeToString(student.getStudentImage());
        }


        // Lấy Student dựa trên userName hay StudenCode

        yearList = getSchoolYears(student);


        // Lấy List class dựa trên userID
        // Có 1 nhược điểm là cái noOfRecord ít khi bị thay đổi nhưng mỗi lần chuyển trang dều phải hỏi db
        if (selectedYear != null && !selectedYear.isEmpty()) {
            classList = service.searchByYear(student, selectedYear, start, elementsPerPage);
        } else {
            classList = service.getClassList(student, start, elementsPerPage);
        }
        if(classList.getCount() == null ){
            noOfRecord = 0L;
        }else {
            noOfRecord = classList.getCount();
        }
        req.setAttribute("classList", classList.getResultList());
        req.setAttribute("student", student);
        req.setAttribute("selectedYear",selectedYear);
        req.setAttribute("studentImage",studentImage);

        req.setAttribute("yearList", yearList);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = "/studentInfo.jsp";
        HttpSession sessionHttp = req.getSession();
        User user = (User) sessionHttp.getAttribute("user");
    }

    public static List<String> getSchoolYears(Student student){
        List<String> list = new ArrayList<>();
        int startYear = student.getEntryYear().getYear();
        int currentYear = LocalDate.now().getYear();

        for(int year = startYear; year < currentYear; year++){
            list.add(year + "-" + (year + 1));
        }
        if (list.size() >= 2) {
            Collections.swap(list, 0, list.size() - 2);
        }
        return list;
    }
}
