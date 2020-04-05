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
        // Cada toString de un operando (alomejor debería ser un toMachineCode también)
        // debería encargarse de traducir el operador dependiendo del modo de direccionamiento! :+1:
        String primeroStr = this.primero.toString();
        String segundoStr = this.segundo.toString();
        
        return "MOVE" + primeroStr + segundoStr;
    }
}