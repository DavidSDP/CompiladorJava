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
import optimizacion.OptimizacionMirilla;
import optimizacion.RetornoOptimizacion;

public class SaltosCondicionales extends OptimizacionMirilla{
	
	public static RetornoOptimizacion optimizar(RetornoOptimizacion optimization) {
		return optimizarSaltosCondicionales(optimization);
	}
	
	private static RetornoOptimizacion optimizarSaltosCondicionales(RetornoOptimizacion optimization) {
        ArrayList<InstruccionTresDirecciones> instrucciones = optimization.getInstrucciones();
        boolean cambiado = false;
        
        ArrayList<InstruccionTresDirecciones> nuevas = new ArrayList<>();
		int numElementos = instrucciones.size();
        
        int i = 0;
        InstruccionTresDirecciones instruccion, siguienteInstruccion, complementario;
        while(i < numElementos) {
            instruccion = instrucciones.get(i);
            switch (instruccion.getOperacion()) {
                case GT:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case GTE:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((GTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case LT:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LT)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case LTE:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((LTE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case EQ:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((EQ)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                case NE:
                    siguienteInstruccion = getSiguiente(instrucciones, i);
                    if (siguienteInstruccion instanceof Goto) {
                        cambiado = true;
                        Goto salto = (Goto)siguienteInstruccion;
                        complementario = ((NE)instruccion).getComplementario(salto);
                        nuevas.add(complementario);
                        i++;
                    } else {
                        nuevas.add(instruccion);
                    }
                    break;
                default:
                    nuevas.add(instruccion);
            }
            i++;
        }
        return new RetornoOptimizacion(nuevas, cambiado);
    }
}
