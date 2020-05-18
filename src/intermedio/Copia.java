/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

import Checkers.Tipo;
import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
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
    	
    	BloqueInstrucciones bI = new BloqueInstrucciones();
    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        if(this.primero.getValor() instanceof DeclaracionConstante) {
        	bI.add(this.primero.loadStringDescriptorConstante(DataRegister.D0, AddressRegister.A1));
        	bI.add(this.segundo.saveStringDescriptorConstante(DataRegister.D0, AddressRegister.A1));
        }else {
        	bI.add(this.primero.loadStringDescriptorVariable(AddressRegister.A1));
        	bI.add(this.segundo.saveStringDescriptorVariable(AddressRegister.A1));
        }
        return bI.toString();
    }

    public String basicTypeToMachineCode() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();

    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
    	bI.add(this.primero.load(DataRegister.D0));
    	bI.add(this.segundo.save(DataRegister.D0));

        return bI.toString();
    }

    @Override
    public String toMachineCode() {
        if (Tipo.String.equals(this.primero.getValor().getTipo().getTipo())) {
            return this.stringToMachineCode();
        } else {
            return this.basicTypeToMachineCode();
        }
    }
}