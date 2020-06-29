package Checkers;

import Errores.ErrorSemantico;
import Procesador.GlobalVariables;

public class OverflowCheck {
	
	public static void string(String string) throws ErrorSemantico{
		Integer caracteresMaximosPermitidos = (GlobalVariables.MEMORY_DATA_BLOCK_SIZE_BYTES / Tipo.getTipo(Tipo.Char).getSize()) - 1;
		Integer tamanyoString = string.length();
		if(tamanyoString > caracteresMaximosPermitidos)
			throw new ErrorSemantico("El string supera el número de carácteres permitidos ("+tamanyoString+"/"+caracteresMaximosPermitidos+")");
	}
	
}
