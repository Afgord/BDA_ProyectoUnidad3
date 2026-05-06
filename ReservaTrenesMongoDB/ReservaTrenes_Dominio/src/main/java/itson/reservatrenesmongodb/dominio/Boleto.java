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
     * Identificador único interno del boleto.
     */
    private String id;

    /**
     * Folio visible del boleto.
     *
     * Este valor se utiliza para que el operador o pasajero puedan identificar
     * el boleto de forma sencilla, por ejemplo: BOL-12345.
     */
    private String folio;

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
     * @param id Identificador único interno.
     * @param folio Folio visible del boleto.
     * @param pasajeroId Identificador del pasajero.
     * @param viajeId Identificador del viaje.
     * @param pasajeroResumen Resumen del pasajero.
     * @param viajeResumen Resumen del viaje.
     * @param tipoBoleto Tipo de boleto adquirido.
     * @param estatus Estatus del boleto.
     * @param fechaReservacion Fecha y hora de reservación.
     * @param fechaCancelacion Fecha y hora de cancelación.
     */
    public Boleto(String id, String folio, String pasajeroId, String viajeId,
            PasajeroResumen pasajeroResumen, ViajeResumen viajeResumen,
            TipoBoleto tipoBoleto, EstatusBoleto estatus,
            Instant fechaReservacion, Instant fechaCancelacion) {
        this.id = id;
        this.folio = folio;
        this.pasajeroId = pasajeroId;
        this.viajeId = viajeId;
        this.pasajeroResumen = pasajeroResumen;
        this.viajeResumen = viajeResumen;
        this.tipoBoleto = tipoBoleto;
        this.estatus = estatus;
        this.fechaReservacion = fechaReservacion;
        this.fechaCancelacion = fechaCancelacion;
    }

    /**
     * Obtiene el identificador interno del boleto.
     *
     * @return Identificador interno.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador interno del boleto.
     *
     * @param id Identificador interno.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el folio visible del boleto.
     *
     * @return Folio del boleto.
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Establece el folio visible del boleto.
     *
     * @param folio Folio del boleto.
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * Obtiene el identificador del pasajero.
     *
     * @return Identificador del pasajero.
     */
    public String getPasajeroId() {
        return pasajeroId;
    }

    /**
     * Establece el identificador del pasajero.
     *
     * @param pasajeroId Identificador del pasajero.
     */
    public void setPasajeroId(String pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    /**
     * Obtiene el identificador del viaje.
     *
     * @return Identificador del viaje.
     */
    public String getViajeId() {
        return viajeId;
    }

    /**
     * Establece el identificador del viaje.
     *
     * @param viajeId Identificador del viaje.
     */
    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    /**
     * Obtiene el resumen del pasajero.
     *
     * @return Resumen del pasajero.
     */
    public PasajeroResumen getPasajeroResumen() {
        return pasajeroResumen;
    }

    /**
     * Establece el resumen del pasajero.
     *
     * @param pasajeroResumen Resumen del pasajero.
     */
    public void setPasajeroResumen(PasajeroResumen pasajeroResumen) {
        this.pasajeroResumen = pasajeroResumen;
    }

    /**
     * Obtiene el resumen del viaje.
     *
     * @return Resumen del viaje.
     */
    public ViajeResumen getViajeResumen() {
        return viajeResumen;
    }

    /**
     * Establece el resumen del viaje.
     *
     * @param viajeResumen Resumen del viaje.
     */
    public void setViajeResumen(ViajeResumen viajeResumen) {
        this.viajeResumen = viajeResumen;
    }

    /**
     * Obtiene el tipo de boleto.
     *
     * @return Tipo de boleto.
     */
    public TipoBoleto getTipoBoleto() {
        return tipoBoleto;
    }

    /**
     * Establece el tipo de boleto.
     *
     * @param tipoBoleto Tipo de boleto.
     */
    public void setTipoBoleto(TipoBoleto tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    /**
     * Obtiene el estatus del boleto.
     *
     * @return Estatus del boleto.
     */
    public EstatusBoleto getEstatus() {
        return estatus;
    }

    /**
     * Establece el estatus del boleto.
     *
     * @param estatus Estatus del boleto.
     */
    public void setEstatus(EstatusBoleto estatus) {
        this.estatus = estatus;
    }

    /**
     * Obtiene la fecha de reservación.
     *
     * @return Fecha de reservación.
     */
    public Instant getFechaReservacion() {
        return fechaReservacion;
    }

    /**
     * Establece la fecha de reservación.
     *
     * @param fechaReservacion Fecha de reservación.
     */
    public void setFechaReservacion(Instant fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    /**
     * Obtiene la fecha de cancelación.
     *
     * @return Fecha de cancelación.
     */
    public Instant getFechaCancelacion() {
        return fechaCancelacion;
    }

    /**
     * Establece la fecha de cancelación.
     *
     * @param fechaCancelacion Fecha de cancelación.
     */
    public void setFechaCancelacion(Instant fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}