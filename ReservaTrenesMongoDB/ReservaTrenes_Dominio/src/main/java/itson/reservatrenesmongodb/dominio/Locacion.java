/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

/**
 * Clase que representa una locación válida dentro del sistema.
 *
 * Una locación puede funcionar como origen o destino de un viaje programado.
 * Esta clase representa la colección locaciones en MongoDB.
 *
 * @author Afgord
 */
public class Locacion {

    /**
     * Identificador único de la locación.
     */
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    private String id;

    /**
     * Clave corta de la locación.
     */
    private String clave;

    /**
     * Nombre de la ciudad o terminal.
     */
    private String nombre;

    /**
     * Estado donde se encuentra la locación.
     */
    private String estado;

    /**
     * País donde se encuentra la locación.
     */
    private String pais;

    /**
     * Indica si la locación se encuentra activa.
     */
    private boolean activa;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public Locacion() {
    }

    /**
     * Constructor que inicializa todos los atributos de la locación.
     *
     * @param id Identificador único.
     * @param clave Clave corta de la locación.
     * @param nombre Nombre de la locación.
     * @param estado Estado de la locación.
     * @param pais País de la locación.
     * @param activa Indica si la locación está activa.
     */
    public Locacion(String id, String clave, String nombre, String estado,
            String pais, boolean activa) {
        this.id = id;
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
