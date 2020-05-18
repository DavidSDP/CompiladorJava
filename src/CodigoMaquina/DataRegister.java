package CodigoMaquina;

public enum DataRegister implements Operando{
	D0, D1, D2, D3, D4, D5, D6, D7;
	@Override
	public String toString() {
		return this.name();
	}
}
