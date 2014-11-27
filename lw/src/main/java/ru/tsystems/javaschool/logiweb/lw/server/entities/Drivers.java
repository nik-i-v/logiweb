package ru.tsystems.javaschool.logiweb.lw.server.entities;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "drivers", uniqueConstraints = @UniqueConstraint(columnNames = "drivers_id"))
public class Drivers implements Serializable {


    @Id
    @Column(name = "drivers_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driversId;

    @NotNull
    @Column(name = "surname")
    @Pattern(regexp = "^[A-z]{1}[a-z]*$")
    private String surname;

    @NotNull
    @Column(name = "name")
    @Pattern(regexp = "^[A-z]{1}[a-z]*$")
    private String name;

    @NotNull
    @Pattern(regexp = "^[A-z]{1}[a-z]*$")
    @Column(name = "patronymic")
    private String patronymic;

    @Digits(integer = 11, fraction = 0)
    @Column(name = "license_id")
    private Long license;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drivers_id", updatable = false, insertable = false)
    private DriverShift driverShift;

    public Drivers() {
    }

    public Drivers(String surname, String name, String patronymic, Long license) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.license = license;
    }

    public DriverShift getDriverShift() {
        return driverShift;
    }

    public void setDriverShift(DriverShift driverShift) {
        this.driverShift = driverShift;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setLicense(Long license) {
        this.license = license;
    }

    public Integer getDriversId() {
        return driversId;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Long getLicense() {
        return license;
    }

}
