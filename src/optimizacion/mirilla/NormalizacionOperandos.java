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
import optimizacion.Optimizador;
import optimizacion.RetornoOptimizacion;
import optimizacion.SecuenciaInstrucciones;

public class NormalizacionOperandos implements Optimizador{
	
	@Override
	public RetornoOptimizacion optimizar(SecuenciaInstrucciones secuenciaInstrucciones) {
		ArrayList<InstruccionTresDirecciones> nuevasInstrucciones = new ArrayList<>();
		Boolean cambiado = false;
		while(secuenciaInstrucciones.hasNext()) {
			InstruccionTresDirecciones nuevaInstruccion = secuenciaInstrucciones.next().clone();
			switch (nuevaInstruccion.getOperacion()) {
	            case EQ:
	            	cambiado = cambiado || eq((EQ)nuevaInstruccion);
	                break;
	            case NE:
	            	cambiado = cambiado || ne((NE)nuevaInstruccion);
	                break;
	            case AND:
	            	cambiado = cambiado || and((And)nuevaInstruccion);
	                break;
	            case OR:
	            	cambiado = cambiado || or((Or)nuevaInstruccion);
	                break;
	            case SUMA:
	            	cambiado = cambiado || suma((Suma)nuevaInstruccion);
	                break;
	            case PRODUCTO:
	            	cambiado = cambiado || producto((Producto)nuevaInstruccion);
	                break;
	            default:
	            	break;
			}
        	nuevasInstrucciones.add(nuevaInstruccion);
		}
		return new RetornoOptimizacion(nuevasInstrucciones, cambiado);
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
