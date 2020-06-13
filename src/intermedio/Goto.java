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
public class Goto extends InstruccionTresDirecciones {
    public Goto(Operando primero) {
        super(OperacionTresDirecciones.GOTO);
        this.primero = primero;
    } 

    @Override
    public String toMachineCode() {
        return "\tbra " + primero.toString() + "\n";
    }

    @Override
    public boolean esDefinicion() {
        return false;
    }

    @Override
    public ArrayList<Declaracion> getArgumentos() {
        return new ArrayList<>();
    }
}
