package ru.tsystems.javaschool.logiweb.lw.util;

import org.picketlink.annotations.PicketLink;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;
/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 *
 */
public class Resources {
    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
    @SuppressWarnings("unused")
    @Produces
    @PersistenceContext(unitName = "logiweb")
    private EntityManager em;

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    @Produces
    @PicketLink
    public EntityManager getPicketLinkEntityManager() {
        return em;
    }

}
