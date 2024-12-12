package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import services.interfaces.IModel;

@Getter
@Setter
@Entity
@Table(name = "course", schema = "school_manage2")

public class Course implements IModel {
    @Id
    @Column(name = "Crs_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 25)
    @Column(name = "Course_Code", length = 25)
    private String courseCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Major_In")
    private models.Major majorIn;

    @Size(max = 225)
    @Column(name = "Course_Name", length = 225)
    private String courseName;

    @Column(name = "Course_Credit")
    private Integer courseCredit;

    @Size(max = 100)
    @Column(name = "Course_Description", length = 100)
    private String courseDescription;

    public Course() {
        this.id = 0;
        this.courseCode = "";
        this.majorIn = null;
        this.courseName = "";
        this.courseCredit = 0;
        this.courseDescription = "";
    }
}