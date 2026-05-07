/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.daos;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.interfaces.IDashboardDAO;
import java.math.BigDecimal;
import java.util.Arrays;
import org.bson.Document;

/**
 * Implementación DAO para consultas agregadas del dashboard.
 *
 * Esta clase utiliza aggregate() para obtener indicadores generales del sistema.
 *
 * @author Afgord
 */
public class DashboardDAO implements IDashboardDAO {

    private final MongoCollection<Document> coleccionViajes;
    private final MongoCollection<Document> coleccionBoletos;
    private final MongoCollection<Document> coleccionTrenes;

    public DashboardDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.coleccionViajes = database.getCollection("viajes");
        this.coleccionBoletos = database.getCollection("boletos");
        this.coleccionTrenes = database.getCollection("trenes");
    }

    @Override
    public long contarViajesProgramados() throws PersistenciaException {
        try {
            Document resultado = coleccionViajes.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("estatus", "PROGRAMADO")),
                    new Document("$count", "total")
            )).first();

            return obtenerTotal(resultado);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al contar viajes programados.", e);
        }
    }

    @Override
    public long contarBoletosConfirmados() throws PersistenciaException {
        try {
            Document resultado = coleccionBoletos.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("estatus", "CONFIRMADO")),
                    new Document("$count", "total")
            )).first();

            return obtenerTotal(resultado);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al contar boletos confirmados.", e);
        }
    }

    @Override
    public long contarBoletosCancelados() throws PersistenciaException {
        try {
            Document resultado = coleccionBoletos.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("estatus", "CANCELADO")),
                    new Document("$count", "total")
            )).first();

            return obtenerTotal(resultado);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al contar boletos cancelados.", e);
        }
    }

    @Override
    public long contarTrenesActivos() throws PersistenciaException {
        try {
            Document resultado = coleccionTrenes.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("estatus", "ACTIVO")),
                    new Document("$count", "total")
            )).first();

            return obtenerTotal(resultado);

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al contar trenes activos.", e);
        }
    }

    @Override
    public BigDecimal calcularIngresosTotalesConfirmados()
            throws PersistenciaException {
        try {
            Document resultado = coleccionBoletos.aggregate(Arrays.asList(
                    new Document("$match",
                            new Document("estatus", "CONFIRMADO")),
                    new Document("$group",
                            new Document("_id", null)
                                    .append("total",
                                            new Document("$sum",
                                                    "$tipoBoleto.precio")))
            )).first();

            if (resultado == null || resultado.get("total") == null) {
                return BigDecimal.ZERO;
            }

            Object total = resultado.get("total");

            if (total instanceof org.bson.types.Decimal128 decimal128) {
                return decimal128.bigDecimalValue();
            }

            if (total instanceof Number number) {
                return BigDecimal.valueOf(number.doubleValue());
            }

            return new BigDecimal(total.toString());

        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al calcular ingresos totales confirmados.", e);
        }
    }

    private long obtenerTotal(Document resultado) {
        if (resultado == null || resultado.get("total") == null) {
            return 0;
        }

        Object total = resultado.get("total");

        if (total instanceof Number number) {
            return number.longValue();
        }

        return Long.parseLong(total.toString());
    }
}