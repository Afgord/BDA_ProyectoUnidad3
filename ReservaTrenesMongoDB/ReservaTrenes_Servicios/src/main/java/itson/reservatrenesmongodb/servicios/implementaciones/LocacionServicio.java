/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dominio.Locacion;
import itson.reservatrenesmongodb.dtos.LocacionDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.LocacionDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ILocacionDAO;
import itson.reservatrenesmongodb.servicios.interfaces.ILocacionServicio;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Implementación del servicio de locaciones.
 *
 * Esta clase contiene validaciones y reglas necesarias antes de interactuar con
 * la capa de persistencia.
 *
 * @author Afgord
 */
public class LocacionServicio implements ILocacionServicio {

    /**
     * DAO utilizado para acceder a la colección de locaciones.
     */
    private final ILocacionDAO locacionDAO;

    /**
     * Constructor que inicializa el DAO de locaciones.
     */
    public LocacionServicio() {
        this.locacionDAO = new LocacionDAO();
    }

    private static final Pattern PATRON_CLAVE_LOCACION = Pattern.compile(
            "^[A-Z]{3,5}$"
    );

    /**
     * Registra una nueva locación.
     *
     * @param locacionDTO Datos de la locación.
     * @return Locación registrada.
     * @throws ServicioException Si ocurre un error durante el registro.
     */
    @Override
    public LocacionDTO registrar(LocacionDTO locacionDTO)
            throws ServicioException {
        try {
            validarLocacion(locacionDTO);

            locacionDTO.setClave(normalizarClave(locacionDTO.getClave()));
            locacionDTO.setNombre(limpiarTexto(locacionDTO.getNombre()));
            locacionDTO.setEstado(limpiarTexto(locacionDTO.getEstado()));
            locacionDTO.setPais(limpiarTexto(locacionDTO.getPais()));

            Locacion existente = locacionDAO.buscarPorClave(
                    locacionDTO.getClave());

            if (existente != null) {
                throw new ServicioException(
                        "Ya existe una locación registrada con la misma clave.");
            }

            Locacion locacion = convertirAEntidad(locacionDTO);
            Locacion registrada = locacionDAO.insertar(locacion);

            return convertirADTO(registrada);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible registrar la locación.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al registrar la locación.", e);
        }
    }

    /**
     * Busca una locación por id.
     *
     * @param id Identificador de la locación.
     * @return Locación encontrada o null.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public LocacionDTO buscarPorId(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador de la locación es obligatorio.");
            }

            Locacion locacion = locacionDAO.buscarPorId(id);

            if (locacion == null) {
                return null;
            }

            return convertirADTO(locacion);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar la locación por id.", e);
        }
    }

    /**
     * Busca una locación por clave.
     *
     * @param clave Clave de la locación.
     * @return Locación encontrada o null.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public LocacionDTO buscarPorClave(String clave) throws ServicioException {
        try {
            if (estaVacio(clave)) {
                throw new ServicioException(
                        "La clave de la locación es obligatoria.");
            }

            String claveNormalizada = normalizarClave(clave);

            Locacion locacion = locacionDAO.buscarPorClave(claveNormalizada);

            if (locacion == null) {
                return null;
            }

            return convertirADTO(locacion);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar la locación por clave.", e);
        }
    }

    /**
     * Consulta todas las locaciones registradas.
     *
     * @return Lista de locaciones.
     * @throws ServicioException Si ocurre un error durante la consulta.
     */
    @Override
    public List<LocacionDTO> consultarTodas() throws ServicioException {
        try {
            List<Locacion> locaciones = locacionDAO.buscarTodos();
            List<LocacionDTO> locacionesDTO = new ArrayList<>();

            for (Locacion locacion : locaciones) {
                locacionesDTO.add(convertirADTO(locacion));
            }

            return locacionesDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar las locaciones.", e);
        }
    }

    /**
     * Actualiza una locación existente.
     *
     * @param locacionDTO Datos actualizados.
     * @return true si la locación fue actualizada.
     * @throws ServicioException Si ocurre un error durante la actualización.
     */
    @Override
    public boolean actualizar(LocacionDTO locacionDTO)
            throws ServicioException {
        try {
            if (locacionDTO == null || estaVacio(locacionDTO.getId())) {
                throw new ServicioException(
                        "El identificador de la locación es obligatorio.");
            }

            validarLocacion(locacionDTO);

            locacionDTO.setClave(normalizarClave(locacionDTO.getClave()));
            locacionDTO.setNombre(limpiarTexto(locacionDTO.getNombre()));
            locacionDTO.setEstado(limpiarTexto(locacionDTO.getEstado()));
            locacionDTO.setPais(limpiarTexto(locacionDTO.getPais()));

            Locacion locacionExistente = locacionDAO.buscarPorId(
                    locacionDTO.getId());

            if (locacionExistente == null) {
                throw new ServicioException(
                        "No existe una locación con el identificador indicado.");
            }

            Locacion locacionPorClave = locacionDAO.buscarPorClave(
                    locacionDTO.getClave());

            if (locacionPorClave != null
                    && !locacionPorClave.getId().equals(locacionDTO.getId())) {
                throw new ServicioException(
                        "Ya existe otra locación registrada con la misma clave.");
            }

            Locacion locacion = convertirAEntidad(locacionDTO);

            return locacionDAO.actualizar(locacion);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible actualizar la locación.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al actualizar la locación.", e);
        }
    }

    /**
     * Elimina una locación por id.
     *
     * @param id Identificador de la locación.
     * @return true si fue eliminada.
     * @throws ServicioException Si ocurre un error durante la eliminación.
     */
    @Override
    public boolean eliminar(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador de la locación es obligatorio.");
            }

            Locacion locacion = locacionDAO.buscarPorId(id);

            if (locacion == null) {
                throw new ServicioException(
                        "No existe una locación con el identificador indicado.");
            }

            return locacionDAO.eliminar(id);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible eliminar la locación.", e);
        }
    }

    /**
     * Valida los datos mínimos de una locación.
     *
     * @param locacionDTO DTO a validar.
     * @throws ServicioException Si algún dato es inválido.
     */
    private void validarLocacion(LocacionDTO locacionDTO)
            throws ServicioException {
        if (locacionDTO == null) {
            throw new ServicioException("Los datos de la locación son obligatorios.");
        }

        if (estaVacio(locacionDTO.getClave())) {
            throw new ServicioException("La clave de la locación es obligatoria.");
        }

        String claveNormalizada = normalizarClave(locacionDTO.getClave());

        if (!PATRON_CLAVE_LOCACION.matcher(claveNormalizada).matches()) {
            throw new ServicioException(
                    "La clave de la locación debe contener entre 3 y 5 letras.");
        }

        if (estaVacio(locacionDTO.getNombre())) {
            throw new ServicioException("El nombre de la locación es obligatorio.");
        }

        if (estaVacio(locacionDTO.getEstado())) {
            throw new ServicioException("El estado de la locación es obligatorio.");
        }

        if (estaVacio(locacionDTO.getPais())) {
            throw new ServicioException("El país de la locación es obligatorio.");
        }
    }

    /**
     * Convierte una entidad Locacion a LocacionDTO.
     *
     * @param locacion Entidad a convertir.
     * @return DTO convertido.
     */
    private LocacionDTO convertirADTO(Locacion locacion) {
        if (locacion == null) {
            return null;
        }

        return new LocacionDTO(
                locacion.getId(),
                locacion.getClave(),
                locacion.getNombre(),
                locacion.getEstado(),
                locacion.getPais(),
                locacion.isActiva()
        );
    }

    /**
     * Convierte un LocacionDTO a entidad Locacion.
     *
     * @param locacionDTO DTO a convertir.
     * @return Entidad convertida.
     */
    private Locacion convertirAEntidad(LocacionDTO locacionDTO) {
        if (locacionDTO == null) {
            return null;
        }

        Locacion locacion = new Locacion();

        locacion.setId(locacionDTO.getId());
        locacion.setClave(locacionDTO.getClave());
        locacion.setNombre(locacionDTO.getNombre());
        locacion.setEstado(locacionDTO.getEstado());
        locacion.setPais(locacionDTO.getPais());
        locacion.setActiva(locacionDTO.isActiva());

        return locacion;
    }

    /**
     * Normaliza una clave eliminando espacios y convirtiéndola a mayúsculas.
     *
     * @param clave Clave a normalizar.
     * @return Clave normalizada.
     */
    private String normalizarClave(String clave) {
        return clave.trim().toUpperCase();
    }

    /**
     * Limpia espacios sobrantes de un texto.
     *
     * @param texto Texto a limpiar.
     * @return Texto sin espacios extremos.
     */
    private String limpiarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        return texto.trim();
    }

    /**
     * Verifica si un texto es nulo o vacío.
     *
     * @param texto Texto a validar.
     * @return true si está vacío.
     */
    private boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
