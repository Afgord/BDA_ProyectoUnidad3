/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.PasajeroDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.PasajeroServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IPasajeroServicio;
import java.util.List;

/**
 * Clase de prueba manual para validar PasajeroServicio.
 *
 * Permite comprobar el flujo:
 * PasajeroDTO -> PasajeroServicio -> PasajeroDAO -> MongoDB.
 *
 * @author Afgord
 */
public class PruebaPasajeroServicio {

    public static void main(String[] args) {
        IPasajeroServicio pasajeroServicio = new PasajeroServicio();

        try {
            System.out.println("=== PRUEBA PASAJERO SERVICIO ===");

            PasajeroDTO pasajeroDTO = new PasajeroDTO(
                    "Harry Potter Evans",
                    "masculino",
                    "1980-07-31",
                    "4154151",
                    "HARRY.EVANS@EMAIL.COM",
                    "4 Privet Drive",
                    "Privet",
                    "Ciudad Obregon",
                    "Sonora"
            );

            PasajeroDTO registrado = pasajeroServicio.registrar(pasajeroDTO);

            System.out.println("\nPasajero registrado:");
            imprimirPasajero(registrado);

            PasajeroDTO encontradoPorId = pasajeroServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nPasajero encontrado por id:");
            imprimirPasajero(encontradoPorId);

            PasajeroDTO encontradoPorCorreo = pasajeroServicio.buscarPorCorreo(
                    "HARRY.EVANS@EMAIL.COM"
            );

            System.out.println("\nPasajero encontrado por correo:");
            imprimirPasajero(encontradoPorCorreo);

            encontradoPorId.setNombreCompleto("Harry James Potter Evans");
            encontradoPorId.setTelefono("6441234567");
            encontradoPorId.setCorreo("harry.potter@email.com");
            encontradoPorId.setCalle("Anden 9 3/4");
            encontradoPorId.setColonia("King Cross");
            encontradoPorId.setCiudad("Ciudad Obregon");
            encontradoPorId.setEstado("Sonora");

            boolean actualizado = pasajeroServicio.actualizar(encontradoPorId);

            System.out.println("\nPasajero actualizado?: " + actualizado);

            PasajeroDTO despuesActualizar = pasajeroServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nPasajero después de actualizar:");
            imprimirPasajero(despuesActualizar);

            List<PasajeroDTO> pasajeros = pasajeroServicio.consultarTodos();

            System.out.println("\nPasajeros registrados:");
            for (PasajeroDTO pasajero : pasajeros) {
                imprimirPasajero(pasajero);
                System.out.println("-----------------------------");
            }

            boolean eliminado = pasajeroServicio.eliminar(registrado.getId());

            System.out.println("\nPasajero eliminado?: " + eliminado);

            PasajeroDTO despuesEliminar = pasajeroServicio.buscarPorId(
                    registrado.getId()
            );

            System.out.println("\nBúsqueda después de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El pasajero ya no existe.");
            } else {
                imprimirPasajero(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA PASAJERO SERVICIO ===");

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirPasajero(PasajeroDTO pasajero) {
        if (pasajero == null) {
            System.out.println("Pasajero nulo.");
            return;
        }

        System.out.println("ID: " + pasajero.getId());
        System.out.println("Nombre completo: " + pasajero.getNombreCompleto());
        System.out.println("Sexo: " + pasajero.getSexo());
        System.out.println("Fecha nacimiento: " + pasajero.getFechaNacimiento());
        System.out.println("Teléfono: " + pasajero.getTelefono());
        System.out.println("Correo: " + pasajero.getCorreo());
        System.out.println("Calle: " + pasajero.getCalle());
        System.out.println("Colonia: " + pasajero.getColonia());
        System.out.println("Ciudad: " + pasajero.getCiudad());
        System.out.println("Estado: " + pasajero.getEstado());
        System.out.println("Boletos comprados: " + pasajero.getBoletosComprados());
    }
}
