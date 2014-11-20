package ru.tsystems.javaschool.logiweb.lw.ui;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.service.admin.FuraService;
import ru.tsystems.javaschool.logiweb.lw.ui.annotations.Admin;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

//@Model
@Admin
@ManagedBean(name = "furaAction")
@ViewScoped
public class FuraAction implements Serializable {

    @Inject
    private FacesContext facesContext;

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
        try {
            furaService.addFura(fura.getFuraNumber(), fura.getDriverCount(), fura.getCapacity());
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Fura was added", "Fura addition successful"));
            furas = furaService.getAllFura();
            fura.setFuraNumber(null);
            fura.setDriverCount(null);
            return true;
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Addition unsuccessful"));
            return false;
        }
    }

    public void allFreeFuras() {
        freeFuras = furaService.getFreeFuras();
    }


}

