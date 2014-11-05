package ru.tsystems.javaschool.logiweb.lw.service;

import ru.tsystems.javaschool.logiweb.lw.server.entities.Fura;

import javax.ejb.Local;
import java.util.List;

@Local
public interface FuraService {
    List<Fura> getAllFura();

    void addFura(String number, Byte driverCount, Fura.Capacity capacity);
}
