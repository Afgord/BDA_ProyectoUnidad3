/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Disponibilidad;
import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.dominio.LocacionResumen;
import itson.reservatrenesmongodb.dominio.RutaViaje;
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.dominio.TrenResumen;
import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import itson.reservatrenesmongodb.dtos.ViajeDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.LocacionDAO;
import itson.reservatrenesmongodb.persistencia.daos.TrenDAO;
import itson.reservatrenesmongodb.persistencia.daos.ViajeDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ILocacionDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import itson.reservatrenesmongodb.servicios.interfaces.IViajeServicio;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de viajes.
 *
 * Esta clase contiene validaciones y reglas de negocio necesarias para
 * programar viajes, consultarlos, actualizarlos y eliminarlos.
 *
 * @author Afgord
 */
public class ViajeServicio implements IViajeServicio {

    private final IViajeDAO viajeDAO;
    private final ITrenDAO trenDAO;
    private final ILocacionDAO locacionDAO;

    public ViajeServicio() {
        this.viajeDAO = new ViajeDAO();
        this.trenDAO = new TrenDAO();
        this.locacionDAO = new LocacionDAO();
    }

    @Override
    public ViajeDTO registrar(ViajeDTO viajeDTO) throws ServicioException {
        try {
            validarViaje(viajeDTO);

            normalizarDatos(viajeDTO);

            Tren tren = obtenerTrenValido(viajeDTO.getTrenId());
            Locacion origen = obtenerLocacionValida(viajeDTO.getOrigenId(), "origen");
            Locacion destino = obtenerLocacionValida(viajeDTO.getDestinoId(), "destino");

            validarReglasDeViaje(viajeDTO, tren, origen, destino);

            Instant fechaSalida = convertirStringAInstant(
                    viajeDTO.getFechaHoraSalida());

            Viaje existente = viajeDAO.buscarPorTrenYFechaSalida(
                    tren.getId(), fechaSalida);

            if (existente != null) {
                throw new ServicioException(
                        "Ya existe un viaje programado para el mismo tren en la misma fecha y hora de salida.");
            }

            viajeDTO.setTrenCodigo(tren.getCodigo());
            viajeDTO.setTrenNombre(tren.getNombre());
            viajeDTO.setOrigenNombre(origen.getNombre());
            viajeDTO.setDestinoNombre(destino.getNombre());

            Viaje viaje = convertirAEntidad(viajeDTO);
            Viaje registrado = viajeDAO.insertar(viaje);

            return convertirADTO(registrado);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible registrar el viaje.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al registrar el viaje.", e);
        }
    }

    @Override
    public ViajeDTO buscarPorId(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del viaje es obligatorio.");
            }

            Viaje viaje = viajeDAO.buscarPorId(id);

            if (viaje == null) {
                return null;
            }

            return convertirADTO(viaje);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar el viaje por id.", e);
        }
    }

    @Override
    public List<ViajeDTO> consultarTodos() throws ServicioException {
        try {
            List<Viaje> viajes = viajeDAO.buscarTodos();
            List<ViajeDTO> viajesDTO = new ArrayList<>();

            for (Viaje viaje : viajes) {
                viajesDTO.add(convertirADTO(viaje));
            }

            return viajesDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar los viajes.", e);
        }
    }

    @Override
    public boolean actualizar(ViajeDTO viajeDTO) throws ServicioException {
        try {
            if (viajeDTO == null || estaVacio(viajeDTO.getId())) {
                throw new ServicioException(
                        "El identificador del viaje es obligatorio.");
            }

            validarViaje(viajeDTO);
            normalizarDatos(viajeDTO);

            Viaje viajeExistente = viajeDAO.buscarPorId(viajeDTO.getId());

            if (viajeExistente == null) {
                throw new ServicioException(
                        "No existe un viaje con el identificador indicado.");
            }

            Tren tren = obtenerTrenValido(viajeDTO.getTrenId());
            Locacion origen = obtenerLocacionValida(viajeDTO.getOrigenId(), "origen");
            Locacion destino = obtenerLocacionValida(viajeDTO.getDestinoId(), "destino");

            validarReglasDeViaje(viajeDTO, tren, origen, destino);

            Instant fechaSalida = convertirStringAInstant(
                    viajeDTO.getFechaHoraSalida());

            Viaje viajePorTrenFecha = viajeDAO.buscarPorTrenYFechaSalida(
                    tren.getId(), fechaSalida);

            if (viajePorTrenFecha != null
                    && !viajePorTrenFecha.getId().equals(viajeDTO.getId())) {
                throw new ServicioException(
                        "Ya existe otro viaje programado para el mismo tren en la misma fecha y hora de salida.");
            }

            viajeDTO.setTrenCodigo(tren.getCodigo());
            viajeDTO.setTrenNombre(tren.getNombre());
            viajeDTO.setOrigenNombre(origen.getNombre());
            viajeDTO.setDestinoNombre(destino.getNombre());

            Viaje viaje = convertirAEntidad(viajeDTO);

            return viajeDAO.actualizar(viaje);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible actualizar el viaje.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al actualizar el viaje.", e);
        }
    }

    @Override
    public boolean eliminar(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del viaje es obligatorio.");
            }

            Viaje viaje = viajeDAO.buscarPorId(id);

            if (viaje == null) {
                throw new ServicioException(
                        "No existe un viaje con el identificador indicado.");
            }

            return viajeDAO.eliminar(id);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible eliminar el viaje.", e);
        }
    }

    private void validarViaje(ViajeDTO viajeDTO) throws ServicioException {
        if (viajeDTO == null) {
            throw new ServicioException("Los datos del viaje son obligatorios.");
        }

        if (estaVacio(viajeDTO.getTrenId())) {
            throw new ServicioException("El tren del viaje es obligatorio.");
        }

        if (estaVacio(viajeDTO.getOrigenId())) {
            throw new ServicioException("La locación de origen es obligatoria.");
        }

        if (estaVacio(viajeDTO.getDestinoId())) {
            throw new ServicioException("La locación de destino es obligatoria.");
        }

        if (estaVacio(viajeDTO.getFechaHoraSalida())) {
            throw new ServicioException(
                    "La fecha y hora de salida es obligatoria.");
        }

        if (estaVacio(viajeDTO.getFechaHoraLlegadaEstimada())) {
            throw new ServicioException(
                    "La fecha y hora de llegada estimada es obligatoria.");
        }

        if (estaVacio(viajeDTO.getEstatus())) {
            throw new ServicioException("El estatus del viaje es obligatorio.");
        }

        convertirStringAEstatus(viajeDTO.getEstatus());

        if (viajeDTO.getCapacidadMaximaGeneral() < 0
                || viajeDTO.getCapacidadMaximaPrimeraClase() < 0) {
            throw new ServicioException(
                    "La capacidad máxima no puede tener valores negativos.");
        }

        if (viajeDTO.getDisponibilidadGeneral() < 0
                || viajeDTO.getDisponibilidadPrimeraClase() < 0) {
            throw new ServicioException(
                    "La disponibilidad no puede tener valores negativos.");
        }

        if (viajeDTO.getCapacidadMaximaGeneral()
                + viajeDTO.getCapacidadMaximaPrimeraClase() <= 0) {
            throw new ServicioException(
                    "El viaje debe tener al menos un asiento disponible.");
        }
    }

    private void validarReglasDeViaje(ViajeDTO viajeDTO, Tren tren,
            Locacion origen, Locacion destino) throws ServicioException {
        if (tren.getEstatus() != EstatusTren.ACTIVO) {
            throw new ServicioException(
                    "Solo se pueden programar viajes con trenes activos.");
        }

        if (!origen.isActiva()) {
            throw new ServicioException(
                    "La locación de origen no se encuentra activa.");
        }

        if (!destino.isActiva()) {
            throw new ServicioException(
                    "La locación de destino no se encuentra activa.");
        }

        if (origen.getId().equals(destino.getId())) {
            throw new ServicioException(
                    "El origen y el destino no pueden ser la misma locación.");
        }

        Instant salida = convertirStringAInstant(viajeDTO.getFechaHoraSalida());
        Instant llegada = convertirStringAInstant(
                viajeDTO.getFechaHoraLlegadaEstimada());

        if (salida.isBefore(Instant.now())) {
            throw new ServicioException(
                    "La fecha de salida no puede ser anterior al momento actual.");
        }

        if (!llegada.isAfter(salida)) {
            throw new ServicioException(
                    "La fecha de llegada estimada debe ser posterior a la fecha de salida.");
        }

        if (viajeDTO.getDisponibilidadGeneral()
                > viajeDTO.getCapacidadMaximaGeneral()) {
            throw new ServicioException(
                    "La disponibilidad general no puede superar la capacidad máxima general.");
        }

        if (viajeDTO.getDisponibilidadPrimeraClase()
                > viajeDTO.getCapacidadMaximaPrimeraClase()) {
            throw new ServicioException(
                    "La disponibilidad de primera clase no puede superar la capacidad máxima de primera clase.");
        }
    }

    private Tren obtenerTrenValido(String trenId)
            throws PersistenciaException, ServicioException {
        Tren tren = trenDAO.buscarPorId(trenId);

        if (tren == null) {
            throw new ServicioException(
                    "No existe el tren indicado para el viaje.");
        }

        return tren;
    }

    private Locacion obtenerLocacionValida(String locacionId, String tipo)
            throws PersistenciaException, ServicioException {
        Locacion locacion = locacionDAO.buscarPorId(locacionId);

        if (locacion == null) {
            throw new ServicioException(
                    "No existe la locación de " + tipo + " indicada.");
        }

        return locacion;
    }

    private ViajeDTO convertirADTO(Viaje viaje) {
        if (viaje == null) {
            return null;
        }

        String trenId = null;
        String trenCodigo = null;
        String trenNombre = null;

        if (viaje.getTren() != null) {
            trenId = viaje.getTren().getTrenId();
            trenCodigo = viaje.getTren().getCodigo();
            trenNombre = viaje.getTren().getNombre();
        }

        String origenId = null;
        String origenNombre = null;
        String destinoId = null;
        String destinoNombre = null;

        if (viaje.getRuta() != null) {
            if (viaje.getRuta().getOrigen() != null) {
                origenId = viaje.getRuta().getOrigen().getLocacionId();
                origenNombre = viaje.getRuta().getOrigen().getNombre();
            }

            if (viaje.getRuta().getDestino() != null) {
                destinoId = viaje.getRuta().getDestino().getLocacionId();
                destinoNombre = viaje.getRuta().getDestino().getNombre();
            }
        }

        int capacidadGeneral = 0;
        int capacidadPrimeraClase = 0;

        if (viaje.getCapacidadMaxima() != null) {
            capacidadGeneral = viaje.getCapacidadMaxima().getGeneral();
            capacidadPrimeraClase = viaje.getCapacidadMaxima().getPrimeraClase();
        }

        int disponibilidadGeneral = 0;
        int disponibilidadPrimeraClase = 0;

        if (viaje.getDisponibilidad() != null) {
            disponibilidadGeneral = viaje.getDisponibilidad().getGeneral();
            disponibilidadPrimeraClase = viaje.getDisponibilidad().getPrimeraClase();
        }

        return new ViajeDTO(
                viaje.getId(),
                trenId,
                trenCodigo,
                trenNombre,
                origenId,
                origenNombre,
                destinoId,
                destinoNombre,
                convertirInstantAString(viaje.getFechaHoraSalida()),
                convertirInstantAString(viaje.getFechaHoraLlegadaEstimada()),
                convertirEstatusAString(viaje.getEstatus()),
                capacidadGeneral,
                capacidadPrimeraClase,
                disponibilidadGeneral,
                disponibilidadPrimeraClase
        );
    }

    private Viaje convertirAEntidad(ViajeDTO viajeDTO)
            throws ServicioException {
        if (viajeDTO == null) {
            return null;
        }

        Viaje viaje = new Viaje();

        viaje.setId(viajeDTO.getId());

        viaje.setTren(new TrenResumen(
                viajeDTO.getTrenId(),
                viajeDTO.getTrenCodigo(),
                viajeDTO.getTrenNombre()
        ));

        viaje.setRuta(new RutaViaje(
                new LocacionResumen(
                        viajeDTO.getOrigenId(),
                        viajeDTO.getOrigenNombre()
                ),
                new LocacionResumen(
                        viajeDTO.getDestinoId(),
                        viajeDTO.getDestinoNombre()
                )
        ));

        viaje.setFechaHoraSalida(convertirStringAInstant(
                viajeDTO.getFechaHoraSalida()));
        viaje.setFechaHoraLlegadaEstimada(convertirStringAInstant(
                viajeDTO.getFechaHoraLlegadaEstimada()));
        viaje.setEstatus(convertirStringAEstatus(viajeDTO.getEstatus()));
        viaje.setCapacidadMaxima(new Capacidad(
                viajeDTO.getCapacidadMaximaGeneral(),
                viajeDTO.getCapacidadMaximaPrimeraClase()
        ));
        viaje.setDisponibilidad(new Disponibilidad(
                viajeDTO.getDisponibilidadGeneral(),
                viajeDTO.getDisponibilidadPrimeraClase()
        ));

        return viaje;
    }

    private EstatusViaje convertirStringAEstatus(String estatus)
            throws ServicioException {
        try {
            return EstatusViaje.valueOf(limpiarTexto(estatus).toUpperCase());

        } catch (Exception e) {
            throw new ServicioException(
                    "El estatus del viaje no es válido. Valores permitidos: "
                    + "PROGRAMADO, FINALIZADO, CANCELADO.");
        }
    }

    private String convertirEstatusAString(EstatusViaje estatus) {
        if (estatus == null) {
            return null;
        }

        return estatus.name();
    }

    private Instant convertirStringAInstant(String fecha)
            throws ServicioException {
        try {
            return Instant.parse(fecha.trim());

        } catch (DateTimeParseException e) {
            throw new ServicioException(
                    "La fecha debe tener formato ISO-8601, por ejemplo: 2026-05-12T08:00:00Z.");
        }
    }

    private String convertirInstantAString(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.toString();
    }

    private void normalizarDatos(ViajeDTO viajeDTO) {
        viajeDTO.setTrenId(limpiarTexto(viajeDTO.getTrenId()));
        viajeDTO.setOrigenId(limpiarTexto(viajeDTO.getOrigenId()));
        viajeDTO.setDestinoId(limpiarTexto(viajeDTO.getDestinoId()));
        viajeDTO.setFechaHoraSalida(limpiarTexto(viajeDTO.getFechaHoraSalida()));
        viajeDTO.setFechaHoraLlegadaEstimada(limpiarTexto(
                viajeDTO.getFechaHoraLlegadaEstimada()));
        viajeDTO.setEstatus(limpiarTexto(viajeDTO.getEstatus()).toUpperCase());
    }

    private String limpiarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        return texto.trim();
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
