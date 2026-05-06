/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import itson.reservatrenesmongodb.dominio.enums.EstatusBoleto;
import java.time.Instant;

/**
 * Clase que representa un boleto generado para un pasajero en un viaje
 * específico.
 *
 * Esta clase representa la colección boletos en MongoDB. Relaciona al pasajero
 * con el viaje reservado e incluye información resumida del pasajero, resumen
 * del viaje, tipo de boleto, estatus y fechas de reservación o cancelación.
 *
 * @author Afgord
 */
public class Boleto {

    /**
     * Identificador único del boleto.
     */
    private String id;

    /**
     * Identificador del pasajero asociado.
     */
    private String pasajeroId;

    /**
     * Identificador del viaje asociado.
     */
    private String viajeId;

    /**
     * Resumen del pasajero al momento de generar el boleto.
     */
    private PasajeroResumen pasajeroResumen;

    /**
     * Resumen del viaje al momento de generar el boleto.
     */
    private ViajeResumen viajeResumen;

    /**
     * Tipo de boleto adquirido.
     */
    private TipoBoleto tipoBoleto;

    /**
     * Estatus actual del boleto.
     */
    private EstatusBoleto estatus;

    /**
     * Fecha y hora de reservación del boleto.
     */
    private Instant fechaReservacion;

    /**
     * Fecha y hora de cancelación del boleto. Puede ser null si el boleto no ha
     * sido cancelado.
     */
    private Instant fechaCancelacion;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Boleto() {
    }

    /**
     * Constructor que inicializa todos los atributos del boleto.
     *
     * @param id Identificador único.
     * @param pasajeroId Identificador del pasajero.
     * @param viajeId Identificador del viaje.
     * @param pasajeroResumen Resumen del pasajero.
     * @param viajeResumen Resumen del viaje.
     * @param tipoBoleto Tipo de boleto adquirido.
     * @param estatus Estatus del boleto.
     * @param fechaReservacion Fecha y hora de reservación.
     * @param fechaCancelacion Fecha y hora de cancelación.
     */
    public Boleto(String id, String pasajeroId, String viajeId,
            PasajeroResumen pasajeroResumen, ViajeResumen viajeResumen,
            TipoBoleto tipoBoleto, EstatusBoleto estatus,
            Instant fechaReservacion, Instant fechaCancelacion) {
        this.id = id;
        this.pasajeroId = pasajeroId;
        this.viajeId = viajeId;
        this.pasajeroResumen = pasajeroResumen;
        this.viajeResumen = viajeResumen;
        this.tipoBoleto = tipoBoleto;
        this.estatus = estatus;
        this.fechaReservacion = fechaReservacion;
        this.fechaCancelacion = fechaCancelacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasajeroId() {
        return pasajeroId;
    }

    public void setPasajeroId(String pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public PasajeroResumen getPasajeroResumen() {
        return pasajeroResumen;
    }

    public void setPasajeroResumen(PasajeroResumen pasajeroResumen) {
        this.pasajeroResumen = pasajeroResumen;
    }

    public ViajeResumen getViajeResumen() {
        return viajeResumen;
    }

    public void setViajeResumen(ViajeResumen viajeResumen) {
        this.viajeResumen = viajeResumen;
    }

    public TipoBoleto getTipoBoleto() {
        return tipoBoleto;
    }

    public void setTipoBoleto(TipoBoleto tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public EstatusBoleto getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusBoleto estatus) {
        this.estatus = estatus;
    }

    public Instant getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(Instant fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public Instant getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Instant fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}
