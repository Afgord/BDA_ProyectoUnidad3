/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.dominio.PasajeroResumen;
import itson.reservatrenesmongodb.dominio.TipoBoleto;
import itson.reservatrenesmongodb.dominio.ViajeResumen;
import itson.reservatrenesmongodb.dominio.enums.EstatusBoleto;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.BoletoDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IBoletoDAO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar las operaciones CRUD de BoletoDAO.
 *
 * Esta clase permite comprobar la inserción, búsqueda, actualización, listado
 * y eliminación de boletos en MongoDB, incluyendo documentos embebidos,
 * fechas y precios Decimal128.
 *
 * @author Afgord
 */
public class PruebaBoletoDAO {

    /**
     * Método principal de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        IBoletoDAO boletoDAO = new BoletoDAO();

        try {
            System.out.println("=== PRUEBA BOLETO DAO ===");

            // 1. Crear resumen del pasajero
            PasajeroResumen pasajeroResumen = new PasajeroResumen(
                    "Harry James Potter Evans",
                    "6441234567"
            );

            // 2. Crear resumen del viaje
            ViajeResumen viajeResumen = new ViajeResumen(
                    Instant.parse("2026-05-12T08:00:00Z"),
                    "Ciudad Obregon",
                    "Hermosillo",
                    "Tren Regional Norte"
            );

            // 3. Crear tipo de boleto
            TipoBoleto tipoBoleto = new TipoBoleto(
                    "PC",
                    "Primera Clase",
                    new BigDecimal("850.00"),
                    new ArrayList<>(Arrays.asList(
                            "Asiento preferente",
                            "Mayor espacio"
                    )),
                    true
            );

            // 4. Crear boleto
            Boleto boleto = new Boleto();

            boleto.setPasajeroId("662f1d000000000000000001");
            boleto.setViajeId("662f1c000000000000000001");
            boleto.setPasajeroResumen(pasajeroResumen);
            boleto.setViajeResumen(viajeResumen);
            boleto.setTipoBoleto(tipoBoleto);
            boleto.setEstatus(EstatusBoleto.CONFIRMADO);
            boleto.setFechaReservacion(Instant.parse("2026-05-01T10:00:00Z"));
            boleto.setFechaCancelacion(null);

            Boleto insertado = boletoDAO.insertar(boleto);

            System.out.println("\nBoleto insertado:");
            imprimirBoleto(insertado);

            // 5. Buscar por id
            Boleto encontrado = boletoDAO.buscarPorId(insertado.getId());

            System.out.println("\nBoleto encontrado por id:");
            imprimirBoleto(encontrado);

            // 6. Buscar por pasajeroId
            List<Boleto> boletosPasajero = boletoDAO.buscarPorPasajeroId(
                    insertado.getPasajeroId()
            );

            System.out.println("\nBoletos encontrados por pasajeroId:");
            for (Boleto b : boletosPasajero) {
                imprimirBoleto(b);
                System.out.println("-----------------------------");
            }

            // 7. Buscar por viajeId
            List<Boleto> boletosViaje = boletoDAO.buscarPorViajeId(
                    insertado.getViajeId()
            );

            System.out.println("\nBoletos encontrados por viajeId:");
            for (Boleto b : boletosViaje) {
                imprimirBoleto(b);
                System.out.println("-----------------------------");
            }

            // 8. Actualizar boleto: simular cancelacion
            encontrado.setEstatus(EstatusBoleto.CANCELADO);
            encontrado.setFechaCancelacion(Instant.parse("2026-05-02T12:00:00Z"));

            boolean actualizado = boletoDAO.actualizar(encontrado);

            System.out.println("\nBoleto actualizado?: " + actualizado);

            Boleto despuesActualizar = boletoDAO.buscarPorId(encontrado.getId());

            System.out.println("\nBoleto despues de actualizar:");
            imprimirBoleto(despuesActualizar);

            // 9. Listar todos
            List<Boleto> boletos = boletoDAO.buscarTodos();

            System.out.println("\nBoletos registrados:");
            for (Boleto b : boletos) {
                imprimirBoleto(b);
                System.out.println("-----------------------------");
            }

            // 10. Eliminar boleto de prueba
            boolean eliminado = boletoDAO.eliminar(insertado.getId());

            System.out.println("\nBoleto eliminado?: " + eliminado);

            Boleto despuesEliminar = boletoDAO.buscarPorId(insertado.getId());

            System.out.println("\nBusqueda despues de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El boleto ya no existe.");
            } else {
                imprimirBoleto(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA BOLETO DAO ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Imprime los datos de un boleto en consola.
     *
     * @param boleto Boleto a imprimir.
     */
    private static void imprimirBoleto(Boleto boleto) {
        if (boleto == null) {
            System.out.println("Boleto nulo.");
            return;
        }

        System.out.println("ID: " + boleto.getId());
        System.out.println("Pasajero ID: " + boleto.getPasajeroId());
        System.out.println("Viaje ID: " + boleto.getViajeId());

        if (boleto.getPasajeroResumen() != null) {
            System.out.println("Pasajero nombre: "
                    + boleto.getPasajeroResumen().getNombreCompleto());
            System.out.println("Pasajero telefono: "
                    + boleto.getPasajeroResumen().getTelefono());
        } else {
            System.out.println("Pasajero resumen: null");
        }

        if (boleto.getViajeResumen() != null) {
            System.out.println("Fecha salida: "
                    + boleto.getViajeResumen().getFechaHoraSalida());
            System.out.println("Origen: "
                    + boleto.getViajeResumen().getOrigen());
            System.out.println("Destino: "
                    + boleto.getViajeResumen().getDestino());
            System.out.println("Tren: "
                    + boleto.getViajeResumen().getTren());
        } else {
            System.out.println("Viaje resumen: null");
        }

        if (boleto.getTipoBoleto() != null) {
            System.out.println("Tipo boleto clave: "
                    + boleto.getTipoBoleto().getClave());
            System.out.println("Tipo boleto nombre: "
                    + boleto.getTipoBoleto().getNombre());
            System.out.println("Precio: "
                    + boleto.getTipoBoleto().getPrecio());
            System.out.println("Beneficios: "
                    + boleto.getTipoBoleto().getBeneficios());
            System.out.println("Reembolsable: "
                    + boleto.getTipoBoleto().isReembolsable());
        } else {
            System.out.println("Tipo boleto: null");
        }

        System.out.println("Estatus: " + boleto.getEstatus());
        System.out.println("Fecha reservacion: " + boleto.getFechaReservacion());
        System.out.println("Fecha cancelacion: " + boleto.getFechaCancelacion());
    }
}