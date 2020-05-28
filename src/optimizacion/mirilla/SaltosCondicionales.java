package optimizacion.mirilla;

import java.util.ArrayList;

import intermedio.EQ;
import intermedio.GT;
import intermedio.GTE;
import intermedio.Goto;
import intermedio.InstruccionTresDirecciones;
import intermedio.LT;
import intermedio.LTE;
import intermedio.NE;
import optimizacion.Optimizador;
import optimizacion.RetornoOptimizacion;
import optimizacion.SecuenciaInstrucciones;

public class SaltosCondicionales implements Optimizador{
	
	@Override
	public RetornoOptimizacion optimizar(SecuenciaInstrucciones secuenciaInstrucciones) {
        ArrayList<InstruccionTresDirecciones> nuevasInstrucciones = new ArrayList<>();
        boolean cambiado = false;
        InstruccionTresDirecciones instruccionActual, instruccionSiguiente, complementario;
        while(secuenciaInstrucciones.hasNext()) {
        	instruccionActual = secuenciaInstrucciones.next();
        	instruccionSiguiente = secuenciaInstrucciones.upcoming();
            if (instruccionSiguiente instanceof Goto) {
                Goto salto = (Goto)instruccionSiguiente;
	            switch (instruccionActual.getOperacion()) {
	                case GT:
                        cambiado = true;
                        complementario = ((GT)instruccionActual).getComplementario(salto);
	                    break;
	                case GTE:
                        cambiado = true;
                        complementario = ((GTE)instruccionActual).getComplementario(salto);
	                    break;
	                case LT:
                        cambiado = true;
                        complementario = ((LT)instruccionActual).getComplementario(salto);
	                    break;
	                case LTE:
                        cambiado = true;
                        complementario = ((LTE)instruccionActual).getComplementario(salto);
	                    break;
	                case EQ:
                        cambiado = true;
                        complementario = ((EQ)instruccionActual).getComplementario(salto);
	                    break;
	                case NE:
                        cambiado = true;
                        complementario = ((NE)instruccionActual).getComplementario(salto);
	                    break;
	                default:
	                	complementario = instruccionActual;
	                	break;
	            }
            	nuevasInstrucciones.add(complementario);
        	} else {
        		nuevasInstrucciones.add(instruccionActual);
        	}
        }
        return new RetornoOptimizacion(nuevasInstrucciones, cambiado);
    }
}
