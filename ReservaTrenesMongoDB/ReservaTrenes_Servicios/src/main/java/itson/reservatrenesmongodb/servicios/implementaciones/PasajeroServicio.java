/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dominio.Contacto;
import itson.reservatrenesmongodb.dominio.Direccion;
import itson.reservatrenesmongodb.dominio.Pasajero;
import itson.reservatrenesmongodb.dominio.enums.Sexo;
import itson.reservatrenesmongodb.dtos.PasajeroDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.PasajeroDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IPasajeroDAO;
import itson.reservatrenesmongodb.servicios.interfaces.IPasajeroServicio;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementación del servicio de pasajeros.
 *
 * Esta clase contiene validaciones y reglas necesarias antes de interactuar con
 * la capa de persistencia.
 *
 * @author Afgord
 */
public class PasajeroServicio implements IPasajeroServicio {

    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+$"
    );

    private static final Pattern PATRON_TELEFONO_MEXICO = Pattern.compile(
            "^\\d{10}$"
    );

    private static final Pattern PATRON_NOMBRE_COMPLETO = Pattern.compile(
            "^[A-Za-zÁÉÍÓÚáéíóúÑñ]+(?:\\s[A-Za-zÁÉÍÓÚáéíóúÑñ]+)*$"
    );

    private final IPasajeroDAO pasajeroDAO;

    public PasajeroServicio() {
        this.pasajeroDAO = new PasajeroDAO();
    }

    @Override
    public PasajeroDTO registrar(PasajeroDTO pasajeroDTO)
            throws ServicioException {
        try {
            validarPasajero(pasajeroDTO);

            normalizarDatos(pasajeroDTO);

            Pasajero existente = pasajeroDAO.buscarPorCorreo(
                    pasajeroDTO.getCorreo()
            );

            if (existente != null) {
                throw new ServicioException(
                        "Ya existe un pasajero registrado con el mismo correo.");
            }

            Pasajero pasajero = convertirAEntidad(pasajeroDTO);
            Pasajero registrado = pasajeroDAO.insertar(pasajero);

            return convertirADTO(registrado);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible registrar el pasajero.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al registrar el pasajero.", e);
        }
    }

    @Override
    public PasajeroDTO buscarPorId(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del pasajero es obligatorio.");
            }

            Pasajero pasajero = pasajeroDAO.buscarPorId(id);

            if (pasajero == null) {
                return null;
            }

            return convertirADTO(pasajero);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar el pasajero por id.", e);
        }
    }

    @Override
    public PasajeroDTO buscarPorCorreo(String correo) throws ServicioException {
        try {
            if (estaVacio(correo)) {
                throw new ServicioException(
                        "El correo del pasajero es obligatorio.");
            }

            String correoNormalizado = normalizarCorreo(correo);

            Pasajero pasajero = pasajeroDAO.buscarPorCorreo(correoNormalizado);

            if (pasajero == null) {
                return null;
            }

            return convertirADTO(pasajero);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar el pasajero por correo.", e);
        }
    }

    /**
     * Consulta pasajeros por coincidencia parcial en teléfono o correo
     * electrónico.
     *
     * Si el criterio está vacío, devuelve todos los pasajeros registrados.
     *
     * @param criterio Texto a buscar en teléfono o correo.
     * @return Lista de pasajeros coincidentes.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public List<PasajeroDTO> consultarPorTelefonoOCorreo(String criterio)
            throws ServicioException {
        try {
            if (estaVacio(criterio)) {
                return consultarTodos();
            }

            String criterioLimpio = limpiarTexto(criterio);

            List<Pasajero> pasajeros
                    = pasajeroDAO.buscarPorTelefonoOCorreo(criterioLimpio);

            List<PasajeroDTO> pasajerosDTO = new ArrayList<>();

            for (Pasajero pasajero : pasajeros) {
                pasajerosDTO.add(convertirADTO(pasajero));
            }

            return pasajerosDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar pasajeros por teléfono o correo.", e);
        }
    }

    /**
     * Consulta las ciudades registradas en pasajeros.
     *
     * @return Lista de ciudades registradas.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public List<String> consultarCiudadesRegistradas()
            throws ServicioException {
        try {
            return pasajeroDAO.buscarCiudadesRegistradas();

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar las ciudades registradas.", e);
        }
    }

    /**
     * Consulta los estados registrados en pasajeros.
     *
     * @return Lista de estados registrados.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public List<String> consultarEstadosRegistrados()
            throws ServicioException {
        try {
            return pasajeroDAO.buscarEstadosRegistrados();

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar los estados registrados.", e);
        }
    }

    @Override
    public List<PasajeroDTO> consultarTodos() throws ServicioException {
        try {
            List<Pasajero> pasajeros = pasajeroDAO.buscarTodos();
            List<PasajeroDTO> pasajerosDTO = new ArrayList<>();

            for (Pasajero pasajero : pasajeros) {
                pasajerosDTO.add(convertirADTO(pasajero));
            }

            return pasajerosDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar los pasajeros.", e);
        }
    }

    @Override
    public boolean actualizar(PasajeroDTO pasajeroDTO)
            throws ServicioException {
        try {
            if (pasajeroDTO == null || estaVacio(pasajeroDTO.getId())) {
                throw new ServicioException(
                        "El identificador del pasajero es obligatorio.");
            }

            validarPasajero(pasajeroDTO);
            normalizarDatos(pasajeroDTO);

            Pasajero pasajeroExistente = pasajeroDAO.buscarPorId(
                    pasajeroDTO.getId()
            );

            if (pasajeroExistente == null) {
                throw new ServicioException(
                        "No existe un pasajero con el identificador indicado.");
            }

            Pasajero pasajeroPorCorreo = pasajeroDAO.buscarPorCorreo(
                    pasajeroDTO.getCorreo()
            );

            if (pasajeroPorCorreo != null
                    && !pasajeroPorCorreo.getId().equals(pasajeroDTO.getId())) {
                throw new ServicioException(
                        "Ya existe otro pasajero registrado con el mismo correo.");
            }

            Pasajero pasajero = convertirAEntidad(pasajeroDTO);

            /*
            * Se conserva el historial de viajes ya registrado, ya que actualizar
            * los datos personales del pasajero no debe eliminar su historial.
             */
            pasajero.setHistorialViajes(pasajeroExistente.getHistorialViajes());

            return pasajeroDAO.actualizar(pasajero);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible actualizar el pasajero.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al actualizar el pasajero.", e);
        }
    }

    @Override
    public boolean eliminar(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del pasajero es obligatorio.");
            }

            Pasajero pasajero = pasajeroDAO.buscarPorId(id);

            if (pasajero == null) {
                throw new ServicioException(
                        "No existe un pasajero con el identificador indicado.");
            }

            return pasajeroDAO.eliminar(id);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible eliminar el pasajero.", e);
        }
    }

    private void validarPasajero(PasajeroDTO pasajeroDTO)
            throws ServicioException {
        if (pasajeroDTO == null) {
            throw new ServicioException(
                    "Los datos del pasajero son obligatorios.");
        }

        if (estaVacio(pasajeroDTO.getNombreCompleto())) {
            throw new ServicioException(
                    "El nombre completo del pasajero es obligatorio.");
        }

        String nombreLimpio = limpiarTexto(pasajeroDTO.getNombreCompleto());

        if (nombreLimpio.length() < 2 || nombreLimpio.length() > 80) {
            throw new ServicioException(
                    "El nombre completo debe tener entre 2 y 80 caracteres.");
        }

        if (!PATRON_NOMBRE_COMPLETO.matcher(nombreLimpio).matches()) {
            throw new ServicioException(
                    "El nombre completo solo puede contener letras, acentos y espacios.");
        }

        if (estaVacio(pasajeroDTO.getSexo())) {
            throw new ServicioException("El sexo del pasajero es obligatorio.");
        }

        convertirStringASexo(pasajeroDTO.getSexo());

        if (estaVacio(pasajeroDTO.getFechaNacimiento())) {
            throw new ServicioException(
                    "La fecha de nacimiento del pasajero es obligatoria.");
        }

        Instant fechaNacimiento = convertirStringAInstant(
                pasajeroDTO.getFechaNacimiento()
        );

        if (fechaNacimiento.isAfter(Instant.now())) {
            throw new ServicioException(
                    "La fecha de nacimiento no puede ser futura.");
        }

        if (estaVacio(pasajeroDTO.getTelefono())) {
            throw new ServicioException(
                    "El teléfono del pasajero es obligatorio.");
        }

        String telefonoNormalizado = normalizarTelefono(pasajeroDTO.getTelefono());

        if (!PATRON_TELEFONO_MEXICO.matcher(telefonoNormalizado).matches()) {
            throw new ServicioException(
                    "El teléfono debe tener 10 dígitos numéricos, usando formato de México.");
        }

        if (estaVacio(pasajeroDTO.getCorreo())) {
            throw new ServicioException(
                    "El correo del pasajero es obligatorio.");
        }

        if (!PATRON_CORREO.matcher(pasajeroDTO.getCorreo().trim()).matches()) {
            throw new ServicioException(
                    "El formato del correo electrónico no es válido.");
        }

        if (estaVacio(pasajeroDTO.getCalle())) {
            throw new ServicioException("La calle es obligatoria.");
        }

        if (estaVacio(pasajeroDTO.getColonia())) {
            throw new ServicioException("La colonia es obligatoria.");
        }

        if (estaVacio(pasajeroDTO.getCiudad())) {
            throw new ServicioException("La ciudad es obligatoria.");
        }

        if (estaVacio(pasajeroDTO.getEstado())) {
            throw new ServicioException("El estado es obligatorio.");
        }
    }

    private String normalizarTelefono(String telefono) {
        if (telefono == null) {
            return null;
        }

        return telefono.replaceAll("\\D", "");
    }

    private void normalizarDatos(PasajeroDTO pasajeroDTO) {
        pasajeroDTO.setNombreCompleto(limpiarTexto(
                pasajeroDTO.getNombreCompleto()));
        pasajeroDTO.setSexo(limpiarTexto(pasajeroDTO.getSexo()).toUpperCase());
        pasajeroDTO.setFechaNacimiento(limpiarTexto(
                pasajeroDTO.getFechaNacimiento()));
        pasajeroDTO.setTelefono(normalizarTelefono(pasajeroDTO.getTelefono()));
        pasajeroDTO.setCorreo(normalizarCorreo(pasajeroDTO.getCorreo()));
        pasajeroDTO.setCalle(limpiarTexto(pasajeroDTO.getCalle()));
        pasajeroDTO.setColonia(limpiarTexto(pasajeroDTO.getColonia()));
        pasajeroDTO.setCiudad(limpiarTexto(pasajeroDTO.getCiudad()));
        pasajeroDTO.setEstado(limpiarTexto(pasajeroDTO.getEstado()));
    }

    private PasajeroDTO convertirADTO(Pasajero pasajero) {
        if (pasajero == null) {
            return null;
        }

        String telefono = null;
        String correo = null;

        if (pasajero.getContacto() != null) {
            telefono = pasajero.getContacto().getTelefono();
            correo = pasajero.getContacto().getCorreo();
        }

        String calle = null;
        String colonia = null;
        String ciudad = null;
        String estado = null;

        if (pasajero.getDireccion() != null) {
            calle = pasajero.getDireccion().getCalle();
            colonia = pasajero.getDireccion().getColonia();
            ciudad = pasajero.getDireccion().getCiudad();
            estado = pasajero.getDireccion().getEstado();
        }

        int viajesRegistrados = 0;
        if (pasajero.getHistorialViajes() != null) {
            viajesRegistrados = pasajero.getHistorialViajes().size();
        }

        return new PasajeroDTO(
                pasajero.getId(),
                pasajero.getNombreCompleto(),
                convertirSexoAString(pasajero.getSexo()),
                convertirInstantAString(pasajero.getFechaNacimiento()),
                telefono,
                correo,
                calle,
                colonia,
                ciudad,
                estado,
                viajesRegistrados
        );
    }

    private Pasajero convertirAEntidad(PasajeroDTO pasajeroDTO)
            throws ServicioException {
        if (pasajeroDTO == null) {
            return null;
        }

        Pasajero pasajero = new Pasajero();

        pasajero.setId(pasajeroDTO.getId());
        pasajero.setNombreCompleto(pasajeroDTO.getNombreCompleto());
        pasajero.setSexo(convertirStringASexo(pasajeroDTO.getSexo()));
        pasajero.setFechaNacimiento(convertirStringAInstant(
                pasajeroDTO.getFechaNacimiento()));
        pasajero.setContacto(new Contacto(
                pasajeroDTO.getTelefono(),
                pasajeroDTO.getCorreo()
        ));
        pasajero.setDireccion(new Direccion(
                pasajeroDTO.getCalle(),
                pasajeroDTO.getColonia(),
                pasajeroDTO.getCiudad(),
                pasajeroDTO.getEstado()
        ));

        pasajero.setHistorialViajes(new ArrayList<>());

        return pasajero;
    }

    private Sexo convertirStringASexo(String sexo) throws ServicioException {
        try {
            return Sexo.valueOf(limpiarTexto(sexo).toUpperCase());

        } catch (Exception e) {
            throw new ServicioException(
                    "El sexo del pasajero no es válido. Valores permitidos: "
                    + "MASCULINO, FEMENINO.");
        }
    }

    private String convertirSexoAString(Sexo sexo) {
        if (sexo == null) {
            return null;
        }

        return sexo.name();
    }

    private Instant convertirStringAInstant(String fecha)
            throws ServicioException {
        try {
            LocalDate localDate = LocalDate.parse(fecha.trim());
            return localDate.atStartOfDay().toInstant(ZoneOffset.UTC);

        } catch (DateTimeParseException e) {
            throw new ServicioException(
                    "La fecha debe tener el formato yyyy-MM-dd.");
        }
    }

    private String convertirInstantAString(Instant instant) {
        if (instant == null) {
            return null;
        }

        return instant.atZone(ZoneOffset.UTC).toLocalDate().toString();
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
}
