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
@Table(name = "user", schema = "school_manage2")
public class User implements IModel {
    @Id
    @Column(name = "User_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "User_Name", length = 50)
    private String userName;

    @Size(max = 225)
    @Column(name = "User_PassWord", length = 225)
    private String userPassword;

    @Column(name = "User_Role")
    private Boolean userRole;

    public User() {}

    public User(String userPassword) {
        this.userPassword = userPassword;
        this.userRole = false;
    }
}