/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.LocacionServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import java.util.List;

/**
 * Clase de prueba manual para validar LocacionServicio.
 *
 * Permite comprobar el flujo:
 * LocacionDTO -> LocacionServicio -> LocacionDAO -> MongoDB.
 *
 * @author Afgord
 */
public class PruebaLocacionServicio {

    public static void main(String[] args) {
        ILocacionServicio locacionServicio = new LocacionServicio();

        try {
            System.out.println("=== PRUEBA LOCACION SERVICIO ===");

            LocacionDTO locacionDTO = new LocacionDTO(
                    "cob",
                    "Ciudad Obregón",
                    "Sonora",
                    "México",
                    true
            );

            LocacionDTO registrada = locacionServicio.registrar(locacionDTO);

            System.out.println("\nLocación registrada:");
            imprimirLocacion(registrada);

            LocacionDTO encontradaPorId = locacionServicio.buscarPorId(
                    registrada.getId()
            );

            System.out.println("\nLocación encontrada por id:");
            imprimirLocacion(encontradaPorId);

            LocacionDTO encontradaPorClave = locacionServicio.buscarPorClave(
                    "cob"
            );

            System.out.println("\nLocación encontrada por clave:");
            imprimirLocacion(encontradaPorClave);

            encontradaPorId.setNombre("Ciudad Obregón Centro");
            encontradaPorId.setEstado("Sonora");
            encontradaPorId.setPais("México");
            encontradaPorId.setActiva(true);

            boolean actualizada = locacionServicio.actualizar(encontradaPorId);

            System.out.println("\nLocación actualizada?: " + actualizada);

            LocacionDTO despuesActualizar = locacionServicio.buscarPorId(
                    registrada.getId()
            );

            System.out.println("\nLocación después de actualizar:");
            imprimirLocacion(despuesActualizar);

            List<LocacionDTO> locaciones = locacionServicio.consultarTodas();

            System.out.println("\nLocaciones registradas:");
            for (LocacionDTO locacion : locaciones) {
                imprimirLocacion(locacion);
                System.out.println("-----------------------------");
            }

            boolean eliminada = locacionServicio.eliminar(registrada.getId());

            System.out.println("\nLocación eliminada?: " + eliminada);

            LocacionDTO despuesEliminar = locacionServicio.buscarPorId(
                    registrada.getId()
            );

            System.out.println("\nBúsqueda después de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("La locación ya no existe.");
            } else {
                imprimirLocacion(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA LOCACION SERVICIO ===");

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirLocacion(LocacionDTO locacion) {
        if (locacion == null) {
            System.out.println("Locación nula.");
            return;
        }

        System.out.println("ID: " + locacion.getId());
        System.out.println("Clave: " + locacion.getClave());
        System.out.println("Nombre: " + locacion.getNombre());
        System.out.println("Estado: " + locacion.getEstado());
        System.out.println("País: " + locacion.getPais());
        System.out.println("Activa: " + locacion.isActiva());
    }
}