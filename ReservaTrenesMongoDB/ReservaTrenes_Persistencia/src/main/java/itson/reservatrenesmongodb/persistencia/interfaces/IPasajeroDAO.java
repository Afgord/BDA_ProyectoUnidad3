/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la colección de pasajeros.
 *
 * Define las operaciones CRUD básicas que debe implementar cualquier clase
 * encargada de administrar pasajeros en la base de datos.
 *
 * @author Afgord
 */
public interface IPasajeroDAO {

    /**
     * Inserta un nuevo pasajero en la base de datos.
     *
     * @param pasajero Pasajero que se desea insertar.
     * @return Pasajero insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    Pasajero insertar(Pasajero pasajero) throws PersistenciaException;

    /**
     * Busca un pasajero por su identificador.
     *
     * @param id Identificador del pasajero.
     * @return Pasajero encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Pasajero buscarPorId(String id) throws PersistenciaException;

    /**
     * Busca un pasajero por su correo electrónico.
     *
     * @param correo Correo electrónico del pasajero. * @return Pasajero
     * encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Pasajero buscarPorCorreo(String correo) throws PersistenciaException;

    /**
     * Consulta todos los pasajeros registrados.
     *
     * @return Lista de pasajeros.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Pasajero> buscarTodos() throws PersistenciaException;

    /**
     * Actualiza un pasajero existente.
     *
     * @param pasajero Pasajero con los datos actualizados.
     * @return true si el pasajero fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    boolean actualizar(Pasajero pasajero) throws PersistenciaException;

    /**
     * Elimina un pasajero por su identificador.
     *
     * @param id Identificador del pasajero.
     * @return true si el pasajero fue eliminado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws PersistenciaException;

    /**
     * Consulta las ciudades registradas en las direcciones de los pasajeros.
     *
     * @return Lista de ciudades sin duplicados.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<String> buscarCiudadesRegistradas() throws PersistenciaException;

    /**
     * Consulta los estados registrados en las direcciones de los pasajeros.
     *
     * @return Lista de estados sin duplicados.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<String> buscarEstadosRegistrados() throws PersistenciaException;

}
