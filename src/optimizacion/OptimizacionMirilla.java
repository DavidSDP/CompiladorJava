package optimizacion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import optimizacion.mirilla.NormalizacionOperandos;
import optimizacion.mirilla.SaltosCondicionales;

public class OptimizacionMirilla implements Optimizador{
	
	private List<Optimizador> optimizadores;
	
	public OptimizacionMirilla() {
		this.optimizadores = new ArrayList<>();
		this.optimizadores.add(new SaltosCondicionales());
		this.optimizadores.add(new NormalizacionOperandos());
	}
	
	@Override
	public RetornoOptimizacion optimizar(SecuenciaInstrucciones secuenciaInstrucciones) {
		
        boolean cambiado;
		SecuenciaInstrucciones secuenciaActual = secuenciaInstrucciones;
        do {
        	cambiado = false;
			Iterator<Optimizador> iterador = this.optimizadores.iterator();
			while(iterador.hasNext()) {
				Optimizador optimizador = iterador.next();
				RetornoOptimizacion retornoOptimizacion = optimizador.optimizar(secuenciaActual);
				if(retornoOptimizacion != null) {
					cambiado = cambiado || retornoOptimizacion.isCambiado();
					secuenciaActual = new SecuenciaInstrucciones(retornoOptimizacion.getInstrucciones());
				}
			}
		}while(cambiado);
        return new RetornoOptimizacion(secuenciaActual.getInstrucciones(), cambiado);
	}
	
}
