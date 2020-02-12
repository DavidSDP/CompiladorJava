package Errores;

public class ErrorProcesador extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String mensaje;
	
	public ErrorProcesador(String mensaje) {
		this.setMensaje(mensaje);
	}
	
	protected String getErrorLine() {
		if(getMensaje() != null)
			return getMensaje();
		return "Error no controlado";
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}