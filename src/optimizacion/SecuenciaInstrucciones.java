package optimizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import intermedio.BloqueBasico;
import intermedio.Clase;
import intermedio.EQ;
import intermedio.Etiqueta;
import intermedio.GT;
import intermedio.GTE;
import intermedio.Goto;
import intermedio.Grafo;
import intermedio.InstruccionTresDirecciones;
import intermedio.LT;
import intermedio.LTE;
import intermedio.NE;
import intermedio.OperandoEtiqueta;
import intermedio.Preambulo;
import intermedio.Retorno;

public class SecuenciaInstrucciones implements Iterator<InstruccionTresDirecciones>, Cloneable{
	
	private List<InstruccionTresDirecciones> instrucciones;
	private Integer currentIndex;
	private Integer size;
	
	public SecuenciaInstrucciones(List<InstruccionTresDirecciones> instrucciones) {
		this.instrucciones = instrucciones;
		this.size = instrucciones.size();
		this.currentIndex = 0;
	}

	@Override
	public boolean hasNext() {
		return currentIndex < size;
	}

	@Override
	public InstruccionTresDirecciones next() {
		if(!hasNext())
			return null;
		return getInstrucciones().get(currentIndex++);
	}
	
	public InstruccionTresDirecciones upcoming() {
		if(!hasNext())
			return null;
		return getInstrucciones().get(currentIndex);
	}
	
	public InstruccionTresDirecciones current() {
		if(currentIndex < 1)
			return null;
		return getInstrucciones().get(currentIndex-1);
	}
	
	public InstruccionTresDirecciones get(Integer indice) {
		if(indice >= 0 && indice < this.size)
			return this.instrucciones.get(indice);
		return null;
	}
	
	public InstruccionTresDirecciones getFollowing(Integer indice) {
		if(indice+1 >= 0 && indice+1 < this.size)
			return this.instrucciones.get(indice+1);
		return null;
	}

	public List<InstruccionTresDirecciones> getInstrucciones() {
		return instrucciones;
	}
	
	@Override
	public SecuenciaInstrucciones clone(){
		try {
			return (SecuenciaInstrucciones) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public RangoInstruccionesFuncion getSiguienteFuncion(int inicioBusqueda) {
        int inicio, fin, actual;
        boolean encontradoFin = false;
        InstruccionTresDirecciones instruccion;
        inicio = fin = -1;
        for (actual = inicioBusqueda; actual < instrucciones.size() && !encontradoFin; actual++) {
            instruccion = instrucciones.get(actual);
            if (instruccion instanceof Preambulo && inicio == -1) {
                // La primera "instruccion" de una funcion es su etiqueta, pero es más fácil identificar preambulos.
                inicio = actual - 1;
            } else if (instruccion instanceof Preambulo) {
                // Ha encontrado el preambulo de otra función, por tanto, debemos retroceder 2 instrucciones
                // (preambulo + etiqueta) para encontrar la última instrucción de la funcion
                fin = actual - 2;
                encontradoFin = true;
            } else if (instruccion instanceof Clase) {
                // Hemos encontrado la declaración de la clase, por tanto, también se ha finalizado la función
                // actual
                fin = actual - 2;
                encontradoFin = true;
            } else if (instruccion instanceof Retorno) {
                // Hemos encontrado un posible final, pero no podemos asegurar que no haya más instrucciones despues
                // por tanto, seguimos buscando hasta que encontremos la declaración de una funcion
                // o hasta que acabemos las instrucciones
                fin = actual;
            }
        }

        if (inicio > -1) {
            fin = encontradoFin ? fin : actual - 1;
            return new RangoInstruccionesFuncion(inicio, fin);
        } else {
            // No hemos encontrado más funciones
            return null;
        }
    }

	public Grafo getGrafoFlujoFuncion(RangoInstruccionesFuncion rango) {
        int idBloque = 0;
        boolean finales;
        BloqueBasico e, s, bloque, siguienteBloque;
        InstruccionTresDirecciones instruccion, siguiente;
        Grafo grafoFlujoBloquesBasicos = new Grafo();
        ArrayList<BloqueBasico> bloques = new ArrayList<>();
        HashMap<OperandoEtiqueta, BloqueBasico> etiquetaToLider = new HashMap<>();
        OperandoEtiqueta etiqueta;

        e = new BloqueBasico(++idBloque);
        s = new BloqueBasico(++idBloque);
        bloques.add(e);
        bloques.add(s);
        grafoFlujoBloquesBasicos.addVertice(e);
        grafoFlujoBloquesBasicos.addVertice(s);

        // Identificacion de lideres
        for (int i = rango.getInicio(); i <= rango.getFin(); i++) {
            instruccion = instrucciones.get(i);

            if (isEtiqueta(instruccion)) {
                bloque = new BloqueBasico(++idBloque, i);
                grafoFlujoBloquesBasicos.addVertice(bloque);
                bloques.add(bloque);
                etiqueta = (OperandoEtiqueta)instruccion.getPrimero();
                etiquetaToLider.put(etiqueta, bloque);
            } else if (isConditionalBranch(instruccion)) {
                siguiente = getFollowing(i + 1);
                finales = isBranch(siguiente) || isRetorno(siguiente) || isEtiqueta(siguiente);
                if (!finales) {
                    bloque = new BloqueBasico(++idBloque, i + 1);
                    grafoFlujoBloquesBasicos.addVertice(bloque);
                    bloques.add(bloque);
                }
            }
        }

        bloque = bloques.get(2);
        grafoFlujoBloquesBasicos.addArista(e, bloque);

        int indiceInstruccion;
        // Identificacion de finales
        for (int b = 2; b < bloques.size(); b++) {
            bloque = bloques.get(b);
            indiceInstruccion = bloque.getInicio();
            instruccion = instrucciones.get(indiceInstruccion);
            finales = isBranch(instruccion) || isRetorno(instruccion) || isEtiqueta(instruccion);
            while(!finales || esEtiquetaBloquePropio(instruccion, bloque, etiquetaToLider)) {
                instruccion = instrucciones.get(++indiceInstruccion);
                finales = isBranch(instruccion) || isRetorno(instruccion) || isEtiqueta(instruccion);
            }

            while(isConditionalBranch(instruccion)) {
                etiqueta = (OperandoEtiqueta)instruccion.getTercero();
                siguienteBloque = etiquetaToLider.get(etiqueta);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque);
                instruccion = instrucciones.get(++indiceInstruccion);
            }

            if (isUnconditionalBranch(instruccion)) {
                bloque.setFin(indiceInstruccion);
                etiqueta = (OperandoEtiqueta)instruccion.getTercero();
                siguienteBloque = etiquetaToLider.get(etiqueta);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque);
            } else if (isRetorno(instruccion)) {
                // En todos los retornos lo que tenemos es que "finaliza" la ejecución actual, por
                // tanto es como si estuvieramos saliendo de un programa
                bloque.setFin(indiceInstruccion);
                grafoFlujoBloquesBasicos.addArista(bloque, s);
            } else {
                // Ojo! Este caso es especial, ya que el bloque basico no acaba
                // con un salto ( ya sea retorno o goto ), si no que el flujo puede
                // "caer" al siguiente bloque y por tanto, debemos marcar que este
                // orden no se puede alterar
                bloque.setFin(indiceInstruccion - 1);
                siguienteBloque = bloques.get(b + 1);
                grafoFlujoBloquesBasicos.addArista(bloque, siguienteBloque, true);
            }
        }

        return grafoFlujoBloquesBasicos;
    }

	private static boolean esEtiquetaBloquePropio(InstruccionTresDirecciones instruccion, BloqueBasico bloque, HashMap<OperandoEtiqueta, BloqueBasico> etiquetaToLider) {
        if (isEtiqueta(instruccion)) {
            OperandoEtiqueta etiqueta = (OperandoEtiqueta)instruccion.getPrimero();
            return bloque == etiquetaToLider.get(etiqueta);
        }
        return false;
    }

	private static boolean isConditionalBranch(InstruccionTresDirecciones instruccion) {
        if (instruccion instanceof GT) {
            return ((GT)instruccion).isBranch();
        } else if (instruccion instanceof GTE) {
            return ((GTE)instruccion).isBranch();
        } else if (instruccion instanceof LT) {
            return ((LT)instruccion).isBranch();
        } else if (instruccion instanceof LTE) {
            return ((LTE)instruccion).isBranch();
        } else if (instruccion instanceof NE) {
            return ((NE)instruccion).isBranch();
        } else if (instruccion instanceof EQ) {
            return ((EQ)instruccion).isBranch();
        }
        return false;
    }

	private static boolean isUnconditionalBranch(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Goto;
    }

	private static boolean isBranch(InstruccionTresDirecciones instruccion) {
        return isUnconditionalBranch(instruccion) || isConditionalBranch(instruccion);
    }

	private static boolean isRetorno(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Retorno;
    }

	private static boolean isEtiqueta(InstruccionTresDirecciones instruccion) {
        return instruccion instanceof Etiqueta;
    }
	
}
