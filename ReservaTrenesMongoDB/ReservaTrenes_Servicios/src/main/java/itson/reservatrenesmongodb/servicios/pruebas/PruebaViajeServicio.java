/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.LocacionServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.TrenServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.ViajeServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IViajeServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar ViajeServicio.
 *
 * Permite comprobar el flujo:
 * ViajeDTO -> ViajeServicio -> ViajeDAO -> MongoDB.
 *
 * También crea locaciones y tren necesarios para validar las reglas de negocio.
 *
 * @author Afgord
 */
public class PruebaViajeServicio {

    public static void main(String[] args) {
        ILocacionServicio locacionServicio = new LocacionServicio();
        ITrenServicio trenServicio = new TrenServicio();
        IViajeServicio viajeServicio = new ViajeServicio();

        LocacionDTO origen = null;
        LocacionDTO destino = null;
        TrenDTO tren = null;
        ViajeDTO viajeRegistrado = null;

        try {
            System.out.println("=== PRUEBA VIAJE SERVICIO ===");

            // 1. Registrar locación origen
            origen = locacionServicio.registrar(new LocacionDTO(
                    "cob",
                    "Ciudad Obregón",
                    "Sonora",
                    "México",
                    true
            ));

            // 2. Registrar locación destino
            destino = locacionServicio.registrar(new LocacionDTO(
                    "hmo",
                    "Hermosillo",
                    "Sonora",
                    "México",
                    true
            ));

            // 3. Registrar tren activo
            tren = trenServicio.registrar(new TrenDTO(
                    "tr-viaje-01",
                    "Tren Regional Norte",
                    "Interurbano Ligero",
                    "activo",
                    5,
                    5,
                    new ArrayList<>(Arrays.asList(
                            "Aire acondicionado",
                            "Baño",
                            "WiFi"
                    ))
            ));

            System.out.println("\nDatos base creados:");
            System.out.println("Origen ID: " + origen.getId());
            System.out.println("Destino ID: " + destino.getId());
            System.out.println("Tren ID: " + tren.getId());

            // 4. Crear viaje
            ViajeDTO viajeDTO = new ViajeDTO(
                    tren.getId(),
                    tren.getCodigo(),
                    tren.getNombre(),
                    origen.getId(),
                    origen.getNombre(),
                    destino.getId(),
                    destino.getNombre(),
                    "2026-05-12T08:00:00Z",
                    "2026-05-12T11:30:00Z",
                    "programado",
                    5,
                    5,
                    3,
                    2
            );

            viajeRegistrado = viajeServicio.registrar(viajeDTO);

            System.out.println("\nViaje registrado:");
            imprimirViaje(viajeRegistrado);

            // 5. Buscar por id
            ViajeDTO encontradoPorId = viajeServicio.buscarPorId(
                    viajeRegistrado.getId()
            );

            System.out.println("\nViaje encontrado por id:");
            imprimirViaje(encontradoPorId);

            // 6. Actualizar viaje
            encontradoPorId.setEstatus("finalizado");
            encontradoPorId.setDisponibilidadGeneral(0);
            encontradoPorId.setDisponibilidadPrimeraClase(0);

            boolean actualizado = viajeServicio.actualizar(encontradoPorId);

            System.out.println("\nViaje actualizado?: " + actualizado);

            ViajeDTO despuesActualizar = viajeServicio.buscarPorId(
                    viajeRegistrado.getId()
            );

            System.out.println("\nViaje después de actualizar:");
            imprimirViaje(despuesActualizar);

            // 7. Consultar todos
            List<ViajeDTO> viajes = viajeServicio.consultarTodos();

            System.out.println("\nViajes registrados:");
            for (ViajeDTO viaje : viajes) {
                imprimirViaje(viaje);
                System.out.println("-----------------------------");
            }

            // 8. Eliminar viaje
            boolean viajeEliminado = viajeServicio.eliminar(
                    viajeRegistrado.getId()
            );

            System.out.println("\nViaje eliminado?: " + viajeEliminado);

            ViajeDTO despuesEliminar = viajeServicio.buscarPorId(
                    viajeRegistrado.getId()
            );

            System.out.println("\nBúsqueda después de eliminar viaje:");
            if (despuesEliminar == null) {
                System.out.println("El viaje ya no existe.");
            } else {
                imprimirViaje(despuesEliminar);
            }

            // 9. Limpiar datos base
            trenServicio.eliminar(tren.getId());
            locacionServicio.eliminar(origen.getId());
            locacionServicio.eliminar(destino.getId());

            System.out.println("\nDatos base eliminados correctamente.");

            System.out.println("\n=== FIN DE PRUEBA VIAJE SERVICIO ===");

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirViaje(ViajeDTO viaje) {
        if (viaje == null) {
            System.out.println("Viaje nulo.");
            return;
        }

        System.out.println("ID: " + viaje.getId());
        System.out.println("Tren ID: " + viaje.getTrenId());
        System.out.println("Tren código: " + viaje.getTrenCodigo());
        System.out.println("Tren nombre: " + viaje.getTrenNombre());
        System.out.println("Origen ID: " + viaje.getOrigenId());
        System.out.println("Origen nombre: " + viaje.getOrigenNombre());
        System.out.println("Destino ID: " + viaje.getDestinoId());
        System.out.println("Destino nombre: " + viaje.getDestinoNombre());
        System.out.println("Fecha salida: " + viaje.getFechaHoraSalida());
        System.out.println("Fecha llegada estimada: "
                + viaje.getFechaHoraLlegadaEstimada());
        System.out.println("Estatus: " + viaje.getEstatus());
        System.out.println("Capacidad máxima general: "
                + viaje.getCapacidadMaximaGeneral());
        System.out.println("Capacidad máxima primera clase: "
                + viaje.getCapacidadMaximaPrimeraClase());
        System.out.println("Disponibilidad general: "
                + viaje.getDisponibilidadGeneral());
        System.out.println("Disponibilidad primera clase: "
                + viaje.getDisponibilidadPrimeraClase());
    }
}