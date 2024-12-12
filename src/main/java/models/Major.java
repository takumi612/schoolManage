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
@Table(name = "major", schema = "school_manage2")
public class Major implements IModel {
    @Id
    @Column(name = "Major_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Facility_In")
    private Facility facilityIn;

    @Size(max = 25)
    @Column(name = "MajorCode", length = 25)
    private String majorCode;

    @Size(max = 100)
    @Column(name = "MajorName", length = 100)
    private String majorName;

}