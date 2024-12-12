package models;

import services.interfaces.IModel;
import utils.WorkingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lecturer", schema = "school_manage2")
public class Lecturer implements IModel {
    @Id
    @Column(name = "Lec_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @Size(max = 25)
    @Column(name = "Lec_Code", length = 25)
    private String lecCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Major_in")
    private models.Major majorIn;

    @Size(max = 30)
    @Column(name = "Lec_FNAME", length = 30)
    private String lecFname;

    @Size(max = 30)
    @Column(name = "Lec_LNAME", length = 30)
    private String lecLname;

    @Column(name = "Lec_DOB")
    private LocalDate lecDob;

    @Column(name = "Lec_HIRE_DATE")
    private LocalDate lecHireDate;

    @Size(max = 50)
    @Column(name = "Lec_PHONE", length = 50)
    private String lecPhone;

    @Size(max = 50)
    @Column(name = "Lec_Email", length = 50)
    private String lecEmail;

    @Column(name = "Lec_Gender")
    private Boolean lecGender;

    @Size(max = 125)
    @Column(name = "Lec_Image", length = 125)
    private String lecImage;

    @Lob
    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private WorkingStatus status;

}