/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

/**
 * Clase que representa la dirección registrada de un pasajero.
 *
 * Esta clase se utiliza como documento embebido dentro de Pasajero.
 *
 * @author Afgord
 */
public class Direccion {

    /**
     * Calle de la dirección.
     */
    private String calle;

    /**
     * Colonia de la dirección.
     */
    private String colonia;

    /**
     * Ciudad de la dirección.
     */
    private String ciudad;

    /**
     * Estado de la dirección.
     */
    private String estado;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Direccion() {
    }

    /**
     * Constructor que inicializa la dirección completa.
     *
     * @param calle Calle de la dirección.
     * @param colonia Colonia de la dirección.
     * @param ciudad Ciudad de la dirección.
     * @param estado Estado de la dirección.
     */
    public Direccion(String calle, String colonia, String ciudad, String estado) {
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
    }

    /**
     * Obtiene la calle de la dirección.
     *
     * @return Calle de la dirección.
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Establece la calle de la dirección.
     *
     * @param calle Calle de la dirección.
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Obtiene la colonia de la dirección.
     *
     * @return Colonia de la dirección.
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Establece la colonia de la dirección.
     *
     * @param colonia Colonia de la dirección.
     */
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    /**
     * Obtiene la ciudad de la dirección.
     *
     * @return Ciudad de la dirección.
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad de la dirección.
     *
     * @param ciudad Ciudad de la dirección.
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el estado de la dirección.
     *
     * @return Estado de la dirección.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la dirección.
     *
     * @param estado Estado de la dirección.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}