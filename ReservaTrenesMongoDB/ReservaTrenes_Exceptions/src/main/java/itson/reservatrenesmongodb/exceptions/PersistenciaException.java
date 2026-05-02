package itson.reservatrenesmongodb.exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Excepción utilizada para representar errores ocurridos en la capa de
 * persistencia.
 *
 * Esta excepción debe utilizarse cuando ocurra un problema al interactuar con
 * la base de datos MongoDB, los DAOs, la conexión o cualquier operación de
 * almacenamiento, consulta, actualización o eliminación de datos.
 *
 * Ejemplos de uso:
 *
 * throw new PersistenciaException("Error al guardar la reserva.", e);
 * throw new PersistenciaException("No fue posible consultar los trenes.");
 *
 * @author Afgord
 */
public class PersistenciaException extends Exception {

    /**
     * Crea una nueva excepción de persistencia sin mensaje.
     */
    public PersistenciaException() {
        super();
    }

    /**
     * Crea una nueva excepción de persistencia con un mensaje descriptivo.
     *
     * @param message Mensaje que describe el error ocurrido.
     */
    public PersistenciaException(String message) {
        super(message);
    }

    /**
     * Crea una nueva excepción de persistencia con un mensaje descriptivo y la
     * causa original del error.
     *
     * @param message Mensaje que describe el error ocurrido.
     * @param cause Excepción original que causó el error.
     */
    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva excepción de persistencia a partir de una causa original.
     *
     * @param cause Excepción original que causó el error.
     */
    public PersistenciaException(Throwable cause) {
        super(cause);
    }
}
