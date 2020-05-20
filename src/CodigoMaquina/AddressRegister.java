package CodigoMaquina;

public enum AddressRegister implements Operando{
	A0, A1, A2, A3, A4, A5, A6;
	@Override
	public String toString() {
		return this.name();
	}
}
