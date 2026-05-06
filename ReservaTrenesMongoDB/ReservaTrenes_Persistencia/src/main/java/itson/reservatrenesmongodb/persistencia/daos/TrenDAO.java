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
import com.mongodb.client.model.Updates;
import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import static itson.reservatrenesmongodb.dominio.enums.EstatusTren.ACTIVO;
import static itson.reservatrenesmongodb.dominio.enums.EstatusTren.FUERA_SERVICIO;
import static itson.reservatrenesmongodb.dominio.enums.EstatusTren.MANTENIMIENTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección trenes en MongoDB.
 *
 * Esta clase se encarga de realizar operaciones CRUD sobre la colección trenes,
 * convirtiendo entre objetos Tren y documentos BSON.
 *
 * @author Afgord
 */
public class TrenDAO implements ITrenDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "trenes";

    /**
     * Colección de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Document> collection;

    /**
     * Constructor que obtiene la colección trenes desde la conexión Singleton.
     */
    public TrenDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION);
        crearIndices();
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
            Document documento = convertirADocumento(tren);

            collection.insertOne(documento);

            ObjectId idGenerado = documento.getObjectId("_id");
            tren.setId(idGenerado.toHexString());

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

            Document documento = collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

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

            for (Document documento : collection.find()) {
                trenes.add(convertirAEntidad(documento));
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
     * @throws PersistenciaException Si ocurre un error durante la
     * actualización.
     */
    @Override
    public boolean actualizar(Tren tren) throws PersistenciaException {
        try {
            if (tren.getId() == null || !ObjectId.isValid(tren.getId())) {
                return false;
            }

            var resultado = collection.updateOne(
                    Filters.eq("_id", new ObjectId(tren.getId())),
                    Updates.combine(
                            Updates.set("codigo", tren.getCodigo()),
                            Updates.set("nombre", tren.getNombre()),
                            Updates.set("modelo", tren.getModelo()),
                            Updates.set("estatus", convertirEstatusAString(tren.getEstatus())),
                            Updates.set("capacidad", convertirCapacidadADocumento(tren.getCapacidad())),
                            Updates.set("servicios", tren.getServicios())
                    )
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

    /**
     * Convierte una entidad Tren a un documento BSON para MongoDB.
     *
     * @param tren Tren a convertir.
     * @return Documento BSON.
     */
    private Document convertirADocumento(Tren tren) {
        return new Document()
                .append("codigo", tren.getCodigo())
                .append("nombre", tren.getNombre())
                .append("modelo", tren.getModelo())
                .append("estatus", convertirEstatusAString(tren.getEstatus()))
                .append("capacidad", convertirCapacidadADocumento(tren.getCapacidad()))
                .append("servicios", tren.getServicios());
    }

    /**
     * Convierte un documento BSON de MongoDB a una entidad Tren.
     *
     * @param documento Documento BSON.
     * @return Entidad Tren.
     */
    private Tren convertirAEntidad(Document documento) {
        Tren tren = new Tren();

        ObjectId id = documento.getObjectId("_id");
        if (id != null) {
            tren.setId(id.toHexString());
        }

        tren.setCodigo(documento.getString("codigo"));
        tren.setNombre(documento.getString("nombre"));
        tren.setModelo(documento.getString("modelo"));
        tren.setEstatus(convertirStringAEstatus(documento.getString("estatus")));
        tren.setCapacidad(convertirDocumentoACapacidad(
                documento.get("capacidad", Document.class)
        ));

        List<String> servicios = documento.getList("servicios", String.class);
        if (servicios == null) {
            servicios = new ArrayList<>();
        }
        tren.setServicios(servicios);

        return tren;
    }

    /**
     * Convierte un objeto Capacidad a documento BSON.
     *
     * @param capacidad Capacidad a convertir.
     * @return Documento BSON con general y primeraClase.
     */
    private Document convertirCapacidadADocumento(Capacidad capacidad) {
        if (capacidad == null) {
            return new Document()
                    .append("general", 0)
                    .append("primeraClase", 0);
        }

        return new Document()
                .append("general", capacidad.getGeneral())
                .append("primeraClase", capacidad.getPrimeraClase());
    }

    /**
     * Convierte un documento BSON a objeto Capacidad.
     *
     * @param documento Documento de capacidad.
     * @return Objeto Capacidad.
     */
    private Capacidad convertirDocumentoACapacidad(Document documento) {
        if (documento == null) {
            return new Capacidad();
        }

        Capacidad capacidad = new Capacidad();

        capacidad.setGeneral(documento.getInteger("general", 0));
        capacidad.setPrimeraClase(documento.getInteger("primeraClase", 0));

        return capacidad;
    }

    /**
     * Convierte el enum EstatusTren a texto para almacenarlo en MongoDB.
     *
     * @param estatus Estatus del tren.
     * @return Texto equivalente.
     */
    private String convertirEstatusAString(EstatusTren estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case ACTIVO:
                return "Activo";
            case MANTENIMIENTO:
                return "Mantenimiento";
            case FUERA_SERVICIO:
                return "FueraServicio";
            default:
                return null;
        }
    }

    /**
     * Convierte el texto almacenado en MongoDB al enum EstatusTren.
     *
     * @param estatus Texto del estatus.
     * @return Enum EstatusTren correspondiente.
     */
    private EstatusTren convertirStringAEstatus(String estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case "Activo":
                return EstatusTren.ACTIVO;
            case "Mantenimiento":
                return EstatusTren.MANTENIMIENTO;
            case "FueraServicio":
                return EstatusTren.FUERA_SERVICIO;
            default:
                return null;
        }
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
}
