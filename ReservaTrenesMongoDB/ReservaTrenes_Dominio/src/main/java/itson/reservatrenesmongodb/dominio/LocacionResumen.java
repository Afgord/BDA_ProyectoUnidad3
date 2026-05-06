/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

/**
 * Clase que representa un resumen de una locación dentro de una ruta de viaje.
 *
 * Esta clase se utiliza como documento embebido para indicar origen o destino,
 * conservando el identificador de la locación referenciada y su nombre.
 *
 * @author Afgord
 */
public class LocacionResumen {

    /**
     * Identificador de la locación referenciada.
     */
    private String locacionId;

    /**
     * Nombre de la locación.
     */
    private String nombre;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public LocacionResumen() {
    }

    /**
     * Constructor que inicializa el resumen de locación.
     *
     * @param locacionId Identificador de la locación.
     * @param nombre Nombre de la locación.
     */
    public LocacionResumen(String locacionId, String nombre) {
        this.locacionId = locacionId;
        this.nombre = nombre;
    }

    public String getLocacionId() {
        return locacionId;
    }

    public void setLocacionId(String locacionId) {
        this.locacionId = locacionId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}