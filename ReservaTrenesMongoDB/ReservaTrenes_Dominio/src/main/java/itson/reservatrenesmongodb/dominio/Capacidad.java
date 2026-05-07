/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Clase que representa la capacidad de lugares disponibles por tipo dentro de
 * un tren o viaje.
 *
 * Esta clase se utiliza como documento embebido en las entidades del dominio,
 * principalmente en Tren y Viaje.
 *
 * @author Afgord
 */
public class Capacidad {

    /**
     * Cantidad de lugares de tipo general.
     */
    private int general;

    /**
     * Cantidad de lugares de primera clase.
     */
    @BsonProperty("primera_clase")
    private int primeraClase;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Capacidad() {
    }

    /**
     * Constructor que inicializa la capacidad con lugares generales y de primera
     * clase.
     *
     * @param general Cantidad de lugares generales.
     * @param primeraClase Cantidad de lugares de primera clase.
     */
    public Capacidad(int general, int primeraClase) {
        this.general = general;
        this.primeraClase = primeraClase;
    }

    /**
     * Obtiene la cantidad de lugares generales.
     *
     * @return Cantidad de lugares generales.
     */
    public int getGeneral() {
        return general;
    }

    /**
     * Establece la cantidad de lugares generales.
     *
     * @param general Cantidad de lugares generales.
     */
    public void setGeneral(int general) {
        this.general = general;
    }

    /**
     * Obtiene la cantidad de lugares de primera clase.
     *
     * @return Cantidad de lugares de primera clase.
     */
    public int getPrimeraClase() {
        return primeraClase;
    }

    /**
     * Establece la cantidad de lugares de primera clase.
     *
     * @param primeraClase Cantidad de lugares de primera clase.
     */
    public void setPrimeraClase(int primeraClase) {
        this.primeraClase = primeraClase;
    }
}