/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.time.Instant;
import java.util.List;

/**
 * Interfaz DAO para la colección de viajes.
 *
 * Define las operaciones CRUD básicas que debe implementar cualquier clase
 * encargada de administrar viajes programados en la base de datos.
 *
 * @author Afgord
 */
public interface IViajeDAO {

    /**
     * Inserta un nuevo viaje en la base de datos.
     *
     * @param viaje Viaje que se desea insertar.
     * @return Viaje insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    Viaje insertar(Viaje viaje) throws PersistenciaException;

    /**
     * Busca un viaje por su identificador.
     *
     * @param id Identificador del viaje.
     * @return Viaje encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Viaje buscarPorId(String id) throws PersistenciaException;

    /**
     * Consulta todos los viajes registrados.
     *
     * @return Lista de viajes.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    List<Viaje> buscarTodos() throws PersistenciaException;

    /**
     * Actualiza un viaje existente.
     *
     * @param viaje Viaje con los datos actualizados.
     * @return true si el viaje fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    boolean actualizar(Viaje viaje) throws PersistenciaException;

    /**
     * Elimina un viaje por su identificador.
     *
     * @param id Identificador del viaje.
     * @return true si el viaje fue eliminado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    boolean eliminar(String id) throws PersistenciaException;

    /**
     * Busca un viaje por tren y fecha/hora de salida.
     *
     * @param trenId Identificador del tren.
     * @param fechaHoraSalida Fecha y hora de salida.
     * @return Viaje encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    Viaje buscarPorTrenYFechaSalida(String trenId, Instant fechaHoraSalida)
            throws PersistenciaException;
}
