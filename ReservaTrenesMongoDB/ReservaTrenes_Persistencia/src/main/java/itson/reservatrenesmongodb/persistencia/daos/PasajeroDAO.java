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
import itson.reservatrenesmongodb.dominio.Contacto;
import itson.reservatrenesmongodb.dominio.Direccion;
import itson.reservatrenesmongodb.dominio.HistorialViaje;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.dominio.enums.Sexo;
import static itson.reservatrenesmongodb.dominio.enums.Sexo.FEMENINO;
import static itson.reservatrenesmongodb.dominio.enums.Sexo.MASCULINO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección pasajeros en MongoDB.
 *
 * Esta clase se encarga de realizar operaciones CRUD sobre la colección
 * pasajeros, convirtiendo entre objetos Pasajero y documentos BSON.
 *
 * @author Afgord
 */
public class PasajeroDAO implements IPasajeroDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "pasajeros";

    /**
     * Colección de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Document> collection;

    /**
     * Constructor que obtiene la colección pasajeros desde la conexión
     * Singleton.
     */
    public PasajeroDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION);
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
            Document documento = convertirADocumento(pasajero);

            collection.insertOne(documento);

            ObjectId idGenerado = documento.getObjectId("_id");
            pasajero.setId(idGenerado.toHexString());

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

            Document documento = collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

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
            Document documento = collection.find(
                    Filters.eq("contacto.correo", correo)
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

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

            for (Document documento : collection.find()) {
                pasajeros.add(convertirAEntidad(documento));
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

            var resultado = collection.updateOne(
                    Filters.eq("_id", new ObjectId(pasajero.getId())),
                    Updates.combine(
                            Updates.set("nombreCompleto", pasajero.getNombreCompleto()),
                            Updates.set("sexo", convertirSexoAString(pasajero.getSexo())),
                            Updates.set("fechaNacimiento", convertirInstantADate(pasajero.getFechaNacimiento())),
                            Updates.set("contacto", convertirContactoADocumento(pasajero.getContacto())),
                            Updates.set("direccion", convertirDireccionADocumento(pasajero.getDireccion())),
                            Updates.set("historialViajes",
                                    convertirHistorialADocumentos(pasajero.getHistorialViajes()))
                    )
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

    /**
     * Convierte una entidad Pasajero a un documento BSON para MongoDB.
     *
     * @param pasajero Pasajero a convertir.
     * @return Documento BSON.
     */
    private Document convertirADocumento(Pasajero pasajero) {
        return new Document()
                .append("nombreCompleto", pasajero.getNombreCompleto())
                .append("sexo", convertirSexoAString(pasajero.getSexo()))
                .append("fechaNacimiento", convertirInstantADate(pasajero.getFechaNacimiento()))
                .append("contacto", convertirContactoADocumento(pasajero.getContacto()))
                .append("direccion", convertirDireccionADocumento(pasajero.getDireccion()))
                .append("historialViajes",
                        convertirHistorialADocumentos(pasajero.getHistorialViajes()));
    }

    /**
     * Convierte un documento BSON de MongoDB a una entidad Pasajero.
     *
     * @param documento Documento BSON.
     * @return Entidad Pasajero.
     */
    private Pasajero convertirAEntidad(Document documento) {
        Pasajero pasajero = new Pasajero();

        ObjectId id = documento.getObjectId("_id");
        if (id != null) {
            pasajero.setId(id.toHexString());
        }

        pasajero.setNombreCompleto(documento.getString("nombreCompleto"));
        pasajero.setSexo(convertirStringASexo(documento.getString("sexo")));
        pasajero.setFechaNacimiento(convertirDateAInstant(
                documento.getDate("fechaNacimiento")
        ));

        pasajero.setContacto(convertirDocumentoAContacto(
                documento.get("contacto", Document.class)
        ));

        pasajero.setDireccion(convertirDocumentoADireccion(
                documento.get("direccion", Document.class)
        ));

        pasajero.setHistorialViajes(convertirDocumentosAHistorial(
                documento.getList("historialViajes", Document.class)
        ));

        return pasajero;
    }

    /**
     * Convierte un objeto Contacto a documento BSON.
     *
     * @param contacto Contacto a convertir.
     * @return Documento BSON.
     */
    private Document convertirContactoADocumento(Contacto contacto) {
        if (contacto == null) {
            return new Document()
                    .append("telefono", null)
                    .append("correo", null);
        }

        return new Document()
                .append("telefono", contacto.getTelefono())
                .append("correo", contacto.getCorreo());
    }

    /**
     * Convierte un documento BSON a objeto Contacto.
     *
     * @param documento Documento de contacto.
     * @return Objeto Contacto.
     */
    private Contacto convertirDocumentoAContacto(Document documento) {
        if (documento == null) {
            return new Contacto();
        }

        Contacto contacto = new Contacto();

        contacto.setTelefono(documento.getString("telefono"));
        contacto.setCorreo(documento.getString("correo"));

        return contacto;
    }

    /**
     * Convierte un objeto Direccion a documento BSON.
     *
     * @param direccion Dirección a convertir.
     * @return Documento BSON.
     */
    private Document convertirDireccionADocumento(Direccion direccion) {
        if (direccion == null) {
            return new Document()
                    .append("calle", null)
                    .append("colonia", null)
                    .append("ciudad", null)
                    .append("estado", null);
        }

        return new Document()
                .append("calle", direccion.getCalle())
                .append("colonia", direccion.getColonia())
                .append("ciudad", direccion.getCiudad())
                .append("estado", direccion.getEstado());
    }

    /**
     * Convierte un documento BSON a objeto Direccion.
     *
     * @param documento Documento de dirección.
     * @return Objeto Direccion.
     */
    private Direccion convertirDocumentoADireccion(Document documento) {
        if (documento == null) {
            return new Direccion();
        }

        Direccion direccion = new Direccion();

        direccion.setCalle(documento.getString("calle"));
        direccion.setColonia(documento.getString("colonia"));
        direccion.setCiudad(documento.getString("ciudad"));
        direccion.setEstado(documento.getString("estado"));

        return direccion;
    }

    /**
     * Convierte una lista de HistorialViaje a lista de documentos BSON.
     *
     * @param historial Lista de historial de viajes.
     * @return Lista de documentos BSON.
     */
    private List<Document> convertirHistorialADocumentos(List<HistorialViaje> historial) {
        List<Document> documentos = new ArrayList<>();

        if (historial == null) {
            return documentos;
        }

        for (HistorialViaje h : historial) {
            Document documento = new Document()
                    .append("boletoId", h.getBoletoId())
                    .append("viajeId", h.getViajeId())
                    .append("destino", h.getDestino());

            documentos.add(documento);
        }

        return documentos;
    }

    /**
     * Convierte una lista de documentos BSON a lista de HistorialViaje.
     *
     * @param documentos Lista de documentos BSON.
     * @return Lista de historial de viajes.
     */
    private List<HistorialViaje> convertirDocumentosAHistorial(
            List<Document> documentos) {
        List<HistorialViaje> historial = new ArrayList<>();

        if (documentos == null) {
            return historial;
        }

        for (Document documento : documentos) {
            HistorialViaje h = new HistorialViaje();

            h.setBoletoId(documento.getString("boletoId"));
            h.setViajeId(documento.getString("viajeId"));
            h.setDestino(documento.getString("destino"));

            historial.add(h);
        }

        return historial;
    }

    /**
     * Convierte el enum Sexo a texto para almacenarlo en MongoDB.
     *
     * @param sexo Sexo del pasajero.
     * @return Texto equivalente.
     */
    private String convertirSexoAString(Sexo sexo) {
        if (sexo == null) {
            return null;
        }

        switch (sexo) {
            case MASCULINO:
                return "Masculino";
            case FEMENINO:
                return "Femenino";
            default:
                return null;
        }
    }

    /**
     * Convierte el texto almacenado en MongoDB al enum Sexo.
     *
     * @param sexo Texto del sexo.
     * @return Enum Sexo correspondiente.
     */
    private Sexo convertirStringASexo(String sexo) {
        if (sexo == null) {
            return null;
        }

        switch (sexo) {
            case "Masculino":
                return Sexo.MASCULINO;
            case "Femenino":
                return Sexo.FEMENINO;
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