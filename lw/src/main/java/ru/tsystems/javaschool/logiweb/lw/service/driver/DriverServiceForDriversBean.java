package ru.tsystems.javaschool.logiweb.lw.service.driver;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.service.admin.OrderServiceBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;

@Named
@Stateless
public class DriverServiceForDriversBean implements DriverServiceForDrivers {

    @EJB
    private OrderServiceForDrivers orderServiceForDrivers;

    @Inject
    private EntityManager entityManager;

    @Override
    public void changeDriverStatusForDrivers(String license, String status) {
        String currentStatus = getDriverStatus(license);
        if (currentStatus.equals(status) || currentStatus.equals(DriverShift.Status.notShift.toString())) {
            throw new IllegalArgumentException("You can't change your status to " + status);
        }
        if (status.equals(DriverShift.Status.atWeel.toString())) {
            isAnybodyAtWeel(orderServiceForDrivers.hasOrder(license));
            changeDriverStatus(license, DriverShift.Status.atWeel);
        } else if (status.equals(DriverShift.Status.shift.toString())) {
            changeDriverStatus(license, DriverShift.Status.shift);
        }
    }

    private String getDriverStatus(String license) {
        Query query = entityManager.createQuery("SELECT ds.status FROM  DriverShift ds " +
                "WHERE ds.drivers.license = :license");
        query.setParameter("license", Integer.parseInt(license));
        return query.getSingleResult().toString();
    }

    private void isAnybodyAtWeel(Integer orderNumber) {
        Query query = entityManager.createQuery("SELECT COUNT(ds.status) FROM  DriverShift ds " +
                "WHERE ds.orderNumber = :number");
        query.setParameter("number", orderNumber);
        if (query.getSingleResult() != 0) {
            throw new IllegalArgumentException("Another driver or you is already at weel");
        }
    }

    private void changeDriverStatus(String license, DriverShift.Status status) {
        Query newStatus = entityManager.createQuery("UPDATE DriverShift ds SET ds.status = :status WHERE ds.drivers.license = :license");
        newStatus.setParameter("status", status);
        newStatus.setParameter("license", license);
        newStatus.executeUpdate();
    }
}
