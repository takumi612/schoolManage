package dtos;

import lombok.Getter;
import lombok.Setter;
import models.Classroom;
import models.Course;
import models.Lecturer;
import models.User;
import services.implement.ClassService;
import services.implement.CourseService;
import services.implement.LecturerService;
import services.implement.UserService;
import services.interfaces.IDto;
import utils.StateStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ClassDto implements IDto {
    private String userID;
    private String className;
    private int courseID;
    private int lecID;
    private String lecLName;
    private String selectedMajor;
    private String quantity;
    private String startDate;
    private String endDate;


    public ClassDto() {}

    public ClassDto(String startDate, int courseCode, String lecLName, String selectedMajor) {
        this.startDate = startDate;
        this.courseID = courseCode;
        this.lecLName = lecLName;
        this.selectedMajor = selectedMajor;
    }

    public static Classroom updateClassFromDto(int classId ,ClassDto classDto) {
        CourseService courseService = new CourseService();
        ClassService classroomService = new ClassService();
        LecturerService lecturerService = new LecturerService();

        Course course = courseService.selectedById(classDto.getCourseID());
        Classroom classroom =classroomService.selectedById(classId);

        Lecturer lec = lecturerService.selectedById(classDto.getLecID());

        classroom.setClassName(classDto.getClassName());
        classroom.setCrs(course);
        classroom.setClassName(classDto.getClassName());
        classroom.setLec(lec);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Chuyển đổi String thành LocalDate
        LocalDate startDate = LocalDate.parse(classDto.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(classDto.getEndDate(), formatter);
        classroom.setStartDate(startDate);
        classroom.setEndDate(endDate);
        classroom.setClassQuantity(Integer.parseInt(classDto.getQuantity()));
        return classroom;
    }

    public static Classroom createClassFromDto(ClassDto classDto) {

        UserService userService = new UserService();
        CourseService courseService = new CourseService();
        LecturerService lecturerService = new LecturerService();

        User user = userService.selectedById(Integer.parseInt(classDto.getUserID()));
        Course course = courseService.selectedById(classDto.getCourseID());
        Lecturer lec = lecturerService.selectedById(classDto.getLecID());
        Classroom classroom = new Classroom();

        classroom.setCreateBy(user);
        classroom.setLec(lec);
        classroom.setCrs(course);
        classroom.setClassName(classDto.getClassName());
        classroom.setClassName(classDto.getClassName());
        classroom.setClassQuantity(Integer.parseInt(classDto.getQuantity()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Chuyển đổi String thành LocalDate
        LocalDate startDate = LocalDate.parse(classDto.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(classDto.getEndDate(), formatter);
        classroom.setStartDate(startDate);
        classroom.setEndDate(endDate);


        return classroom;
    }
}
