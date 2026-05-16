/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para transportar información de pasajeros entre la capa de
 * presentación y la capa de servicios.
 *
 * Se utiliza en operaciones como registrar pasajero, consultar pasajero,
 * actualizar datos y mostrar información básica en tablas.
 *
 * @author Afgord
 */
public class PasajeroDTO {

    private String id;
    private String nombreCompleto;
    private String sexo;
    private String fechaNacimiento;
    private String telefono;
    private String correo;
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private int viajesRegistrados;

    public PasajeroDTO() {
    }

    public PasajeroDTO(String id, String nombreCompleto, String sexo,
            String fechaNacimiento, String telefono, String correo,
            String calle, String colonia, String ciudad, String estado,
            int viajesRegistrados) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.viajesRegistrados = viajesRegistrados;
    }

    public PasajeroDTO(String nombreCompleto, String sexo,
            String fechaNacimiento, String telefono, String correo,
            String calle, String colonia, String ciudad, String estado) {
        this.nombreCompleto = nombreCompleto;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.calle = calle;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getViajesRegistrados() {
        return viajesRegistrados;
    }

    public void setViajesRegistrados(int viajesRegistrados) {
        this.viajesRegistrados = viajesRegistrados;
    }
}