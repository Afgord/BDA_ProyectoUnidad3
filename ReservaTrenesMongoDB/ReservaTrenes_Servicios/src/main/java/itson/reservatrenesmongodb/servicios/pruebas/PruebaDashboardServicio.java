/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.BoletoDTO;
import itson.reservatrenesmongodb.dtos.CancelacionBoletoDTO;
import itson.reservatrenesmongodb.dtos.CompraBoletoDTO;
import itson.reservatrenesmongodb.dtos.DashboardDTO;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.BoletoServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.DashboardServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.LocacionServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.TrenServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.ViajeServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IBoletoServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IDashboardServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IViajeServicio;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Clase de prueba manual para validar DashboardServicio.
 *
 * Permite comprobar consultas agregadas usando aggregate() sobre viajes,
 * boletos y trenes.
 *
 * @author Afgord
 */
public class PruebaDashboardServicio {

    public static void main(String[] args) {
        ILocacionServicio locacionServicio = new LocacionServicio();
        ITrenServicio trenServicio = new TrenServicio();
        IViajeServicio viajeServicio = new ViajeServicio();
        IBoletoServicio boletoServicio = new BoletoServicio();
        IDashboardServicio dashboardServicio = new DashboardServicio();

        LocacionDTO origen = null;
        LocacionDTO destino = null;
        TrenDTO tren = null;
        ViajeDTO viaje = null;
        BoletoDTO boletoConfirmado = null;
        BoletoDTO boletoCancelado = null;

        try {
            System.out.println("=== PRUEBA DASHBOARD SERVICIO ===");

            origen = locacionServicio.registrar(new LocacionDTO(
                    "cob",
                    "Ciudad Obregón",
                    "Sonora",
                    "México",
                    true
            ));

            destino = locacionServicio.registrar(new LocacionDTO(
                    "hmo",
                    "Hermosillo",
                    "Sonora",
                    "México",
                    true
            ));

            tren = trenServicio.registrar(new TrenDTO(
                    "tr-dash-01",
                    "Tren Dashboard Norte",
                    "Interurbano Ligero",
                    "activo",
                    10,
                    5,
                    new ArrayList<>(Arrays.asList(
                            "Aire acondicionado",
                            "Baño",
                            "WiFi"
                    ))
            ));

            viaje = viajeServicio.registrar(new ViajeDTO(
                    tren.getId(),
                    tren.getCodigo(),
                    tren.getNombre(),
                    origen.getId(),
                    origen.getNombre(),
                    destino.getId(),
                    destino.getNombre(),
                    "2026-05-15T08:00:00Z",
                    "2026-05-15T11:30:00Z",
                    "programado",
                    10,
                    5,
                    10,
                    5
            ));

            boletoConfirmado = boletoServicio.generarBoleto(new CompraBoletoDTO(
                    viaje.getId(),
                    "general",
                    "Harry Potter Evans",
                    "masculino",
                    "1980-07-31",
                    "(644) 123-4567",
                    "harry.dashboard@email.com",
                    "4 Privet Drive",
                    "Privet",
                    "Ciudad Obregon",
                    "Sonora"
            ));

            boletoCancelado = boletoServicio.generarBoleto(new CompraBoletoDTO(
                    viaje.getId(),
                    "primera clase",
                    "Hermione Granger Evans",
                    "femenino",
                    "1979-09-19",
                    "644 765 4321",
                    "hermione.dashboard@email.com",
                    "Calle Biblioteca 123",
                    "Centro",
                    "Ciudad Obregon",
                    "Sonora"
            ));

            boletoServicio.cancelarBoleto(new CancelacionBoletoDTO(
                    boletoCancelado.getFolio()
            ));

            System.out.println("\nDatos base creados:");
            System.out.println("Tren ID: " + tren.getId());
            System.out.println("Viaje ID: " + viaje.getId());
            System.out.println("Boleto confirmado: " + boletoConfirmado.getFolio());
            System.out.println("Boleto cancelado: " + boletoCancelado.getFolio());

            DashboardDTO resumen = dashboardServicio.consultarResumen();

            System.out.println("\nResumen dashboard:");
            imprimirDashboard(resumen);

            System.out.println("\n=== FIN DE PRUEBA DASHBOARD SERVICIO ===");

            /*
             * Nota:
             * No eliminamos los datos antes de consultar porque el dashboard
             * necesita leerlos. Si quieres dejar la BD limpia, puedes descomentar
             * el bloque de limpieza después de revisar la salida.
             */

            /*
            viajeServicio.eliminar(viaje.getId());
            trenServicio.eliminar(tren.getId());
            locacionServicio.eliminar(origen.getId());
            locacionServicio.eliminar(destino.getId());
            */

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirDashboard(DashboardDTO dashboard) {
        if (dashboard == null) {
            System.out.println("Dashboard nulo.");
            return;
        }

        System.out.println("Total viajes programados: "
                + dashboard.getTotalViajesProgramados());
        System.out.println("Total boletos confirmados: "
                + dashboard.getTotalBoletosConfirmados());
        System.out.println("Total boletos cancelados: "
                + dashboard.getTotalBoletosCancelados());
        System.out.println("Total trenes activos: "
                + dashboard.getTotalTrenesActivos());
        System.out.println("Ingresos totales confirmados: $"
                + dashboard.getIngresosTotalesConfirmados());
    }
}