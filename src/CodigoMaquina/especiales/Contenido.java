package CodigoMaquina.especiales;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.OperandoEspecial;

public class Contenido {
	
	public static OperandoEspecial __(AddressRegister AX) {
		return new OperandoEspecial("(" + AX + ")");
	}
	public static OperandoEspecial __(int offset, AddressRegister AX) {
		return new OperandoEspecial( offset + "(" + AX + ")");
	}
	
}
