package models;

import services.interfaces.IModel;
import utils.StateStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
@Table(name = "classroom", schema = "school_manage2")
public class Classroom implements IModel {
    @Id
    @Column(name = "Class_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 25)
    @Column(name = "CLass_Code", length = 25)
    private String classCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Crs_ID")
    private models.Course crs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Lec_ID")
    private models.Lecturer lec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Create_By")
    private models.User createBy;

    @Size(max = 225)
    @Column(name = "Class_NAME", length = 225)
    private String className;

    @Column(name = "Class_Quantity")
    private Integer classQuantity;

    @Column(name = "Start_Date")
    private LocalDate startDate;

    @Column(name = "End_Date")
    private LocalDate endDate;

    @Lob
    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private StateStatus status;

    public Classroom() {}

    public Classroom(String className, Course crs, Lecturer lec, Integer classQuantity, LocalDate startDate, LocalDate endDate, StateStatus status, User createBy) {
        this.className = className;
        this.crs = crs;
        this.lec = lec;
        this.classQuantity = classQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createBy = createBy;
    }

    @PostPersist
    public void generatedClassCode(){
        if(this.classCode == null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMyy");
            // đổi ở đây
            this.classCode = this.crs.getCourseCode() +startDate.format(formatter) + endDate.format(formatter) + this.id ;
        }
    }
}