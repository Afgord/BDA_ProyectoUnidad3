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
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección trenes en MongoDB.
 *
 * Esta clase utiliza colecciones tipadas con PojoCodecProvider para mapear
 * automáticamente objetos Tren hacia documentos BSON.
 *
 * @author Afgord
 */
public class TrenDAO implements ITrenDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "trenes";

    /**
     * Colección tipada de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Tren> collection;

    /**
     * Constructor que obtiene la colección trenes desde la conexión Singleton.
     */
    public TrenDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION, Tren.class);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de trenes.
     *
     * El índice único sobre codigo evita registrar dos trenes con el mismo
     * código.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.ascending("codigo"),
                new IndexOptions().unique(true)
        );
    }

    /**
     * Inserta un nuevo tren en MongoDB.
     *
     * @param tren Tren a insertar.
     * @return Tren insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Tren insertar(Tren tren) throws PersistenciaException {
        try {
            collection.insertOne(tren);
            return tren;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un tren registrado con el mismo código.", e);
            }

            throw new PersistenciaException("Error al insertar el tren.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al insertar el tren.", e);
        }
    }

    /**
     * Busca un tren por su identificador.
     *
     * @param id Identificador del tren.
     * @return Tren encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Tren buscarPorId(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return null;
            }

            return collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el tren por id.", e);
        }
    }

    /**
     * Consulta todos los trenes registrados.
     *
     * @return Lista de trenes.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Tren> buscarTodos() throws PersistenciaException {
        try {
            List<Tren> trenes = new ArrayList<>();

            for (Tren tren : collection.find()) {
                trenes.add(tren);
            }

            return trenes;

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar los trenes.", e);
        }
    }

    /**
     * Actualiza un tren existente.
     *
     * @param tren Tren con datos actualizados.
     * @return true si el tren fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(Tren tren) throws PersistenciaException {
        try {
            if (tren.getId() == null || !ObjectId.isValid(tren.getId())) {
                return false;
            }

            var resultado = collection.replaceOne(
                    Filters.eq("_id", new ObjectId(tren.getId())),
                    tren
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro tren registrado con el mismo código.", e);
            }

            throw new PersistenciaException("Error al actualizar el tren.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar el tren.", e);
        }
    }

    /**
     * Elimina un tren por su identificador.
     *
     * @param id Identificador del tren.
     * @return true si el tren fue eliminado, false si no se encontró.
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
            throw new PersistenciaException("Error al eliminar el tren.", e);
        }
    }
}