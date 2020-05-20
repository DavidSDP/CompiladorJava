package CodigoMaquina.especiales;

import CodigoMaquina.OperandoEspecial;

public class Literal {
	
	public static OperandoEspecial __(Integer literal) {
		return new OperandoEspecial("#" + literal);
	}
	
}
