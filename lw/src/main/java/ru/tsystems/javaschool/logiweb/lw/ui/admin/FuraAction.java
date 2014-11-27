package ru.tsystems.javaschool.logiweb.lw.ui.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;
import ru.tsystems.javaschool.logiweb.lw.service.admin.FuraService;

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

/**
 * This class provides access to services of actions with fura company employee.
 * The class is serializable.
 *
 * @author Irina Nikulina
 */
@ManagedBean(name = "furaAction")
@ViewScoped
public class FuraAction implements Serializable {

    private Fura fura;
    private List<Fura> furas;
    private List<String> freeFuras;
    private List<Fura> freeFuraTable;

    @Inject
    private FacesContext facesContext;

    @EJB
    private FuraService furaService;

    /**
     * Initialized a new fura and sets a value for the list of furas.
     */
    @PostConstruct
    public void initNewFura() {
        fura = new Fura();
        furas = furaService.getAllFura();
    }

    /**
     * Adds a new fura.
     * @return true if the fura was added and false if it's not.
     */
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

    /**
     * Sets a value for the list of free furas.
     */
    public void allFreeFuras() {
        freeFuras = furaService.getFreeFuras();
    }

    @Named
    @Produces
    public List<String> getFreeFuras() {
        allFreeFuras();
        return freeFuras;
    }

    @Produces
    @Named
    public List<Fura> getFuras() {
        return furas;
    }

    @Produces
    @Named
    public Fura getFura() {
        return fura;
    }

    @Named
    public void setFuras(List<Fura> furas) {
        this.furas = furas;
    }

    public void setFreeFuras(List<String> freeFuras) {
        this.freeFuras = freeFuras;
    }

    public void setFura(Fura fura) {
        this.fura = fura;
    }


}

