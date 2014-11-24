package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.ejb.Local;
import java.sql.SQLException;
import java.util.List;

/**
 * This interface provides operations for company employees to manage the list of drivers.
 *
 *
 * @author Irina Nikulina
 *
 */
@Local
public interface DriverService {

    /**
     * Returns all drivers from the database.
     * @return the list of drivers
     */
    List<DriverShift> getAllDrivers();

    /**
     * Returns all license numbers of drivers.
     * @return the list of license numbers
     */
    List<Long> getAllDriverId();

    /**
     * Adds a driver to the database. Also adds the record with this driver to the user list.
     * @param surname surname of a driver
     * @param name name of a driver
     * @param patronymic patronymic of a driver
     * @param licenseId license number of a driver
     * @throws IncorrectDataException if there is a driver with such license number
     */
    void addDriver(String surname, String name, String patronymic, Long licenseId) throws IncorrectDataException;

    /**
     * Returns all free drivers from the database.
     * @return the list of drivers
     */
    List<Long> getAllFreeDrivers();
}
