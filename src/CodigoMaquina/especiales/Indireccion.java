package CodigoMaquina.especiales;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.OperandoEspecial;

public class Indireccion{
	
	public static OperandoEspecial __(Integer bytes, AddressRegister AX) {
		return new OperandoEspecial(bytes+"(" + AX + ")");
	}
	
}
