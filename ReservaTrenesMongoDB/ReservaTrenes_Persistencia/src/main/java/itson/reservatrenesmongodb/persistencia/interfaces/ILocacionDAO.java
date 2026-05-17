/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.util.List;

/**
 * Interfaz DAO para la colección de locaciones.
 *
 * Define las operaciones CRUD básicas que debe implementar cualquier clase
 * encargada de administrar locaciones en la base de datos.
 *
 * @author Afgord
 */
public interface ILocacionDAO {

    /**
     * Inserta una nueva locación en la base de datos.
     *
     * @param locacion Locación que se desea insertar.
     * @return Locación insertada con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    Locacion insertar(Locacion locacion) throws PersistenciaException;

    /**
     * Busca una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Locacion buscarPorId(String id) throws PersistenciaException;

    /**
     * Busca una locación por su clave.
     *
     * @param clave Clave de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Locacion buscarPorClave(String clave) throws PersistenciaException;

    /**
     * Consulta todas las locaciones registradas.
     *
     * @return Lista de locaciones.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Locacion> buscarTodos() throws PersistenciaException;

    /**
     * Actualiza una locación existente.
     *
     * @param locacion Locación con los datos actualizados.
     * @return true si la locación fue actualizada, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    boolean actualizar(Locacion locacion) throws PersistenciaException;

    /**
     * Elimina una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return true si la locación fue eliminada, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws PersistenciaException;
}
