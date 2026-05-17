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
import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IBoletoDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección boletos en MongoDB.
 *
 * Esta clase utiliza colecciones tipadas con PojoCodecProvider para mapear
 * automáticamente objetos Boleto hacia documentos BSON.
 *
 * @author Afgord
 */
public class BoletoDAO implements IBoletoDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "boletos";

    /**
     * Campo BSON que representa el folio visible del boleto.
     *
     * Si en Boleto.java usaste @BsonProperty("folio"), este valor está
     * correcto.
     */
    private static final String CAMPO_FOLIO = "folio";

    /**
     * Campo BSON que representa el identificador del pasajero.
     *
     * Si en Boleto.java usaste @BsonProperty("pasajero_id"), este valor está
     * correcto. Si usaste @BsonProperty("pasajeroId"), cámbialo a "pasajeroId".
     */
    private static final String CAMPO_PASAJERO_ID = "pasajeroId";

    /**
     * Campo BSON que representa el identificador del viaje.
     *
     * Si en Boleto.java usaste @BsonProperty("viaje_id"), este valor está
     * correcto. Si usaste @BsonProperty("viajeId"), cámbialo a "viajeId".
     */
    private static final String CAMPO_VIAJE_ID = "viajeId";

    /**
     * Colección tipada de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Boleto> collection;

    /**
     * Constructor que obtiene la colección boletos desde la conexión Singleton.
     */
    public BoletoDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION, Boleto.class);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de boletos.
     *
     * El índice único sobre folio evita registrar dos boletos con el mismo
     * folio visible.
     *
     * La regla que impide que un pasajero tenga más de un boleto confirmado
     * para el mismo viaje se valida en la capa de servicios, ya que un boleto
     * cancelado puede permitir una nueva compra.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.ascending(CAMPO_FOLIO),
                new IndexOptions().unique(true)
        );
    }

    /**
     * Inserta un nuevo boleto en MongoDB.
     *
     * @param boleto Boleto a insertar.
     * @return Boleto insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Boleto insertar(Boleto boleto) throws PersistenciaException {
        try {
            collection.insertOne(boleto);
            return boleto;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un boleto registrado con el mismo folio.", e);
            }

            throw new PersistenciaException("Error al insertar el boleto.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al insertar el boleto.", e);
        }
    }

    /**
     * Busca un boleto por su identificador.
     *
     * @param id Identificador del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Boleto buscarPorId(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return null;
            }

            return collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el boleto por id.", e);
        }
    }

    /**
     * Busca un boleto por su folio visible.
     *
     * @param folio Folio del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Boleto buscarPorFolio(String folio) throws PersistenciaException {
        try {
            return collection.find(
                    Filters.eq(CAMPO_FOLIO, folio)
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el boleto por folio.", e);
        }
    }

    /**
     * Consulta todos los boletos registrados.
     *
     * @return Lista de boletos.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Boleto> buscarTodos() throws PersistenciaException {
        try {
            List<Boleto> boletos = new ArrayList<>();

            for (Boleto boleto : collection.find()) {
                boletos.add(boleto);
            }

            return boletos;

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar los boletos.", e);
        }
    }

    /**
     * Consulta los boletos asociados a un pasajero.
     *
     * @param pasajeroId Identificador del pasajero.
     * @return Lista de boletos del pasajero.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Boleto> buscarPorPasajeroId(String pasajeroId)
            throws PersistenciaException {
        try {
            List<Boleto> boletos = new ArrayList<>();

            for (Boleto boleto : collection.find(
                    Filters.eq(CAMPO_PASAJERO_ID, pasajeroId))) {
                boletos.add(boleto);
            }

            return boletos;

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar boletos por pasajero.", e);
        }
    }

    /**
     * Consulta los boletos asociados a un viaje.
     *
     * @param viajeId Identificador del viaje.
     * @return Lista de boletos del viaje.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Boleto> buscarPorViajeId(String viajeId)
            throws PersistenciaException {
        try {
            List<Boleto> boletos = new ArrayList<>();

            for (Boleto boleto : collection.find(
                    Filters.eq(CAMPO_VIAJE_ID, viajeId))) {
                boletos.add(boleto);
            }

            return boletos;

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar boletos por viaje.", e);
        }
    }

    /**
     * Actualiza un boleto existente.
     *
     * @param boleto Boleto con datos actualizados.
     * @return true si el boleto fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    @Override
    public boolean actualizar(Boleto boleto) throws PersistenciaException {
        try {
            if (boleto.getId() == null || !ObjectId.isValid(boleto.getId())) {
                return false;
            }

            var resultado = collection.replaceOne(
                    Filters.eq("_id", new ObjectId(boleto.getId())),
                    boleto
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro boleto registrado con el mismo folio.", e);
            }

            throw new PersistenciaException("Error al actualizar el boleto.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar el boleto.", e);
        }
    }

    /**
     * Elimina un boleto por su identificador.
     *
     * @param id Identificador del boleto.
     * @return true si el boleto fue eliminado, false si no se encontró.
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
            throw new PersistenciaException("Error al eliminar el boleto.", e);
        }
    }
}
