package CodigoMaquina;

public class OperandoEspecial implements Operando{
	
	private String operandoEspecial;
	
	public OperandoEspecial(String operandoEspecial) {
		this.operandoEspecial = operandoEspecial;
	}
	
	@Override
	public String toString() {
		return this.operandoEspecial;
	}
	
}
