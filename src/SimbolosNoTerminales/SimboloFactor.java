package SimbolosNoTerminales;

import Procesador.Tipo;

public class SimboloFactor implements TipoSubyacente{
	
	private String id;
	private String string;
	private String numero;
	private String booleano;
	private SimboloFuncionInvk funcionInvk;
	private SimboloOperacion operacion;

	public SimboloFactor(String s, Tipo tipo) {
		switch(tipo) {
			case Boolean:
				this.booleano = s;
				break;
			case Integer:
				this.numero = s;
				break;
			case String:
				this.string = s;
				break;
			case Identificador:
				this.id = s;
				break;
			default:
				throw new Error("Mal uso -> generación de nuevo SimboloFactor()");
		}
	}
	
	public SimboloFactor(SimboloFuncionInvk funcionInvk) {
		this.funcionInvk = funcionInvk;
	}
	public SimboloFactor(SimboloOperacion operacion) {
		this.operacion = operacion;
	}

	@Override
	public Tipo getTipoSubyacente() {
		throw new Error("NYI");
//		if(id != null) {
//			return GlobalVariables.getTipoIdentificadorEnEntornoActual(id);
//		}else if(string != null) {
//			return Tipo.String;
//		}else if(numero != null) {
//			return Tipo.Integer;
//		}else if(booleano != null) {
//			return Tipo.Boolean;
//		}else if() {
//			
//		}else if() {
//			
//		}
	}
	
}
