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
import itson.reservatrenesmongodb.dominio.Disponibilidad;
import itson.reservatrenesmongodb.dominio.LocacionResumen;
import itson.reservatrenesmongodb.dominio.RutaViaje;
import itson.reservatrenesmongodb.dominio.TrenResumen;
import itson.reservatrenesmongodb.dominio.Viaje;
import static itson.reservatrenesmongodb.dominio.enums.EstatusBoleto.CANCELADO;
import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import static itson.reservatrenesmongodb.dominio.enums.EstatusViaje.FINALIZADO;
import static itson.reservatrenesmongodb.dominio.enums.EstatusViaje.PROGRAMADO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección viajes en MongoDB.
 *
 * Esta clase se encarga de realizar operaciones CRUD sobre la colección viajes,
 * convirtiendo entre objetos Viaje y documentos BSON.
 *
 * @author Afgord
 */
public class ViajeDAO implements IViajeDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "viajes";

    /**
     * Colección de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Document> collection;

    /**
     * Constructor que obtiene la colección viajes desde la conexión Singleton.
     */
    public ViajeDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION);
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
                        Indexes.ascending("tren.trenId"),
                        Indexes.ascending("fechaHoraSalida")
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
            Document documento = convertirADocumento(viaje);

            collection.insertOne(documento);

            ObjectId idGenerado = documento.getObjectId("_id");
            viaje.setId(idGenerado.toHexString());

            return viaje;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un viaje registrado para el mismo tren en la misma fecha y hora de salida.", e);
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

            Document documento = collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el viaje por id.", e);
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

            for (Document documento : collection.find()) {
                viajes.add(convertirAEntidad(documento));
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
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(Viaje viaje) throws PersistenciaException {
        try {
            if (viaje.getId() == null || !ObjectId.isValid(viaje.getId())) {
                return false;
            }

            var resultado = collection.updateOne(
                    Filters.eq("_id", new ObjectId(viaje.getId())),
                    Updates.combine(
                            Updates.set("tren", convertirTrenResumenADocumento(viaje.getTren())),
                            Updates.set("ruta", convertirRutaViajeADocumento(viaje.getRuta())),
                            Updates.set("fechaHoraSalida", convertirInstantADate(viaje.getFechaHoraSalida())),
                            Updates.set("fechaHoraLlegadaEstimada", convertirInstantADate(viaje.getFechaHoraLlegadaEstimada())),
                            Updates.set("estatus", convertirEstatusAString(viaje.getEstatus())),
                            Updates.set("capacidadMaxima", convertirCapacidadADocumento(viaje.getCapacidadMaxima())),
                            Updates.set("disponibilidad", convertirDisponibilidadADocumento(viaje.getDisponibilidad()))
                    )
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro viaje registrado para el mismo tren en la misma fecha y hora de salida.", e);
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
     * Convierte una entidad Viaje a un documento BSON para MongoDB.
     *
     * @param viaje Viaje a convertir.
     * @return Documento BSON.
     */
    private Document convertirADocumento(Viaje viaje) {
        return new Document()
                .append("tren", convertirTrenResumenADocumento(viaje.getTren()))
                .append("ruta", convertirRutaViajeADocumento(viaje.getRuta()))
                .append("fechaHoraSalida", convertirInstantADate(viaje.getFechaHoraSalida()))
                .append("fechaHoraLlegadaEstimada", convertirInstantADate(viaje.getFechaHoraLlegadaEstimada()))
                .append("estatus", convertirEstatusAString(viaje.getEstatus()))
                .append("capacidadMaxima", convertirCapacidadADocumento(viaje.getCapacidadMaxima()))
                .append("disponibilidad", convertirDisponibilidadADocumento(viaje.getDisponibilidad()));
    }

    /**
     * Convierte un documento BSON de MongoDB a una entidad Viaje.
     *
     * @param documento Documento BSON.
     * @return Entidad Viaje.
     */
    private Viaje convertirAEntidad(Document documento) {
        Viaje viaje = new Viaje();

        ObjectId id = documento.getObjectId("_id");
        if (id != null) {
            viaje.setId(id.toHexString());
        }

        viaje.setTren(convertirDocumentoATrenResumen(
                documento.get("tren", Document.class)
        ));

        viaje.setRuta(convertirDocumentoARutaViaje(
                documento.get("ruta", Document.class)
        ));

        viaje.setFechaHoraSalida(convertirDateAInstant(
                documento.getDate("fechaHoraSalida")
        ));

        viaje.setFechaHoraLlegadaEstimada(convertirDateAInstant(
                documento.getDate("fechaHoraLlegadaEstimada")
        ));

        viaje.setEstatus(convertirStringAEstatus(documento.getString("estatus")));

        viaje.setCapacidadMaxima(convertirDocumentoACapacidad(
                documento.get("capacidadMaxima", Document.class)
        ));

        viaje.setDisponibilidad(convertirDocumentoADisponibilidad(
                documento.get("disponibilidad", Document.class)
        ));

        return viaje;
    }

    /**
     * Convierte un TrenResumen a documento BSON.
     *
     * @param tren TrenResumen a convertir.
     * @return Documento BSON.
     */
    private Document convertirTrenResumenADocumento(TrenResumen tren) {
        if (tren == null) {
            return new Document()
                    .append("trenId", null)
                    .append("codigo", null)
                    .append("nombre", null);
        }

        return new Document()
                .append("trenId", tren.getTrenId())
                .append("codigo", tren.getCodigo())
                .append("nombre", tren.getNombre());
    }

    /**
     * Convierte un documento BSON a TrenResumen.
     *
     * @param documento Documento BSON.
     * @return TrenResumen.
     */
    private TrenResumen convertirDocumentoATrenResumen(Document documento) {
        if (documento == null) {
            return new TrenResumen();
        }

        TrenResumen tren = new TrenResumen();

        tren.setTrenId(documento.getString("trenId"));
        tren.setCodigo(documento.getString("codigo"));
        tren.setNombre(documento.getString("nombre"));

        return tren;
    }

    /**
     * Convierte una RutaViaje a documento BSON.
     *
     * @param ruta RutaViaje a convertir.
     * @return Documento BSON.
     */
    private Document convertirRutaViajeADocumento(RutaViaje ruta) {
        if (ruta == null) {
            return new Document()
                    .append("origen", convertirLocacionResumenADocumento(null))
                    .append("destino", convertirLocacionResumenADocumento(null));
        }

        return new Document()
                .append("origen", convertirLocacionResumenADocumento(ruta.getOrigen()))
                .append("destino", convertirLocacionResumenADocumento(ruta.getDestino()));
    }

    /**
     * Convierte un documento BSON a RutaViaje.
     *
     * @param documento Documento BSON.
     * @return RutaViaje.
     */
    private RutaViaje convertirDocumentoARutaViaje(Document documento) {
        if (documento == null) {
            return new RutaViaje();
        }

        RutaViaje ruta = new RutaViaje();

        ruta.setOrigen(convertirDocumentoALocacionResumen(
                documento.get("origen", Document.class)
        ));

        ruta.setDestino(convertirDocumentoALocacionResumen(
                documento.get("destino", Document.class)
        ));

        return ruta;
    }

    /**
     * Convierte una LocacionResumen a documento BSON.
     *
     * @param locacion LocacionResumen a convertir.
     * @return Documento BSON.
     */
    private Document convertirLocacionResumenADocumento(LocacionResumen locacion) {
        if (locacion == null) {
            return new Document()
                    .append("locacionId", null)
                    .append("nombre", null);
        }

        return new Document()
                .append("locacionId", locacion.getLocacionId())
                .append("nombre", locacion.getNombre());
    }

    /**
     * Convierte un documento BSON a LocacionResumen.
     *
     * @param documento Documento BSON.
     * @return LocacionResumen.
     */
    private LocacionResumen convertirDocumentoALocacionResumen(Document documento) {
        if (documento == null) {
            return new LocacionResumen();
        }

        LocacionResumen locacion = new LocacionResumen();

        locacion.setLocacionId(documento.getString("locacionId"));
        locacion.setNombre(documento.getString("nombre"));

        return locacion;
    }

    /**
     * Convierte un objeto Capacidad a documento BSON.
     *
     * @param capacidad Capacidad a convertir.
     * @return Documento BSON.
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
     * @param documento Documento BSON.
     * @return Capacidad.
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
     * Convierte una Disponibilidad a documento BSON.
     *
     * @param disponibilidad Disponibilidad a convertir.
     * @return Documento BSON.
     */
    private Document convertirDisponibilidadADocumento(Disponibilidad disponibilidad) {
        if (disponibilidad == null) {
            return new Document()
                    .append("general", 0)
                    .append("primeraClase", 0);
        }

        return new Document()
                .append("general", disponibilidad.getGeneral())
                .append("primeraClase", disponibilidad.getPrimeraClase());
    }

    /**
     * Convierte un documento BSON a Disponibilidad.
     *
     * @param documento Documento BSON.
     * @return Disponibilidad.
     */
    private Disponibilidad convertirDocumentoADisponibilidad(Document documento) {
        if (documento == null) {
            return new Disponibilidad();
        }

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setGeneral(documento.getInteger("general", 0));
        disponibilidad.setPrimeraClase(documento.getInteger("primeraClase", 0));

        return disponibilidad;
    }

    /**
     * Convierte el enum EstatusViaje a texto para almacenarlo en MongoDB.
     *
     * @param estatus Estatus del viaje.
     * @return Texto equivalente.
     */
    private String convertirEstatusAString(EstatusViaje estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case PROGRAMADO:
                return "Programado";
            case FINALIZADO:
                return "Finalizado";
            case CANCELADO:
                return "Cancelado";
            default:
                return null;
        }
    }

    /**
     * Convierte el texto almacenado en MongoDB al enum EstatusViaje.
     *
     * @param estatus Texto del estatus.
     * @return Enum EstatusViaje correspondiente.
     */
    private EstatusViaje convertirStringAEstatus(String estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case "Programado":
                return EstatusViaje.PROGRAMADO;
            case "Finalizado":
                return EstatusViaje.FINALIZADO;
            case "Cancelado":
                return EstatusViaje.CANCELADO;
            default:
                return null;
        }
    }

    /**
     * Convierte un Instant a Date para almacenarlo como ISODate en MongoDB.
     *
     * @param instant Fecha en formato Instant.
     * @return Fecha en formato Date.
     */
    private Date convertirInstantADate(Instant instant) {
        if (instant == null) {
            return null;
        }

        return Date.from(instant);
    }

    /**
     * Convierte un Date de MongoDB a Instant.
     *
     * @param date Fecha en formato Date.
     * @return Fecha en formato Instant.
     */
    private Instant convertirDateAInstant(Date date) {
        if (date == null) {
            return null;
        }

        return date.toInstant();
    }
}