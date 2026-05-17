/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Clase que representa un elemento del historial de viajes de un pasajero.
 *
 * Esta clase se utiliza como subdocumento dentro del arreglo historialViajes de
 * Pasajero.
 *
 * @author Afgord
 */
public class HistorialViaje {

    /**
     * Identificador del boleto asociado al viaje.
     */
    @BsonProperty("boleto_id")
    private String boletoId;

    /**
     * Identificador del viaje asociado.
     */
    @BsonProperty("viaje_id")
    private String viajeId;

    /**
     * Destino del viaje realizado o reservado.
     */
    private String destino;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public HistorialViaje() {
    }

    /**
     * Constructor que inicializa el historial de viaje.
     *
     * @param boletoId Identificador del boleto.
     * @param viajeId Identificador del viaje.
     * @param destino Destino del viaje.
     */
    public HistorialViaje(String boletoId, String viajeId, String destino) {
        this.boletoId = boletoId;
        this.viajeId = viajeId;
        this.destino = destino;
    }

    public String getBoletoId() {
        return boletoId;
    }

    public void setBoletoId(String boletoId) {
        this.boletoId = boletoId;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}