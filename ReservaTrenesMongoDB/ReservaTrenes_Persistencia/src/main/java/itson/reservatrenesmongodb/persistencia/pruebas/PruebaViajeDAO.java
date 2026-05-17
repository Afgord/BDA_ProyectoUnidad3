/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Disponibilidad;
import itson.reservatrenesmongodb.dominio.LocacionResumen;
import itson.reservatrenesmongodb.dominio.RutaViaje;
import itson.reservatrenesmongodb.dominio.TrenResumen;
import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.ViajeDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import java.time.Instant;
import java.util.List;

/**
 * Clase de prueba manual para validar las operaciones CRUD de ViajeDAO.
 *
 * Esta clase permite comprobar la inserción, búsqueda, actualización, listado
 * y eliminación de viajes en MongoDB, incluyendo documentos embebidos.
 *
 * @author Afgord
 */
public class PruebaViajeDAO {

    /**
     * Método principal de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        IViajeDAO viajeDAO = new ViajeDAO();

        try {
            System.out.println("=== PRUEBA VIAJE DAO ===");

            // 1. Crear subdocumento de tren
            TrenResumen trenResumen = new TrenResumen(
                    "662f1b000000000000000001",
                    "TR-NORTE-01",
                    "Tren Regional Norte"
            );

            // 2. Crear subdocumentos de locaciones
            LocacionResumen origen = new LocacionResumen(
                    "662f1a000000000000000001",
                    "Ciudad Obregon"
            );

            LocacionResumen destino = new LocacionResumen(
                    "662f1a000000000000000004",
                    "Hermosillo"
            );

            RutaViaje ruta = new RutaViaje(origen, destino);

            // 3. Crear viaje
            Viaje viaje = new Viaje();

            viaje.setTren(trenResumen);
            viaje.setRuta(ruta);
            viaje.setFechaHoraSalida(Instant.parse("2026-05-12T08:00:00Z"));
            viaje.setFechaHoraLlegadaEstimada(Instant.parse("2026-05-12T11:30:00Z"));
            viaje.setEstatus(EstatusViaje.PROGRAMADO);
            viaje.setCapacidadMaxima(new Capacidad(5, 5));
            viaje.setDisponibilidad(new Disponibilidad(3, 2));

            Viaje insertado = viajeDAO.insertar(viaje);

            System.out.println("\nViaje insertado:");
            imprimirViaje(insertado);

            // 4. Buscar por id
            Viaje encontrado = viajeDAO.buscarPorId(insertado.getId());

            System.out.println("\nViaje encontrado por id:");
            imprimirViaje(encontrado);

            // 5. Actualizar viaje
            encontrado.setEstatus(EstatusViaje.FINALIZADO);
            encontrado.setDisponibilidad(new Disponibilidad(0, 0));

            boolean actualizado = viajeDAO.actualizar(encontrado);

            System.out.println("\nViaje actualizado?: " + actualizado);

            Viaje despuesActualizar = viajeDAO.buscarPorId(encontrado.getId());

            System.out.println("\nViaje despues de actualizar:");
            imprimirViaje(despuesActualizar);

            // 6. Listar todos
            List<Viaje> viajes = viajeDAO.buscarTodos();

            System.out.println("\nViajes registrados:");
            for (Viaje v : viajes) {
                imprimirViaje(v);
                System.out.println("-----------------------------");
            }

            // 7. Eliminar viaje de prueba
            boolean eliminado = viajeDAO.eliminar(insertado.getId());

            System.out.println("\nViaje eliminado?: " + eliminado);

            Viaje despuesEliminar = viajeDAO.buscarPorId(insertado.getId());

            System.out.println("\nBusqueda despues de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El viaje ya no existe.");
            } else {
                imprimirViaje(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA VIAJE DAO ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Imprime los datos de un viaje en consola.
     *
     * @param viaje Viaje a imprimir.
     */
    private static void imprimirViaje(Viaje viaje) {
        if (viaje == null) {
            System.out.println("Viaje nulo.");
            return;
        }

        System.out.println("ID: " + viaje.getId());

        if (viaje.getTren() != null) {
            System.out.println("Tren ID: " + viaje.getTren().getTrenId());
            System.out.println("Tren codigo: " + viaje.getTren().getCodigo());
            System.out.println("Tren nombre: " + viaje.getTren().getNombre());
        } else {
            System.out.println("Tren: null");
        }

        if (viaje.getRuta() != null) {
            if (viaje.getRuta().getOrigen() != null) {
                System.out.println("Origen ID: "
                        + viaje.getRuta().getOrigen().getLocacionId());
                System.out.println("Origen nombre: "
                        + viaje.getRuta().getOrigen().getNombre());
            }

            if (viaje.getRuta().getDestino() != null) {
                System.out.println("Destino ID: "
                        + viaje.getRuta().getDestino().getLocacionId());
                System.out.println("Destino nombre: "
                        + viaje.getRuta().getDestino().getNombre());
            }
        } else {
            System.out.println("Ruta: null");
        }

        System.out.println("Fecha salida: " + viaje.getFechaHoraSalida());
        System.out.println("Fecha llegada estimada: "
                + viaje.getFechaHoraLlegadaEstimada());
        System.out.println("Estatus: " + viaje.getEstatus());

        if (viaje.getCapacidadMaxima() != null) {
            System.out.println("Capacidad maxima general: "
                    + viaje.getCapacidadMaxima().getGeneral());
            System.out.println("Capacidad maxima primera clase: "
                    + viaje.getCapacidadMaxima().getPrimeraClase());
        }

        if (viaje.getDisponibilidad() != null) {
            System.out.println("Disponibilidad general: "
                    + viaje.getDisponibilidad().getGeneral());
            System.out.println("Disponibilidad primera clase: "
                    + viaje.getDisponibilidad().getPrimeraClase());
        }
    }
}
