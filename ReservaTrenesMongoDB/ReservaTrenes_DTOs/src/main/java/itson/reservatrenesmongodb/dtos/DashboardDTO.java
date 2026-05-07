/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.dtos;

/**
 * DTO que representa el resumen de información mostrado en el dashboard.
 *
 * Contiene indicadores generales del sistema, obtenidos a partir de consultas
 * agregadas en MongoDB.
 *
 * @author Afgord
 */
public class DashboardDTO {

    private long totalViajesProgramados;
    private long totalBoletosConfirmados;
    private long totalBoletosCancelados;
    private long totalTrenesActivos;
    private String ingresosTotalesConfirmados;

    public DashboardDTO() {
    }

    public DashboardDTO(long totalViajesProgramados,
            long totalBoletosConfirmados,
            long totalBoletosCancelados,
            long totalTrenesActivos,
            String ingresosTotalesConfirmados) {
        this.totalViajesProgramados = totalViajesProgramados;
        this.totalBoletosConfirmados = totalBoletosConfirmados;
        this.totalBoletosCancelados = totalBoletosCancelados;
        this.totalTrenesActivos = totalTrenesActivos;
        this.ingresosTotalesConfirmados = ingresosTotalesConfirmados;
    }

    public long getTotalViajesProgramados() {
        return totalViajesProgramados;
    }

    public void setTotalViajesProgramados(long totalViajesProgramados) {
        this.totalViajesProgramados = totalViajesProgramados;
    }

    public long getTotalBoletosConfirmados() {
        return totalBoletosConfirmados;
    }

    public void setTotalBoletosConfirmados(long totalBoletosConfirmados) {
        this.totalBoletosConfirmados = totalBoletosConfirmados;
    }

    public long getTotalBoletosCancelados() {
        return totalBoletosCancelados;
    }

    public void setTotalBoletosCancelados(long totalBoletosCancelados) {
        this.totalBoletosCancelados = totalBoletosCancelados;
    }

    public long getTotalTrenesActivos() {
        return totalTrenesActivos;
    }

    public void setTotalTrenesActivos(long totalTrenesActivos) {
        this.totalTrenesActivos = totalTrenesActivos;
    }

    public String getIngresosTotalesConfirmados() {
        return ingresosTotalesConfirmados;
    }

    public void setIngresosTotalesConfirmados(String ingresosTotalesConfirmados) {
        this.ingresosTotalesConfirmados = ingresosTotalesConfirmados;
    }
}