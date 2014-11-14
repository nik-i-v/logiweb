package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.service.admin.FuraService;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

//@Model
@ManagedBean(name = "furaAction")
@SessionScoped
public class FuraAction implements Serializable {


    @EJB
    private FuraService furaService;
    private Fura fura;
    private List<Fura> furas;
    private List<String> freeFuras;

    public void setFura(Fura fura) {
        this.fura = fura;
    }

    @Named
    @Produces
    public List<String> getFreeFuras() {
        allFreeFuras();
        return freeFuras;
    }

    public void setFreeFuras(List<String> freeFuras) {
        this.freeFuras = freeFuras;
    }

    @Produces
    @Named
    public List<Fura> getFuras() {
        return furas;
    }

    @Named
    public void setFuras(List<Fura> furas) {
        this.furas = furas;
    }

    @Produces
    @Named
    public Fura getFura() {
        return fura;
    }

    @PostConstruct
    public void initNewFura() {
        fura = new Fura();
        furas = furaService.getAllFura();
    }

    public boolean addFura() {
        furaService.addFura(fura.getFuraNumber(), fura.getDriverCount(), fura.getCapacity());
        furas = furaService.getAllFura();
        return true;

    }

    public void allFreeFuras(){
        freeFuras = furaService.getFreeFuras();
    }


}

