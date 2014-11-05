package ru.tsystems.javaschool.logiweb.lw.server.entities;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name = "driver_shift", uniqueConstraints = @UniqueConstraint(columnNames = "driver_shift_id"))
public class DriverShift implements Serializable {

    @Id
    @Column(name = "driver_shift_id")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;


    @Column(name = "order_id")
    private Integer orderId;


    @OneToOne(mappedBy = "driverShift")
    private Drivers drivers;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Drivers getDrivers() {
        return drivers;
    }

    public void setDrivers(Drivers drivers) {
        this.drivers = drivers;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }



    public DriverShift() {

    }


    public DriverShift(Integer id) {
        this.driverId = id;
    }

    public enum Status {
        shift, notShift, atWeel
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }


    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public Status getStatus() {
        return status;
    }

}
