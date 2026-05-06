/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package itson.reservatrenesmongodb.dominio.enums;

/**
 * Enum que representa el estatus de un boleto generado por el sistema.
 *
 * @author Afgord
 */
public enum EstatusBoleto {

    /**
     * El boleto fue confirmado correctamente.
     */
    CONFIRMADO,

    /**
     * El boleto fue cancelado por el pasajero o por el sistema.
     */
    CANCELADO
}