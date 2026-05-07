/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.BoletoDTO;
import itson.reservatrenesmongodb.dtos.CancelacionBoletoDTO;
import itson.reservatrenesmongodb.dtos.CompraBoletoDTO;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.BoletoServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.LocacionServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.TrenServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.ViajeServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IBoletoServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IViajeServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar BoletoServicio.
 *
 * Permite comprobar el flujo:
 * CompraBoletoDTO -> BoletoServicio -> PasajeroDAO / ViajeDAO / BoletoDAO -> MongoDB.
 *
 * También crea locaciones, tren y viaje necesarios para validar la generación
 * y cancelación de boletos.
 *
 * @author Afgord
 */
public class PruebaBoletoServicio {

    public static void main(String[] args) {
        ILocacionServicio locacionServicio = new LocacionServicio();
        ITrenServicio trenServicio = new TrenServicio();
        IViajeServicio viajeServicio = new ViajeServicio();
        IBoletoServicio boletoServicio = new BoletoServicio();

        LocacionDTO origen = null;
        LocacionDTO destino = null;
        TrenDTO tren = null;
        ViajeDTO viaje = null;
        BoletoDTO boletoGenerado = null;

        try {
            System.out.println("=== PRUEBA BOLETO SERVICIO ===");

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
                    "tr-boleto-01",
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

            // 4. Registrar viaje programado
            viaje = viajeServicio.registrar(new ViajeDTO(
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
            ));

            System.out.println("\nDatos base creados:");
            System.out.println("Origen ID: " + origen.getId());
            System.out.println("Destino ID: " + destino.getId());
            System.out.println("Tren ID: " + tren.getId());
            System.out.println("Viaje ID: " + viaje.getId());

            // 5. Generar boleto
            CompraBoletoDTO compraDTO = new CompraBoletoDTO(
                    viaje.getId(),
                    "primera clase",
                    "Harry Potter Evans",
                    "masculino",
                    "1980-07-31",
                    "(644) 123-4567",
                    "HARRY.EVANS@EMAIL.COM",
                    "4 Privet Drive",
                    "Privet",
                    "Ciudad Obregon",
                    "Sonora"
            );

            boletoGenerado = boletoServicio.generarBoleto(compraDTO);

            System.out.println("\nBoleto generado:");
            imprimirBoleto(boletoGenerado);

            // 6. Buscar boleto por folio
            BoletoDTO encontradoPorFolio = boletoServicio.buscarPorFolio(
                    boletoGenerado.getFolio()
            );

            System.out.println("\nBoleto encontrado por folio:");
            imprimirBoleto(encontradoPorFolio);

            // 7. Consultar boletos por pasajero
            List<BoletoDTO> boletosPasajero = boletoServicio.consultarPorPasajero(
                    boletoGenerado.getPasajeroId()
            );

            System.out.println("\nBoletos encontrados por pasajero:");
            for (BoletoDTO boleto : boletosPasajero) {
                imprimirBoleto(boleto);
                System.out.println("-----------------------------");
            }

            // 8. Consultar boletos por viaje
            List<BoletoDTO> boletosViaje = boletoServicio.consultarPorViaje(
                    boletoGenerado.getViajeId()
            );

            System.out.println("\nBoletos encontrados por viaje:");
            for (BoletoDTO boleto : boletosViaje) {
                imprimirBoleto(boleto);
                System.out.println("-----------------------------");
            }

            // 9. Revisar disponibilidad después de compra
            ViajeDTO viajeDespuesCompra = viajeServicio.buscarPorId(viaje.getId());

            System.out.println("\nViaje después de generar boleto:");
            imprimirViaje(viajeDespuesCompra);

            // 10. Cancelar boleto
            BoletoDTO boletoCancelado = boletoServicio.cancelarBoleto(
                    new CancelacionBoletoDTO(boletoGenerado.getFolio())
            );

            System.out.println("\nBoleto cancelado:");
            imprimirBoleto(boletoCancelado);

            // 11. Revisar disponibilidad después de cancelación
            ViajeDTO viajeDespuesCancelacion = viajeServicio.buscarPorId(viaje.getId());

            System.out.println("\nViaje después de cancelar boleto:");
            imprimirViaje(viajeDespuesCancelacion);

            // 12. Limpiar datos base
            viajeServicio.eliminar(viaje.getId());
            trenServicio.eliminar(tren.getId());
            locacionServicio.eliminar(origen.getId());
            locacionServicio.eliminar(destino.getId());

            System.out.println("\nDatos base eliminados correctamente.");

            System.out.println("\n=== FIN DE PRUEBA BOLETO SERVICIO ===");

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    private static void imprimirBoleto(BoletoDTO boleto) {
        if (boleto == null) {
            System.out.println("Boleto nulo.");
            return;
        }

        System.out.println("ID: " + boleto.getId());
        System.out.println("Folio: " + boleto.getFolio());
        System.out.println("Pasajero ID: " + boleto.getPasajeroId());
        System.out.println("Pasajero nombre: " + boleto.getPasajeroNombre());
        System.out.println("Pasajero teléfono: " + boleto.getPasajeroTelefono());
        System.out.println("Viaje ID: " + boleto.getViajeId());
        System.out.println("Origen: " + boleto.getOrigen());
        System.out.println("Destino: " + boleto.getDestino());
        System.out.println("Tren: " + boleto.getTren());
        System.out.println("Fecha salida: " + boleto.getFechaHoraSalida());
        System.out.println("Tipo boleto: " + boleto.getTipoBoleto());
        System.out.println("Precio: " + boleto.getPrecio());
        System.out.println("Estatus: " + boleto.getEstatus());
        System.out.println("Fecha reservación: " + boleto.getFechaReservacion());
        System.out.println("Fecha cancelación: " + boleto.getFechaCancelacion());
    }

    private static void imprimirViaje(ViajeDTO viaje) {
        if (viaje == null) {
            System.out.println("Viaje nulo.");
            return;
        }

        System.out.println("ID: " + viaje.getId());
        System.out.println("Tren: " + viaje.getTrenNombre());
        System.out.println("Origen: " + viaje.getOrigenNombre());
        System.out.println("Destino: " + viaje.getDestinoNombre());
        System.out.println("Fecha salida: " + viaje.getFechaHoraSalida());
        System.out.println("Estatus: " + viaje.getEstatus());
        System.out.println("Disponibilidad general: "
                + viaje.getDisponibilidadGeneral());
        System.out.println("Disponibilidad primera clase: "
                + viaje.getDisponibilidadPrimeraClase());
    }
}
