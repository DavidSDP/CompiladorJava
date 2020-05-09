/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

import Checkers.Tipo;
import Procesador.DeclaracionConstante;

/**
 * Primero es el origen de la copia. Puede ser una constante o una variable
 * Segundo es el destino a donde se quiere copiar. Esto solo puede ser una
 * variable.
 * <p>
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

        sb.append(super.toMachineCode());
        sb.append(this.primero.load("D0"))
                .append(this.segundo.save("D0"));

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