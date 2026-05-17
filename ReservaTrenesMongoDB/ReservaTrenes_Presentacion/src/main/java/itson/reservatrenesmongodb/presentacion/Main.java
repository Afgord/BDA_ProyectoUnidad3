/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.reservatrenesmongodb.presentacion;

import itson.reservatrenesmongodb.presentacion.frm.frmPrincipal;
import javax.swing.SwingUtilities;

/**
 * Clase principal encargada de iniciar la aplicación.
 *
 * @author Afgord
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frmPrincipal principal = new frmPrincipal();
            principal.setVisible(true);
        });
    }
}