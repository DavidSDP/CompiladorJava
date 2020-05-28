package optimizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

public class OptimizacionLocal implements Optimizador{
	
	/**
     * Las optimizaciones locales solo se aplican sobre las funciones. Así que, para cada una de las funciones
     * se genera un grafo independiente sobre el cual se puede optimizar el código
     * @param instrucciones
     * @return
     */
	@Override
	public RetornoOptimizacion optimizar(SecuenciaInstrucciones secuenciaInstrucciones) {

        ArrayList<RangoInstruccionesFuncion> funciones = new ArrayList<>();
        HashMap<RangoInstruccionesFuncion, Grafo> grafosFunciones = new HashMap<>();
        Grafo grafoFuncion;
        RangoInstruccionesFuncion rangoInstrucciones = secuenciaInstrucciones.getSiguienteFuncion(0);
        while (rangoInstrucciones != null) {
            grafoFuncion = secuenciaInstrucciones.getGrafoFlujoFuncion(rangoInstrucciones);
            funciones.add(rangoInstrucciones);
            grafosFunciones.put(rangoInstrucciones, grafoFuncion);
            rangoInstrucciones = secuenciaInstrucciones.getSiguienteFuncion(rangoInstrucciones.getFin() + 1);
        }
        return null;
	}
}
