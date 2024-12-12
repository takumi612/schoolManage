package models;

import dtos.StudentDto;
import services.interfaces.IModel;
import utils.WorkingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student implements IModel {
    @Id
    @Column(name = "Student_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "Student_Code", referencedColumnName = "User_Name")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Create_By",referencedColumnName = "User_ID")
    private User createBy;

    @Size(max = 20)
    @Column(name = "Student_Class", length = 20)
    private String studentClass;

    @Size(max = 30)
    @Column(name = "Student_FNAME", length = 30)
    private String studentFname;

    @Size(max = 30)
    @Column(name = "Student_LNAME", length = 30)
    private String studentLname;

    @Column(name = "Student_DOB")
    private LocalDate studentDob;

    @Column(name = "Student_Entry_Year")
    private LocalDate entryYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Student_Major")
    private models.Major major;

    @Size(max = 10)
    @Column(name = "Student_Phone", length = 10)
    private String studentPhone;

    @Size(max = 50)
    @Column(name = "Student_Email", length = 50)
    private String studentEmail;

    @Column(name = "Student_Gender")
    private Boolean studentGender;

    @Size(max = 125)
    @Column(name = "Student_Image")
    private byte[] studentImage;

    @Lob
    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private WorkingStatus status;

}
