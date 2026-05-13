/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.pruebas;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import itson.reservatrenesmongodb.conexion.MongoDBConnection;
import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Contacto;
import itson.reservatrenesmongodb.dominio.Direccion;
import itson.reservatrenesmongodb.dominio.Disponibilidad;
import itson.reservatrenesmongodb.dominio.HistorialViaje;
import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.dominio.LocacionResumen;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.dominio.PasajeroResumen;
import itson.reservatrenesmongodb.dominio.RutaViaje;
import itson.reservatrenesmongodb.dominio.TipoBoleto;
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.dominio.TrenResumen;
import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.dominio.ViajeResumen;
import itson.reservatrenesmongodb.dominio.enums.EstatusBoleto;
import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import itson.reservatrenesmongodb.dominio.enums.Sexo;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.persistencia.daos.BoletoDAO;
import itson.reservatrenesmongodb.persistencia.daos.LocacionDAO;
import itson.reservatrenesmongodb.persistencia.daos.PasajeroDAO;
import itson.reservatrenesmongodb.persistencia.daos.TrenDAO;
import itson.reservatrenesmongodb.persistencia.daos.ViajeDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IBoletoDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ILocacionDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase encargada de cargar datos iniciales controlados en MongoDB.
 *
 * Su propósito es dejar una base de datos consistente para probar la interfaz
 * gráfica, el dashboard y los flujos generales del sistema.
 *
 * @author cmartinez
 */
public class CargaDatosInicialesDAO {

    public static void main(String[] args) {
        ILocacionDAO locacionDAO = new LocacionDAO();
        ITrenDAO trenDAO = new TrenDAO();
        IViajeDAO viajeDAO = new ViajeDAO();
        IPasajeroDAO pasajeroDAO = new PasajeroDAO();
        IBoletoDAO boletoDAO = new BoletoDAO();

        try {
            System.out.println("=== CARGA DE DATOS INICIALES ===");

            limpiarColecciones();

            System.out.println("\nColecciones limpiadas correctamente.");

            /*
             * 1. LOCACIONES
             */
            Locacion obregon = crearLocacion(
                    "COB",
                    "Ciudad Obregón",
                    "Sonora",
                    "México"
            );

            Locacion hermosillo = crearLocacion(
                    "HMO",
                    "Hermosillo",
                    "Sonora",
                    "México"
            );

            Locacion guaymas = crearLocacion(
                    "GYM",
                    "Guaymas",
                    "Sonora",
                    "México"
            );

            Locacion nogales = crearLocacion(
                    "NOG",
                    "Nogales",
                    "Sonora",
                    "México"
            );

            Locacion navojoa = crearLocacion(
                    "NAV",
                    "Navojoa",
                    "Sonora",
                    "México"
            );

            obregon = locacionDAO.insertar(obregon);
            hermosillo = locacionDAO.insertar(hermosillo);
            guaymas = locacionDAO.insertar(guaymas);
            nogales = locacionDAO.insertar(nogales);
            navojoa = locacionDAO.insertar(navojoa);

            System.out.println("\nLocaciones insertadas: 5");

            /*
             * 2. TRENES
             */
            Tren regionalNorte = crearTren(
                    "TR-NORTE-01",
                    "Regional Norte",
                    "RN-2000",
                    45,
                    12,
                    Arrays.asList("WiFi", "Aire acondicionado", "Baño")
            );

            Tren pacificoExpress = crearTren(
                    "TR-PACIFICO-02",
                    "Pacífico Express",
                    "PE-3500",
                    38,
                    8,
                    Arrays.asList("WiFi", "Cafetería", "Baño")
            );

            Tren sonoraExpress = crearTren(
                    "TR-SONORA-03",
                    "Sonora Express",
                    "SE-2500",
                    52,
                    15,
                    Arrays.asList("WiFi", "Aire acondicionado", "Cafetería")
            );

            regionalNorte = trenDAO.insertar(regionalNorte);
            pacificoExpress = trenDAO.insertar(pacificoExpress);
            sonoraExpress = trenDAO.insertar(sonoraExpress);

            System.out.println("Trenes insertados: 3");

            /*
             * 3. VIAJES PROGRAMADOS FUTUROS
             *
             * Fechas relativas al día actual para que siempre aparezcan en el
             * dashboard como "Próximos viajes".
             */
            Instant salidaViaje1 = Instant.now()
                    .plus(1, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Instant salidaViaje2 = Instant.now()
                    .plus(2, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Instant salidaViaje3 = Instant.now()
                    .plus(3, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Instant salidaViaje4 = Instant.now()
                    .plus(4, ChronoUnit.DAYS)
                    .truncatedTo(ChronoUnit.MINUTES);

            Viaje viaje1 = crearViaje(
                    regionalNorte,
                    obregon,
                    hermosillo,
                    salidaViaje1,
                    salidaViaje1.plus(3, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES),
                    45,
                    12,
                    44,
                    12
            );

            Viaje viaje2 = crearViaje(
                    pacificoExpress,
                    hermosillo,
                    guaymas,
                    salidaViaje2,
                    salidaViaje2.plus(2, ChronoUnit.HOURS).plus(45, ChronoUnit.MINUTES),
                    38,
                    8,
                    38,
                    7
            );

            Viaje viaje3 = crearViaje(
                    sonoraExpress,
                    obregon,
                    navojoa,
                    salidaViaje3,
                    salidaViaje3.plus(1, ChronoUnit.HOURS).plus(15, ChronoUnit.MINUTES),
                    52,
                    15,
                    52,
                    15
            );

            Viaje viaje4 = crearViaje(
                    regionalNorte,
                    guaymas,
                    nogales,
                    salidaViaje4,
                    salidaViaje4.plus(4, ChronoUnit.HOURS).plus(30, ChronoUnit.MINUTES),
                    45,
                    12,
                    45,
                    12
            );

            viaje1 = viajeDAO.insertar(viaje1);
            viaje2 = viajeDAO.insertar(viaje2);
            viaje3 = viajeDAO.insertar(viaje3);
            viaje4 = viajeDAO.insertar(viaje4);

            System.out.println("Viajes programados insertados: 4");

            /*
             * 4. PASAJEROS
             */
            Pasajero pasajero1 = crearPasajero(
                    "Harry Potter Evans",
                    Sexo.MASCULINO,
                    Instant.parse("1980-07-31T00:00:00Z"),
                    "6441234567",
                    "harry.evans@email.com",
                    "4 Privet Drive",
                    "Privet",
                    "Ciudad Obregón",
                    "Sonora"
            );

            Pasajero pasajero2 = crearPasajero(
                    "Hermione Granger Evans",
                    Sexo.FEMENINO,
                    Instant.parse("1979-09-19T00:00:00Z"),
                    "6447654321",
                    "hermione.granger@email.com",
                    "Calle Biblioteca 123",
                    "Centro",
                    "Hermosillo",
                    "Sonora"
            );

            Pasajero pasajero3 = crearPasajero(
                    "Ronald Weasley Evans",
                    Sexo.MASCULINO,
                    Instant.parse("1980-03-01T00:00:00Z"),
                    "6449876543",
                    "ron.weasley@email.com",
                    "Calle Madriguera 9",
                    "Norte",
                    "Guaymas",
                    "Sonora"
            );

            pasajero1 = pasajeroDAO.insertar(pasajero1);
            pasajero2 = pasajeroDAO.insertar(pasajero2);
            pasajero3 = pasajeroDAO.insertar(pasajero3);

            System.out.println("Pasajeros insertados: 3");

            /*
             * 5. BOLETOS
             */
            Boleto boletoConfirmado1 = crearBoleto(
                    "BOL-10001",
                    pasajero1,
                    viaje1,
                    crearTipoBoletoGeneral(),
                    EstatusBoleto.CONFIRMADO,
                    Instant.now().minus(2, ChronoUnit.HOURS),
                    null
            );

            Boleto boletoConfirmado2 = crearBoleto(
                    "BOL-10002",
                    pasajero2,
                    viaje2,
                    crearTipoBoletoPrimeraClase(),
                    EstatusBoleto.CONFIRMADO,
                    Instant.now().minus(1, ChronoUnit.HOURS),
                    null
            );

            Boleto boletoCancelado = crearBoleto(
                    "BOL-10003",
                    pasajero3,
                    viaje3,
                    crearTipoBoletoGeneral(),
                    EstatusBoleto.CANCELADO,
                    Instant.now().minus(3, ChronoUnit.HOURS),
                    Instant.now().minus(30, ChronoUnit.MINUTES)
            );

            boletoConfirmado1 = boletoDAO.insertar(boletoConfirmado1);
            boletoConfirmado2 = boletoDAO.insertar(boletoConfirmado2);
            boletoCancelado = boletoDAO.insertar(boletoCancelado);

            System.out.println("Boletos insertados: 3");

            /*
             * 6. HISTORIAL RESUMIDO DE PASAJEROS
             */
            pasajero1.setHistorialViajes(new ArrayList<>());
            pasajero1.getHistorialViajes().add(
                    crearHistorialViaje(
                            boletoConfirmado1,
                            viaje1,
                            "Hermosillo"
                    )
            );

            pasajero2.setHistorialViajes(new ArrayList<>());
            pasajero2.getHistorialViajes().add(
                    crearHistorialViaje(
                            boletoConfirmado2,
                            viaje2,
                            "Guaymas"
                    )
            );

            pasajero3.setHistorialViajes(new ArrayList<>());
            pasajero3.getHistorialViajes().add(
                    crearHistorialViaje(
                            boletoCancelado,
                            viaje3,
                            "Navojoa"
                    )
            );

            pasajeroDAO.actualizar(pasajero1);
            pasajeroDAO.actualizar(pasajero2);
            pasajeroDAO.actualizar(pasajero3);

            System.out.println("Historiales de pasajeros actualizados.");

            imprimirResumenFinal(
                    obregon,
                    hermosillo,
                    guaymas,
                    nogales,
                    navojoa,
                    regionalNorte,
                    pacificoExpress,
                    sonoraExpress,
                    viaje1,
                    viaje2,
                    viaje3,
                    viaje4,
                    boletoConfirmado1,
                    boletoConfirmado2,
                    boletoCancelado
            );

            System.out.println("\n=== FIN DE CARGA DE DATOS INICIALES ===");

        } catch (PersistenciaException e) {
            System.out.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();

        } finally {
            MongoDBConnection.getInstance().close();
        }
    }

    /**
     * Elimina todos los documentos de las colecciones principales.
     */
    private static void limpiarColecciones() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();

        database.getCollection("boletos").deleteMany(Filters.empty());
        database.getCollection("pasajeros").deleteMany(Filters.empty());
        database.getCollection("viajes").deleteMany(Filters.empty());
        database.getCollection("trenes").deleteMany(Filters.empty());
        database.getCollection("locaciones").deleteMany(Filters.empty());
    }

    private static Locacion crearLocacion(String clave, String nombre,
            String estado, String pais) {
        Locacion locacion = new Locacion();
        locacion.setClave(clave);
        locacion.setNombre(nombre);
        locacion.setEstado(estado);
        locacion.setPais(pais);
        locacion.setActiva(true);
        return locacion;
    }

    private static Tren crearTren(String codigo, String nombre, String modelo,
            int capacidadGeneral, int capacidadPrimeraClase,
            List<String> servicios) {
        Tren tren = new Tren();
        tren.setCodigo(codigo);
        tren.setNombre(nombre);
        tren.setModelo(modelo);
        tren.setEstatus(EstatusTren.ACTIVO);
        tren.setCapacidad(new Capacidad(
                capacidadGeneral,
                capacidadPrimeraClase
        ));
        tren.setServicios(new ArrayList<>(servicios));
        return tren;
    }

    private static Viaje crearViaje(Tren tren, Locacion origen,
            Locacion destino, Instant fechaHoraSalida,
            Instant fechaHoraLlegadaEstimada, int capacidadGeneral,
            int capacidadPrimeraClase, int disponibilidadGeneral,
            int disponibilidadPrimeraClase) {

        Viaje viaje = new Viaje();

        viaje.setTren(new TrenResumen(
                tren.getId(),
                tren.getCodigo(),
                tren.getNombre()
        ));

        viaje.setRuta(new RutaViaje(
                new LocacionResumen(
                        origen.getId(),
                        origen.getNombre()
                ),
                new LocacionResumen(
                        destino.getId(),
                        destino.getNombre()
                )
        ));

        viaje.setFechaHoraSalida(fechaHoraSalida);
        viaje.setFechaHoraLlegadaEstimada(fechaHoraLlegadaEstimada);
        viaje.setEstatus(EstatusViaje.PROGRAMADO);
        viaje.setCapacidadMaxima(new Capacidad(
                capacidadGeneral,
                capacidadPrimeraClase
        ));
        viaje.setDisponibilidad(new Disponibilidad(
                disponibilidadGeneral,
                disponibilidadPrimeraClase
        ));

        return viaje;
    }

    private static Pasajero crearPasajero(String nombreCompleto, Sexo sexo,
            Instant fechaNacimiento, String telefono, String correo,
            String calle, String colonia, String ciudad, String estado) {

        Pasajero pasajero = new Pasajero();
        pasajero.setNombreCompleto(nombreCompleto);
        pasajero.setSexo(sexo);
        pasajero.setFechaNacimiento(fechaNacimiento);
        pasajero.setContacto(new Contacto(telefono, correo));
        pasajero.setDireccion(new Direccion(
                calle,
                colonia,
                ciudad,
                estado
        ));
        pasajero.setHistorialViajes(new ArrayList<>());

        return pasajero;
    }

    private static Boleto crearBoleto(String folio, Pasajero pasajero,
            Viaje viaje, TipoBoleto tipoBoleto, EstatusBoleto estatus,
            Instant fechaReservacion, Instant fechaCancelacion) {

        Boleto boleto = new Boleto();

        boleto.setFolio(folio);
        boleto.setPasajeroId(pasajero.getId());
        boleto.setViajeId(viaje.getId());
        boleto.setPasajeroResumen(new PasajeroResumen(
                pasajero.getNombreCompleto(),
                pasajero.getContacto().getTelefono()
        ));
        boleto.setViajeResumen(crearViajeResumen(viaje));
        boleto.setTipoBoleto(tipoBoleto);
        boleto.setEstatus(estatus);
        boleto.setFechaReservacion(fechaReservacion);
        boleto.setFechaCancelacion(fechaCancelacion);

        return boleto;
    }

    private static ViajeResumen crearViajeResumen(Viaje viaje) {
        return new ViajeResumen(
                viaje.getFechaHoraSalida(),
                viaje.getRuta().getOrigen().getNombre(),
                viaje.getRuta().getDestino().getNombre(),
                viaje.getTren().getNombre()
        );
    }

    private static TipoBoleto crearTipoBoletoGeneral() {
        return new TipoBoleto(
                "GENERAL",
                "General",
                new BigDecimal("450.00"),
                new ArrayList<>(Arrays.asList(
                        "Asiento estándar"
                )),
                false
        );
    }

    private static TipoBoleto crearTipoBoletoPrimeraClase() {
        return new TipoBoleto(
                "PRIMERA_CLASE",
                "Primera Clase",
                new BigDecimal("850.00"),
                new ArrayList<>(Arrays.asList(
                        "Asiento preferente",
                        "Mayor espacio",
                        "Servicio prioritario"
                )),
                true
        );
    }

    private static HistorialViaje crearHistorialViaje(Boleto boleto,
            Viaje viaje, String destino) {
        HistorialViaje historial = new HistorialViaje();
        historial.setBoletoId(boleto.getId());
        historial.setViajeId(viaje.getId());
        historial.setDestino(destino);
        return historial;
    }

    private static void imprimirResumenFinal(
            Locacion obregon,
            Locacion hermosillo,
            Locacion guaymas,
            Locacion nogales,
            Locacion navojoa,
            Tren regionalNorte,
            Tren pacificoExpress,
            Tren sonoraExpress,
            Viaje viaje1,
            Viaje viaje2,
            Viaje viaje3,
            Viaje viaje4,
            Boleto boletoConfirmado1,
            Boleto boletoConfirmado2,
            Boleto boletoCancelado) {

        System.out.println("\n--- RESUMEN DE DATOS CARGADOS ---");

        System.out.println("Locaciones:");
        System.out.println("- " + obregon.getClave() + " | " + obregon.getNombre());
        System.out.println("- " + hermosillo.getClave() + " | " + hermosillo.getNombre());
        System.out.println("- " + guaymas.getClave() + " | " + guaymas.getNombre());
        System.out.println("- " + nogales.getClave() + " | " + nogales.getNombre());
        System.out.println("- " + navojoa.getClave() + " | " + navojoa.getNombre());

        System.out.println("\nTrenes:");
        System.out.println("- " + regionalNorte.getCodigo() + " | " + regionalNorte.getNombre());
        System.out.println("- " + pacificoExpress.getCodigo() + " | " + pacificoExpress.getNombre());
        System.out.println("- " + sonoraExpress.getCodigo() + " | " + sonoraExpress.getNombre());

        System.out.println("\nViajes programados:");
        imprimirViajeResumen(viaje1);
        imprimirViajeResumen(viaje2);
        imprimirViajeResumen(viaje3);
        imprimirViajeResumen(viaje4);

        System.out.println("\nBoletos:");
        System.out.println("- " + boletoConfirmado1.getFolio()
                + " | " + boletoConfirmado1.getEstatus());
        System.out.println("- " + boletoConfirmado2.getFolio()
                + " | " + boletoConfirmado2.getEstatus());
        System.out.println("- " + boletoCancelado.getFolio()
                + " | " + boletoCancelado.getEstatus());

        System.out.println("\nResultado esperado para Dashboard:");
        System.out.println("- Viajes programados: 4");
        System.out.println("- Boletos vendidos: 2");
        System.out.println("- Trenes activos: 3");
        System.out.println("- Cancelaciones: 1");
    }

    private static void imprimirViajeResumen(Viaje viaje) {
        System.out.println("- "
                + viaje.getTren().getNombre()
                + " | "
                + viaje.getRuta().getOrigen().getNombre()
                + " -> "
                + viaje.getRuta().getDestino().getNombre()
                + " | "
                + viaje.getFechaHoraSalida());
    }
}
