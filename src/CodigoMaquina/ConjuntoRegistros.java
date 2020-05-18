package CodigoMaquina;

import java.util.Stack;

public class ConjuntoRegistros {
	
	private Stack<DataRegister> dataRegisters;
	private Stack<AddressRegister> addressRegisters;
	
	public ConjuntoRegistros() {
		
	}
	
	public Boolean contains(DataRegister dataRegister) {
		return this.dataRegisters.contains(dataRegister);
	}
	
	public Boolean contains(AddressRegister addressRegister) {
		return this.addressRegisters.contains(addressRegister);
	}
	
	public void push(DataRegister dataRegister) {
		this.dataRegisters.push(dataRegister);
	}
	
	public void push(AddressRegister addressRegister) {
		this.addressRegisters.push(addressRegister);
	}
	
	public DataRegister popDataRegister() {
		return this.dataRegisters.pop();
	}
	
	public AddressRegister popAddressRegister() {
		return this.addressRegisters.pop();
	}
	
}