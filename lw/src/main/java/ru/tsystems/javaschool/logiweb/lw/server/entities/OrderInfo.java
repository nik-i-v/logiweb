package ru.tsystems.javaschool.logiweb.lw.server.entities;

 import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import javax.persistence.*;
 import javax.xml.bind.annotation.XmlRootElement;
 import java.io.Serializable;

@Entity
@Table(name = "order_info")
public class OrderInfo implements Serializable {
    @Id
    @Column(name = "order_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderInfoId;

    @NotNull
    @Column(name = "order_number")
    private Integer orderNumber;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "gps_lat")
    private Double gpsLat;

    @NotNull
    @Column(name = "gps_long")
    private Double gpsLong;

    @NotNull
    @Column(name = "weight")
    private Double weight;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_number", insertable = false, updatable = false)
    private OrderStatus orderStatus;

    public enum Status {
        yes, no
    }

    public OrderInfo() {
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrderInfoId(Integer orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public void setGpsLat(Double gpsLat) {
        this.gpsLat = gpsLat;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setGpsLong(Double gpsLong) {
        this.gpsLong = gpsLong;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getOrderInfoId() {
        return orderInfoId;
    }

    public String getName() {
        return name;
    }

    public Double getGpsLat() {
        return gpsLat;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getGpsLong() {
        return gpsLong;
    }

    public Status getStatus() {
        return status;
    }

}
