package CodigoMaquina;

public class Instruccion {

	private static int SEQ_LOCAL_LABEL = 0;
	
	private OpCode opCode;
	private Size size;
	private Operando op1;
	private Operando op2;
	private String etiqueta;
	private String intruccionPersonalizada;

	private static final String TAB = "\t";
	
	public String toVisualRepresentation() {
		StringBuilder stringVisualRepresentation = new StringBuilder();
		if(intruccionPersonalizada != null) {
			stringVisualRepresentation.append(intruccionPersonalizada);
		}else if(etiqueta != null) {
			stringVisualRepresentation.append(etiqueta + ": skip");
		}else if(op2 != null) {
			if(size != null) {
				stringVisualRepresentation.append(opCode + "." + size + " " + op1 + ", " + op2);
			}else {
				stringVisualRepresentation.append(opCode + " " + op1 + ", " + op2);
			}
		}else if(op1 != null) {
			if(size != null) {
				stringVisualRepresentation.append(opCode + "." + size + " " + op1);
			}else {
				stringVisualRepresentation.append(opCode + " " + op1);
			}
		}
		return stringVisualRepresentation.toString();
	}
	
	public String toMachineCode() {
		StringBuilder stringMachineCode = new StringBuilder();
		if(intruccionPersonalizada != null) {
			stringMachineCode.append(intruccionPersonalizada);
		}else if(etiqueta != null) {
			stringMachineCode.append(etiqueta);
		}else if(op2 != null) {
			if(size != null) {
				stringMachineCode.append(TAB+TAB+TAB+opCode + "." + size + " " + TAB + op1 + ", " + op2);
			}else {
				stringMachineCode.append(TAB+TAB+TAB+opCode + " " + TAB + op1 + ", " + op2);
			}
		}else if(op1 != null) {
			if(size != null) {
				stringMachineCode.append(TAB+TAB+TAB+opCode + "." + size + TAB + op1);
			}else {
				stringMachineCode.append(TAB+TAB+TAB+opCode + " " + TAB + op1);
			}
		} else {
			// Hay instrucciones que solo es el opcode ( rts )
			stringMachineCode.append(TAB+TAB+TAB+opCode);
		}
		return stringMachineCode.toString();
	}
	
	public static Instruccion nuevaInstruccion(String intruccionPersonalizada) {
		Instruccion nueva = new Instruccion();
		nueva.setIntruccionPersonalizada(intruccionPersonalizada);
		return nueva;
	}
	
	public Instruccion() {
	}
	
	public Instruccion(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	
	public Instruccion(OpCode opCode) {
		this.opCode = opCode;
	}
	
	public Instruccion(OpCode opCode, Size size, Operando op1) {
		this.opCode = opCode;
		this.size = size;
		this.op1 = op1;
	}
	
	public Instruccion(OpCode opCode, Size size, Operando op1, Operando op2) {
		this.opCode = opCode;
		this.size = size;
		this.op1 = op1;
		this.op2 = op2;
	}
	
	public Instruccion(OpCode opCode, Operando op1) {
		this.opCode = opCode;
		this.op1 = op1;
	}
	
	public Instruccion(OpCode opCode, Operando op1, Operando op2) {
		this.opCode = opCode;
		this.op1 = op1;
		this.op2 = op2;
	}

	public String getIntruccionPersonalizada() {
		return intruccionPersonalizada;
	}

	public void setIntruccionPersonalizada(String intruccionPersonalizada) {
		this.intruccionPersonalizada = intruccionPersonalizada;
	}

	/**
	 * Used to automate jumps
	 */
	public static String getLocalLabel() {
		return "l" + SEQ_LOCAL_LABEL++;
	}
	
}