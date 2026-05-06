/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import itson.reservatrenesmongodb.dominio.enums.Sexo;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un pasajero registrado en el sistema.
 *
 * Esta clase representa la colección pasajeros en MongoDB e incluye datos
 * personales, contacto, dirección e historial resumido de viajes.
 *
 * @author Afgord
 */
public class Pasajero {

    /**
     * Identificador único del pasajero.
     */
    private String id;

    /**
     * Nombre completo del pasajero.
     */
    private String nombreCompleto;

    /**
     * Sexo registrado del pasajero.
     */
    private Sexo sexo;

    /**
     * Fecha de nacimiento del pasajero.
     */
    private Instant fechaNacimiento;

    /**
     * Información de contacto del pasajero.
     */
    private Contacto contacto;

    /**
     * Dirección registrada del pasajero.
     */
    private Direccion direccion;

    /**
     * Historial resumido de viajes del pasajero.
     */
    private List<HistorialViaje> historialViajes;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Pasajero() {
        this.historialViajes = new ArrayList<>();
    }

    /**
     * Constructor que inicializa todos los atributos del pasajero.
     *
     * @param id Identificador único.
     * @param nombreCompleto Nombre completo.
     * @param sexo Sexo registrado.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param contacto Información de contacto.
     * @param direccion Dirección registrada.
     * @param historialViajes Historial resumido de viajes.
     */
    public Pasajero(String id, String nombreCompleto, Sexo sexo,
            Instant fechaNacimiento, Contacto contacto, Direccion direccion,
            List<HistorialViaje> historialViajes) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.contacto = contacto;
        this.direccion = direccion;
        this.historialViajes = historialViajes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Instant getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Instant fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Contacto getContacto() {
        return contacto;
    }

    public void setContacto(Contacto contacto) {
        this.contacto = contacto;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<HistorialViaje> getHistorialViajes() {
        return historialViajes;
    }

    public void setHistorialViajes(List<HistorialViaje> historialViajes) {
        this.historialViajes = historialViajes;
    }
}