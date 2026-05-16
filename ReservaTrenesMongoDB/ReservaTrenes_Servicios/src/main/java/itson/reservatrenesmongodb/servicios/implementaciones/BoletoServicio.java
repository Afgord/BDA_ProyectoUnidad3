/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dominio.Boleto;
import itson.reservatrenesmongodb.dominio.Contacto;
import itson.reservatrenesmongodb.dominio.Direccion;
import itson.reservatrenesmongodb.dominio.Disponibilidad;
import itson.reservatrenesmongodb.dominio.HistorialViaje;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.dominio.PasajeroResumen;
import itson.reservatrenesmongodb.dominio.TipoBoleto;
import itson.reservatrenesmongodb.dominio.Viaje;
import itson.reservatrenesmongodb.dominio.ViajeResumen;
import itson.reservatrenesmongodb.dominio.enums.EstatusBoleto;
import itson.reservatrenesmongodb.dominio.enums.EstatusViaje;
import itson.reservatrenesmongodb.dominio.enums.Sexo;
import itson.reservatrenesmongodb.dtos.BoletoDTO;
import itson.reservatrenesmongodb.dtos.CancelacionBoletoDTO;
import itson.reservatrenesmongodb.dtos.CompraBoletoDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.BoletoDAO;
import itson.reservatrenesmongodb.persistencia.daos.PasajeroDAO;
import itson.reservatrenesmongodb.persistencia.daos.ViajeDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IBoletoDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IViajeDAO;
import itson.reservatrenesmongodb.servicios.interfaces.IBoletoServicio;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Implementación del servicio de boletos.
 *
 * Esta clase coordina las operaciones necesarias para generar y cancelar
 * boletos, utilizando pasajeros, viajes y boletos.
 *
 * @author Afgord
 */
public class BoletoServicio implements IBoletoServicio {

    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private static final Pattern PATRON_TELEFONO_MEXICO = Pattern.compile(
            "^\\d{10}$"
    );

    private static final Pattern PATRON_NOMBRE_COMPLETO = Pattern.compile(
            "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?:\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$"
    );

    private static final String TIPO_GENERAL = "GENERAL";
    private static final String TIPO_PRIMERA_CLASE = "PRIMERA_CLASE";

    private static final BigDecimal PRECIO_GENERAL = new BigDecimal("450.00");
    private static final BigDecimal PRECIO_PRIMERA_CLASE = new BigDecimal("850.00");

    private final IBoletoDAO boletoDAO;
    private final IPasajeroDAO pasajeroDAO;
    private final IViajeDAO viajeDAO;

    public BoletoServicio() {
        this.boletoDAO = new BoletoDAO();
        this.pasajeroDAO = new PasajeroDAO();
        this.viajeDAO = new ViajeDAO();
    }

    @Override
    public BoletoDTO generarBoleto(CompraBoletoDTO compraDTO)
            throws ServicioException {
        try {
            validarCompra(compraDTO);
            normalizarCompra(compraDTO);

            Viaje viaje = viajeDAO.buscarPorId(compraDTO.getViajeId());

            if (viaje == null) {
                throw new ServicioException(
                        "No existe el viaje seleccionado.");
            }

            validarViajeParaCompra(viaje, compraDTO.getTipoBoleto());

            Pasajero pasajero = obtenerORegistrarPasajero(compraDTO);

            List<Boleto> boletosDelPasajero = boletoDAO.buscarPorPasajeroId(
                    pasajero.getId());

            for (Boleto boleto : boletosDelPasajero) {
                if (boleto.getViajeId().equals(viaje.getId())
                        && boleto.getEstatus() == EstatusBoleto.CONFIRMADO) {
                    throw new ServicioException(
                            "El pasajero ya tiene un boleto confirmado para este viaje.");
                }
            }

            Boleto boleto = new Boleto();

            boleto.setFolio(generarFolio());
            boleto.setPasajeroId(pasajero.getId());
            boleto.setViajeId(viaje.getId());
            boleto.setPasajeroResumen(crearPasajeroResumen(pasajero));
            boleto.setViajeResumen(crearViajeResumen(viaje));
            boleto.setTipoBoleto(crearTipoBoleto(compraDTO.getTipoBoleto()));
            boleto.setEstatus(EstatusBoleto.CONFIRMADO);
            boleto.setFechaReservacion(Instant.now());
            boleto.setFechaCancelacion(null);

            Boleto boletoInsertado = boletoDAO.insertar(boleto);

            agregarViajeAlHistorialPasajero(
                    pasajero,
                    boletoInsertado,
                    viaje
            );

            descontarDisponibilidad(viaje, compraDTO.getTipoBoleto());
            viajeDAO.actualizar(viaje);

            return convertirADTO(boletoInsertado);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible generar el boleto.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al generar el boleto.", e);
        }
    }

    @Override
    public BoletoDTO cancelarBoleto(CancelacionBoletoDTO cancelacionDTO)
            throws ServicioException {
        try {
            if (cancelacionDTO == null || estaVacio(cancelacionDTO.getFolio())) {
                throw new ServicioException(
                        "El folio del boleto es obligatorio.");
            }

            String folio = limpiarTexto(cancelacionDTO.getFolio()).toUpperCase();

            Boleto boleto = boletoDAO.buscarPorFolio(folio);

            if (boleto == null) {
                throw new ServicioException(
                        "No existe un boleto con el folio indicado.");
            }

            if (boleto.getEstatus() != EstatusBoleto.CONFIRMADO) {
                throw new ServicioException(
                        "Solo se pueden cancelar boletos confirmados.");
            }

            Viaje viaje = viajeDAO.buscarPorId(boleto.getViajeId());

            if (viaje == null) {
                throw new ServicioException(
                        "No fue posible encontrar el viaje asociado al boleto.");
            }

            boleto.setEstatus(EstatusBoleto.CANCELADO);
            boleto.setFechaCancelacion(Instant.now());

            regresarDisponibilidad(viaje, boleto.getTipoBoleto().getClave());

            boletoDAO.actualizar(boleto);
            viajeDAO.actualizar(viaje);

            return convertirADTO(boleto);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible cancelar el boleto.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al cancelar el boleto.", e);
        }
    }

    @Override
    public BoletoDTO buscarPorFolio(String folio) throws ServicioException {
        try {
            if (estaVacio(folio)) {
                throw new ServicioException(
                        "El folio del boleto es obligatorio.");
            }

            Boleto boleto = boletoDAO.buscarPorFolio(
                    limpiarTexto(folio).toUpperCase());

            if (boleto == null) {
                return null;
            }

            return convertirADTO(boleto);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar el boleto por folio.", e);
        }
    }

    @Override
    public List<BoletoDTO> consultarPorPasajero(String pasajeroId)
            throws ServicioException {
        try {
            if (estaVacio(pasajeroId)) {
                throw new ServicioException(
                        "El identificador del pasajero es obligatorio.");
            }

            List<Boleto> boletos = boletoDAO.buscarPorPasajeroId(pasajeroId);
            List<BoletoDTO> boletosDTO = new ArrayList<>();

            for (Boleto boleto : boletos) {
                boletosDTO.add(convertirADTO(boleto));
            }

            return boletosDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar boletos por pasajero.", e);
        }
    }

    @Override
    public List<BoletoDTO> consultarPorViaje(String viajeId)
            throws ServicioException {
        try {
            if (estaVacio(viajeId)) {
                throw new ServicioException(
                        "El identificador del viaje es obligatorio.");
            }

            List<Boleto> boletos = boletoDAO.buscarPorViajeId(viajeId);
            List<BoletoDTO> boletosDTO = new ArrayList<>();

            for (Boleto boleto : boletos) {
                boletosDTO.add(convertirADTO(boleto));
            }

            return boletosDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar boletos por viaje.", e);
        }
    }

    private void validarCompra(CompraBoletoDTO compraDTO)
            throws ServicioException {
        if (compraDTO == null) {
            throw new ServicioException(
                    "Los datos de compra del boleto son obligatorios.");
        }

        if (estaVacio(compraDTO.getViajeId())) {
            throw new ServicioException("El viaje es obligatorio.");
        }

        if (estaVacio(compraDTO.getTipoBoleto())) {
            throw new ServicioException("El tipo de boleto es obligatorio.");
        }

        validarTipoBoleto(compraDTO.getTipoBoleto());

        if (estaVacio(compraDTO.getNombreCompleto())) {
            throw new ServicioException(
                    "El nombre completo del pasajero es obligatorio.");
        }

        String nombreLimpio = limpiarTexto(compraDTO.getNombreCompleto());

        if (nombreLimpio.length() < 2 || nombreLimpio.length() > 80) {
            throw new ServicioException(
                    "El nombre completo debe tener entre 2 y 80 caracteres.");
        }

        if (!PATRON_NOMBRE_COMPLETO.matcher(nombreLimpio).matches()) {
            throw new ServicioException(
                    "El nombre completo solo puede contener letras, acentos y espacios.");
        }

        if (estaVacio(compraDTO.getSexo())) {
            throw new ServicioException("El sexo del pasajero es obligatorio.");
        }

        convertirStringASexo(compraDTO.getSexo());

        if (estaVacio(compraDTO.getFechaNacimiento())) {
            throw new ServicioException(
                    "La fecha de nacimiento del pasajero es obligatoria.");
        }

        Instant fechaNacimiento = convertirFechaNacimientoAInstant(
                compraDTO.getFechaNacimiento());

        if (fechaNacimiento.isAfter(Instant.now())) {
            throw new ServicioException(
                    "La fecha de nacimiento no puede ser futura.");
        }

        if (estaVacio(compraDTO.getTelefono())) {
            throw new ServicioException(
                    "El teléfono del pasajero es obligatorio.");
        }

        String telefonoNormalizado = normalizarTelefono(compraDTO.getTelefono());

        if (!PATRON_TELEFONO_MEXICO.matcher(telefonoNormalizado).matches()) {
            throw new ServicioException(
                    "El teléfono debe tener 10 dígitos numéricos, usando formato de México.");
        }

        if (estaVacio(compraDTO.getCorreo())) {
            throw new ServicioException(
                    "El correo del pasajero es obligatorio.");
        }

        if (!PATRON_CORREO.matcher(compraDTO.getCorreo().trim()).matches()) {
            throw new ServicioException(
                    "El formato del correo electrónico no es válido.");
        }

        if (estaVacio(compraDTO.getCalle())) {
            throw new ServicioException("La calle es obligatoria.");
        }

        if (estaVacio(compraDTO.getColonia())) {
            throw new ServicioException("La colonia es obligatoria.");
        }

        if (estaVacio(compraDTO.getCiudad())) {
            throw new ServicioException("La ciudad es obligatoria.");
        }

        if (estaVacio(compraDTO.getEstado())) {
            throw new ServicioException("El estado es obligatorio.");
        }
    }

    private void normalizarCompra(CompraBoletoDTO compraDTO) {
        compraDTO.setViajeId(limpiarTexto(compraDTO.getViajeId()));
        compraDTO.setTipoBoleto(normalizarTipoBoleto(compraDTO.getTipoBoleto()));
        compraDTO.setNombreCompleto(limpiarTexto(compraDTO.getNombreCompleto()));
        compraDTO.setSexo(limpiarTexto(compraDTO.getSexo()).toUpperCase());
        compraDTO.setFechaNacimiento(limpiarTexto(compraDTO.getFechaNacimiento()));
        compraDTO.setTelefono(normalizarTelefono(compraDTO.getTelefono()));
        compraDTO.setCorreo(normalizarCorreo(compraDTO.getCorreo()));
        compraDTO.setCalle(limpiarTexto(compraDTO.getCalle()));
        compraDTO.setColonia(limpiarTexto(compraDTO.getColonia()));
        compraDTO.setCiudad(limpiarTexto(compraDTO.getCiudad()));
        compraDTO.setEstado(limpiarTexto(compraDTO.getEstado()));
    }

    private void validarViajeParaCompra(Viaje viaje, String tipoBoleto)
            throws ServicioException {
        if (viaje.getEstatus() != EstatusViaje.PROGRAMADO) {
            throw new ServicioException(
                    "Solo se pueden generar boletos para viajes programados.");
        }

        if (viaje.getDisponibilidad() == null) {
            throw new ServicioException(
                    "El viaje no tiene disponibilidad registrada.");
        }

        if (esTipoGeneral(tipoBoleto)
                && viaje.getDisponibilidad().getGeneral() <= 0) {
            throw new ServicioException(
                    "No hay disponibilidad para boleto general.");
        }

        if (esTipoPrimeraClase(tipoBoleto)
                && viaje.getDisponibilidad().getPrimeraClase() <= 0) {
            throw new ServicioException(
                    "No hay disponibilidad para boleto de primera clase.");
        }
    }

    private Pasajero obtenerORegistrarPasajero(CompraBoletoDTO compraDTO)
            throws PersistenciaException, ServicioException {
        Pasajero pasajero = pasajeroDAO.buscarPorCorreo(compraDTO.getCorreo());

        if (pasajero != null) {
            return pasajero;
        }

        pasajero = new Pasajero();
        pasajero.setNombreCompleto(compraDTO.getNombreCompleto());
        pasajero.setSexo(convertirStringASexo(compraDTO.getSexo()));
        pasajero.setFechaNacimiento(convertirFechaNacimientoAInstant(
                compraDTO.getFechaNacimiento()));
        pasajero.setContacto(new Contacto(
                compraDTO.getTelefono(),
                compraDTO.getCorreo()
        ));
        pasajero.setDireccion(new Direccion(
                compraDTO.getCalle(),
                compraDTO.getColonia(),
                compraDTO.getCiudad(),
                compraDTO.getEstado()
        ));
        pasajero.setHistorialViajes(new ArrayList<>());

        return pasajeroDAO.insertar(pasajero);
    }

    private PasajeroResumen crearPasajeroResumen(Pasajero pasajero) {
        String telefono = null;

        if (pasajero.getContacto() != null) {
            telefono = pasajero.getContacto().getTelefono();
        }

        return new PasajeroResumen(
                pasajero.getNombreCompleto(),
                telefono
        );
    }

    private ViajeResumen crearViajeResumen(Viaje viaje) {
        String origen = null;
        String destino = null;

        if (viaje.getRuta() != null) {
            if (viaje.getRuta().getOrigen() != null) {
                origen = viaje.getRuta().getOrigen().getNombre();
            }

            if (viaje.getRuta().getDestino() != null) {
                destino = viaje.getRuta().getDestino().getNombre();
            }
        }

        String tren = null;

        if (viaje.getTren() != null) {
            tren = viaje.getTren().getNombre();
        }

        return new ViajeResumen(
                viaje.getFechaHoraSalida(),
                origen,
                destino,
                tren
        );
    }

    private TipoBoleto crearTipoBoleto(String tipoBoleto) {
        if (esTipoPrimeraClase(tipoBoleto)) {
            return new TipoBoleto(
                    TIPO_PRIMERA_CLASE,
                    "Primera Clase",
                    PRECIO_PRIMERA_CLASE,
                    new ArrayList<>(Arrays.asList(
                            "Asiento preferente",
                            "Mayor espacio",
                            "Servicio prioritario"
                    )),
                    true
            );
        }

        return new TipoBoleto(
                TIPO_GENERAL,
                "General",
                PRECIO_GENERAL,
                new ArrayList<>(Arrays.asList(
                        "Asiento estándar"
                )),
                false
        );
    }

    private void descontarDisponibilidad(Viaje viaje, String tipoBoleto)
            throws ServicioException {
        Disponibilidad disponibilidad = viaje.getDisponibilidad();

        if (disponibilidad == null) {
            throw new ServicioException(
                    "El viaje no tiene disponibilidad registrada.");
        }

        if (esTipoGeneral(tipoBoleto)) {
            disponibilidad.setGeneral(disponibilidad.getGeneral() - 1);
        } else if (esTipoPrimeraClase(tipoBoleto)) {
            disponibilidad.setPrimeraClase(
                    disponibilidad.getPrimeraClase() - 1);
        }
    }

    private void regresarDisponibilidad(Viaje viaje, String tipoBoleto)
            throws ServicioException {
        Disponibilidad disponibilidad = viaje.getDisponibilidad();

        if (disponibilidad == null) {
            throw new ServicioException(
                    "El viaje no tiene disponibilidad registrada.");
        }

        if (esTipoGeneral(tipoBoleto)) {
            disponibilidad.setGeneral(disponibilidad.getGeneral() + 1);
        } else if (esTipoPrimeraClase(tipoBoleto)) {
            disponibilidad.setPrimeraClase(
                    disponibilidad.getPrimeraClase() + 1);
        }
    }

    private BoletoDTO convertirADTO(Boleto boleto) {
        if (boleto == null) {
            return null;
        }

        String pasajeroNombre = null;
        String pasajeroTelefono = null;

        if (boleto.getPasajeroResumen() != null) {
            pasajeroNombre = boleto.getPasajeroResumen().getNombreCompleto();
            pasajeroTelefono = boleto.getPasajeroResumen().getTelefono();
        }

        String origen = null;
        String destino = null;
        String tren = null;
        String fechaHoraSalida = null;

        if (boleto.getViajeResumen() != null) {
            origen = boleto.getViajeResumen().getOrigen();
            destino = boleto.getViajeResumen().getDestino();
            tren = boleto.getViajeResumen().getTren();
            fechaHoraSalida = convertirInstantAString(
                    boleto.getViajeResumen().getFechaHoraSalida());
        }

        String tipoBoleto = null;
        String precio = null;

        if (boleto.getTipoBoleto() != null) {
            tipoBoleto = boleto.getTipoBoleto().getNombre();

            if (boleto.getTipoBoleto().getPrecio() != null) {
                precio = boleto.getTipoBoleto().getPrecio().toPlainString();
            }
        }

        return new BoletoDTO(
                boleto.getId(),
                boleto.getFolio(),
                boleto.getPasajeroId(),
                pasajeroNombre,
                pasajeroTelefono,
                boleto.getViajeId(),
                origen,
                destino,
                tren,
                fechaHoraSalida,
                tipoBoleto,
                precio,
                convertirEstatusAString(boleto.getEstatus()),
                convertirInstantAString(boleto.getFechaReservacion()),
                convertirInstantAString(boleto.getFechaCancelacion())
        );
    }

    private String generarFolio() {
        String uuid = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "BOL-" + uuid;
    }

    private void validarTipoBoleto(String tipoBoleto) throws ServicioException {
        String tipo = normalizarTipoBoleto(tipoBoleto);

        if (!TIPO_GENERAL.equals(tipo) && !TIPO_PRIMERA_CLASE.equals(tipo)) {
            throw new ServicioException(
                    "El tipo de boleto no es válido. Valores permitidos: GENERAL, PRIMERA_CLASE.");
        }
    }

    private String normalizarTipoBoleto(String tipoBoleto) {
        return limpiarTexto(tipoBoleto).toUpperCase().replace(" ", "_");
    }

    private boolean esTipoGeneral(String tipoBoleto) {
        return TIPO_GENERAL.equals(normalizarTipoBoleto(tipoBoleto));
    }

    private boolean esTipoPrimeraClase(String tipoBoleto) {
        return TIPO_PRIMERA_CLASE.equals(normalizarTipoBoleto(tipoBoleto));
    }

    private Sexo convertirStringASexo(String sexo) throws ServicioException {
        try {
            return Sexo.valueOf(limpiarTexto(sexo).toUpperCase());

        } catch (Exception e) {
            throw new ServicioException(
                    "El sexo del pasajero no es válido. Valores permitidos: MASCULINO, FEMENINO.");
        }
    }

    private Instant convertirFechaNacimientoAInstant(String fecha)
            throws ServicioException {
        try {
            LocalDate localDate = LocalDate.parse(fecha.trim());
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);

        } catch (DateTimeParseException e) {
            throw new ServicioException(
                    "La fecha de nacimiento debe tener el formato yyyy-MM-dd.");
        }
    }

    private String convertirEstatusAString(EstatusBoleto estatus) {
        if (estatus == null) {
            return null;
        }

        return estatus.name();
    }

    private String convertirInstantAString(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.toString();
    }

    private String normalizarTelefono(String telefono) {
        if (telefono == null) {
            return null;
        }

        return telefono.replaceAll("\\D", "");
    }

    private String normalizarCorreo(String correo) {
        return correo.trim().toLowerCase();
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

    /**
     * Agrega al pasajero un registro resumido del viaje asociado al boleto
     * generado.
     *
     * @param pasajero Pasajero al que se agregará el historial.
     * @param boleto Boleto recién generado.
     * @param viaje Viaje asociado al boleto.
     * @throws PersistenciaException Si ocurre un error al actualizar al
     * pasajero.
     */
    private void agregarViajeAlHistorialPasajero(Pasajero pasajero,
            Boleto boleto,
            Viaje viaje) throws PersistenciaException {

        if (pasajero.getHistorialViajes() == null) {
            pasajero.setHistorialViajes(new ArrayList<>());
        }

        String destino = null;

        if (viaje.getRuta() != null
                && viaje.getRuta().getDestino() != null) {
            destino = viaje.getRuta().getDestino().getNombre();
        }

        HistorialViaje historialViaje = new HistorialViaje(
                boleto.getId(),
                viaje.getId(),
                destino
        );

        pasajero.getHistorialViajes().add(historialViaje);

        pasajeroDAO.actualizar(pasajero);
    }

    /**
     * Consulta el precio asociado a un tipo de boleto.
     *
     * @param tipoBoleto Tipo de boleto solicitado.
     * @return Precio del boleto.
     * @throws ServicioException Si el tipo de boleto no es válido.
     */
    @Override
    public String consultarPrecioTipoBoleto(String tipoBoleto)
            throws ServicioException {

        if (estaVacio(tipoBoleto)) {
            throw new ServicioException(
                    "El tipo de boleto es obligatorio.");
        }

        validarTipoBoleto(tipoBoleto);

        if (esTipoPrimeraClase(tipoBoleto)) {
            return PRECIO_PRIMERA_CLASE.toPlainString();
        }

        return PRECIO_GENERAL.toPlainString();
    }
}
