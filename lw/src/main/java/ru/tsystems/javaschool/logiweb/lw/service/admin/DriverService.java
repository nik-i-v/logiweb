package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.DriverShift;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import javax.ejb.Local;
import java.sql.SQLException;
import java.util.List;

@Local
public interface DriverService {
    List<DriverShift> getAllDrivers();

    List<Long> getAllDriverId();

    void addDriver(String surname, String name, String patronymic, Long licenseId) throws IncorrectDataException;

    List<Long> getAllFreeDrivers();
}
