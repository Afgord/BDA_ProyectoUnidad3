package itson.reservatrenesmongodb.exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Excepción utilizada para representar errores relacionados con reglas de
 * negocio.
 *
 * Esta excepción debe utilizarse cuando una operación no puede realizarse
 * porque viola una regla del sistema de reservaciones de tren, aunque los datos
 * recibidos sean técnicamente válidos.
 *
 * Ejemplos de uso:
 *
 * throw new NegocioException("No hay asientos disponibles para este viaje.");
 * throw new NegocioException("La reserva ya fue cancelada.");
 *
 * @author Afgord
 */
public class NegocioException extends Exception {

    /**
     * Crea una nueva excepción de negocio sin mensaje.
     */
    public NegocioException() {
        super();
    }

    /**
     * Crea una nueva excepción de negocio con un mensaje descriptivo.
     *
     * @param message Mensaje que describe la regla de negocio incumplida.
     */
    public NegocioException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción de negocio con un mensaje descriptivo y la causa
     * original del error.
     *
     * @param message Mensaje que describe la regla de negocio incumplida.
     * @param cause Excepción original que causó el error.
     */
    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva excepción de negocio a partir de una causa original.
     *
     * @param cause Excepción original que causó el error.
     */
    public NegocioException(Throwable cause) {
        super(cause);
    }
}
