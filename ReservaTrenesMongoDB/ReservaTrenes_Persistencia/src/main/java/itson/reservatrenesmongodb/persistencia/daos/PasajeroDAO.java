/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.daos;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección pasajeros en MongoDB.
 *
 * Esta clase utiliza colecciones tipadas con PojoCodecProvider para mapear
 * automáticamente objetos Pasajero hacia documentos BSON.
 *
 * @author Afgord
 */
public class PasajeroDAO implements IPasajeroDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "pasajeros";

    /**
     * Colección tipada de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Pasajero> collection;

    /**
     * Constructor que obtiene la colección pasajeros desde la conexión
     * Singleton.
     */
    public PasajeroDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION, Pasajero.class);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de pasajeros.
     *
     * El índice único sobre contacto.correo evita registrar dos pasajeros con
     * el mismo correo electrónico.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.ascending("contacto.correo"),
                new IndexOptions().unique(true)
        );
    }

    /**
     * Inserta un nuevo pasajero en MongoDB.
     *
     * @param pasajero Pasajero a insertar.
     * @return Pasajero insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Pasajero insertar(Pasajero pasajero) throws PersistenciaException {
        try {
            collection.insertOne(pasajero);
            return pasajero;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un pasajero registrado con el mismo correo.", e);
            }

            throw new PersistenciaException("Error al insertar el pasajero.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al insertar el pasajero.", e);
        }
    }

    /**
     * Busca un pasajero por su identificador.
     *
     * @param id Identificador del pasajero.
     * @return Pasajero encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Pasajero buscarPorId(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return null;
            }

            return collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el pasajero por id.", e);
        }
    }

    /**
     * Busca un pasajero por su correo electrónico.
     *
     * @param correo Correo electrónico del pasajero.
     * @return Pasajero encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Pasajero buscarPorCorreo(String correo) throws PersistenciaException {
        try {
            return collection.find(
                    Filters.eq("contacto.correo", correo)
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el pasajero por correo.", e);
        }
    }

    /**
     * Consulta todos los pasajeros registrados.
     *
     * @return Lista de pasajeros.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Pasajero> buscarTodos() throws PersistenciaException {
        try {
            List<Pasajero> pasajeros = new ArrayList<>();

            for (Pasajero pasajero : collection.find()) {
                pasajeros.add(pasajero);
            }

            return pasajeros;

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar los pasajeros.", e);
        }
    }

    /**
     * Actualiza un pasajero existente.
     *
     * @param pasajero Pasajero con datos actualizados.
     * @return true si el pasajero fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(Pasajero pasajero) throws PersistenciaException {
        try {
            if (pasajero.getId() == null || !ObjectId.isValid(pasajero.getId())) {
                return false;
            }

            var resultado = collection.replaceOne(
                    Filters.eq("_id", new ObjectId(pasajero.getId())),
                    pasajero
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro pasajero registrado con el mismo correo.", e);
            }

            throw new PersistenciaException("Error al actualizar el pasajero.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar el pasajero.", e);
        }
    }

    /**
     * Elimina un pasajero por su identificador.
     *
     * @param id Identificador del pasajero.
     * @return true si el pasajero fue eliminado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la eliminación.
     */
    @Override
    public boolean eliminar(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return false;
            }

            var resultado = collection.deleteOne(
                    Filters.eq("_id", new ObjectId(id))
            );

            return resultado.getDeletedCount() > 0;

        } catch (Exception e) {
            throw new PersistenciaException("Error al eliminar el pasajero.", e);
        }
    }
}