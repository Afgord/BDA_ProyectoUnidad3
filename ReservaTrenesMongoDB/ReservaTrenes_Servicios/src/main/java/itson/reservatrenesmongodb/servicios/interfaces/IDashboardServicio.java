/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package itson.reservatrenesmongodb.servicios.interfaces;

import itson.reservatrenesmongodb.dtos.DashboardDTO;
import itson.reservatrenesmongodb.exceptions.ServicioException;

/**
 * Interfaz de servicio para obtener indicadores del dashboard.
 *
 * @author Afgord
 */
public interface IDashboardServicio {

    DashboardDTO consultarResumen() throws ServicioException;
}