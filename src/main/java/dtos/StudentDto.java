package dtos;

import lombok.Getter;
import lombok.Setter;
import models.Major;
import models.Student;
import models.User;
import org.modelmapper.ModelMapper;
import services.implement.MajorService;
import services.implement.StudentService;
import services.implement.UserService;
import services.interfaces.IDto;
import utils.WorkingStatus;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Getter
@Setter
public class StudentDto implements IDto {
    private String studentFirstName;
    private String studentLastName;
    private String studentEmail;
    private String studentDoB;
    private String studentGender;
    private String studentPhone;
    private String studentClass;
    private String studentEntryYear;
    private int studentID;
    private String major;
    private String studentImageBase64;
    private String studentCode;
    private WorkingStatus studentStatus;

    public StudentDto() {}

    public StudentDto(Student student) {
        this.studentFirstName = student.getStudentFname();
        this.studentLastName = student.getStudentLname();
        this.studentEmail = student.getStudentEmail();
        String dob = student.getStudentDob().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String entryYear = student.getEntryYear().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.studentDoB = dob;
        this.studentGender = student.getStudentGender() ? "Male" : "Female";
        this.studentPhone = student.getStudentPhone();
        this.studentClass = student.getStudentClass();
        this.studentEntryYear = entryYear;
        this.studentID = student.getId();
        this.major = student.getMajor().getMajorName();
        this.studentCode = student.getUser().getUserName();
        this.studentStatus = student.getStatus();
    }

    public StudentDto(String studentLastName, String studentGender) {
        this.studentLastName = studentLastName;
        this.studentGender = studentGender;
    }

    public StudentDto( String studentEntryYear, String major, String studentCode) {
        this.studentEntryYear = studentEntryYear;
        this.major = major;
        this.studentCode = studentCode;
    }

    public StudentDto( String studentFirstName, String studentLastName, String studentEmail, String studentDoB, String studentPhone, String studentEntryYear, String studentGender, String major,byte[] studentImage) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentEmail = studentEmail;
        this.studentDoB = studentDoB;
        this.studentPhone = studentPhone;
        this.studentEntryYear = studentEntryYear;
        this.studentGender = studentGender;
        this.major = major;
        MajorService majorService = new MajorService();
        Major major1 = majorService.selectedbyName(major);
        String studentYear = studentEntryYear.split("-")[0];
        this.studentCode = major1.getMajorCode()+ studentYear + studentID;
        this.studentImageBase64 = Base64.getEncoder().encodeToString(studentImage);
    }
    public StudentDto( String studentFirstName, String studentLastName, String studentEmail, String studentDoB, String studentPhone, String studentEntryYear, String studentGender, String major) {
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.studentEmail = studentEmail;
        this.studentDoB = studentDoB;
        this.studentPhone = studentPhone;
        this.studentEntryYear = studentEntryYear;
        this.studentGender = studentGender;
        this.major = major;
        MajorService majorService = new MajorService();
        Major major1 = majorService.selectedbyName(major);
        String studentYear = studentEntryYear.split("-")[0];
        this.studentCode = major1.getMajorCode()+ studentYear + studentID;
    }

    public static Student updateStudentFromDto(int studentID, StudentDto studentDto) {
        StudentService studentService = new StudentService();
        MajorService majorService = new MajorService();

        Student student = studentService.selectedById(studentID);

        Major major = majorService.selectedbyName(studentDto.getMajor());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Chuyển đổi String thành LocalDate
        LocalDate dobDate = LocalDate.parse(studentDto.getStudentDoB(), formatter);
        LocalDate entryDate = LocalDate.parse(studentDto.getStudentEntryYear(), formatter);

        student.setStudentDob(dobDate);
        student.setEntryYear(entryDate);

        student.setStudentFname(studentDto.getStudentFirstName());
        student.setStudentLname(studentDto.getStudentLastName());
        student.setStudentEmail(studentDto.getStudentEmail());
        student.setStudentPhone(studentDto.getStudentPhone());
        student.setStudentGender(studentDto.getStudentGender().equalsIgnoreCase("Male"));
        student.setMajor(major);
        byte[] imageBytes;
        if (studentDto.getStudentImageBase64() != null && !studentDto.getStudentImageBase64().isEmpty()) {
            String imageBase64 = studentDto.getStudentImageBase64();
            imageBase64 = imageBase64.replaceFirst("data:image/[^;]+;base64,", "");
            imageBytes = Base64.getDecoder().decode(imageBase64);
        }else{
            imageBytes = null;
        }
        student.setStudentImage(imageBytes);
        student.setStatus(WorkingStatus.ONGOING);
        return student;
    }
    public static Student createStudentFromDto(StudentDto dto) {
        UserService userService = new UserService();
        User user = new User("1");
        userService.Insert(user);
        dto.setStudentCode(dto.getStudentCode()+user.getId());
        user.setUserName(dto.getStudentCode());
        userService.Update(user);


        Student student = new Student();
        student.setUser(user);

        MajorService majorService = new MajorService();
        Major major = majorService.selectedbyName(dto.getMajor());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Chuyển đổi String thành LocalDate
        LocalDate dobDate = LocalDate.parse(dto.getStudentDoB(), formatter);
        LocalDate entryDate = LocalDate.parse(dto.getStudentEntryYear(), formatter);

        student.setStudentDob(dobDate);
        student.setEntryYear(entryDate);

        student.setStudentFname(dto.getStudentFirstName());
        student.setStudentLname(dto.getStudentLastName());
        student.setStudentEmail(dto.getStudentEmail());
        student.setStudentPhone(dto.getStudentPhone());
        student.setStudentGender(dto.getStudentGender().equalsIgnoreCase("Male"));
        student.setMajor(major);
        byte[] imageBytes;
        if (dto.getStudentImageBase64() != null && !dto.getStudentImageBase64().isEmpty()) {
            String imageBase64 = dto.getStudentImageBase64();
            imageBase64 = imageBase64.replaceFirst("data:image/[^;]+;base64,", "");
            imageBytes = Base64.getDecoder().decode(imageBase64);
        }else{
            imageBytes = null;
        }
        student.setStudentImage(imageBytes);
        student.setStatus(WorkingStatus.ONGOING);

        return student;
    }
}
