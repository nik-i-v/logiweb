package ru.tsystems.javaschool.logiweb.lw.util;

/**
 * Thrown when user attempts :
 * <p>
 *     <li>to add busy fura to an order</li>
 *     <li>to add fura with existing fura number</li>
 *     <li>to add existing driver</li>
 *     <li>to add incorrect amount of drivers to a fura</li>
 *     <li>to add too heavy goods to a fura</li>
 *     <li>to set wrong status for a driver</li>
 * </p>
 *
 * @author Irina Niculina
 */
public class IncorrectDataException extends Exception{

    /**
     * Throws IncorrectDataException with a message.
     * @param message a message
     */
    public IncorrectDataException(String message) {
        super(message);
    }
}
