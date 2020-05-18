package CodigoMaquina.especiales;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.OperandoEspecial;
import CodigoMaquina.StackPointer;

public class PostIncremento {
	
	public static OperandoEspecial __(AddressRegister AX) {
		return new OperandoEspecial("(" + AX + ")+");
	}
	
	public static OperandoEspecial __(StackPointer A7) {
		return new OperandoEspecial("(" + A7 + ")+");
	}
	
}
