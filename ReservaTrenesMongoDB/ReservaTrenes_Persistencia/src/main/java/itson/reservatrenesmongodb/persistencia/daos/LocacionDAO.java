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
import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.ILocacionDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección locaciones en MongoDB.
 *
 * Esta clase utiliza colecciones tipadas con PojoCodecProvider para mapear
 * automáticamente objetos Locacion hacia documentos BSON.
 *
 * @author Afgord
 */
public class LocacionDAO implements ILocacionDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "locaciones";

    /**
     * Colección tipada de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Locacion> collection;

    /**
     * Constructor que obtiene la colección locaciones desde la conexión
     * Singleton.
     */
    public LocacionDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION, Locacion.class);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de locaciones.
     *
     * El índice único sobre clave evita registrar dos locaciones con la misma
     * clave.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.ascending("clave"),
                new IndexOptions().unique(true)
        );
    }

    /**
     * Inserta una nueva locación en MongoDB.
     *
     * @param locacion Locación a insertar.
     * @return Locación insertada con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Locacion insertar(Locacion locacion) throws PersistenciaException {
        try {
            collection.insertOne(locacion);
            return locacion;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe una locación registrada con la misma clave.", e);
            }

            throw new PersistenciaException("Error al insertar la locación.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al insertar la locación.", e);
        }
    }

    /**
     * Busca una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Locacion buscarPorId(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return null;
            }

            return collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar la locación por id.", e);
        }
    }

    /**
     * Busca una locación por su clave.
     *
     * @param clave Clave de la locación.
     * @return Locación encontrada o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Locacion buscarPorClave(String clave) throws PersistenciaException {
        try {
            return collection.find(
                    Filters.eq("clave", clave)
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar la locación por clave.", e);
        }
    }

    /**
     * Consulta todas las locaciones registradas.
     *
     * @return Lista de locaciones.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Locacion> buscarTodos() throws PersistenciaException {
        try {
            List<Locacion> locaciones = new ArrayList<>();

            for (Locacion locacion : collection.find()) {
                locaciones.add(locacion);
            }

            return locaciones;

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar las locaciones.", e);
        }
    }

    /**
     * Actualiza una locación existente.
     *
     * @param locacion Locación con datos actualizados.
     * @return true si la locación fue actualizada, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(Locacion locacion) throws PersistenciaException {
        try {
            if (locacion.getId() == null || !ObjectId.isValid(locacion.getId())) {
                return false;
            }

            var resultado = collection.replaceOne(
                    Filters.eq("_id", new ObjectId(locacion.getId())),
                    locacion
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otra locación registrada con la misma clave.", e);
            }

            throw new PersistenciaException("Error al actualizar la locación.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar la locación.", e);
        }
    }

    /**
     * Elimina una locación por su identificador.
     *
     * @param id Identificador de la locación.
     * @return true si la locación fue eliminada, false si no se encontró.
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
            throw new PersistenciaException("Error al eliminar la locación.", e);
        }
    }
}