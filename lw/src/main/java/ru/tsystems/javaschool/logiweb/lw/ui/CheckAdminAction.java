package ru.tsystems.javaschool.logiweb.lw.ui;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckAdminAction extends Dispatcher {

    private static Logger logger = Logger.getLogger(CheckAdminAction.class.getName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("text/html");
//        String action = req.getParameter("action");
//        PrintWriter out = resp.getWriter();
//        if (action.equals("Show all orders")) {
//            OrderService orderService = new OrderServiceImpl();
//            try {
//                List<OrderDTO> orders = orderService.getOrders();
//                req.setAttribute("ordersList", orders);
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "Exception: ", e);
//            }
//            this.forward("/allOrders.jsp", req, resp);
//        } else if (action.equals("Show all drivers")) {
//            DriverService driverService = new DriverServiceImpl();
//            try {
//                List<DriversDTO> drivers = driverService.getDrivers();
//                req.setAttribute("driversList", drivers);
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "Exception: ", e);
//            }
//            this.forward("/allDrivers.jsp", req, resp);
//        } else if (action.equals("Show all trucks")) {
//            FuraService furaService = new FuraServiceImpl();
//            try {
//                List<Fura> furas = furaService.getFuras();
//                req.setAttribute("furasList", furas);
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "Exception: ", e);
//            }
//            this.forward("/allFura.jsp", req, resp);
//        } else if (action.equals("Add new truck")) {
//            this.forward("/addFura.jsp", req, resp);
//        } else if (action.equals("Add new driver")) {
//            this.forward("/addDriver.jsp", req, resp);
//        } else if (action.equals("Add new order")) {
//            logger.info("Create new order.");
//            OrderService orderService = new OrderServiceImpl();
//            try {
//                orderService.addOrder();
//                out.println("New order was created successfull.");
//            } catch (SQLException e) {
//                logger.log(Level.SEVERE, "Exception: ", e);
//            }
//        } else if (action.equals("Add new goods")) {
//            this.forward("/addGoods.jsp", req, resp);
//        } else if (action.equals("Add fura and drivers to order")) {
//            this.forward("/addFuraAndDriversToOrder.jsp", req, resp);
//        } else if (action.equals("Close Order")) {
//            this.forward("/closeOrder.jsp", req, resp);
//        }
    }
}
