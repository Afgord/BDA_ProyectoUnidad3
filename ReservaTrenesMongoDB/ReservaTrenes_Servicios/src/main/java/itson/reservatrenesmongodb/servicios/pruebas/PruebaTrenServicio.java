/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.TrenServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar TrenServicio.
 *
 * Permite comprobar el flujo:
 * TrenDTO -> TrenServicio -> TrenDAO -> MongoDB.
 *
 * @author Afgord
 */
public class PruebaTrenServicio {

    public static void main(String[] args) {
        ITrenServicio trenServicio = new TrenServicio();

        try {
            System.out.println("=== PRUEBA TREN SERVICIO ===");

            TrenDTO trenDTO = new TrenDTO(
                    "tr-norte-01",
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
            );

            TrenDTO registrado = trenServicio.registrar(trenDTO);

            System.out.println("\nTren registrado:");
            imprimirTren(registrado);

            TrenDTO encontradoPorId = trenServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nTren encontrado por id:");
            imprimirTren(encontradoPorId);

            TrenDTO encontradoPorCodigo = trenServicio.buscarPorCodigo(
                    "tr-norte-01"
            );

            System.out.println("\nTren encontrado por código:");
            imprimirTren(encontradoPorCodigo);

            encontradoPorId.setNombre("Tren Regional Norte Actualizado");
            encontradoPorId.setModelo("Interurbano Ligero 2026");
            encontradoPorId.setEstatus("mantenimiento");
            encontradoPorId.setCapacidadGeneral(8);
            encontradoPorId.setCapacidadPrimeraClase(6);
            encontradoPorId.setServicios(new ArrayList<>(Arrays.asList(
                    "Aire acondicionado",
                    "Baño",
                    "WiFi",
                    "Cafetería"
            )));

            boolean actualizado = trenServicio.actualizar(encontradoPorId);

            System.out.println("\nTren actualizado?: " + actualizado);

            TrenDTO despuesActualizar = trenServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nTren después de actualizar:");
            imprimirTren(despuesActualizar);

            List<TrenDTO> trenes = trenServicio.consultarTodos();

            System.out.println("\nTrenes registrados:");
            for (TrenDTO tren : trenes) {
                imprimirTren(tren);
                System.out.println("-----------------------------");
            }

            boolean eliminado = trenServicio.eliminar(registrado.getId());

            System.out.println("\nTren eliminado?: " + eliminado);

            TrenDTO despuesEliminar = trenServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nBúsqueda después de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El tren ya no existe.");
            } else {
                imprimirTren(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA TREN SERVICIO ===");

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirTren(TrenDTO tren) {
        if (tren == null) {
            System.out.println("Tren nulo.");
            return;
        }

        System.out.println("ID: " + tren.getId());
        System.out.println("Código: " + tren.getCodigo());
        System.out.println("Nombre: " + tren.getNombre());
        System.out.println("Modelo: " + tren.getModelo());
        System.out.println("Estatus: " + tren.getEstatus());
        System.out.println("Capacidad general: " + tren.getCapacidadGeneral());
        System.out.println("Capacidad primera clase: "
                + tren.getCapacidadPrimeraClase());
        System.out.println("Servicios: " + tren.getServicios());
    }
}