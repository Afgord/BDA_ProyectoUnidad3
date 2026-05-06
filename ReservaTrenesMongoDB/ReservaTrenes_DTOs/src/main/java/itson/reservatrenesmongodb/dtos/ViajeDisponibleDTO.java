/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para mostrar viajes disponibles en la pantalla de consulta.
 *
 * Contiene únicamente la información necesaria para que el operador seleccione
 * un viaje disponible y continúe con la compra del boleto.
 *
 * @author Afgord
 */
public class ViajeDisponibleDTO {

    private String viajeId;

    private String trenNombre;
    private String origen;
    private String destino;

    private String fechaHoraSalida;
    private String horaLlegada;

    private int disponibilidadGeneral;
    private int disponibilidadPrimeraClase;

    private String estatus;

    public ViajeDisponibleDTO() {
    }

    public ViajeDisponibleDTO(String viajeId, String trenNombre, String origen,
            String destino, String fechaHoraSalida, String horaLlegada,
            int disponibilidadGeneral, int disponibilidadPrimeraClase,
            String estatus) {
        this.viajeId = viajeId;
        this.trenNombre = trenNombre;
        this.origen = origen;
        this.destino = destino;
        this.fechaHoraSalida = fechaHoraSalida;
        this.horaLlegada = horaLlegada;
        this.disponibilidadGeneral = disponibilidadGeneral;
        this.disponibilidadPrimeraClase = disponibilidadPrimeraClase;
        this.estatus = estatus;
    }

    public String getViajeId() {
        return viajeId;
    }

    public void setViajeId(String viajeId) {
        this.viajeId = viajeId;
    }

    public String getTrenNombre() {
        return trenNombre;
    }

    public void setTrenNombre(String trenNombre) {
        this.trenNombre = trenNombre;
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

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(String horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public int getDisponibilidadGeneral() {
        return disponibilidadGeneral;
    }

    public void setDisponibilidadGeneral(int disponibilidadGeneral) {
        this.disponibilidadGeneral = disponibilidadGeneral;
    }

    public int getDisponibilidadPrimeraClase() {
        return disponibilidadPrimeraClase;
    }

    public void setDisponibilidadPrimeraClase(int disponibilidadPrimeraClase) {
        this.disponibilidadPrimeraClase = disponibilidadPrimeraClase;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}