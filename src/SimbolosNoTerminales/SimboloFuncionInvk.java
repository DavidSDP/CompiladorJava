package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Procesador.GlobalVariables;
import Procesador.Declaracion;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloFuncionInvk extends Nodo implements TipoSubyacente{
	
	private String idFuncion;
	private SimboloParams params;
        private Declaracion declaracionRetorno;
	
	public SimboloFuncionInvk(String i, SimboloParams p) {
		this.idFuncion = i;
		this.params = p;
	}
	
	@Override
	public List<INodo> getChildren() {
		List<INodo> hijos = new ArrayList<>();
		hijos.add(new SimboloTerminal(idFuncion, Tipo.Identificador));
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
		if(params != null)
			hijos.add(params);
		hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
		return hijos;
	}
	
	@Override
	public String getName() {
		return "SimboloFuncionInvk";
	}

	@Override
	public Tipo getTipoSubyacente() {
		Declaracion identificadorFuncion = GlobalVariables.entornoActual().fullGetFuncion(idFuncion);
		return identificadorFuncion.getTipo();
	}
        
        /**
         * TODO No estoy seguro de que las funciones se tengan que gestionar
         * asi ya que realmente se interactua con la pila para obtener los
         * parametros de la llamada.
         * 
         * Probablemente no se utilice de la misma forma que las variables
         * locales
         * @return 
         */
        public Declaracion getDeclaracion() {
            return declaracionRetorno;
        }
	
}
