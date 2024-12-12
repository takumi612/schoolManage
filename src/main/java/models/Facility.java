package models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import services.interfaces.IModel;

@Getter
@Setter
@Entity
@Table(name = "facility", schema = "school_manage2")
public class Facility implements IModel {
    @Id
    @Column(name = "Facility_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 25)
    @Column(name = "FacilityCode", length = 25)
    private String facilityCode;

    @Size(max = 100)
    @Column(name = "FacilityName", length = 100)
    private String facilityName;

}