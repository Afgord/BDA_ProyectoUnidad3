/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la colección de boletos.
 *
 * Define las operaciones CRUD básicas que debe implementar cualquier clase
 * encargada de administrar boletos en la base de datos.
 *
 * @author Afgord
 */
public interface IBoletoDAO {

    /**
     * Inserta un nuevo boleto en la base de datos.
     *
     * @param boleto Boleto que se desea insertar.
     * @return Boleto insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    Boleto insertar(Boleto boleto) throws PersistenciaException;

    /**
     * Busca un boleto por su identificador.
     *
     * @param id Identificador del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Boleto buscarPorId(String id) throws PersistenciaException;

    /**
     * Busca un boleto por su folio.
     *
     * @param folio Folio visible del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Boleto buscarPorFolio(String folio) throws PersistenciaException;

    /**
     * Consulta todos los boletos registrados.
     *
     * @return Lista de boletos.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Boleto> buscarTodos() throws PersistenciaException;

    /**
     * Consulta los boletos asociados a un pasajero.
     *
     * @param pasajeroId Identificador del pasajero.
     * @return Lista de boletos del pasajero.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Boleto> buscarPorPasajeroId(String pasajeroId) throws PersistenciaException;

    /**
     * Consulta los boletos asociados a un viaje.
     *
     * @param viajeId Identificador del viaje.
     * @return Lista de boletos del viaje.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Boleto> buscarPorViajeId(String viajeId) throws PersistenciaException;

    /**
     * Actualiza un boleto existente.
     *
     * @param boleto Boleto con los datos actualizados.
     * @return true si el boleto fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    boolean actualizar(Boleto boleto) throws PersistenciaException;

    /**
     * Elimina un boleto por su identificador.
     *
     * @param id Identificador del boleto.
     * @return true si el boleto fue eliminado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws PersistenciaException;
}
