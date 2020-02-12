package analisisSintactico.arbol;

import java.util.List;

import Procesador.GlobalVariables;

public class Nodo implements INodo{
	
	private Integer identificadorNodoUnico = GlobalVariables.getNodoIdentificadorUnico();
	
	@Override
	public List<INodo> getChildren() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Integer getIdentificadorUnico() {
		return identificadorNodoUnico;
	}

}
