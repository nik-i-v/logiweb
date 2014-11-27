package ru.tsystems.javaschool.logiweb.lw.server.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@Entity
@Table(name = "fura")
public class Fura implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "furas_id")
    private Integer furasId;

    @NotNull
    @Size(min = 7, max = 7, message = "must have format: 2 letters and 5 digits")
    @Pattern(regexp = "^[A-Z]{2}\\d{5}$")
    @Column(name = "fura_number", nullable = false, length = 7)
    private String furaNumber;

    @NotNull
    @Column(name = "driver_count", nullable = false)
    private Byte driverCount;

    @NotNull
    @Column(name = "capacity", nullable = false)
    @Enumerated(EnumType.STRING)
    private Capacity capacity;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "furas_id", referencedColumnName = "fura_id", insertable = false, updatable = false)
    private Order order;

    public Fura() {
    }

    public Fura(String furaNumber, Byte driverCount, Capacity capacity) {
        this.furaNumber = furaNumber;
        this.driverCount = driverCount;
        this.capacity = capacity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public enum Capacity {
        small, middle, large
    }
    public enum Status {
        yes, no
    }

    public Integer getFurasId() {
        return furasId;
    }

    public void setFurasId(Integer furasId) {
        this.furasId = furasId;
    }

    public void setFuraNumber(String id) {
        this.furaNumber = id;
    }

    public void setDriverCount(Byte driverCount) {
        this.driverCount = driverCount;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFuraNumber() {
        return furaNumber;
    }

    public Byte getDriverCount() {
        return driverCount;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public Status getStatus() {
        return status;
    }
}
