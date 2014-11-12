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
@ManagedBean
@SessionScoped
public class FuraAction implements Serializable {
    private Fura fura;
    private List<Fura> furas;

    public List<Fura> getFuras() {
        return furas;
    }

    public void setFuras(List<Fura> furas) {
        this.furas = furas;
    }

    @Produces
    @Named
    public Fura getFura() {
        return fura;
    }

    public void setFura(Fura fura) {
        this.fura = fura;
    }


    @EJB
    private FuraService furaService;

    @PostConstruct
    public void initNewFura() {
        fura = new Fura();
//        getAllDrivers();
    }

    //
    public List<Fura> getAllFuras() {
        return furaService.getAllFura();
    }

    public boolean addFura() {
        furaService.addFura(fura.getFuraNumber(), fura.getDriverCount(), fura.getCapacity());
        return true;

    }
}

