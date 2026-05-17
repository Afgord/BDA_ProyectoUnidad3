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
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.bson.conversions.Bson;
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

    private static final String CAMPO_CIUDAD = "direccion.ciudad";
    private static final String CAMPO_ESTADO = "direccion.estado";

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
     * Busca pasajeros por coincidencia parcial en teléfono o correo
     * electrónico.
     *
     * Para el teléfono se eliminan caracteres no numéricos del criterio
     * capturado. Para el correo se realiza una búsqueda insensible a mayúsculas
     * y minúsculas.
     *
     * @param criterio Texto a buscar en teléfono o correo.
     * @return Lista de pasajeros coincidentes.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public List<Pasajero> buscarPorTelefonoOCorreo(String criterio)
            throws PersistenciaException {
        try {
            if (criterio == null || criterio.trim().isEmpty()) {
                return buscarTodos();
            }

            String criterioLimpio = criterio.trim();
            String criterioTelefono = criterioLimpio.replaceAll("\\D", "");

            List<Bson> filtros = new ArrayList<>();

            /*
         * Búsqueda parcial por correo.
         * Se utiliza Pattern.quote para evitar que caracteres como "." o "@"
         * sean interpretados como operadores de expresión regular.
             */
            filtros.add(
                    Filters.regex(
                            "contacto.correo",
                            Pattern.compile(
                                    Pattern.quote(criterioLimpio),
                                    Pattern.CASE_INSENSITIVE
                            )
                    )
            );

            /*
         * Búsqueda parcial por teléfono.
         * Solo se agrega si el usuario escribió al menos un dígito.
             */
            if (!criterioTelefono.isEmpty()) {
                filtros.add(
                        Filters.regex(
                                "contacto.telefono",
                                Pattern.compile(Pattern.quote(criterioTelefono))
                        )
                );
            }

            Bson filtroBusqueda = filtros.size() == 1
                    ? filtros.get(0)
                    : Filters.or(filtros);

            List<Pasajero> pasajeros = new ArrayList<>();

            for (Pasajero pasajero : collection.find(filtroBusqueda)) {
                pasajeros.add(pasajero);
            }

            return pasajeros;

        } catch (PersistenciaException e) {
            throw e;

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al buscar pasajeros por teléfono o correo.", e);
        }
    }

    /**
     * Consulta las ciudades registradas en las direcciones de los pasajeros.
     *
     * @return Lista de ciudades sin duplicados y ordenadas alfabéticamente.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<String> buscarCiudadesRegistradas()
            throws PersistenciaException {
        try {
            Set<String> ciudades = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

            collection.distinct(CAMPO_CIUDAD, String.class)
                    .forEach(ciudad -> {
                        if (ciudad != null && !ciudad.trim().isEmpty()) {
                            ciudades.add(ciudad.trim());
                        }
                    });

            return new ArrayList<>(ciudades);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar las ciudades registradas.", e);
        }
    }

    /**
     * Consulta los estados registrados en las direcciones de los pasajeros.
     *
     * @return Lista de estados sin duplicados y ordenados alfabéticamente.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<String> buscarEstadosRegistrados()
            throws PersistenciaException {
        try {
            Set<String> estados = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

            collection.distinct(CAMPO_ESTADO, String.class)
                    .forEach(estado -> {
                        if (estado != null && !estado.trim().isEmpty()) {
                            estados.add(estado.trim());
                        }
                    });

            return new ArrayList<>(estados);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al consultar los estados registrados.", e);
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
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
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
