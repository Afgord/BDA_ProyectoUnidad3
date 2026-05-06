/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el tipo de boleto adquirido por un pasajero.
 *
 * Esta clase se utiliza como documento embebido dentro de Boleto e incluye
 * información como clave, nombre, precio, beneficios y si el boleto es
 * reembolsable.
 *
 * @author Afgord
 */
public class TipoBoleto {

    /**
     * Clave identificadora del tipo de boleto.
     */
    private String clave;

    /**
     * Nombre descriptivo del tipo de boleto.
     */
    private String nombre;

    /**
     * Precio del boleto.
     */
    private BigDecimal precio;

    /**
     * Lista de beneficios incluidos en el boleto.
     */
    private List<String> beneficios;

    /**
     * Indica si el boleto puede ser reembolsado.
     */
    private boolean reembolsable;

    /**
     * Constructor vacío requerido para la creación automática de objetos.
     */
    public TipoBoleto() {
        this.beneficios = new ArrayList<>();
    }

    /**
     * Constructor que inicializa todos los atributos del tipo de boleto.
     *
     * @param clave Clave identificadora.
     * @param nombre Nombre del tipo de boleto.
     * @param precio Precio del boleto.
     * @param beneficios Lista de beneficios.
     * @param reembolsable Indica si el boleto es reembolsable.
     */
    public TipoBoleto(String clave, String nombre, BigDecimal precio,
            List<String> beneficios, boolean reembolsable) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.beneficios = beneficios;
        this.reembolsable = reembolsable;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public List<String> getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(List<String> beneficios) {
        this.beneficios = beneficios;
    }

    public boolean isReembolsable() {
        return reembolsable;
    }

    public void setReembolsable(boolean reembolsable) {
        this.reembolsable = reembolsable;
    }
}