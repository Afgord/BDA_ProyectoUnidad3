/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Contacto;
import itson.reservatrenesmongodb.dominio.Direccion;
import itson.reservatrenesmongodb.dominio.HistorialViaje;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.dominio.enums.Sexo;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.PasajeroDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar las operaciones CRUD de PasajeroDAO.
 *
 * Esta clase permite comprobar la inserción, búsqueda, actualización, listado
 * y eliminación de pasajeros en MongoDB, incluyendo documentos embebidos y
 * arreglos.
 *
 * @author Afgord
 */
public class PruebaPasajeroDAO {

    /**
     * Método principal de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        IPasajeroDAO pasajeroDAO = new PasajeroDAO();

        try {
            System.out.println("=== PRUEBA PASAJERO DAO ===");

            // 1. Insertar pasajero
            Pasajero pasajero = new Pasajero();

            pasajero.setNombreCompleto("Harry Potter Evans");
            pasajero.setSexo(Sexo.MASCULINO);
            pasajero.setFechaNacimiento(Instant.parse("1980-07-31T00:00:00Z"));

            pasajero.setContacto(new Contacto(
                    "4154151",
                    "harry.evans@email.com"
            ));

            pasajero.setDireccion(new Direccion(
                    "4 Privet Drive",
                    "Privet",
                    "Ciudad Obregon",
                    "Sonora"
            ));

            pasajero.setHistorialViajes(new ArrayList<>());

            Pasajero insertado = pasajeroDAO.insertar(pasajero);

            System.out.println("\nPasajero insertado:");
            imprimirPasajero(insertado);

            // 2. Buscar por id
            Pasajero encontradoPorId = pasajeroDAO.buscarPorId(insertado.getId());

            System.out.println("\nPasajero encontrado por id:");
            imprimirPasajero(encontradoPorId);

            // 3. Buscar por correo
            Pasajero encontradoPorCorreo = pasajeroDAO.buscarPorCorreo(
                    "harry.evans@email.com"
            );

            System.out.println("\nPasajero encontrado por correo:");
            imprimirPasajero(encontradoPorCorreo);

            // 4. Actualizar pasajero
            encontradoPorId.setNombreCompleto("Harry James Potter Evans");
            encontradoPorId.setContacto(new Contacto(
                    "6441234567",
                    "harry.potter@email.com"
            ));

            encontradoPorId.setDireccion(new Direccion(
                    "Anden 9 3/4",
                    "King Cross",
                    "Ciudad Obregon",
                    "Sonora"
            ));

            encontradoPorId.setHistorialViajes(new ArrayList<>(Arrays.asList(
                    new HistorialViaje(
                            "662f1e000000000000000001",
                            "662f1c000000000000000001",
                            "Hermosillo"
                    )
            )));

            boolean actualizado = pasajeroDAO.actualizar(encontradoPorId);

            System.out.println("\nPasajero actualizado?: " + actualizado);

            Pasajero despuesActualizar = pasajeroDAO.buscarPorId(encontradoPorId.getId());

            System.out.println("\nPasajero despues de actualizar:");
            imprimirPasajero(despuesActualizar);

            // 5. Listar todos
            List<Pasajero> pasajeros = pasajeroDAO.buscarTodos();

            System.out.println("\nPasajeros registrados:");
            for (Pasajero p : pasajeros) {
                imprimirPasajero(p);
                System.out.println("-----------------------------");
            }

            // 6. Eliminar pasajero de prueba
            boolean eliminado = pasajeroDAO.eliminar(insertado.getId());

            System.out.println("\nPasajero eliminado?: " + eliminado);

            Pasajero despuesEliminar = pasajeroDAO.buscarPorId(insertado.getId());

            System.out.println("\nBusqueda despues de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El pasajero ya no existe.");
            } else {
                imprimirPasajero(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA PASAJERO DAO ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Imprime los datos de un pasajero en consola.
     *
     * @param pasajero Pasajero a imprimir.
     */
    private static void imprimirPasajero(Pasajero pasajero) {
        if (pasajero == null) {
            System.out.println("Pasajero nulo.");
            return;
        }

        System.out.println("ID: " + pasajero.getId());
        System.out.println("Nombre completo: " + pasajero.getNombreCompleto());
        System.out.println("Sexo: " + pasajero.getSexo());
        System.out.println("Fecha nacimiento: " + pasajero.getFechaNacimiento());

        if (pasajero.getContacto() != null) {
            System.out.println("Telefono: " + pasajero.getContacto().getTelefono());
            System.out.println("Correo: " + pasajero.getContacto().getCorreo());
        } else {
            System.out.println("Contacto: null");
        }

        if (pasajero.getDireccion() != null) {
            System.out.println("Calle: " + pasajero.getDireccion().getCalle());
            System.out.println("Colonia: " + pasajero.getDireccion().getColonia());
            System.out.println("Ciudad: " + pasajero.getDireccion().getCiudad());
            System.out.println("Estado: " + pasajero.getDireccion().getEstado());
        } else {
            System.out.println("Direccion: null");
        }

        if (pasajero.getHistorialViajes() != null) {
            System.out.println("Historial viajes:");
            if (pasajero.getHistorialViajes().isEmpty()) {
                System.out.println("  Sin viajes registrados.");
            } else {
                for (HistorialViaje h : pasajero.getHistorialViajes()) {
                    System.out.println("  Boleto ID: " + h.getBoletoId());
                    System.out.println("  Viaje ID: " + h.getViajeId());
                    System.out.println("  Destino: " + h.getDestino());
                }
            }
        } else {
            System.out.println("Historial viajes: null");
        }
    }
}