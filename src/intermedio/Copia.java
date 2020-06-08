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
import CodigoMaquina.OpCode;
import CodigoMaquina.Size;
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
        if(primero.getValor() instanceof DeclaracionConstante) {
        	bI.add(primero.loadStringDescriptorConstante(AddressRegister.A1));
        	bI.add(handleReferences(AddressRegister.A1, segundo));
        	bI.add(segundo.saveStringDescriptorConstante(AddressRegister.A1));
        } else {
        	bI.add(primero.loadStringDescriptorVariable(AddressRegister.A1));
            bI.add(handleReferences(AddressRegister.A1, segundo));
        	bI.add(segundo.saveStringDescriptorVariable(AddressRegister.A1));
        }
        if(!segundo.getValor().isInitialized())
        	segundo.getValor().markAsInitialized();
        return bI.toString();
    }

    public String basicTypeToMachineCode() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();

    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
    	bI.add(primero.load(DataRegister.D0));
    	bI.add(segundo.save(DataRegister.D0));

        return bI.toString();
    }

    @Override
    public String toMachineCode() {
        if (isComplexArgument(primero)) {
            return this.stringToMachineCode();
        } else {
            return this.basicTypeToMachineCode();
        }
    }

    private boolean isComplexArgument(Operando operando) {
        return Tipo.String.equals(primero.getValor().getTipo().getTipo()) || (primero.getValor() instanceof DeclaracionArray);
    }
}