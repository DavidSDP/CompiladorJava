package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.DeclaracionFuncion;
import Procesador.GlobalVariables;
import Procesador.Declaracion;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloFuncionInvk extends Nodo implements TipoSubyacente {

    private String idFuncion;
    private SimboloParams params;
    private DeclaracionFuncion declFuncion;
    private Declaracion declaracionRetorno;

    public SimboloFuncionInvk(String i, SimboloParams p) {
        this.idFuncion = i;
        this.params = p;
    }

	public SimboloFuncionInvk(DeclaracionFuncion decl, Declaracion declRetorno, String i, SimboloParams p) {
    	this.declFuncion = decl;
    	this.declaracionRetorno = declRetorno;
		this.idFuncion = i;
		this.params = p;
	}

    @Override
    public List<INodo> getChildren() {
        List<INodo> hijos = new ArrayList<>();
        hijos.add(new SimboloTerminal(idFuncion, Tipo.Identificador));
        hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
        if (params != null)
            hijos.add(params);
        hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
        return hijos;
    }

    @Override
    public String getName() {
        return "SimboloFuncionInvk";
    }

    @Override
    public TipoObject getTipoSubyacente() {
        DeclaracionFuncion identificadorFuncion = GlobalVariables.entornoActual().fullGetFuncion(idFuncion);
        return identificadorFuncion.getTipo();
    }

    /**
     * TODO No estoy seguro de que las funciones se tengan que gestionar
     * asi ya que realmente se interactua con la pila para obtener los
     * parametros de la llamada.
     * <p>
     * Probablemente no se utilice de la misma forma que las variables
     * locales
     *
     * @return
     */
    public Declaracion getDeclaracion() {
    	return declaracionRetorno;
    }

	/**
	 * Devuelve la declaracion de la función con
	 * toda la información ( incluida la etiqueta de inicio )
	 */
	public DeclaracionFuncion getDeclaracionFuncion() {
		return this.declFuncion;
	}

}
