/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.persistencia.interfaces;

import itson.reservatrenesmongodb.exceptions.PersistenciaException;
import java.math.BigDecimal;

/**
 * Interfaz DAO para consultas agregadas del dashboard.
 *
 * Define operaciones de consulta utilizadas para obtener indicadores generales
 * del sistema.
 *
 * @author Afgord
 */
public interface IDashboardDAO {

    long contarViajesProgramados() throws PersistenciaException;

    long contarBoletosConfirmados() throws PersistenciaException;

    long contarBoletosCancelados() throws PersistenciaException;

    long contarTrenesActivos() throws PersistenciaException;

    BigDecimal calcularIngresosTotalesConfirmados()
            throws PersistenciaException;
}