/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dominio.Capacidad;
import itson.reservatrenesmongodb.dominio.Tren;
import itson.reservatrenesmongodb.dominio.enums.EstatusTren;
import itson.reservatrenesmongodb.dtos.TrenDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.TrenDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.ITrenDAO;
import itson.reservatrenesmongodb.servicios.interfaces.ITrenServicio;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de trenes.
 *
 * Esta clase contiene validaciones y reglas necesarias antes de interactuar
 * con la capa de persistencia.
 *
 * @author Afgord
 */
public class TrenServicio implements ITrenServicio {

    private final ITrenDAO trenDAO;

    public TrenServicio() {
        this.trenDAO = new TrenDAO();
    }

    @Override
    public TrenDTO registrar(TrenDTO trenDTO) throws ServicioException {
        try {
            validarTren(trenDTO);

            trenDTO.setCodigo(normalizarCodigo(trenDTO.getCodigo()));
            trenDTO.setNombre(limpiarTexto(trenDTO.getNombre()));
            trenDTO.setModelo(limpiarTexto(trenDTO.getModelo()));
            trenDTO.setEstatus(normalizarEstatus(trenDTO.getEstatus()));

            Tren existente = trenDAO.buscarPorCodigo(trenDTO.getCodigo());

            if (existente != null) {
                throw new ServicioException(
                        "Ya existe un tren registrado con el mismo código.");
            }

            Tren tren = convertirAEntidad(trenDTO);
            Tren registrado = trenDAO.insertar(tren);

            return convertirADTO(registrado);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException("No fue posible registrar el tren.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al registrar el tren.", e);
        }
    }

    @Override
    public TrenDTO buscarPorId(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del tren es obligatorio.");
            }

            Tren tren = trenDAO.buscarPorId(id);

            if (tren == null) {
                return null;
            }

            return convertirADTO(tren);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException("No fue posible buscar el tren por id.", e);
        }
    }

    @Override
    public TrenDTO buscarPorCodigo(String codigo) throws ServicioException {
        try {
            if (estaVacio(codigo)) {
                throw new ServicioException("El código del tren es obligatorio.");
            }

            String codigoNormalizado = normalizarCodigo(codigo);

            Tren tren = trenDAO.buscarPorCodigo(codigoNormalizado);

            if (tren == null) {
                return null;
            }

            return convertirADTO(tren);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible buscar el tren por código.", e);
        }
    }

    @Override
    public List<TrenDTO> consultarTodos() throws ServicioException {
        try {
            List<Tren> trenes = trenDAO.buscarTodos();
            List<TrenDTO> trenesDTO = new ArrayList<>();

            for (Tren tren : trenes) {
                trenesDTO.add(convertirADTO(tren));
            }

            return trenesDTO;

        } catch (PersistenciaException e) {
            throw new ServicioException("No fue posible consultar los trenes.", e);
        }
    }

    @Override
    public boolean actualizar(TrenDTO trenDTO) throws ServicioException {
        try {
            if (trenDTO == null || estaVacio(trenDTO.getId())) {
                throw new ServicioException(
                        "El identificador del tren es obligatorio.");
            }

            validarTren(trenDTO);

            trenDTO.setCodigo(normalizarCodigo(trenDTO.getCodigo()));
            trenDTO.setNombre(limpiarTexto(trenDTO.getNombre()));
            trenDTO.setModelo(limpiarTexto(trenDTO.getModelo()));
            trenDTO.setEstatus(normalizarEstatus(trenDTO.getEstatus()));

            Tren trenExistente = trenDAO.buscarPorId(trenDTO.getId());

            if (trenExistente == null) {
                throw new ServicioException(
                        "No existe un tren con el identificador indicado.");
            }

            Tren trenPorCodigo = trenDAO.buscarPorCodigo(trenDTO.getCodigo());

            if (trenPorCodigo != null
                    && !trenPorCodigo.getId().equals(trenDTO.getId())) {
                throw new ServicioException(
                        "Ya existe otro tren registrado con el mismo código.");
            }

            Tren tren = convertirAEntidad(trenDTO);

            return trenDAO.actualizar(tren);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException("No fue posible actualizar el tren.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al actualizar el tren.", e);
        }
    }

    @Override
    public boolean eliminar(String id) throws ServicioException {
        try {
            if (estaVacio(id)) {
                throw new ServicioException(
                        "El identificador del tren es obligatorio.");
            }

            Tren tren = trenDAO.buscarPorId(id);

            if (tren == null) {
                throw new ServicioException(
                        "No existe un tren con el identificador indicado.");
            }

            return trenDAO.eliminar(id);

        } catch (ServicioException e) {
            throw e;

        } catch (PersistenciaException e) {
            throw new ServicioException("No fue posible eliminar el tren.", e);
        }
    }

    private void validarTren(TrenDTO trenDTO) throws ServicioException {
        if (trenDTO == null) {
            throw new ServicioException("Los datos del tren son obligatorios.");
        }

        if (estaVacio(trenDTO.getCodigo())) {
            throw new ServicioException("El código del tren es obligatorio.");
        }

        if (estaVacio(trenDTO.getNombre())) {
            throw new ServicioException("El nombre del tren es obligatorio.");
        }

        if (estaVacio(trenDTO.getModelo())) {
            throw new ServicioException("El modelo del tren es obligatorio.");
        }

        if (estaVacio(trenDTO.getEstatus())) {
            throw new ServicioException("El estatus del tren es obligatorio.");
        }

        if (trenDTO.getCapacidadGeneral() < 0) {
            throw new ServicioException(
                    "La capacidad general no puede ser negativa.");
        }

        if (trenDTO.getCapacidadPrimeraClase() < 0) {
            throw new ServicioException(
                    "La capacidad de primera clase no puede ser negativa.");
        }

        if (trenDTO.getCapacidadGeneral()
                + trenDTO.getCapacidadPrimeraClase() <= 0) {
            throw new ServicioException(
                    "El tren debe tener al menos un asiento disponible.");
        }
    }

    private TrenDTO convertirADTO(Tren tren) {
        if (tren == null) {
            return null;
        }

        int capacidadGeneral = 0;
        int capacidadPrimeraClase = 0;

        if (tren.getCapacidad() != null) {
            capacidadGeneral = tren.getCapacidad().getGeneral();
            capacidadPrimeraClase = tren.getCapacidad().getPrimeraClase();
        }

        return new TrenDTO(
                tren.getId(),
                tren.getCodigo(),
                tren.getNombre(),
                tren.getModelo(),
                convertirEstatusAString(tren.getEstatus()),
                capacidadGeneral,
                capacidadPrimeraClase,
                tren.getServicios()
        );
    }

    private Tren convertirAEntidad(TrenDTO trenDTO) throws ServicioException {
        if (trenDTO == null) {
            return null;
        }

        Tren tren = new Tren();

        tren.setId(trenDTO.getId());
        tren.setCodigo(trenDTO.getCodigo());
        tren.setNombre(trenDTO.getNombre());
        tren.setModelo(trenDTO.getModelo());
        tren.setEstatus(convertirStringAEstatus(trenDTO.getEstatus()));
        tren.setCapacidad(new Capacidad(
                trenDTO.getCapacidadGeneral(),
                trenDTO.getCapacidadPrimeraClase()
        ));

        if (trenDTO.getServicios() == null) {
            tren.setServicios(new ArrayList<>());
        } else {
            tren.setServicios(trenDTO.getServicios());
        }

        return tren;
    }

    private String convertirEstatusAString(EstatusTren estatus) {
        if (estatus == null) {
            return null;
        }

        return estatus.name();
    }

    private EstatusTren convertirStringAEstatus(String estatus)
            throws ServicioException {
        try {
            return EstatusTren.valueOf(normalizarEstatus(estatus));

        } catch (Exception e) {
            throw new ServicioException(
                    "El estatus del tren no es válido. Valores permitidos: "
                    + "ACTIVO, MANTENIMIENTO, FUERA_SERVICIO.");
        }
    }

    private String normalizarCodigo(String codigo) {
        return codigo.trim().toUpperCase();
    }

    private String normalizarEstatus(String estatus) {
        return estatus.trim().toUpperCase();
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