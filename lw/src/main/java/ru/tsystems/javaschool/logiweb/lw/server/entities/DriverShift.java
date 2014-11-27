package ru.tsystems.javaschool.logiweb.lw.server.entities;

import javax.validation.constraints.NotNull;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "driver_shift", uniqueConstraints = @UniqueConstraint(columnNames = "driver_shift_id"))
public class DriverShift implements Serializable {

    @Id
    @Column(name = "driver_shift_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer driverId;

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @Column(name = "order_id")
    private Integer orderId;

    @OneToOne(mappedBy = "driverShift", fetch = FetchType.EAGER)
    private Drivers drivers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;


    public DriverShift() {
    }

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

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public DriverStatus getStatus() {
        return status;
    }

}
