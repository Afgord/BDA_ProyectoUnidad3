/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

/**
 * Clase que representa un tren registrado en el sistema.
 *
 * Esta clase representa la colección trenes en MongoDB e incluye información
 * como código, nombre, modelo, estatus operativo, capacidad y servicios.
 *
 * @author Afgord
 */
public class Tren {

    /**
     * Identificador único del tren.
     */
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    /**
     * Código identificador del tren.
     */
    @BsonProperty("codigo")
    private String codigo;

    /**
     * Nombre del tren.
     */
    private String nombre;

    /**
     * Modelo del tren.
     */
    private String modelo;

    /**
     * Estatus operativo del tren.
     */
    private EstatusTren estatus;

    /**
     * Capacidad del tren por tipo de lugar.
     */
    private Capacidad capacidad;

    /**
     * Lista de servicios disponibles en el tren.
     */
    private List<String> servicios;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Tren() {
        this.servicios = new ArrayList<>();
    }

    /**
     * Constructor que inicializa todos los atributos del tren.
     *
     * @param id Identificador único.
     * @param codigo Código del tren.
     * @param nombre Nombre del tren.
     * @param modelo Modelo del tren.
     * @param estatus Estatus operativo.
     * @param capacidad Capacidad del tren.
     * @param servicios Lista de servicios.
     */
    public Tren(String id, String codigo, String nombre, String modelo,
            EstatusTren estatus, Capacidad capacidad, List<String> servicios) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.modelo = modelo;
        this.estatus = estatus;
        this.capacidad = capacidad;
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

    public EstatusTren getEstatus() {
        return estatus;
    }

    public void setEstatus(EstatusTren estatus) {
        this.estatus = estatus;
    }

    public Capacidad getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Capacidad capacidad) {
        this.capacidad = capacidad;
    }

    public List<String> getServicios() {
        return servicios;
    }

    public void setServicios(List<String> servicios) {
        this.servicios = servicios;
    }
}
