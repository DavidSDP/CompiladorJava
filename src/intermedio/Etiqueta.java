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
        // En el caso del 68k el add directamente guarda la info en el segundo
        // operando, así que necesitamos guardar el valor en el registro para
        // devolver donde toque eso.
        StringBuilder sb = new StringBuilder();
        
        /**
         * 1. Cargamos el valor del primer elemento en el registro
         * 2. Le div el valor del segundo al registro
         * 3. Copiamos el valor del registro a la variable de destino
         */
        sb.append(primero.toString() + ": skip \n");
        
        return sb.toString();
    }
}
