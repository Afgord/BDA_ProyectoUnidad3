/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import java.time.Instant;

/**
 * Clase que representa un viaje programado dentro del sistema.
 *
 * Esta clase representa la colección viajes en MongoDB. Relaciona un tren con
 * una ruta, una fecha de salida, una fecha estimada de llegada, capacidad
 * máxima y disponibilidad.
 *
 * @author Afgord
 */
public class Viaje {

    /**
     * Identificador único del viaje.
     */
    private String id;

    /**
     * Resumen del tren asignado al viaje.
     */
    private TrenResumen tren;

    /**
     * Ruta del viaje, compuesta por origen y destino.
     */
    private RutaViaje ruta;

    /**
     * Fecha y hora de salida del viaje.
     */
    private Instant fechaHoraSalida;

    /**
     * Fecha y hora estimada de llegada.
     */
    private Instant fechaHoraLlegadaEstimada;

    /**
     * Estatus actual del viaje.
     */
    private EstatusViaje estatus;

    /**
     * Capacidad máxima del viaje.
     */
    private Capacidad capacidadMaxima;

    /**
     * Disponibilidad actual del viaje.
     */
    private Disponibilidad disponibilidad;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Viaje() {
    }

    /**
     * Constructor que inicializa todos los atributos del viaje.
     *
     * @param id Identificador único.
     * @param tren Resumen del tren asignado.
     * @param ruta Ruta del viaje.
     * @param fechaHoraSalida Fecha y hora de salida.
     * @param fechaHoraLlegadaEstimada Fecha y hora estimada de llegada.
     * @param estatus Estatus del viaje.
     * @param capacidadMaxima Capacidad máxima.
     * @param disponibilidad Disponibilidad actual.
     */
    public Viaje(String id, TrenResumen tren, RutaViaje ruta,
            Instant fechaHoraSalida, Instant fechaHoraLlegadaEstimada,
            EstatusViaje estatus, Capacidad capacidadMaxima,
            Disponibilidad disponibilidad) {
        this.id = id;
        this.tren = tren;
        this.ruta = ruta;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
        this.estatus = estatus;
        this.capacidadMaxima = capacidadMaxima;
        this.disponibilidad = disponibilidad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrenResumen getTren() {
        return tren;
    }

    public void setTren(TrenResumen tren) {
        this.tren = tren;
    }

    public RutaViaje getRuta() {
        return ruta;
    }

    public void setRuta(RutaViaje ruta) {
        this.ruta = ruta;
    }

    public Instant getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(Instant fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Instant getFechaHoraLlegadaEstimada() {
        return fechaHoraLlegadaEstimada;
    }

    public void setFechaHoraLlegadaEstimada(Instant fechaHoraLlegadaEstimada) {
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public EstatusViaje getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusViaje estatus) {
        this.estatus = estatus;
    }

    public Capacidad getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Capacidad capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}