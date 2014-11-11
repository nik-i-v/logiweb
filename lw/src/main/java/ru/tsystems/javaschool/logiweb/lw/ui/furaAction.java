package ru.tsystems.javaschool.logiweb.lw.ui;

import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class furaAction implements Serializable {
    private String number;
    private Integer driverCount;
    private String capacity;

    public String getNumber() {
        return number;
    }

    public Integer getDriverCount() {
        return driverCount;
    }

    public String getCapacity() {
        return capacity;
    }
}
