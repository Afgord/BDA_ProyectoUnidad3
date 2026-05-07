/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.exceptions.ServicioException;
import java.util.List;

/**
 * Interfaz de servicio para la administración de locaciones.
 *
 * Define las operaciones disponibles para registrar, consultar, actualizar
 * y eliminar locaciones desde la capa de presentación.
 *
 * @author Afgord
 */
public interface ILocacionServicio {

    /**
     * Registra una nueva locación en el sistema.
     *
     * @param locacionDTO Datos de la locación a registrar.
     * @return Locación registrada.
     * @throws ServicioException Si ocurre un error de validación, negocio o
     * persistencia.
     */
    LocacionDTO registrar(LocacionDTO locacionDTO) throws ServicioException;

    /**
     * Busca una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    LocacionDTO buscarPorId(String id) throws ServicioException;

    /**
     * Busca una locación por su clave.
     *
     * @param clave Clave de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    LocacionDTO buscarPorClave(String clave) throws ServicioException;

    /**
     * Consulta todas las locaciones registradas.
     *
     * @return Lista de locaciones.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    List<LocacionDTO> consultarTodas() throws ServicioException;

    /**
     * Actualiza una locación existente.
     *
     * @param locacionDTO Datos actualizados de la locación.
     * @return true si la locación fue actualizada.
     * @throws ServicioException Si ocurre un error de validación, negocio o
     * persistencia.
     */
    boolean actualizar(LocacionDTO locacionDTO) throws ServicioException;

    /**
     * Elimina una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return true si la locación fue eliminada.
     * @throws ServicioException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws ServicioException;
}