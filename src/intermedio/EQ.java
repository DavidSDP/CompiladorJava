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
        this.setPrimero(primero);
        this.setSegundo(segundo);
        this.setTercero(resultado);
    }

    public BloqueInstrucciones generateBranch() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();
    	bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));
    	bI.add(this.getPrimero().load(DataRegister.D0));
    	bI.add(this.getSegundo().load(DataRegister.D1));
        bI.add(new Instruccion(OpCode.CMP, DataRegister.D0, DataRegister.D1));
        bI.add(new Instruccion(OpCode.BEQ, new OperandoEspecial(this.getTercero().toString())));
        return bI;
    }

    public BloqueInstrucciones generateOperation() {
    	BloqueInstrucciones bI = new BloqueInstrucciones();

        bI.add(Instruccion.nuevaInstruccion(super.toMachineCode()));

        // Preparado para Descriptor de String del tipo @.L y #.L
        if(Tipo.String.equals(this.getPrimero().getValor().getTipo().getTipo())){
        	bI.add(this.getPrimero().loadStringDescriptorVariable(AddressRegister.A0));
        	bI.add(this.getSegundo().loadStringDescriptorVariable(AddressRegister.A1));
        	bI.add(Instruccion.nuevaInstruccion("\t\t\tJSR\tSTREQUALS"));
        	bI.add(this.getTercero().save(DataRegister.D0));
        } else if(Tipo.Array.equals(this.getPrimero().getValor().getTipo().getTipo())){

        } else {
	        // Estoy casi convencido de que la comprobacion de igualdad se puede hacer como LT y familiares
        	bI.add(this.getPrimero().load(DataRegister.D0));
        	bI.add(this.getSegundo().load(DataRegister.D1));
            bI.add(new Instruccion(OpCode.CMP, DataRegister.D0, DataRegister.D1));
            bI.add(new Instruccion(OpCode.MOVE, Size.W, Variables.SR, DataRegister.D1));
            bI.add(new Instruccion(OpCode.AND, Size.W, Literal.__(4), DataRegister.D1));
            bI.add(new Instruccion(OpCode.LSR, Literal.__(2), DataRegister.D1));
			bI.add(this.getTercero().save(DataRegister.D1));
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
        if (this.getTercero() instanceof OperandoEtiqueta) {
            return generateBranch().toString();
        } else {
            return generateOperation().toString();
        }
    }

    public boolean isBranch() {
        return this.getTercero() instanceof OperandoEtiqueta;
    }

    public InstruccionTresDirecciones getComplementario(Goto salto) {
        return new NE(getPrimero(), getSegundo(), salto.getTercero());
    }
}
