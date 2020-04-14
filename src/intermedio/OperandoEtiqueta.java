/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package intermedio;

import Procesador.Declaracion;

/**
 *
 * @author jesus
 */
public class OperandoEtiqueta extends Operando {
    
    public String etiqueta;
    public OperandoEtiqueta(String etiqueta) {
        // TODO Ahora mismo en operando tenemos la declaracion.
        // Probablemente esto debería estar en un OperandoVariable o algo así
        // que nos permita meter utilizar etiquetas, variables, constantes y 
        // literales
        super(null, 0);
        this.etiqueta = etiqueta;
    }
    
    @Override
    public String toString() {        
        return etiqueta; 
    }

    @Override
    public String toMachineCode() {
        return etiqueta + ":  ;skip ;)\n";
    }
}
