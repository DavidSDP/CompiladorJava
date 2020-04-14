package SimbolosNoTerminales;

import java.util.ArrayList;
import java.util.List;

import Checkers.Tipo;
import Checkers.TipoObject;
import Procesador.DeclaracionArray;
import Procesador.GlobalVariables;
import Procesador.Declaracion;
import Procesador.TipoSubyacente;
import analisisSintactico.sym;
import analisisSintactico.arbol.INodo;
import analisisSintactico.arbol.Nodo;
import analisisSintactico.arbol.SimboloTerminal;

public class SimboloFactor extends Nodo implements TipoSubyacente {

    private String id;
    private String string;
    private String numero;
    private String booleano;
    private SimboloFuncionInvk funcionInvk;
    private SimboloOperacion operacion;

    private Declaracion decl;
    private int arrayIndex;


    public SimboloFactor(Declaracion decl) {
        this.decl = decl;
        this.arrayIndex = -1;
    }

	public SimboloFactor(Declaracion decl, int index) {
		this.decl = decl;
		this.arrayIndex = index;
	}

    public SimboloFactor(String s, Tipo tipo) {
    	this.arrayIndex = -1;
        switch (tipo) {
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
    	this.arrayIndex = -1;
        this.funcionInvk = funcionInvk;
    }

    public SimboloFactor(SimboloOperacion operacion) {
    	this.arrayIndex = -1;
        this.operacion = operacion;
    }

    @Override
    public List<INodo> getChildren() {
        List<INodo> hijos = new ArrayList<>();
        if (id != null) {
            hijos.add(new SimboloTerminal(id, Tipo.Identificador));
            return hijos;
        }
        if (numero != null) {
            hijos.add(new SimboloTerminal(numero, Tipo.Integer));
            return hijos;
        }
        if (booleano != null) {
            hijos.add(new SimboloTerminal(booleano, Tipo.Boolean));
            return hijos;
        }
        if (string != null) {
            String newString = new String(string);
            newString = newString.replace("\"", "'");
            hijos.add(new SimboloTerminal(newString, Tipo.String));
            return hijos;
        }
        if (funcionInvk != null) {
            hijos.add(funcionInvk);
            return hijos;
        }
        if (operacion != null) {
            hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENIZQ], Tipo.Token));
            hijos.add(operacion);
            hijos.add(new SimboloTerminal(sym.terminalNames[sym.PARENDER], Tipo.Token));
            return hijos;
        }
        return hijos;
    }

    @Override
    public String getName() {
        return "SimboloFactor";
    }

    @Override
    public TipoObject getTipoSubyacente() {
        if (id != null) {
            Declaracion identificador = GlobalVariables.entornoActual().fullGet(id);
            if (identificador != null) {
                return identificador.getTipo();
            }
        } else if (string != null) {
            return Tipo.getTipoSafe(Tipo.String);
        } else if (numero != null) {
            return Tipo.getTipoSafe(Tipo.Integer);
        } else if (booleano != null) {
            return Tipo.getTipoSafe(Tipo.Boolean);
        } else if (funcionInvk != null) {
            return funcionInvk.getTipoSubyacente();
        } else if (operacion != null) {
            return operacion.getTipoSubyacente();
        }

        if (this.arrayIndex > -1) {
			return ((DeclaracionArray)decl).getTipoDato();
		} else {
        	return decl.getTipo();
		}
    }

    public Declaracion getDeclaracionResultado() {
        return decl;
    }

}
