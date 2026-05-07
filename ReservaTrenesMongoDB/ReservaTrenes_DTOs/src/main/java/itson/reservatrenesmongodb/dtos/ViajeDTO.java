/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para transportar información de viajes entre la capa de
 * presentación y la capa de servicios.
 *
 * Se utiliza principalmente en la administración y programación de viajes.
 *
 * @author Afgord
 */
public class ViajeDTO {

    private String id;

    private String trenId;
    private String trenCodigo;
    private String trenNombre;

    private String origenId;
    private String origenNombre;

    private String destinoId;
    private String destinoNombre;

    private String fechaHoraSalida;
    private String fechaHoraLlegadaEstimada;

    private String estatus;

    private int capacidadMaximaGeneral;
    private int capacidadMaximaPrimeraClase;

    private int disponibilidadGeneral;
    private int disponibilidadPrimeraClase;

    public ViajeDTO() {
    }

    public ViajeDTO(String id, String trenId, String trenCodigo,
            String trenNombre, String origenId, String origenNombre,
            String destinoId, String destinoNombre, String fechaHoraSalida,
            String fechaHoraLlegadaEstimada, String estatus,
            int capacidadMaximaGeneral, int capacidadMaximaPrimeraClase,
            int disponibilidadGeneral, int disponibilidadPrimeraClase) {
        this.id = id;
        this.trenId = trenId;
        this.trenCodigo = trenCodigo;
        this.trenNombre = trenNombre;
        this.origenId = origenId;
        this.origenNombre = origenNombre;
        this.destinoId = destinoId;
        this.destinoNombre = destinoNombre;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
        this.estatus = estatus;
        this.capacidadMaximaGeneral = capacidadMaximaGeneral;
        this.capacidadMaximaPrimeraClase = capacidadMaximaPrimeraClase;
        this.disponibilidadGeneral = disponibilidadGeneral;
        this.disponibilidadPrimeraClase = disponibilidadPrimeraClase;
    }

    public ViajeDTO(String trenId, String trenCodigo, String trenNombre,
            String origenId, String origenNombre, String destinoId,
            String destinoNombre, String fechaHoraSalida,
            String fechaHoraLlegadaEstimada, String estatus,
            int capacidadMaximaGeneral, int capacidadMaximaPrimeraClase,
            int disponibilidadGeneral, int disponibilidadPrimeraClase) {
        this.trenId = trenId;
        this.trenCodigo = trenCodigo;
        this.trenNombre = trenNombre;
        this.origenId = origenId;
        this.origenNombre = origenNombre;
        this.destinoId = destinoId;
        this.destinoNombre = destinoNombre;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
        this.estatus = estatus;
        this.capacidadMaximaGeneral = capacidadMaximaGeneral;
        this.capacidadMaximaPrimeraClase = capacidadMaximaPrimeraClase;
        this.disponibilidadGeneral = disponibilidadGeneral;
        this.disponibilidadPrimeraClase = disponibilidadPrimeraClase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrenId() {
        return trenId;
    }

    public void setTrenId(String trenId) {
        this.trenId = trenId;
    }

    public String getTrenCodigo() {
        return trenCodigo;
    }

    public void setTrenCodigo(String trenCodigo) {
        this.trenCodigo = trenCodigo;
    }

    public String getTrenNombre() {
        return trenNombre;
    }

    public void setTrenNombre(String trenNombre) {
        this.trenNombre = trenNombre;
    }

    public String getOrigenId() {
        return origenId;
    }

    public void setOrigenId(String origenId) {
        this.origenId = origenId;
    }

    public String getOrigenNombre() {
        return origenNombre;
    }

    public void setOrigenNombre(String origenNombre) {
        this.origenNombre = origenNombre;
    }

    public String getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(String destinoId) {
        this.destinoId = destinoId;
    }

    public String getDestinoNombre() {
        return destinoNombre;
    }

    public void setDestinoNombre(String destinoNombre) {
        this.destinoNombre = destinoNombre;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public String getFechaHoraLlegadaEstimada() {
        return fechaHoraLlegadaEstimada;
    }

    public void setFechaHoraLlegadaEstimada(String fechaHoraLlegadaEstimada) {
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getCapacidadMaximaGeneral() {
        return capacidadMaximaGeneral;
    }

    public void setCapacidadMaximaGeneral(int capacidadMaximaGeneral) {
        this.capacidadMaximaGeneral = capacidadMaximaGeneral;
    }

    public int getCapacidadMaximaPrimeraClase() {
        return capacidadMaximaPrimeraClase;
    }

    public void setCapacidadMaximaPrimeraClase(int capacidadMaximaPrimeraClase) {
        this.capacidadMaximaPrimeraClase = capacidadMaximaPrimeraClase;
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
}