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
import com.mongodb.client.model.Sorts;
import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección viajes en MongoDB.
 *
 * Esta clase utiliza colecciones tipadas con PojoCodecProvider para mapear
 * automáticamente objetos Viaje hacia documentos BSON.
 *
 * @author Afgord
 */
public class ViajeDAO implements IViajeDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "viajes";
    private static final String CAMPO_ESTATUS = "estatus";
    private static final String CAMPO_ORIGEN_ID
            = "ruta.origen.locacion_id";

    private static final String CAMPO_DESTINO_ID
            = "ruta.destino.locacion_id";

    private static final String CAMPO_DISPONIBILIDAD_GENERAL
            = "disponibilidad.general";

    private static final String CAMPO_DISPONIBILIDAD_PRIMERA_CLASE
            = "disponibilidad.primera_clase";

    /**
     * Campo BSON que representa el identificador del tren dentro del documento
     * embebido tren.
     *
     * Si en TrenResumen usaste @BsonProperty("tren_id"), este valor debe ser
     * "tren.tren_id".
     */
    private static final String CAMPO_TREN_ID = "tren.tren_id";

    /**
     * Campo BSON que representa la fecha y hora de salida.
     *
     * Si en Viaje usaste @BsonProperty("fecha_hora_salida"), este valor debe
     * ser "fecha_hora_salida".
     */
    private static final String CAMPO_FECHA_HORA_SALIDA = "fecha_hora_salida";

    /**
     * Colección tipada de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Viaje> collection;

    /**
     * Constructor que obtiene la colección viajes desde la conexión Singleton.
     */
    public ViajeDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION, Viaje.class);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de viajes.
     *
     * El índice único evita registrar dos viajes del mismo tren con la misma
     * fecha y hora de salida.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.compoundIndex(
                        Indexes.ascending(CAMPO_TREN_ID),
                        Indexes.ascending(CAMPO_FECHA_HORA_SALIDA)
                ),
                new IndexOptions().unique(true)
        );
    }

    /**
     * Inserta un nuevo viaje en MongoDB.
     *
     * @param viaje Viaje a insertar.
     * @return Viaje insertado con su identificador asignado.
     * @throws PersistenciaException Si ocurre un error durante la inserción.
     */
    @Override
    public Viaje insertar(Viaje viaje) throws PersistenciaException {
        try {
            collection.insertOne(viaje);
            return viaje;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un viaje registrado para el mismo tren "
                        + "en la misma fecha y hora de salida.", e);
            }

            throw new PersistenciaException("Error al insertar el viaje.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al insertar el viaje.", e);
        }
    }

    /**
     * Busca un viaje por su identificador.
     *
     * @param id Identificador del viaje.
     * @return Viaje encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Viaje buscarPorId(String id) throws PersistenciaException {
        try {
            if (!ObjectId.isValid(id)) {
                return null;
            }

            return collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el viaje por id.", e);
        }
    }

    /**
     * Consulta los próximos viajes programados, ordenados por fecha de salida.
     *
     * @param limite Cantidad máxima de viajes a consultar.
     * @return Lista de viajes programados próximos.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Viaje> buscarProximosViajesProgramados(int limite)
            throws PersistenciaException {
        try {
            List<Viaje> viajes = new ArrayList<>();

            collection.find(
                    Filters.and(
                            Filters.eq(CAMPO_ESTATUS, "PROGRAMADO"),
                            Filters.gte(CAMPO_FECHA_HORA_SALIDA, Instant.now())
                    )
            )
                    .sort(Sorts.ascending(CAMPO_FECHA_HORA_SALIDA))
                    .limit(limite)
                    .into(viajes);

            return viajes;

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar los próximos viajes programados.", e);
        }
    }

    /**
     * Busca viajes programados disponibles según origen, destino, fecha y tipo
     * de boleto solicitado.
     *
     * @param origenId Identificador de la locación de origen.
     * @param destinoId Identificador de la locación de destino.
     * @param inicioDia Inicio del día consultado.
     * @param finDia Inicio del día siguiente.
     * @param primeraClase Indica si se requiere disponibilidad en primera
     * clase.
     * @return Lista de viajes disponibles.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Viaje> buscarViajesDisponibles(
            String origenId,
            String destinoId,
            Instant inicioDia,
            Instant finDia,
            boolean primeraClase
    ) throws PersistenciaException {
        try {
            List<Viaje> viajes = new ArrayList<>();

            Bson filtroDisponibilidad = primeraClase
                    ? Filters.gt(CAMPO_DISPONIBILIDAD_PRIMERA_CLASE, 0)
                    : Filters.gt(CAMPO_DISPONIBILIDAD_GENERAL, 0);

            collection.find(
                    Filters.and(
                            Filters.eq(CAMPO_ESTATUS, "PROGRAMADO"),
                            Filters.eq(CAMPO_ORIGEN_ID, origenId),
                            Filters.eq(CAMPO_DESTINO_ID, destinoId),
                            Filters.gte(CAMPO_FECHA_HORA_SALIDA, inicioDia),
                            Filters.lt(CAMPO_FECHA_HORA_SALIDA, finDia),
                            Filters.gte(CAMPO_FECHA_HORA_SALIDA, Instant.now()),
                            filtroDisponibilidad
                    )
            )
                    .sort(Sorts.ascending(CAMPO_FECHA_HORA_SALIDA))
                    .into(viajes);

            return viajes;

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar los viajes disponibles.", e);
        }
    }

    /**
     * Consulta todos los viajes registrados.
     *
     * @return Lista de viajes.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<Viaje> buscarTodos() throws PersistenciaException {
        try {
            List<Viaje> viajes = new ArrayList<>();

            for (Viaje viaje : collection.find()) {
                viajes.add(viaje);
            }

            return viajes;

        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar los viajes.", e);
        }
    }

    /**
     * Actualiza un viaje existente.
     *
     * @param viaje Viaje con datos actualizados.
     * @return true si el viaje fue actualizado, false si no se encontró.
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    @Override
    public boolean actualizar(Viaje viaje) throws PersistenciaException {
        try {
            if (viaje.getId() == null || !ObjectId.isValid(viaje.getId())) {
                return false;
            }

            var resultado = collection.replaceOne(
                    Filters.eq("_id", new ObjectId(viaje.getId())),
                    viaje
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro viaje registrado para el mismo tren "
                        + "en la misma fecha y hora de salida.", e);
            }

            throw new PersistenciaException("Error al actualizar el viaje.", e);

        } catch (Exception e) {
            throw new PersistenciaException("Error al actualizar el viaje.", e);
        }
    }

    /**
     * Elimina un viaje por su identificador.
     *
     * @param id Identificador del viaje.
     * @return true si el viaje fue eliminado, false si no se encontró.
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
            throw new PersistenciaException("Error al eliminar el viaje.", e);
        }
    }

    /**
     * Busca un viaje por tren y fecha/hora de salida.
     *
     * @param trenId Identificador del tren.
     * @param fechaHoraSalida Fecha y hora de salida.
     * @return Viaje encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Viaje buscarPorTrenYFechaSalida(String trenId, Instant fechaHoraSalida)
            throws PersistenciaException {
        try {
            return collection.find(
                    Filters.and(
                            Filters.eq(CAMPO_TREN_ID, trenId),
                            Filters.eq(CAMPO_FECHA_HORA_SALIDA, fechaHoraSalida)
                    )
            ).first();

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al buscar el viaje por tren y fecha de salida.", e);
        }
    }
}
