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
public class Goto extends InstruccionTresDirecciones {
    public Goto(Operando primero) {
        super(OperacionTresDirecciones.GOTO);
        this.primero = primero;
    } 

    @Override
    public String toMachineCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}