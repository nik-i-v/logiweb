package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.OrderInfo;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ManagedBean(name = "modifyOrder")
@RequestScoped
public class ModifyOrderAction  implements Serializable{
    private OrderInfo orderInfo;

    @EJB
    private OrderService orderService;

    @Inject
    private FacesContext facesContext;

    @Produces
    @Named
    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @PostConstruct
    public void init(){
        orderInfo = new OrderInfo();
    }

    public void addGoods() {
        try {
            orderService.addGoods(orderInfo.getOrderNumber(), orderInfo.getName(), orderInfo.getGpsLat(), orderInfo.getGpsLong(),
                    orderInfo.getWeight());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Goods was added", "Goods addition successful"));
            orderInfo.setName(null);
            orderInfo.setGpsLat(null);
            orderInfo.setGpsLong(null);
            orderInfo.setWeight(null);
        }catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
        }
    }
}
