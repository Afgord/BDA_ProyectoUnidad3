/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.implementaciones;

import itson.reservatrenesmongodb.dtos.DashboardDTO;
import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import itson.reservatrenesmongodb.exceptions.ServicioException;
import itson.reservatrenesmongodb.persistencia.daos.DashboardDAO;
import itson.reservatrenesmongodb.persistencia.interfaces.IDashboardDAO;
import itson.reservatrenesmongodb.servicios.interfaces.IDashboardServicio;
import java.math.BigDecimal;

/**
 * Implementación del servicio de dashboard.
 *
 * Coordina la obtención de indicadores generales del sistema.
 *
 * @author Afgord
 */
public class DashboardServicio implements IDashboardServicio {

    private final IDashboardDAO dashboardDAO;

    public DashboardServicio() {
        this.dashboardDAO = new DashboardDAO();
    }

    @Override
    public DashboardDTO consultarResumen() throws ServicioException {
        try {
            long viajesProgramados = dashboardDAO.contarViajesProgramados();
            long boletosConfirmados = dashboardDAO.contarBoletosConfirmados();
            long boletosCancelados = dashboardDAO.contarBoletosCancelados();
            long trenesActivos = dashboardDAO.contarTrenesActivos();
            BigDecimal ingresos = dashboardDAO.calcularIngresosTotalesConfirmados();

            return new DashboardDTO(
                    viajesProgramados,
                    boletosConfirmados,
                    boletosCancelados,
                    trenesActivos,
                    ingresos.toPlainString()
            );

        } catch (PersistenciaException e) {
            throw new ServicioException(
                    "No fue posible consultar el resumen del dashboard.", e);

        } catch (Exception e) {
            throw new ServicioException(
                    "Ocurrió un error inesperado al consultar el dashboard.", e);
        }
    }
}
