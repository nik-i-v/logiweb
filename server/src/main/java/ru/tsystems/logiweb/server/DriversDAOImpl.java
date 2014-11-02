package ru.tsystems.logiweb.server;

import ru.tsystems.logiweb.server.entities.DriverShift;
import ru.tsystems.logiweb.server.entities.Drivers;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.lang.IllegalArgumentException;

class DriversDAOImpl  {

    private Logger logger = Logger.getLogger(DriversDAOImpl.class.getName());

    //@Override
    public void addDriver(Drivers driver, DriverShift driverShift, Long license, EntityManager entityManager) throws SQLException, IllegalArgumentException {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            logger.info("Add new driver with license: " + license);
//            List<Long> ids = checkIfDriverIdIsUnique(entityManager);
//            if (ids.contains(license)) {
//                throw new IllegalArgumentException("Driver with this license is already exists.");
//            }
            entityManager.merge(driver);
            entityManager.merge(driverShift);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

//    @Override
//    public List<DriversDTO> getDrivers(EntityManager entityManager) throws SQLException {
//        DriversDTO dto;
//        List<DriversDTO> custDTOs = new ArrayList<DriversDTO>();
//        List<Object[]> list = entityManager.createQuery("SELECT d.driversId, d.surname, d.name, d.patronymic, " +
//                "d.license, ds.status FROM Drivers d INNER JOIN d.driverShift ds " +
//                "WHERE ds.driverId = d.driversId").getResultList();
//        for (Object[] objectArray : list) {
//            dto = new DriversDTO(objectArray[0], objectArray[1],
//                    objectArray[2], objectArray[3], objectArray[4], objectArray[5]);
//            custDTOs.add(dto);
//        }
//        return custDTOs;
//    }
//
//    @Override
//    public List<OrderForDriversDTO> getCurrentOrderInfoForDrivers(String login, EntityManager entityManager) throws IllegalArgumentException {
//        OrderForDriversDTO dto;
//        List<OrderForDriversDTO> orderForDriversDTOs = new ArrayList<OrderForDriversDTO>();
//        EntityTransaction transaction = entityManager.getTransaction();
//        Integer driverId = Integer.parseInt(login);
//        try {
//            transaction.begin();
//            Query hasOrder = entityManager.createQuery("SELECT COUNT(ds.orderId) FROM DriverShift ds WHERE ds.driverId = :driverId");
//            hasOrder.setParameter("driverId", driverId);
//            Integer orderCount = Integer.parseInt(hasOrder.getSingleResult().toString());
//            if (orderCount == 0) {
//                throw new IllegalArgumentException("You have not any orders now");
//            }
//            Query isInShift = entityManager.createQuery("SELECT ds.status FROM DriverShift ds WHERE ds.driverId = :id");
//            isInShift.setParameter("id", Integer.parseInt(login));
//            String driverStatus = isInShift.getSingleResult().toString();
//            if (driverStatus.equals(DriverShift.Status.notShift.toString())) {
//                throw new IllegalArgumentException("You are not in shift now");
//            }
//            Query getOrderNumber = entityManager.createQuery("SELECT ds.orderId FROM DriverShift ds WHERE ds.driverId = :driverId");
//            getOrderNumber.setParameter("driverId", driverId);
//            Integer orderNumber = Integer.parseInt(getOrderNumber.getSingleResult().toString());
//            Query query = entityManager.createQuery("SELECT DISTINCT  f.furasId, ds.driverId, o.id, " +
//                    "oi.gpsLat, oi.gpsLong, oi.name, oi.status, oi.weight FROM DriverShift ds " +
//                    "LEFT JOIN ds.order o " +
//                    "LEFT JOIN o.orderStatus os " +
//                    "LEFT JOIN os.orderInfo oi " +
//                    "LEFT JOIN o.fura f " +
//                    "LEFT JOIN o.driverShift ds " +
//                    "WHERE ds.orderId = :id");
//            query.setParameter("id", orderNumber);
//            List<Object[]> list = query.getResultList();
//            for (Object[] objectArray : list) {
//                if (!objectArray[1].toString().equals(null) && objectArray[1].toString().equals(login)) {
//                    objectArray[1] = null;
//                }
//                dto = new OrderForDriversDTO(objectArray[0], objectArray[1], objectArray[2], objectArray[3], objectArray[4],
//                        objectArray[5], objectArray[6], objectArray[7]);
//                orderForDriversDTOs.add(dto);
//            }
//            transaction.commit();
//        } finally {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//        }
//        return orderForDriversDTOs;
//    }
//
//    @Override
//    public void changeDriverStatusForDrivers(String status, Integer driverId, EntityManager entityManager) throws IllegalArgumentException {
//        EntityTransaction transaction = entityManager.getTransaction();
//        try {
//            transaction.begin();
//            Query haveOrder = entityManager.createQuery("SELECT ds.orderId FROM DriverShift ds WHERE ds.driverId = :driverId");
//            haveOrder.setParameter("driverId", driverId);
//            if (haveOrder.getSingleResult().toString().equals(null)) {
//                throw new IllegalArgumentException("You have not orders");
//            }
//            Query driverCurrentStatus = entityManager.createQuery("SELECT ds.status FROM  DriverShift ds " +
//                    "WHERE ds.orderId = :number");
//            driverCurrentStatus.setParameter("number", Integer.parseInt(haveOrder.getSingleResult().toString()));
//            String driverStatus = driverCurrentStatus.getSingleResult().toString();
//            if (status.equals(DriverShift.Status.atWeel.toString())) {
//                Query driverAlreadyAtWeel = entityManager.createQuery("SELECT COUNT (ds.driverId) FROM  DriverShift ds " +
//                        "WHERE ds.status = :status AND ds.orderId = :number");
//                driverAlreadyAtWeel.setParameter("status", DriverShift.Status.atWeel);
//                driverAlreadyAtWeel.setParameter("number", Integer.parseInt(haveOrder.getSingleResult().toString()));
//                if (Integer.parseInt(driverAlreadyAtWeel.getSingleResult().toString()) != 0) {
//                    throw new IllegalArgumentException("Another driver or you is already at weel");
//                }
//
//                Query newStatus = entityManager.createQuery("UPDATE DriverShift ds SET ds.status = :status WHERE ds.driverId = :id");
//                    newStatus.setParameter("status", DriverShift.Status.atWeel);
//                newStatus.setParameter("id", driverId);
//                newStatus.executeUpdate();
//            } else  {
//                if (driverStatus.equals(DriverShift.Status.shift.toString())){
//                    throw new IllegalArgumentException("You already have 'shift' status");
//                }
//                Query newStatus = entityManager.createQuery("UPDATE DriverShift ds SET ds.status = :status WHERE ds.driverId = :id");
//                newStatus.setParameter("id", driverId);
//                newStatus.setParameter("status", DriverShift.Status.shift);
//                newStatus.executeUpdate();
//            }
//            transaction.commit();
//        } finally {
//            if (transaction.isActive()) {
//                transaction.rollback();
//            }
//        }
//    }
//
//
//    private List<Long> checkIfDriverIdIsUnique(EntityManager entityManager) throws SQLException {
//        return entityManager.createQuery("SELECT d.license FROM Drivers d").getResultList();
//    }
}

