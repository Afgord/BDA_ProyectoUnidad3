/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para transportar información de locaciones entre la capa de
 * presentación y la capa de servicios.
 *
 * Se utiliza en operaciones como registrar, consultar, actualizar y listar
 * locaciones.
 *
 * @author Afgord
 */
public class LocacionDTO {

    private String id;
    private String clave;
    private String nombre;
    private String estado;
    private String pais;
    private boolean activa;

    public LocacionDTO() {
    }

    public LocacionDTO(String id, String clave, String nombre, String estado,
            String pais, boolean activa) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.estado = estado;
        this.pais = pais;
        this.activa = activa;
    }

    public LocacionDTO(String clave, String nombre, String estado,
            String pais, boolean activa) {
        this.clave = clave;
        this.nombre = nombre;
        this.estado = estado;
        this.pais = pais;
        this.activa = activa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}