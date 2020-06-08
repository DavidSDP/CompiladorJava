package intermedio;

import Checkers.Tipo;
import CodigoMaquina.AddressRegister;
import CodigoMaquina.BloqueInstrucciones;
import CodigoMaquina.DataRegister;
import CodigoMaquina.Instruccion;
import CodigoMaquina.OpCode;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.Size;
import CodigoMaquina.Variables;
import CodigoMaquina.especiales.Literal;

public class EQ extends InstruccionTresDirecciones {
    public EQ(Operando primero, Operando segundo, Operando resultado) {
        super(OperacionTresDirecciones.EQ);
        this.primero = primero;
        this.segundo = segundo;
        this.tercero = resultado;
    }

    public BloqueInstrucciones generateBranch() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
    	bI.add(primero.load(DataRegister.D0));
    	bI.add(segundo.load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, Size.W, DataRegister.D0, DataRegister.D1));
        bI.add(new Instruccion(OpCode.BEQ, new OperandoEspecial(tercero.toString())));
        return bI;
    }

    public BloqueInstrucciones generateOperation() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));

        // Preparado para Descriptor de String del tipo @.L y #.L
        if(Tipo.String.equals(primero.getValor().getTipo().getTipo())){
        	bI.add(primero.loadStringDescriptorVariable(AddressRegister.A0));
        	bI.add(segundo.loadStringDescriptorVariable(AddressRegister.A1));
        	bI.add(Instruccion.nuevaInstruccion("\t\t\tJSR\tSTREQUALS"));
        	bI.add(tercero.save(DataRegister.D0));
        } else if(Tipo.Array.equals(primero.getValor().getTipo().getTipo())){

        } else {
	        // Estoy casi convencido de que la comprobacion de igualdad se puede hacer como LT y familiares
        	bI.add(primero.load(DataRegister.D0));
        	bI.add(segundo.load(DataRegister.D1));
            bI.add(new Instruccion(OpCode.CMP, Size.W, DataRegister.D0, DataRegister.D1));
            bI.add(new Instruccion(OpCode.MOVE, Size.W, Variables.SR, DataRegister.D1));
            bI.add(new Instruccion(OpCode.AND, Size.W, Literal.__(4), DataRegister.D1));
            bI.add(new Instruccion(OpCode.LSR, Literal.__(2), DataRegister.D1));
			bI.add(tercero.save(DataRegister.D1));
        }

        return bI;
    }

    @Override
    public String toMachineCode() {
        /**
         * Disclaimer: El codigo que estás a punto de leer es una puta mierda
         * y htodo porque no tenemos diferenciadas las instrucciones de 3 direcciones
         * para las condiciones en los saltos de las operaciones que se asignan a algún lado.
         */
        if (tercero instanceof OperandoEtiqueta) {
            return generateBranch().toString();
        } else {
            return generateOperation().toString();
        }
    }

    public boolean isBranch() {
        return tercero instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new NE(primero, segundo, salto.tercero);
    }
}
