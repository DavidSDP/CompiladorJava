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
    private Integer numero;
    private String booleano;
    private SimboloFuncionInvk funcionInvk;
    private SimboloOperacion operacion;

    /**
     * Decl contendrá la declaración de la variable que contiene
     * la información requerida.
     * En el caso de los arrays es algo especial:
     *  1.  Si es un array ( sin dereferenciación a un elemento ) decl contendrá
     *      la declaración del array.
     *  2.  Si es la dereferenciación del array, decl contiene la variable que
     *      contiene el valor del elemento del array
     */
    private Declaracion decl;
    private Declaracion array;
    private int arrayIndex;

    @Override
    public String toString() {
        if (id != null) {
            return id;
        } else if (string != null) {
            return string;
        } else if (numero != null) {
            return numero.toString();
        } else if (booleano != null) {
            return booleano;
        }
        return "";
    }

    public SimboloFactor(Declaracion decl) {
        this.decl = decl;
        this.arrayIndex = -1;
    }

	public SimboloFactor(Declaracion decl, int index) {
		this.decl = decl;
		this.arrayIndex = index;
	}

    public SimboloFactor(Declaracion decl, int index, Declaracion arrayElement) {
        this.decl = arrayElement;
        this.arrayIndex = index;
        this.array = decl;
    }

    public SimboloFactor(String s, Tipo tipo) {
    	this.arrayIndex = -1;
        switch (tipo) {
            case Boolean:
                this.booleano = s;
                break;
            case Integer:
                this.numero = Integer.parseInt(s);
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
            hijos.add(new SimboloTerminal(numero.toString(), Tipo.Integer));
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
            return null;
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
        } else {
            return decl.getTipo();
        }
    }

    public Declaracion getDeclaracionResultado() {
        return decl;
    }

}
