/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para transportar información resumida de boletos entre la capa
 * de servicios y la capa de presentación.
 *
 * Se utiliza para mostrar boletos generados, consultados o cancelados.
 *
 * @author Afgord
 */
public class BoletoDTO {

    private String id;
    private String folio;

    private String pasajeroId;
    private String pasajeroNombre;
    private String pasajeroTelefono;

    private String viajeId;
    private String origen;
    private String destino;
    private String tren;
    private String fechaHoraSalida;

    private String tipoBoleto;
    private String precio;

    private String estatus;
    private String fechaReservacion;
    private String fechaCancelacion;

    public BoletoDTO() {
    }

    public BoletoDTO(String id, String folio, String pasajeroId,
            String pasajeroNombre, String pasajeroTelefono, String viajeId,
            String origen, String destino, String tren, String fechaHoraSalida,
            String tipoBoleto, String precio, String estatus,
            String fechaReservacion, String fechaCancelacion) {
        this.id = id;
        this.folio = folio;
        this.pasajeroId = pasajeroId;
        this.pasajeroNombre = pasajeroNombre;
        this.pasajeroTelefono = pasajeroTelefono;
        this.viajeId = viajeId;
        this.origen = origen;
        this.destino = destino;
        this.tren = tren;
        this.fechaHoraSalida = fechaHoraSalida;
        this.tipoBoleto = tipoBoleto;
        this.precio = precio;
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

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getPasajeroId() {
        return pasajeroId;
    }

    public void setPasajeroId(String pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    public String getPasajeroNombre() {
        return pasajeroNombre;
    }

    public void setPasajeroNombre(String pasajeroNombre) {
        this.pasajeroNombre = pasajeroNombre;
    }

    public String getPasajeroTelefono() {
        return pasajeroTelefono;
    }

    public void setPasajeroTelefono(String pasajeroTelefono) {
        this.pasajeroTelefono = pasajeroTelefono;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getTren() {
        return tren;
    }

    public void setTren(String tren) {
        this.tren = tren;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getTipoBoleto() {
        return tipoBoleto;
    }

    public void setTipoBoleto(String tipoBoleto) {
        this.tipoBoleto = tipoBoleto;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(String fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public String getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(String fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }
}