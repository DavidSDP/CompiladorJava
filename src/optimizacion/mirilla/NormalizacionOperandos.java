package optimizacion.mirilla;

import java.util.ArrayList;

import Procesador.DeclaracionConstante;
import intermedio.And;
import intermedio.EQ;
import intermedio.InstruccionTresDirecciones;
import intermedio.NE;
import intermedio.Operando;
import intermedio.Or;
import intermedio.Producto;
import intermedio.Suma;
import optimizacion.OptimizacionMirilla;
import optimizacion.RetornoOptimizacion;

public class NormalizacionOperandos extends OptimizacionMirilla{

	public static RetornoOptimizacion optimizar(RetornoOptimizacion optimizacion) {
		ArrayList<InstruccionTresDirecciones> instrucciones = optimizacion.getInstrucciones();
        boolean cambiado = optimizacion.isCambiado();
        ArrayList<InstruccionTresDirecciones> nuevas = new ArrayList<>();
		int numElementos = instrucciones.size();
        int i = 0;
        InstruccionTresDirecciones instruccion;
        while(i < numElementos) {
            boolean cambiadoLocal = false;
            instruccion = instrucciones.get(i);
            switch (instruccion.getOperacion()) {
	            case EQ:
	            	cambiadoLocal = eq((EQ)instruccion);
	            	nuevas.add(instruccion);
	                break;
	            case NE:
	            	cambiadoLocal = ne((NE)instruccion);
	            	nuevas.add(instruccion);
	                break;
	            case AND:
	            	cambiadoLocal = and((And)instruccion);
	            	nuevas.add(instruccion);
	                break;
	            case OR:
	            	cambiadoLocal = or((Or)instruccion);
	            	nuevas.add(instruccion);
	                break;
	            case SUMA:
	            	cambiadoLocal = suma((Suma)instruccion);
	            	nuevas.add(instruccion);
	                break;
	            case PRODUCTO:
	            	cambiadoLocal = producto((Producto)instruccion);
	            	nuevas.add(instruccion);
	                break;
                default:
                    nuevas.add(instruccion);
                	break;
            }
            cambiado = cambiado || cambiadoLocal;
            i++;
        }
        return new RetornoOptimizacion(nuevas, cambiado);
	}
	
	private static Boolean suma(Suma instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean producto(Producto instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean and(And instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean or(Or instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean eq(EQ instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean ne(NE instruccion) {
		return aplicaConmutatividadDeOperandos(instruccion);
	}
	
	private static Boolean aplicaConmutatividadDeOperandos(InstruccionTresDirecciones instruccion) {
		Boolean cambiado = false;
		Operando op1 = instruccion.getPrimero();
		Operando op2 = instruccion.getSegundo();
		Boolean op1Constante = (op1.getValor() instanceof DeclaracionConstante);
		Boolean op2Constante = (op2.getValor() instanceof DeclaracionConstante);
		if(op1Constante && !op2Constante) {
			// No se requiere optimizar
		}else if(!op1Constante && op2Constante) {
			// Invertimos operandos
			instruccion.setPrimero(op2);
			instruccion.setSegundo(op1);
			cambiado = true;
		}else if(!op1Constante && !op2Constante) {
			// Ordenamos según valor nv
			if(op1.getValor().getId().getNv() > op2.getValor().getId().getNv()) {
				// Invertimos operandos
				instruccion.setPrimero(op2);
				instruccion.setSegundo(op1);
				cambiado = true;
			}
		}else {
			// TODO: Aplicar optimización de constantes
		}
		return cambiado;
	}
	
}
