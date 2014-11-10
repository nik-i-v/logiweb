package ru.tsystems.javaschool.logiweb.lw.service.admin;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;

import javax.ejb.Local;
import java.sql.SQLException;
import java.util.List;

@Local
public interface DriverService {
    List<Drivers> getAllDrivers();

    void addDriver(String surname, String name, String patronymic, Long licenseId) throws SQLException;
}