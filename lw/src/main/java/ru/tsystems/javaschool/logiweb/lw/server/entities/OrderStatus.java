package ru.tsystems.javaschool.logiweb.lw.server.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "order_status")
public class OrderStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_status_id")
    private Integer orderId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "order_status_id", insertable = false, updatable = false)
    private Order order;

    @OneToMany(mappedBy = "orderStatus", fetch = FetchType.EAGER)
    private List<OrderInfo> orderInfo;

    public enum Status {
        created, confirmed, shipped, made, closed
    }

    public OrderStatus() {
    }

    public List<OrderInfo> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(List<OrderInfo> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus(Integer id) {
        this.orderId = id;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Status getStatus() {
        return status;
    }
}

