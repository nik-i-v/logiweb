import org.junit.Before;
import org.junit.Test;
import ru.tsystems.javaschool.logiweb.lw.server.entities.Drivers;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverService;
import ru.tsystems.javaschool.logiweb.lw.service.admin.DriverServiceBean;
import ru.tsystems.javaschool.logiweb.lw.util.IncorrectDataException;

import java.sql.Driver;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class DriverServicesTest {

    private DriverService service;
    private String surname;
    private String name;
    private String patronymic;
    private Long license;


    @Test
    public void getAllTest() {
        service = mock(DriverServiceBean.class);
        List all = new LinkedList();
        all.add(new Drivers("Petrov", "Petr", "Sergeevich", 38475647364L));
        when(service.getAllDrivers()).thenReturn(all);
        List result = service.getAllDrivers();
        assertEquals(all, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDriverTest_is_not_unique() throws IncorrectDataException {
        service = mock(DriverServiceBean.class);
        service.addDriver("Petrov", "Petr", "Sergeevich", 38475647364L);
        service.addDriver("Petrov", "Petr", "Sergeevich", 38475647364L);
//        verify(service.addDriver());
    }

}
