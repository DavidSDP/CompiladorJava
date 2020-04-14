/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

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
        return this.primero.toString() + ": ;skip ;)\n";
    }
}
