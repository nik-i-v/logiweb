package ru.tsystems.logiweb.server.entities;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Entity
@XmlRootElement
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Order implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 7, max = 7, message = "must have format: 2 letters and 5 digits")
    @Pattern(regexp = "^[A-Z]{2}\\d{5}$")
    @Column(name = "fura_id")
    private Integer furaId;

    @OneToOne(mappedBy = "order")
    private Fura fura;
    @OneToMany(mappedBy = "order")
    private List<DriverShift> driverShift;
    @OneToOne(mappedBy = "order")
    private OrderStatus orderStatus;


    public Order() {
    }

    public Order(Integer id) {
        this.id = id;
    }

    public List<DriverShift> getDriverShift() {
        return driverShift;
    }

    public void setDriverShift(List<DriverShift> driverShift) {
        this.driverShift = driverShift;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Fura getFura() {

        return fura;
    }

    public void setFura(Fura fura) {
        this.fura = fura;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFuraId(Integer furaId) {
        this.furaId = furaId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getFuraId() {
        return furaId;
    }

}
