/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import java.time.Instant;

/**
 * Clase que representa un resumen del viaje asociado a un boleto.
 *
 * Esta clase se utiliza como documento embebido dentro de Boleto para conservar
 * información relevante del viaje al momento de generar el boleto.
 *
 * @author Afgord
 */
public class ViajeResumen {

    /**
     * Fecha y hora de salida del viaje.
     */
    private Instant fechaHoraSalida;

    /**
     * Nombre de la locación de origen.
     */
    private String origen;

    /**
     * Nombre de la locación de destino.
     */
    private String destino;

    /**
     * Nombre del tren asignado al viaje.
     */
    private String tren;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public ViajeResumen() {
    }

    /**
     * Constructor que inicializa el resumen del viaje.
     *
     * @param fechaHoraSalida Fecha y hora de salida.
     * @param origen Nombre de la locación de origen.
     * @param destino Nombre de la locación de destino.
     * @param tren Nombre del tren asignado.
     */
    public ViajeResumen(Instant fechaHoraSalida, String origen, String destino,
            String tren) {
        this.fechaHoraSalida = fechaHoraSalida;
        this.origen = origen;
        this.destino = destino;
        this.tren = tren;
    }

    public Instant getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Instant fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTren() {
        return tren;
    }

    public void setTren(String tren) {
        this.tren = tren;
    }
}