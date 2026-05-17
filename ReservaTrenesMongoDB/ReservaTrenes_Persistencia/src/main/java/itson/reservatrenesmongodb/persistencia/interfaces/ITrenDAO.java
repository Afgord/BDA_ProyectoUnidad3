/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la colección de trenes.
 *
 * Define las operaciones CRUD básicas que debe implementar cualquier clase
 * encargada de administrar trenes en la base de datos.
 *
 * @author Afgord
 */
public interface ITrenDAO {

    /**
     * Inserta un nuevo tren en la base de datos.
     *
     * @param tren Tren que se desea insertar.
     * @return Tren insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    Tren insertar(Tren tren) throws PersistenciaException;

    /**
     * Busca un tren por su identificador.
     *
     * @param id Identificador del tren.
     * @return Tren encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Tren buscarPorId(String id) throws PersistenciaException;

    /**
     * Busca un tren por su código.
     *
     * @param codigo Código del tren.
     * @return Tren encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Tren buscarPorCodigo(String codigo) throws PersistenciaException;

    /**
     * Consulta los servicios distintos registrados en los trenes.
     *
     * @return Lista de servicios disponibles sin duplicados.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<String> consultarServiciosDisponibles()
            throws PersistenciaException;

    /**
     * Consulta todos los trenes registrados.
     *
     * @return Lista de trenes.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Tren> buscarTodos() throws PersistenciaException;

    /**
     * Actualiza un tren existente.
     *
     * @param tren Tren con los datos actualizados.
     * @return true si el tren fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    boolean actualizar(Tren tren) throws PersistenciaException;

    /**
     * Elimina un tren por su identificador.
     *
     * @param id Identificador del tren.
     * @return true si el tren fue eliminado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws PersistenciaException;
}
