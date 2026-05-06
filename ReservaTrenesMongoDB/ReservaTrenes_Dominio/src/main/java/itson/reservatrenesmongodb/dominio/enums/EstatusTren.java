/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package itson.reservatrenesmongodb.dominio.enums;

/**
 * Enum que representa el estatus operativo de un tren dentro del sistema.
 *
 * @author Afgord
 */
public enum EstatusTren {

    /**
     * El tren se encuentra activo y puede ser asignado a viajes.
     */
    ACTIVO,

    /**
     * El tren se encuentra en mantenimiento y no debe asignarse a viajes.
     */
    MANTENIMIENTO,

    /**
     * El tren se encuentra fuera de servicio.
     */
    FUERA_SERVICIO
}