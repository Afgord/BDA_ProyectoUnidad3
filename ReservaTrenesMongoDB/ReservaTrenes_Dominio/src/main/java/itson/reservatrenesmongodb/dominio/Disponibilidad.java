/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

/**
 * Clase que representa la disponibilidad actual de lugares dentro de un viaje.
 *
 * Esta clase se utiliza como documento embebido en Viaje para controlar cuántos
 * lugares generales y de primera clase siguen disponibles.
 *
 * @author Afgord
 */
public class Disponibilidad {

    /**
     * Cantidad de lugares generales disponibles.
     */
    private int general;

    /**
     * Cantidad de lugares de primera clase disponibles.
     */
    private int primeraClase;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Disponibilidad() {
    }

    /**
     * Constructor que inicializa la disponibilidad de lugares.
     *
     * @param general Cantidad de lugares generales disponibles.
     * @param primeraClase Cantidad de lugares de primera clase disponibles.
     */
    public Disponibilidad(int general, int primeraClase) {
        this.general = general;
        this.primeraClase = primeraClase;
    }

    /**
     * Obtiene la cantidad de lugares generales disponibles.
     *
     * @return Cantidad de lugares generales disponibles.
     */
    public int getGeneral() {
        return general;
    }

    /**
     * Establece la cantidad de lugares generales disponibles.
     *
     * @param general Cantidad de lugares generales disponibles.
     */
    public void setGeneral(int general) {
        this.general = general;
    }

    /**
     * Obtiene la cantidad de lugares de primera clase disponibles.
     *
     * @return Cantidad de lugares de primera clase disponibles.
     */
    public int getPrimeraClase() {
        return primeraClase;
    }

    /**
     * Establece la cantidad de lugares de primera clase disponibles.
     *
     * @param primeraClase Cantidad de lugares de primera clase disponibles.
     */
    public void setPrimeraClase(int primeraClase) {
        this.primeraClase = primeraClase;
    }
}