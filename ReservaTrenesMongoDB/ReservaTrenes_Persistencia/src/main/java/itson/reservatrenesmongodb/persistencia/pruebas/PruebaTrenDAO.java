/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.TrenDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar las operaciones CRUD de TrenDAO.
 *
 * Esta clase permite comprobar la inserción, búsqueda, actualización, listado
 * y eliminación de trenes en MongoDB.
 *
 * @author Afgord
 */
public class PruebaTrenDAO {

    /**
     * Método principal de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        ITrenDAO trenDAO = new TrenDAO();

        try {
            System.out.println("=== PRUEBA TREN DAO ===");

            // 1. Insertar tren
            Tren tren = new Tren();

            tren.setCodigo("TR-NORTE-01");
            tren.setNombre("Tren Regional Norte");
            tren.setModelo("Interurbano Ligero");
            tren.setEstatus(EstatusTren.ACTIVO);
            tren.setCapacidad(new Capacidad(5, 5));
            tren.setServicios(new ArrayList<>(Arrays.asList(
                    "Aire acondicionado",
                    "Bano",
                    "WiFi"
            )));

            Tren insertado = trenDAO.insertar(tren);

            System.out.println("\nTren insertado:");
            imprimirTren(insertado);

            // 2. Buscar por id
            Tren encontrado = trenDAO.buscarPorId(insertado.getId());

            System.out.println("\nTren encontrado por id:");
            imprimirTren(encontrado);

            // 3. Actualizar tren
            encontrado.setNombre("Tren Regional Norte Actualizado");
            encontrado.setModelo("Interurbano Ligero 2026");
            encontrado.setEstatus(EstatusTren.MANTENIMIENTO);
            encontrado.setCapacidad(new Capacidad(8, 6));
            encontrado.setServicios(new ArrayList<>(Arrays.asList(
                    "Aire acondicionado",
                    "Bano",
                    "WiFi",
                    "Cafeteria"
            )));

            boolean actualizado = trenDAO.actualizar(encontrado);

            System.out.println("\nTren actualizado?: " + actualizado);

            Tren despuesActualizar = trenDAO.buscarPorId(encontrado.getId());

            System.out.println("\nTren despues de actualizar:");
            imprimirTren(despuesActualizar);

            // 4. Listar todos
            List<Tren> trenes = trenDAO.buscarTodos();

            System.out.println("\nTrenes registrados:");
            for (Tren t : trenes) {
                imprimirTren(t);
                System.out.println("-----------------------------");
            }

            // 5. Eliminar tren de prueba
            boolean eliminado = trenDAO.eliminar(insertado.getId());

            System.out.println("\nTren eliminado?: " + eliminado);

            Tren despuesEliminar = trenDAO.buscarPorId(insertado.getId());

            System.out.println("\nBusqueda despues de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("El tren ya no existe.");
            } else {
                imprimirTren(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA TREN DAO ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Imprime los datos de un tren en consola.
     *
     * @param tren Tren a imprimir.
     */
    private static void imprimirTren(Tren tren) {
        if (tren == null) {
            System.out.println("Tren nulo.");
            return;
        }

        System.out.println("ID: " + tren.getId());
        System.out.println("Codigo: " + tren.getCodigo());
        System.out.println("Nombre: " + tren.getNombre());
        System.out.println("Modelo: " + tren.getModelo());
        System.out.println("Estatus: " + tren.getEstatus());

        if (tren.getCapacidad() != null) {
            System.out.println("Capacidad general: "
                    + tren.getCapacidad().getGeneral());
            System.out.println("Capacidad primera clase: "
                    + tren.getCapacidad().getPrimeraClase());
        } else {
            System.out.println("Capacidad: null");
        }

        System.out.println("Servicios: " + tren.getServicios());
    }
}