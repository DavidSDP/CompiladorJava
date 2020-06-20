/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

import Procesador.Declaracion;

import java.util.ArrayList;

/**
 *
 * @author jesus
 */
public class Etiqueta extends InstruccionTresDirecciones {
    public Etiqueta(Operando primero) {
        super(OperacionTresDirecciones.ETIQUETA);
        this.primero = primero;
    }  

    @Override
    public String toMachineCode() {
        return primero.toString() + ": ;skip ;)\n";
    }

    @Override
    public boolean esDefinicion() {
        return false;
    }

    @Override
    public ArrayList<Declaracion> getArgumentos() {
        return new ArrayList<>();
    }

    public String getEtiqueta() {
        OperandoEtiqueta et = (OperandoEtiqueta)primero;
        return et.toString();
    }
}
