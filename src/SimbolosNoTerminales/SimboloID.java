package SimbolosNoTerminales;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;

public class SimboloID extends ComplexSymbol{
	
	public SimboloID(ComplexSymbol i) {
		super(i.getName(), i.sym, i.value);
	}
	
}
