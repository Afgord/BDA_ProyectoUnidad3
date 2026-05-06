/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

/**
 * Clase que representa un resumen del tren asignado a un viaje.
 *
 * Esta clase se utiliza como documento embebido dentro de Viaje, conservando
 * una referencia al tren original junto con datos básicos para consulta rápida.
 *
 * @author Afgord
 */
public class TrenResumen {

    /**
     * Identificador del tren referenciado.
     */
    private String trenId;

    /**
     * Código del tren.
     */
    private String codigo;

    /**
     * Nombre del tren.
     */
    private String nombre;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public TrenResumen() {
    }

    /**
     * Constructor que inicializa el resumen del tren.
     *
     * @param trenId Identificador del tren.
     * @param codigo Código del tren.
     * @param nombre Nombre del tren.
     */
    public TrenResumen(String trenId, String codigo, String nombre) {
        this.trenId = trenId;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getTrenId() {
        return trenId;
    }

    public void setTrenId(String trenId) {
        this.trenId = trenId;
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
}