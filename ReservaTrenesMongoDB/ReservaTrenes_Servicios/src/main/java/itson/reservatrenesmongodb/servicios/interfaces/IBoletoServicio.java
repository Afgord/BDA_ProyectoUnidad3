/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.dtos.BoletoDTO;
import itson.reservatrenesmongodb.dtos.CancelacionBoletoDTO;
import itson.reservatrenesmongodb.dtos.CompraBoletoDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import java.util.List;

/**
 * Interfaz de servicio para la gestión de boletos.
 *
 * Define las operaciones para generar, cancelar y consultar boletos desde la
 * capa de presentación.
 *
 * @author Afgord
 */
public interface IBoletoServicio {

    /**
     * Genera un boleto a partir de los datos de compra.
     *
     * @param compraDTO Datos de compra capturados en ventanilla.
     * @return Boleto generado.
     * @throws ServicioException Si ocurre un error de validación, negocio o
     * persistencia.
     */
    BoletoDTO generarBoleto(CompraBoletoDTO compraDTO) throws ServicioException;

    /**
     * Cancela un boleto a partir de su folio.
     *
     * @param cancelacionDTO Datos de cancelación.
     * @return Boleto cancelado.
     * @throws ServicioException Si ocurre un error de validación, negocio o
     * persistencia.
     */
    BoletoDTO cancelarBoleto(CancelacionBoletoDTO cancelacionDTO)
            throws ServicioException;

    /**
     * Busca un boleto por folio.
     *
     * @param folio Folio visible del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    BoletoDTO buscarPorFolio(String folio) throws ServicioException;

    /**
     * Consulta boletos por pasajero.
     *
     * @param pasajeroId Identificador del pasajero.
     * @return Lista de boletos.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    List<BoletoDTO> consultarPorPasajero(String pasajeroId)
            throws ServicioException;

    /**
     * Consulta boletos por viaje.
     *
     * @param viajeId Identificador del viaje.
     * @return Lista de boletos.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    List<BoletoDTO> consultarPorViaje(String viajeId)
            throws ServicioException;
}
