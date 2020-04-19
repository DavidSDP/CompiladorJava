/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

import Checkers.Tipo;

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

    public String stringToMachineCode() {
        /*
         * Para poder realizar la copia de un string podemos plantearnos dos tipos de forma de trabajar:
         *      - Usar el heap
         *      - Usar el bloque de activacion
         *
         * En el caso de utilizar el bloque de activación, no podríamos manejar asignaciones de un string de dimensiones
         * diferentes ya que no se puede transformar el tamaño en el bloque de activación.
         *
         * Por otro lado, si utilizamos el heap, necesitamos utilizar el bloque de activación para almacenar el tamaño del string
         * y la dirección al heap. Esto sería lo ideal pero necesitamos instrucciones ad-hoc para poder gestionar las operaciones
         * de copia:
         *      - 1. Copiar de origen a nueva memoria en heap
         *      - 2. Copiar la nueva dirección del heap al bloque de activación
         *      - 3. Actualizar el tamaño en el bloque de activación
         */
        return "";
    }

    public String basicTypeToMachineCode() {
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

    @Override
    public String toMachineCode() {
//        if (this.tercero.getValor().getTipo().getTipo() == Tipo.String) {
//            return this.stringToMachineCode();
//        } else {
//            return this.basicTypeToMachineCode();
//        }
        return this.basicTypeToMachineCode();
    }
}