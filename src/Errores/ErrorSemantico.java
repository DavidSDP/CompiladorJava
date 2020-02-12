 package Errores;

public class ErrorSemantico extends ErrorProcesador{
	
	private static final long serialVersionUID = 1L;
	
	private String str;
	
	private Integer linea;
	
	public ErrorSemantico(String str) {
		super("");
		this.str = str;
	}
	
	@Override
	protected String getErrorLine() {
		return this.str;
	}

	public Integer getLinea() {
		return linea;
	}

	public void setLinea(Integer linea) {
		this.linea = linea;
	}
}
