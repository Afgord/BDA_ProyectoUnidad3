/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import java.util.List;

/**
 * Interfaz de servicio para la administración de viajes.
 *
 * Define las operaciones disponibles para programar, consultar, actualizar y
 * eliminar viajes desde la capa de presentación.
 *
 * @author Afgord
 */
public interface IViajeServicio {

    ViajeDTO registrar(ViajeDTO viajeDTO) throws ServicioException;

    ViajeDTO buscarPorId(String id) throws ServicioException;

    /**
     * Consulta los próximos viajes programados.
     *
     * @param limite Cantidad máxima de viajes a consultar.
     * @return Lista de próximos viajes programados.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    List<ViajeDTO> consultarProximosViajesProgramados(int limite)
            throws ServicioException;

    List<ViajeDTO> consultarTodos() throws ServicioException;

    boolean actualizar(ViajeDTO viajeDTO) throws ServicioException;

    boolean eliminar(String id) throws ServicioException;
}
