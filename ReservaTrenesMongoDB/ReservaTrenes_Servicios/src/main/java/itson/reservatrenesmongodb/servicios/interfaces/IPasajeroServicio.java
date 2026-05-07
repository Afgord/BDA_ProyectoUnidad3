/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.dtos.PasajeroDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import java.util.List;

/**
 * Interfaz de servicio para la administración de pasajeros.
 *
 * Define las operaciones disponibles para registrar, consultar, actualizar
 * y eliminar pasajeros desde la capa de presentación.
 *
 * @author Afgord
 */
public interface IPasajeroServicio {

    PasajeroDTO registrar(PasajeroDTO pasajeroDTO) throws ServicioException;

    PasajeroDTO buscarPorId(String id) throws ServicioException;

    PasajeroDTO buscarPorCorreo(String correo) throws ServicioException;

    List<PasajeroDTO> consultarTodos() throws ServicioException;

    boolean actualizar(PasajeroDTO pasajeroDTO) throws ServicioException;

    boolean eliminar(String id) throws ServicioException;
}
