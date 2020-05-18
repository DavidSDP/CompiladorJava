package CodigoMaquina.especiales;

import CodigoMaquina.AddressRegister;
import CodigoMaquina.DataRegister;
import CodigoMaquina.OperandoEspecial;

public class Restore {
	
	public static OperandoEspecial __(DataRegister Dini, DataRegister Dfin, AddressRegister Aini, AddressRegister Afin) {
		return new OperandoEspecial(Dini+"-"+Dfin+"/"+Aini+"-"+Afin);
	}
	
	public static OperandoEspecial __(DataRegister Dini, AddressRegister Aini, AddressRegister Afin) {
		return new OperandoEspecial(Dini+"/"+Aini+"-"+Afin);
	}
	
	public static OperandoEspecial __(DataRegister Dini, DataRegister Dfin, AddressRegister Aini) {
		return new OperandoEspecial(Dini+"-"+Dfin+"/"+Aini);
	}
	
	public static OperandoEspecial __(DataRegister Dini, DataRegister Dfin) {
		return new OperandoEspecial(Dini+"-"+Dfin);
	}
	
	public static OperandoEspecial __(AddressRegister Aini, AddressRegister Afin) {
		return new OperandoEspecial(Aini+"-"+Afin);
	}
	
	public static OperandoEspecial __(DataRegister Dini, AddressRegister Aini) {
		return new OperandoEspecial(Dini+"/"+Aini);
	}
	
	public static OperandoEspecial __(DataRegister Dini) {
		return new OperandoEspecial(Dini+"");
	}
	
	public static OperandoEspecial __(AddressRegister Aini) {
		return new OperandoEspecial(Aini+"");
	}
	
}
