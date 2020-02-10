package SimbolosNoTerminales;

public class SimboloParams{
	
	private SimboloOperacion operacion;
	private SimboloParams nextParam;
	
	public SimboloParams(SimboloOperacion o, SimboloParams n) {
		this.setOperacion(o);
		this.setNextParam(n);
	}

	public SimboloOperacion getOperacion() {
		return operacion;
	}

	public void setOperacion(SimboloOperacion operacion) {
		this.operacion = operacion;
	}

	public SimboloParams getNextParam() {
		return nextParam;
	}

	public void setNextParam(SimboloParams nextParam) {
		this.nextParam = nextParam;
	}
}
