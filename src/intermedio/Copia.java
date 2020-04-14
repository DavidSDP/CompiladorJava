/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

/**
 * Primero es el origen de la copia. Puede ser una constante o una variable
 * Segundo es el destino a donde se quiere copiar. Esto solo puede ser una 
 * variable.
 * 
 * Nota para el futuro: No es necesario comprobar si la instrucción es correcta
 * en cuanto a constancia de valores y tipos ya que eso debería haberse 
 * comprobado antes. Esto complicaría bastantes las cosas.
 * 
 * @author jesus
 */
public class Copia extends InstruccionTresDirecciones {
    public Copia(Operando primero, Operando segundo) {
        super(OperacionTresDirecciones.COPIA);
        this.primero = primero;
        this.segundo = segundo;
    }

    @Override
    public String toMachineCode() {
        StringBuilder sb = new StringBuilder();


        // Código específico del 68K. No acabo de ver como abstraerlo. Probablemente esto debería se una clase abstracta
        // Y lo que se debería instanciar es una clase que implementara el toMachineCode de forma específica.
        // Dicho esto, de momento se queda asi

        sb.append(putActivationBlockAddressInRegister(this.primero))
                .append("\tmove ").append(this.primero.getValor().getDesplazamiento()).append("(A6), D0\n")
                .append(putActivationBlockAddressInRegister(this.segundo))
                .append("\tmove D0, ").append(this.segundo.getValor().getDesplazamiento()).append("(A6)\n");

        return sb.toString();
    }
}