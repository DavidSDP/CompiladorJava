/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package intermedio;

import Checkers.Tipo;
import CodigoMaquina.*;
import CodigoMaquina.especiales.Indireccion;
import CodigoMaquina.especiales.Literal;
import Procesador.DeclaracionArray;
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

    protected BloqueInstrucciones handleReferences(AddressRegister AX, Operando target) {
        BloqueInstrucciones bI = new BloqueInstrucciones();
        // Increase source refcount
        bI.add(new Instruccion(OpCode.MOVE, Size.W, Indireccion.__(2, AX), DataRegister.D0));
        bI.add(new Instruccion(OpCode.ADDQ, Size.W, Literal.__(1), DataRegister.D0));
        bI.add(new Instruccion(OpCode.MOVE, Size.W, DataRegister.D0, Indireccion.__(2, AX)));

        if (target.getValor().isInitialized()) {
            // Decrease target refcount
            bI.add(target.loadStringDescriptorVariable(AddressRegister.A0));
            bI.add(Instruccion.nuevaInstruccion("\tjsr DECREASEREF"));
        }
        return bI;
    }

    public String stringToMachineCode() {
        /*
         * 1. Reservar memoria dinámica
         * 2. Poner contenido en la memoria
         * 3. Contar numero de caracteres
         * 4. Rellenar metainformación en registro
         *      - En offset 0 está la dirección
         *      - En offset 16 está el tamaño
         */
    	BloqueInstrucciones bI = new BloqueInstrucciones();
    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
        if(this.primero.getValor() instanceof DeclaracionConstante) {
        	bI.add(this.primero.loadStringDescriptorConstante(AddressRegister.A1));
        	bI.add(handleReferences(AddressRegister.A1, this.segundo));
        	bI.add(this.segundo.saveStringDescriptorConstante(AddressRegister.A1));
        } else {
        	bI.add(this.primero.loadStringDescriptorVariable(AddressRegister.A1));
            bI.add(handleReferences(AddressRegister.A1, this.segundo));
        	bI.add(this.segundo.saveStringDescriptorVariable(AddressRegister.A1));
        }

        this.segundo.getValor().markAsInitialized();
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
        if (isComplexArgument(this.primero)) {
            return this.stringToMachineCode();
        } else {
            return this.basicTypeToMachineCode();
        }
    }

    private boolean isComplexArgument(Operando operando) {
        return Tipo.String.equals(this.primero.getValor().getTipo().getTipo()) || (this.primero.getValor() instanceof DeclaracionArray);
    }
}