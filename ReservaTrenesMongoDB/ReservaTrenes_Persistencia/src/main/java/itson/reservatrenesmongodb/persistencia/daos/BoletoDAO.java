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
import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.dominio.PasajeroResumen;
import itson.reservatrenesmongodb.dominio.TipoBoleto;
import itson.reservatrenesmongodb.dominio.ViajeResumen;
import itson.reservatrenesmongodb.dominio.enums.EstatusBoleto;
import static itson.reservatrenesmongodb.dominio.enums.EstatusBoleto.CONFIRMADO;
import static itson.reservatrenesmongodb.dominio.enums.EstatusViaje.CANCELADO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IBoletoDAO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

/**
 * Implementación DAO para la colección boletos en MongoDB.
 *
 * Esta clase se encarga de realizar operaciones CRUD sobre la colección
 * boletos, convirtiendo entre objetos Boleto y documentos BSON.
 *
 * @author Afgord
 */
public class BoletoDAO implements IBoletoDAO {

    /**
     * Nombre de la colección en MongoDB.
     */
    private static final String COLECCION = "boletos";

    /**
     * Colección de MongoDB utilizada por este DAO.
     */
    private final MongoCollection<Document> collection;

    /**
     * Constructor que obtiene la colección boletos desde la conexión Singleton.
     */
    public BoletoDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection(COLECCION);
        crearIndices();
    }

    /**
     * Crea los índices necesarios para la colección de boletos.
     *
     * El índice único sobre folio evita registrar dos boletos con el mismo
     * folio. El índice compuesto evita registrar más de un boleto para el mismo
     * pasajero en el mismo viaje.
     */
    private void crearIndices() {
        collection.createIndex(
                Indexes.ascending("folio"),
                new IndexOptions().unique(true)
        );

        collection.createIndex(
                Indexes.compoundIndex(
                        Indexes.ascending("pasajeroId"),
                        Indexes.ascending("viajeId")
                ),
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
            Document documento = convertirADocumento(boleto);

            collection.insertOne(documento);

            ObjectId idGenerado = documento.getObjectId("_id");
            boleto.setId(idGenerado.toHexString());

            return boleto;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe un boleto registrado con el mismo folio "
                        + "o para el mismo pasajero y viaje.", e);
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

            Document documento = collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

        } catch (Exception e) {
            throw new PersistenciaException("Error al buscar el boleto por id.", e);
        }
    }

    /**
     * Busca un boleto por su folio.
     *
     * @param folio Folio visible del boleto.
     * @return Boleto encontrado o null si no existe.
     * @throws PersistenciaException Si ocurre un error durante la búsqueda.
     */
    @Override
    public Boleto buscarPorFolio(String folio) throws PersistenciaException {
        try {
            Document documento = collection.find(
                    Filters.eq("folio", folio)
            ).first();

            if (documento == null) {
                return null;
            }

            return convertirAEntidad(documento);

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

            for (Document documento : collection.find()) {
                boletos.add(convertirAEntidad(documento));
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

            for (Document documento : collection.find(
                    Filters.eq("pasajeroId", pasajeroId))) {
                boletos.add(convertirAEntidad(documento));
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

            for (Document documento : collection.find(
                    Filters.eq("viajeId", viajeId))) {
                boletos.add(convertirAEntidad(documento));
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
     * @throws PersistenciaException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(Boleto boleto) throws PersistenciaException {
        try {
            if (boleto.getId() == null || !ObjectId.isValid(boleto.getId())) {
                return false;
            }

            var resultado = collection.updateOne(
                    Filters.eq("_id", new ObjectId(boleto.getId())),
                    Updates.combine(
                            Updates.set("folio", boleto.getFolio()),
                            Updates.set("pasajeroId", boleto.getPasajeroId()),
                            Updates.set("viajeId", boleto.getViajeId()),
                            Updates.set("pasajeroResumen",
                                    convertirPasajeroResumenADocumento(
                                            boleto.getPasajeroResumen())),
                            Updates.set("viajeResumen",
                                    convertirViajeResumenADocumento(
                                            boleto.getViajeResumen())),
                            Updates.set("tipoBoleto",
                                    convertirTipoBoletoADocumento(
                                            boleto.getTipoBoleto())),
                            Updates.set("estatus",
                                    convertirEstatusAString(
                                            boleto.getEstatus())),
                            Updates.set("fechaReservacion",
                                    convertirInstantADate(
                                            boleto.getFechaReservacion())),
                            Updates.set("fechaCancelacion",
                                    convertirInstantADate(
                                            boleto.getFechaCancelacion()))
                    )
            );

            return resultado.getModifiedCount() > 0;

        } catch (MongoWriteException e) {
            if (e.getError() != null && e.getError().getCode() == 11000) {
                throw new PersistenciaException(
                        "Ya existe otro boleto registrado con el mismo folio "
                        + "o para el mismo pasajero y viaje.", e);
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

    /**
     * Convierte una entidad Boleto a un documento BSON para MongoDB.
     *
     * @param boleto Boleto a convertir.
     * @return Documento BSON.
     */
    private Document convertirADocumento(Boleto boleto) {
        return new Document()
                .append("folio", boleto.getFolio())
                .append("pasajeroId", boleto.getPasajeroId())
                .append("viajeId", boleto.getViajeId())
                .append("pasajeroResumen",
                        convertirPasajeroResumenADocumento(
                                boleto.getPasajeroResumen()))
                .append("viajeResumen",
                        convertirViajeResumenADocumento(
                                boleto.getViajeResumen()))
                .append("tipoBoleto",
                        convertirTipoBoletoADocumento(
                                boleto.getTipoBoleto()))
                .append("estatus",
                        convertirEstatusAString(boleto.getEstatus()))
                .append("fechaReservacion",
                        convertirInstantADate(boleto.getFechaReservacion()))
                .append("fechaCancelacion",
                        convertirInstantADate(boleto.getFechaCancelacion()));
    }

    /**
     * Convierte un documento BSON de MongoDB a una entidad Boleto.
     *
     * @param documento Documento BSON.
     * @return Entidad Boleto.
     */
    private Boleto convertirAEntidad(Document documento) {
        Boleto boleto = new Boleto();

        ObjectId id = documento.getObjectId("_id");
        if (id != null) {
            boleto.setId(id.toHexString());
        }

        boleto.setFolio(documento.getString("folio"));
        boleto.setPasajeroId(documento.getString("pasajeroId"));
        boleto.setViajeId(documento.getString("viajeId"));

        boleto.setPasajeroResumen(convertirDocumentoAPasajeroResumen(
                documento.get("pasajeroResumen", Document.class)
        ));

        boleto.setViajeResumen(convertirDocumentoAViajeResumen(
                documento.get("viajeResumen", Document.class)
        ));

        boleto.setTipoBoleto(convertirDocumentoATipoBoleto(
                documento.get("tipoBoleto", Document.class)
        ));

        boleto.setEstatus(convertirStringAEstatus(documento.getString("estatus")));

        boleto.setFechaReservacion(convertirDateAInstant(
                documento.getDate("fechaReservacion")
        ));

        boleto.setFechaCancelacion(convertirDateAInstant(
                documento.getDate("fechaCancelacion")
        ));

        return boleto;
    }

    /**
     * Convierte un PasajeroResumen a documento BSON.
     *
     * @param pasajero PasajeroResumen a convertir.
     * @return Documento BSON.
     */
    private Document convertirPasajeroResumenADocumento(
            PasajeroResumen pasajero) {
        if (pasajero == null) {
            return new Document()
                    .append("nombreCompleto", null)
                    .append("telefono", null);
        }

        return new Document()
                .append("nombreCompleto", pasajero.getNombreCompleto())
                .append("telefono", pasajero.getTelefono());
    }

    /**
     * Convierte un documento BSON a PasajeroResumen.
     *
     * @param documento Documento BSON.
     * @return PasajeroResumen.
     */
    private PasajeroResumen convertirDocumentoAPasajeroResumen(
            Document documento) {
        if (documento == null) {
            return new PasajeroResumen();
        }

        PasajeroResumen pasajero = new PasajeroResumen();

        pasajero.setNombreCompleto(documento.getString("nombreCompleto"));
        pasajero.setTelefono(documento.getString("telefono"));

        return pasajero;
    }

    /**
     * Convierte un ViajeResumen a documento BSON.
     *
     * @param viaje ViajeResumen a convertir.
     * @return Documento BSON.
     */
    private Document convertirViajeResumenADocumento(ViajeResumen viaje) {
        if (viaje == null) {
            return new Document()
                    .append("fechaHoraSalida", null)
                    .append("origen", null)
                    .append("destino", null)
                    .append("tren", null);
        }

        return new Document()
                .append("fechaHoraSalida",
                        convertirInstantADate(viaje.getFechaHoraSalida()))
                .append("origen", viaje.getOrigen())
                .append("destino", viaje.getDestino())
                .append("tren", viaje.getTren());
    }

    /**
     * Convierte un documento BSON a ViajeResumen.
     *
     * @param documento Documento BSON.
     * @return ViajeResumen.
     */
    private ViajeResumen convertirDocumentoAViajeResumen(Document documento) {
        if (documento == null) {
            return new ViajeResumen();
        }

        ViajeResumen viaje = new ViajeResumen();

        viaje.setFechaHoraSalida(convertirDateAInstant(
                documento.getDate("fechaHoraSalida")
        ));
        viaje.setOrigen(documento.getString("origen"));
        viaje.setDestino(documento.getString("destino"));
        viaje.setTren(documento.getString("tren"));

        return viaje;
    }

    /**
     * Convierte un TipoBoleto a documento BSON.
     *
     * @param tipoBoleto TipoBoleto a convertir.
     * @return Documento BSON.
     */
    private Document convertirTipoBoletoADocumento(TipoBoleto tipoBoleto) {
        if (tipoBoleto == null) {
            return new Document()
                    .append("clave", null)
                    .append("nombre", null)
                    .append("precio", null)
                    .append("beneficios", new ArrayList<String>())
                    .append("reembolsable", false);
        }

        return new Document()
                .append("clave", tipoBoleto.getClave())
                .append("nombre", tipoBoleto.getNombre())
                .append("precio", convertirBigDecimalADecimal128(
                        tipoBoleto.getPrecio()))
                .append("beneficios", tipoBoleto.getBeneficios())
                .append("reembolsable", tipoBoleto.isReembolsable());
    }

    /**
     * Convierte un documento BSON a TipoBoleto.
     *
     * @param documento Documento BSON.
     * @return TipoBoleto.
     */
    private TipoBoleto convertirDocumentoATipoBoleto(Document documento) {
        if (documento == null) {
            return new TipoBoleto();
        }

        TipoBoleto tipoBoleto = new TipoBoleto();

        tipoBoleto.setClave(documento.getString("clave"));
        tipoBoleto.setNombre(documento.getString("nombre"));
        tipoBoleto.setPrecio(convertirDecimal128ABigDecimal(
                documento.get("precio", Decimal128.class)
        ));

        List<String> beneficios = documento.getList("beneficios", String.class);
        if (beneficios == null) {
            beneficios = new ArrayList<>();
        }
        tipoBoleto.setBeneficios(beneficios);

        tipoBoleto.setReembolsable(documento.getBoolean("reembolsable", false));

        return tipoBoleto;
    }

    /**
     * Convierte el enum EstatusBoleto a texto para almacenarlo en MongoDB.
     *
     * @param estatus Estatus del boleto.
     * @return Texto equivalente.
     */
    private String convertirEstatusAString(EstatusBoleto estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case CONFIRMADO:
                return "Confirmado";
            case CANCELADO:
                return "Cancelado";
            default:
                return null;
        }
    }

    /**
     * Convierte el texto almacenado en MongoDB al enum EstatusBoleto.
     *
     * @param estatus Texto del estatus.
     * @return Enum EstatusBoleto correspondiente.
     */
    private EstatusBoleto convertirStringAEstatus(String estatus) {
        if (estatus == null) {
            return null;
        }

        switch (estatus) {
            case "Confirmado":
                return EstatusBoleto.CONFIRMADO;
            case "Cancelado":
                return EstatusBoleto.CANCELADO;
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

    /**
     * Convierte BigDecimal a Decimal128 para almacenar precios en MongoDB.
     *
     * @param valor Valor BigDecimal.
     * @return Valor Decimal128.
     */
    private Decimal128 convertirBigDecimalADecimal128(BigDecimal valor) {
        if (valor == null) {
            return null;
        }

        return new Decimal128(valor);
    }

    /**
     * Convierte Decimal128 de MongoDB a BigDecimal.
     *
     * @param valor Valor Decimal128.
     * @return Valor BigDecimal.
     */
    private BigDecimal convertirDecimal128ABigDecimal(Decimal128 valor) {
        if (valor == null) {
            return null;
        }

        return valor.bigDecimalValue();
    }
}