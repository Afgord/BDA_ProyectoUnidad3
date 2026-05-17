/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.pruebas;

import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dtos.BoletoDTO;
import itson.reservatrenesmongodb.dtos.CompraBoletoDTO;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.dtos.PasajeroDTO;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.servicios.implementaciones.BoletoServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.LocacionServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.PasajeroServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.TrenServicio;
import itson.reservatrenesmongodb.servicios.implementaciones.ViajeServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IBoletoServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IPasajeroServicio;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import itson.reservatrenesmongodb.servicios.interfaces.IViajeServicio;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba manual para validar BoletoServicio y su integración con
 * Pasajeros e Historial de viajes.
 *
 * Permite comprobar: - La generación de dos boletos para un mismo pasajero en
 * viajes distintos. - La reutilización del pasajero existente a partir del
 * correo. - La actualización automática del historial del pasajero. - El
 * incremento de viajesRegistrados en PasajeroDTO. - La consulta de boletos
 * asociados al pasajero.
 *
 * Los datos generados no se eliminan al final para poder validarlos
 * posteriormente desde frmPasajeros y dlgHistorialPasajero.
 *
 * @author Afgord
 */
public class PruebaBoletoServicio {

    private static final String CORREO_PASAJERO = "harry.evans@email.com";

    public static void main(String[] args) {
        ILocacionServicio locacionServicio = new LocacionServicio();
        ITrenServicio trenServicio = new TrenServicio();
        IViajeServicio viajeServicio = new ViajeServicio();
        IBoletoServicio boletoServicio = new BoletoServicio();
        IPasajeroServicio pasajeroServicio = new PasajeroServicio();

        try {
            System.out.println("=== PRUEBA BOLETO SERVICIO - HISTORIAL PASAJERO ===");

            /*
             * 1. Obtener o registrar locaciones necesarias.
             */
            LocacionDTO obregon = obtenerORegistrarLocacion(
                    locacionServicio,
                    "COB",
                    "Ciudad Obregón",
                    "Sonora",
                    "México"
            );

            LocacionDTO hermosillo = obtenerORegistrarLocacion(
                    locacionServicio,
                    "HMO",
                    "Hermosillo",
                    "Sonora",
                    "México"
            );

            LocacionDTO guaymas = obtenerORegistrarLocacion(
                    locacionServicio,
                    "GYM",
                    "Guaymas",
                    "Sonora",
                    "México"
            );

            /*
             * 2. Obtener o registrar tren de prueba.
             */
            TrenDTO tren = obtenerORegistrarTren(
                    trenServicio,
                    "TR-HIST-01",
                    "Tren Historial de Pasajeros",
                    "THP-2026",
                    40,
                    10,
                    new ArrayList<>(Arrays.asList(
                            "WiFi",
                            "Aire acondicionado",
                            "Baño"
                    ))
            );

            /*
             * 3. Crear dos viajes programados futuros.
             */
            Instant salidaViaje1 = Instant.now()
                    .plus(7, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Instant llegadaViaje1 = salidaViaje1
                    .plus(3, ChronoUnit.HOURS)
                    .plus(30, ChronoUnit.MINUTES);

            Instant salidaViaje2 = Instant.now()
                    .plus(8, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Instant llegadaViaje2 = salidaViaje2
                    .plus(2, ChronoUnit.HOURS)
                    .plus(45, ChronoUnit.MINUTES);

            ViajeDTO viaje1 = viajeServicio.registrar(new ViajeDTO(
                    tren.getId(),
                    tren.getCodigo(),
                    tren.getNombre(),
                    obregon.getId(),
                    obregon.getNombre(),
                    hermosillo.getId(),
                    hermosillo.getNombre(),
                    salidaViaje1.toString(),
                    llegadaViaje1.toString(),
                    "PROGRAMADO",
                    tren.getCapacidadGeneral(),
                    tren.getCapacidadPrimeraClase(),
                    tren.getCapacidadGeneral(),
                    tren.getCapacidadPrimeraClase()
            ));

            ViajeDTO viaje2 = viajeServicio.registrar(new ViajeDTO(
                    tren.getId(),
                    tren.getCodigo(),
                    tren.getNombre(),
                    obregon.getId(),
                    obregon.getNombre(),
                    guaymas.getId(),
                    guaymas.getNombre(),
                    salidaViaje2.toString(),
                    llegadaViaje2.toString(),
                    "PROGRAMADO",
                    tren.getCapacidadGeneral(),
                    tren.getCapacidadPrimeraClase(),
                    tren.getCapacidadGeneral(),
                    tren.getCapacidadPrimeraClase()
            ));

            System.out.println("\nViajes de prueba registrados:");
            imprimirViaje(viaje1);
            System.out.println("-----------------------------");
            imprimirViaje(viaje2);

            /*
             * 4. Consultar al pasajero antes de generar boletos.
             * 
             * Si previamente se ejecutó CargaDatosInicialesDAO, Harry ya
             * existirá con 1 viaje registrado.
             */
            PasajeroDTO pasajeroAntes = pasajeroServicio.buscarPorCorreo(
                    CORREO_PASAJERO
            );

            System.out.println("\nEstado del pasajero antes de generar boletos:");
            imprimirPasajeroResumen(pasajeroAntes);

            /*
             * 5. Generar boleto 1 para Harry en el viaje 1.
             */
            CompraBoletoDTO compra1 = crearCompraHarry(
                    viaje1.getId(),
                    "GENERAL"
            );

            BoletoDTO boleto1 = boletoServicio.generarBoleto(compra1);

            System.out.println("\nBoleto 1 generado:");
            imprimirBoleto(boleto1);

            /*
             * 6. Generar boleto 2 para el mismo Harry en el viaje 2.
             *
             * El servicio debe reutilizar al pasajero por correo, no crear
             * otro pasajero nuevo.
             */
            CompraBoletoDTO compra2 = crearCompraHarry(
                    viaje2.getId(),
                    "PRIMERA_CLASE"
            );

            BoletoDTO boleto2 = boletoServicio.generarBoleto(compra2);

            System.out.println("\nBoleto 2 generado:");
            imprimirBoleto(boleto2);

            /*
             * 7. Consultar nuevamente al pasajero por correo.
             *
             * Aquí se valida que viajesRegistrados haya aumentado.
             */
            PasajeroDTO pasajeroDespues = pasajeroServicio.buscarPorCorreo(
                    CORREO_PASAJERO
            );

            System.out.println("\nEstado del pasajero después de generar boletos:");
            imprimirPasajeroResumen(pasajeroDespues);

            /*
             * 8. Consultar boletos por pasajero.
             *
             * Esta consulta alimentará posteriormente la ventana
             * dlgHistorialPasajero.
             */
            List<BoletoDTO> boletosPasajero
                    = boletoServicio.consultarPorPasajero(
                            pasajeroDespues.getId()
                    );

            System.out.println("\nBoletos encontrados para el pasajero:");
            for (BoletoDTO boleto : boletosPasajero) {
                imprimirBoleto(boleto);
                System.out.println("-----------------------------");
            }

            /*
             * 9. Revisar disponibilidad de los viajes después de la compra.
             */
            ViajeDTO viaje1DespuesCompra = viajeServicio.buscarPorId(
                    viaje1.getId()
            );

            ViajeDTO viaje2DespuesCompra = viajeServicio.buscarPorId(
                    viaje2.getId()
            );

            System.out.println("\nViaje 1 después de generar boleto:");
            imprimirViaje(viaje1DespuesCompra);

            System.out.println("\nViaje 2 después de generar boleto:");
            imprimirViaje(viaje2DespuesCompra);

            System.out.println("\n=== RESULTADO ESPERADO EN INTERFAZ ===");
            System.out.println(
                    "- Abrir frmPasajeros."
            );
            System.out.println(
                    "- Buscar a Harry Potter Evans."
            );
            System.out.println(
                    "- Verificar que 'Viajes registrados' haya aumentado."
            );
            System.out.println(
                    "- Abrir 'Ver Historial' y comprobar que aparecen "
                    + "los boletos/viajes asociados."
            );

            System.out.println(
                    "\nIMPORTANTE: Los datos no fueron eliminados para poder "
                    + "revisarlos desde la interfaz gráfica."
            );

            System.out.println(
                    "\n=== FIN DE PRUEBA BOLETO SERVICIO - HISTORIAL PASAJERO ==="
            );

        } catch (ServicioException e) {
            System.out.println("Error de servicio: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Obtiene una locación por clave o la registra si aún no existe.
     *
     * @param locacionServicio Servicio de locaciones.
     * @param clave Clave de la locación.
     * @param nombre Nombre de la locación.
     * @param estado Estado.
     * @param pais País.
     * @return Locación existente o registrada.
     * @throws ServicioException Si ocurre un error durante la operación.
     */
    private static LocacionDTO obtenerORegistrarLocacion(
            ILocacionServicio locacionServicio,
            String clave,
            String nombre,
            String estado,
            String pais) throws ServicioException {

        LocacionDTO existente = locacionServicio.buscarPorClave(clave);

        if (existente != null) {
            return existente;
        }

        return locacionServicio.registrar(new LocacionDTO(
                clave,
                nombre,
                estado,
                pais,
                true
        ));
    }

    /**
     * Obtiene un tren por código o lo registra si aún no existe.
     *
     * @param trenServicio Servicio de trenes.
     * @param codigo Código del tren.
     * @param nombre Nombre del tren.
     * @param modelo Modelo del tren.
     * @param capacidadGeneral Capacidad general.
     * @param capacidadPrimeraClase Capacidad de primera clase.
     * @param servicios Servicios disponibles.
     * @return Tren existente o registrado.
     * @throws ServicioException Si ocurre un error durante la operación.
     */
    private static TrenDTO obtenerORegistrarTren(
            ITrenServicio trenServicio,
            String codigo,
            String nombre,
            String modelo,
            int capacidadGeneral,
            int capacidadPrimeraClase,
            List<String> servicios) throws ServicioException {

        TrenDTO existente = trenServicio.buscarPorCodigo(codigo);

        if (existente != null) {
            return existente;
        }

        return trenServicio.registrar(new TrenDTO(
                codigo,
                nombre,
                modelo,
                "ACTIVO",
                capacidadGeneral,
                capacidadPrimeraClase,
                servicios
        ));
    }

    /**
     * Construye una compra para el pasajero Harry Potter Evans.
     *
     * Se usa el mismo correo en las dos compras para comprobar que el servicio
     * reutiliza al pasajero existente.
     *
     * @param viajeId Identificador del viaje.
     * @param tipoBoleto Tipo de boleto a generar.
     * @return DTO de compra.
     */
    private static CompraBoletoDTO crearCompraHarry(
            String viajeId,
            String tipoBoleto) {

        return new CompraBoletoDTO(
                viajeId,
                tipoBoleto,
                "Harry Potter Evans",
                "MASCULINO",
                "1980-07-31",
                "6441234567",
                CORREO_PASAJERO,
                "4 Privet Drive",
                "Privet",
                "Ciudad Obregón",
                "Sonora"
        );
    }

    /**
     * Imprime un resumen del pasajero.
     *
     * @param pasajero Pasajero a imprimir.
     */
    private static void imprimirPasajeroResumen(PasajeroDTO pasajero) {
        if (pasajero == null) {
            System.out.println("El pasajero aún no existe.");
            return;
        }

        System.out.println("ID: " + pasajero.getId());
        System.out.println("Nombre: " + pasajero.getNombreCompleto());
        System.out.println("Correo: " + pasajero.getCorreo());
        System.out.println("Viajes registrados: "
                + pasajero.getViajesRegistrados());
    }

    /**
     * Imprime los datos principales de un boleto.
     *
     * @param boleto Boleto a imprimir.
     */
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
        System.out.println("Fecha reservación: "
                + boleto.getFechaReservacion());
        System.out.println("Fecha cancelación: "
                + boleto.getFechaCancelacion());
    }

    /**
     * Imprime los datos principales de un viaje.
     *
     * @param viaje Viaje a imprimir.
     */
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
