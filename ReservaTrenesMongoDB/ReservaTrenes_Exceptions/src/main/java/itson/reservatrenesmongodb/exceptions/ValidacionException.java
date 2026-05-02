package itson.reservatrenesmongodb.exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Excepción utilizada para representar errores de validación de datos.
 *
 * Esta excepción debe utilizarse cuando los datos ingresados por el usuario o
 * recibidos por el sistema no cumplen con las condiciones mínimas requeridas
 * para ejecutar una operación.
 *
 * Ejemplos de uso:
 *
 * throw new ValidacionException("El nombre del pasajero es obligatorio.");
 * throw new ValidacionException("La fecha del viaje no puede estar vacía.");
 *
 * @author Afgord
 */
public class ValidacionException extends Exception {

    /**
     * Crea una nueva excepción de validación sin mensaje.
     */
    public ValidacionException() {
        super();
    }

    /**
     * Crea una nueva excepción de validación con un mensaje descriptivo.
     *
     * @param message Mensaje que describe el error de validación ocurrido.
     */
    public ValidacionException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción de validación con un mensaje descriptivo y la
     * causa original del error.
     *
     * @param message Mensaje que describe el error de validación ocurrido.
     * @param cause Excepción original que causó el error.
     */
    public ValidacionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva excepción de validación a partir de una causa original.
     *
     * @param cause Excepción original que causó el error.
     */
    public ValidacionException(Throwable cause) {
        super(cause);
    }
}
