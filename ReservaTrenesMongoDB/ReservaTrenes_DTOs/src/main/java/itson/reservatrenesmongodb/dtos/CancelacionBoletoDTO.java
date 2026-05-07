/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO utilizado para solicitar la cancelación de un boleto.
 *
 * Transporta el folio visible del boleto que el operador desea cancelar.
 *
 * @author Afgord
 */
public class CancelacionBoletoDTO {

    private String folio;

    public CancelacionBoletoDTO() {
    }

    public CancelacionBoletoDTO(String folio) {
        this.folio = folio;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
}