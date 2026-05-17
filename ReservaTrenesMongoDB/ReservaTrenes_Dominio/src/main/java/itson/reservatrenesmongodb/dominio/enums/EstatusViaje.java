/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package itson.reservatrenesmongodb.dominio.enums;

/**
 * Enum que representa el estatus de un viaje programado.
 *
 * @author Afgord
 */
public enum EstatusViaje {

    /**
     * El viaje está programado y puede recibir reservaciones de boletos.
     */
    PROGRAMADO,

    /**
     * El viaje ya fue finalizado.
     */
    FINALIZADO,

    /**
     * El viaje fue cancelado.
     */
    CANCELADO
}