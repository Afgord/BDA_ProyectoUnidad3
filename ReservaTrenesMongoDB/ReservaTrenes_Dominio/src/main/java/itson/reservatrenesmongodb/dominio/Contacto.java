/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

/**
 * Clase que representa la información de contacto de un pasajero.
 *
 * Esta clase se utiliza como documento embebido dentro de Pasajero.
 *
 * @author Afgord
 */
public class Contacto {

    /**
     * Teléfono de contacto del pasajero.
     */
    private String telefono;

    /**
     * Correo electrónico del pasajero.
     */
    private String correo;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Contacto() {
    }

    /**
     * Constructor que inicializa la información de contacto.
     *
     * @param telefono Teléfono de contacto.
     * @param correo Correo electrónico.
     */
    public Contacto(String telefono, String correo) {
        this.telefono = telefono;
        this.correo = correo;
    }

    /**
     * Obtiene el teléfono de contacto.
     *
     * @return Teléfono de contacto.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto.
     *
     * @param telefono Teléfono de contacto.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo electrónico.
     *
     * @return Correo electrónico.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico.
     *
     * @param correo Correo electrónico.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}