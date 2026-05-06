/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.LocacionDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ILocacionDAO;
import java.util.List;

/**
 * Clase de prueba manual para validar las operaciones CRUD de LocacionDAO.
 *
 * Esta clase permite comprobar la conexión con MongoDB y las operaciones
 * básicas de inserción, búsqueda, actualización, listado y eliminación.
 *
 * @author Afgord
 */
public class PruebaLocacionDAO {

    /**
     * Método principal de prueba.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        ILocacionDAO locacionDAO = new LocacionDAO();

        try {
            System.out.println("=== PRUEBA LOCACION DAO ===");

            // 1. Insertar locación
            Locacion locacion = new Locacion();
            locacion.setClave("COB");
            locacion.setNombre("Ciudad Obregón");
            locacion.setEstado("Sonora");
            locacion.setPais("México");
            locacion.setActiva(true);

            Locacion insertada = locacionDAO.insertar(locacion);

            System.out.println("\nLocación insertada:");
            imprimirLocacion(insertada);

            // 2. Buscar por id
            Locacion encontrada = locacionDAO.buscarPorId(insertada.getId());

            System.out.println("\nLocación encontrada por id:");
            imprimirLocacion(encontrada);

            // 3. Actualizar locación
            encontrada.setNombre("Ciudad Obregón Centro");
            encontrada.setActiva(true);

            boolean actualizada = locacionDAO.actualizar(encontrada);

            System.out.println("\n¿Locación actualizada?: " + actualizada);

            Locacion despuesActualizar = locacionDAO.buscarPorId(encontrada.getId());

            System.out.println("\nLocación después de actualizar:");
            imprimirLocacion(despuesActualizar);

            // 4. Listar todas
            List<Locacion> locaciones = locacionDAO.buscarTodos();

            System.out.println("\nLocaciones registradas:");
            for (Locacion l : locaciones) {
                imprimirLocacion(l);
                System.out.println("-----------------------------");
            }

            // 5. Eliminar locación de prueba
            boolean eliminada = locacionDAO.eliminar(insertada.getId());

            System.out.println("\n¿Locación eliminada?: " + eliminada);

            Locacion despuesEliminar = locacionDAO.buscarPorId(insertada.getId());

            System.out.println("\nBúsqueda después de eliminar:");
            if (despuesEliminar == null) {
                System.out.println("La locación ya no existe.");
            } else {
                imprimirLocacion(despuesEliminar);
            }

            System.out.println("\n=== FIN DE PRUEBA LOCACION DAO ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Imprime los datos de una locación en consola.
     *
     * @param locacion Locación a imprimir.
     */
    private static void imprimirLocacion(Locacion locacion) {
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
