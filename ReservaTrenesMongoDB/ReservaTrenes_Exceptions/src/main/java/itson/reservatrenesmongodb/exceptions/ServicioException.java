package itson.reservatrenesmongodb.exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Excepción utilizada para representar errores ocurridos en la capa de
 * servicios.
 *
 * Esta excepción debe utilizarse cuando ocurra un problema al ejecutar una
 * operación de aplicación, coordinar reglas de negocio, utilizar DAOs o
 * transformar información entre entidades y DTOs.
 *
 * Ejemplos de uso:
 *
 * throw new ServicioException("No fue posible registrar la reserva.", e);
 * throw new ServicioException("No se pudo obtener el historial de reservas.");
 *
 * @author Afgord
 */
public class ServicioException extends Exception {

    /**
     * Crea una nueva excepción de servicio sin mensaje.
     */
    public ServicioException() {
        super();
    }

    /**
     * Crea una nueva excepción de servicio con un mensaje descriptivo.
     *
     * @param message Mensaje que describe el error ocurrido.
     */
    public ServicioException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción de servicio con un mensaje descriptivo y la
     * causa original del error.
     *
     * @param message Mensaje que describe el error ocurrido.
     * @param cause Excepción original que causó el error.
     */
    public ServicioException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva excepción de servicio a partir de una causa original.
     *
     * @param cause Excepción original que causó el error.
     */
    public ServicioException(Throwable cause) {
        super(cause);
    }
}
