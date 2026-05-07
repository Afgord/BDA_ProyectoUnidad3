/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Clase que representa un resumen del pasajero asociado a un boleto.
 *
 * Esta clase se utiliza como documento embebido dentro de Boleto para conservar
 * datos básicos del pasajero al momento de generar el boleto.
 *
 * @author Afgord
 */
public class PasajeroResumen {

    /**
     * Nombre completo del pasajero.
     */
    @BsonProperty("nombre_completo")
    private String nombreCompleto;

    /**
     * Teléfono del pasajero.
     */
    private String telefono;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public PasajeroResumen() {
    }

    /**
     * Constructor que inicializa el resumen del pasajero.
     *
     * @param nombreCompleto Nombre completo del pasajero.
     * @param telefono Teléfono del pasajero.
     */
    public PasajeroResumen(String nombreCompleto, String telefono) {
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}