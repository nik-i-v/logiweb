package ru.tsystems.javaschool.logiweb.lw.server.entities;



import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "orders", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Order implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[A-Z]{2}\\d{5}$")
    @Column(name = "fura_id")
    private String furaId;

    @OneToOne(mappedBy = "order", fetch = FetchType.EAGER)
    private Fura fura;
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
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

    public void setFuraId(String furaId) {
        this.furaId = furaId;
    }

    public Integer getId() {
        return id;
    }

    public String getFuraId() {
        return furaId;
    }

}
