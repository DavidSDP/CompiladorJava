package CodigoMaquina;

import java.util.ArrayList;
import java.util.List;

public class BloqueInstrucciones {
	
	private List<Instruccion> listaInstrucciones;
	private ConjuntoRegistros registrosEntrada;
	private ConjuntoRegistros registrosModificados;
	private ConjuntoRegistros registrosSalida;
	
	public BloqueInstrucciones() {
		this.listaInstrucciones = new ArrayList<Instruccion>();
	}
	
	public void add(Instruccion instruccion) {
		this.listaInstrucciones.add(instruccion);
	}
	
	public void add(BloqueInstrucciones bloqueInstrucciones) {
		this.listaInstrucciones.addAll(bloqueInstrucciones.getListaInstrucciones());
	}
	
	public List<Instruccion> getListaInstrucciones(){
		return this.listaInstrucciones;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		if(!this.listaInstrucciones.isEmpty()) {
			this.listaInstrucciones.stream().forEach(x -> {
				String machineCode = x.toMachineCode();
				if(machineCode.startsWith(";")) {
					sb.append(machineCode + "\n");
				}else {
					sb.append(machineCode + "   \t\t\t; " + x.toVisualRepresentation() + "\n");
				}
			});
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public ConjuntoRegistros getRegistrosEntrada() {
		return registrosEntrada;
	}

	public void setRegistrosEntrada(ConjuntoRegistros registrosEntrada) {
		this.registrosEntrada = registrosEntrada;
	}

	public ConjuntoRegistros getRegistrosModificados() {
		return registrosModificados;
	}

	public void setRegistrosModificados(ConjuntoRegistros registrosModificados) {
		this.registrosModificados = registrosModificados;
	}

	public ConjuntoRegistros getRegistrosSalida() {
		return registrosSalida;
	}

	public void setRegistrosSalida(ConjuntoRegistros conjuntoRegistros) {
		this.registrosSalida = conjuntoRegistros;
	}
	
}
