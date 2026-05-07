/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import java.util.List;

/**
 * Interfaz de servicio para la administración de trenes.
 *
 * Define las operaciones disponibles para registrar, consultar, actualizar
 * y eliminar trenes desde la capa de presentación.
 *
 * @author Afgord
 */
public interface ITrenServicio {

    TrenDTO registrar(TrenDTO trenDTO) throws ServicioException;

    TrenDTO buscarPorId(String id) throws ServicioException;

    TrenDTO buscarPorCodigo(String codigo) throws ServicioException;

    List<TrenDTO> consultarTodos() throws ServicioException;

    boolean actualizar(TrenDTO trenDTO) throws ServicioException;

    boolean eliminar(String id) throws ServicioException;
}