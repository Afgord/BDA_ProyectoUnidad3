/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO utilizado para transportar información de trenes entre la capa de
 * presentación y la capa de servicios.
 *
 * Se utiliza en operaciones como registrar, consultar, actualizar y listar
 * trenes.
 *
 * @author Afgord
 */
public class TrenDTO {

    private String id;
    private String codigo;
    private String nombre;
    private String modelo;
    private String estatus;
    private int capacidadGeneral;
    private int capacidadPrimeraClase;
    private List<String> servicios;

    public TrenDTO() {
        this.servicios = new ArrayList<>();
    }

    public TrenDTO(String id, String codigo, String nombre, String modelo,
            String estatus, int capacidadGeneral, int capacidadPrimeraClase,
            List<String> servicios) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.modelo = modelo;
        this.estatus = estatus;
        this.capacidadGeneral = capacidadGeneral;
        this.capacidadPrimeraClase = capacidadPrimeraClase;
        this.servicios = servicios;
    }

    public TrenDTO(String codigo, String nombre, String modelo, String estatus,
            int capacidadGeneral, int capacidadPrimeraClase,
            List<String> servicios) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.modelo = modelo;
        this.estatus = estatus;
        this.capacidadGeneral = capacidadGeneral;
        this.capacidadPrimeraClase = capacidadPrimeraClase;
        this.servicios = servicios;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getCapacidadGeneral() {
        return capacidadGeneral;
    }

    public void setCapacidadGeneral(int capacidadGeneral) {
        this.capacidadGeneral = capacidadGeneral;
    }

    public int getCapacidadPrimeraClase() {
        return capacidadPrimeraClase;
    }

    public void setCapacidadPrimeraClase(int capacidadPrimeraClase) {
        this.capacidadPrimeraClase = capacidadPrimeraClase;
    }

    public List<String> getServicios() {
        return servicios;
    }

    public void setServicios(List<String> servicios) {
        this.servicios = servicios;
    }
}