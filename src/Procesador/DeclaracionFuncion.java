/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Procesador;

import Checkers.Tipo;

/**
 *
 * @author jesus
 */
public class DeclaracionFuncion extends Declaracion {

    private String etiqueta;

    public DeclaracionFuncion(Identificador identificador, Tipo tipo, String etiqueta) {
        super(identificador, tipo);
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }

}
