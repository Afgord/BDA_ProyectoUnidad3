/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;


/**
 * Clase que representa la ruta de un viaje, formada por una locación de origen
 * y una locación de destino.
 *
 * Esta clase se utiliza como documento embebido dentro de Viaje.
 *
 * @author Afgord
 */
public class RutaViaje {

    /**
     * Locación de origen del viaje.
     */
    private LocacionResumen origen;

    /**
     * Locación de destino del viaje.
     */
    private LocacionResumen destino;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public RutaViaje() {
    }

    /**
     * Constructor que inicializa la ruta del viaje.
     *
     * @param origen Locación de origen.
     * @param destino Locación de destino.
     */
    public RutaViaje(LocacionResumen origen, LocacionResumen destino) {
        this.origen = origen;
        this.destino = destino;
    }

    public LocacionResumen getOrigen() {
        return origen;
    }

    public void setOrigen(LocacionResumen origen) {
        this.origen = origen;
    }

    public LocacionResumen getDestino() {
        return destino;
    }

    public void setDestino(LocacionResumen destino) {
        this.destino = destino;
    }
}