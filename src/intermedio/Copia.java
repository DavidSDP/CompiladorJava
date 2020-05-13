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
        /**
         * 1. Reservar memoria dinámica
         * 2. Poner contenido en la memoria
         * 3. Contar numero de caracteres
         * 4. Rellenar metainformación en registro
         *      - En offset 0 está la dirección
         *      - En offset 16 está el tamaño
         */
        // guardar A0
        StringBuilder sb = new StringBuilder();
        sb.append(super.toMachineCode());
        if (this.primero.getValor() instanceof DeclaracionConstante) {
        	// Copia String Constante
                    // Reservar espacio en memoria dinamica y crear descriptor
                    // Ojo, las rutinas del DMM están demasiado lejos para usar bsr
            		// ^ Hostia es verdad, qu� putada xD
        	
            DeclaracionConstante constante = (DeclaracionConstante) this.primero.getValor();
            String text = (String) constante.getValor();
            int size = text.length();
            
            sb.append("\tmovem.l A1/D0, -(A7)\n")
	            .append("\tjsr DMMALLOC\n")
	            .append("\tclr.l D0\n")
	            .append("\tmove.l #").append(size).append(", D0\n")
	            .append("\tmove.l A0, A1\n");
            for (int idx = 0; idx < size; idx++) {
                sb.append("\tmove.w #").append((int)text.charAt(idx)).append(", (A1)+\n");
            }
            sb.append("\tmovem.l (A7)+, A1/D0\n");
            sb.append(this.segundo.putActivationBlockAddressInRegister())
            /*
             * 1000 BP XXXX
             * 1004 STRING #
             * 1008 STRING @
             * 100C
             */
            .append("\tmove.l A6, A1\n")
            .append("\tadd.l #").append(this.segundo.getValor().getDesplazamiento()).append(", A1\n")
            .append("\tmove.l D0, (A1)\n")
            .append("\tmove.l A0, 4(A1)\n");
        }else {
        	// Copia String Variable
            sb.append("\tmove.l #0, A6\n");
            sb.append(this.primero.putActivationBlockAddressInRegister())
		            .append("\tmove.l A6, A0\n")
		            .append("\tadd.l #").append(this.primero.getValor().getDesplazamiento()).append(", A0\n");
            sb.append("\tmove.l #0, A6\n");
		    sb.append(this.segundo.putActivationBlockAddressInRegister())
		            .append("\tmove.l A6, A1\n")
		            .append("\tadd.l #").append(this.segundo.getValor().getDesplazamiento()).append(", A1\n")
            		.append("\tmove.l (A0), (A1)\n")
            		.append("\tmove.l 4(A0), 4(A1)\n");
        }
        return sb.toString();
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
        if (Tipo.String.equals(this.primero.getValor().getTipo().getTipo())) {
            return this.stringToMachineCode();
        } else {
            return this.basicTypeToMachineCode();
        }
//        return this.basicTypeToMachineCode();
    }
}