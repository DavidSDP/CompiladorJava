package CodigoMaquina;

public enum StackPointer implements Operando{
	A7;
	@Override
	public String toString() {
		return this.name();
	}
}
