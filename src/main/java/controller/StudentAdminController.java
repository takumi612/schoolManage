package controller;

import dtos.StudentDto;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import models.Major;
import models.Student;
import models.User;
import org.apache.commons.io.IOUtils;
import services.implement.MajorService;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
@WebServlet("/studentAdmin")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class StudentAdminController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession sessionHttp = req.getSession();
        String url = "/studentAdmin.jsp";
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Param để xử lý phân trang
        Long noOfRecord;
        int pageNumber;
        int elementsPerPage = 5;

        if(req.getParameter("page") != null) {
            pageNumber = Integer.parseInt(req.getParameter("page"));
        }else{
            pageNumber =1 ;
        }

        int start = (pageNumber - 1) * elementsPerPage;


        String selectedYear ;
        if( req.getParameter("selectedYear") == null || req.getParameter("selectedYear").equalsIgnoreCase("Select Year")) {
            selectedYear = "";
        }else{
            selectedYear = req.getParameter("selectedYear");
        }

        String selectedMajor;
        if( req.getParameter("selectedMajor") == null|| req.getParameter("selectedMajor").equalsIgnoreCase("Select Major") ) {
            selectedMajor ="";
        }else{
            selectedMajor = req.getParameter("selectedMajor");
        }

        String selectedCode;
        if(req.getParameter("selectedCode") != null) {
            selectedCode = req.getParameter("selectedCode");
        }else{
            selectedCode = "";
        }

        User user = (User) sessionHttp.getAttribute("user");
        if(user == null ){
            getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(req, resp);
            return;
        }
        if(!user.getUserRole()){
            getServletContext()
                    .getRequestDispatcher("/authen_error.jsp")
                    .forward(req, resp);
        }

        StudentService service = new StudentService();
        ResultList<Student> studentList;
        StudentDto studentDto = new StudentDto(selectedYear,selectedMajor,selectedCode);

        if(Stream.of(selectedYear, selectedMajor, selectedCode)
                .allMatch(s -> s == null || s.isEmpty())
        ) {
            studentList = service.selectAll(start, elementsPerPage);
        }else{
            studentList = service.findByCondition(studentDto, start, elementsPerPage);
        }

        if(studentList.getCount() == null){
            noOfRecord = 0L;
        }else {
            noOfRecord = studentList.getCount();
        }

        // Chuyển byte[] ảnh sang base64 và gán vào attribute
        List<String> studentImagesBase64 = new ArrayList<>();
        for (Student student : studentList.getResultList()) {
            if (student.getStudentImage() != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(student.getStudentImage());
                studentImagesBase64.add(imageBase64);
            } else {
                studentImagesBase64.add(null); // Hoặc chuỗi base64 của ảnh default
            }
        }

        List<StudentDto> studentDtoList = new ArrayList<>();
        for(Student student : studentList.getResultList()) {
            StudentDto studentDto1 = new StudentDto(student);
            if (student.getStudentImage() != null) {
                String imageBase64 = Base64.getEncoder().encodeToString(student.getStudentImage());
                studentDto1.setStudentImageBase64(imageBase64);
            } else {
                studentDto1.setStudentImageBase64("");
            }
            studentDtoList.add(studentDto1);
        }

        StudentService studentService = new StudentService();
        MajorService majorService = new MajorService();

        List<Major> majorList = majorService.selectAll();
        List<Integer> yearList  = studentService.listYear();

        sessionHttp.setAttribute("mayorList", majorList);
        req.setAttribute("yearList",yearList);
        req.setAttribute("studentList", studentDtoList);
        req.setAttribute("searchDto",studentDto);
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
        HttpSession sessionHttp = req.getSession();
        StudentService service = new StudentService();

        String studentId = req.getParameter("studentId");
        String email = req.getParameter("studentEmail");
        String dob = req.getParameter("studentDob");
        String phone = req.getParameter("studentPhone");
        String gender = req.getParameter("studentGender");
        String entryYear = req.getParameter("studentEntryYear");
        String fName = req.getParameter("studentFName");
        String lName = req.getParameter("studentLName");
        String major = req.getParameter("studentMajor");

        User user = (User) sessionHttp.getAttribute("user");

        if(user == null ){
            getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(req, resp);
            return;
        }
        StudentDto studentDto;
        Student student;

        // Lấy file từ request

        Part filePart = req.getPart("imageInput");
        byte[] imageBytes = null;

        if (filePart != null && filePart.getSize() > 0) {
            try (InputStream is = filePart.getInputStream()) {
                imageBytes = IOUtils.toByteArray(is);
            } catch (IOException e) {
                log.error("Error reading image bytes: ", e);
                // Xử lý lỗi
                return;
            }
        }

        if(studentId == null || studentId.isEmpty()) {
            studentDto = new StudentDto(fName,lName,email,dob,phone,entryYear,gender,major,imageBytes);
            // trong hàm from này đã insert user
            student = StudentDto.createStudentFromDto(studentDto);
            student.setCreateBy(user);
            service.Insert(student);
        }else {
            int studentID = Integer.parseInt(studentId);
            if(imageBytes==null){
                studentDto = new StudentDto(fName,lName,email,dob,phone,entryYear,gender,major);
            }else{
                studentDto = new StudentDto(fName,lName,email,dob,phone,entryYear,gender,major,imageBytes);
            }
            student = StudentDto.updateStudentFromDto(studentID,studentDto);
            service.Update(student);
        }
        doGet(req, resp);
    }
}
