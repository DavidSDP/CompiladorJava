package optimizacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import intermedio.BloqueBasico;
import optimizacion.local.Grafo;

public class OptimizacionLocal implements Optimizador{
	
	/**
     * Las optimizaciones locales solo se aplican sobre las funciones. AsÃ­ que, para cada una de las funciones
     * se genera un grafo independiente sobre el cual se puede optimizar el cÃ³digo
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
        
        HashMap<RangoInstruccionesFuncion, IdentificacionBucles> identificacionBucles = new HashMap<>();
        for(RangoInstruccionesFuncion rangoInstruccionesFuncion: funciones) {
        	IdentificacionBucles idBucles = new IdentificacionBucles(grafosFunciones.get(rangoInstruccionesFuncion));
        	System.out.println(rangoInstruccionesFuncion.toString());
        	idBucles.print();
        	identificacionBucles.put(rangoInstruccionesFuncion, idBucles);
        }
        return null;
	}
	
	public static class IdentificacionBucles {
		
		private Grafo grafoBloquesBasicos;
		
		private List<BloqueBasico> tablaBB = new ArrayList<>();		// Bloques Básicos
		private HashMap<BloqueBasico, List<BloqueBasico>> tablaDom = new HashMap<>(); 	// Tabla Dominadores
		private HashMap<BloqueBasico, BloqueBasico> tablaDI = new HashMap<>();			// Dominadores Inmediatos
		
		private HashMap<BloqueBasico, Integer> tablaIL = new HashMap<>();	// Identificadores de Bucles
		private HashMap<Integer, BloqueBasico> tablaH = new HashMap<>();	// Tabla de Headers
		private HashMap<Integer, Arco> tablaAXD = new HashMap<>();			// Tabla de Arcos
		
		private HashMap<Integer, List<BloqueBasico>> tablaBLC = new HashMap<>();			// Tabla de Bucles Calculados
		
		private Integer nh;		// Última posición ocupada en tabla de Headers
		private Integer naxd;	// Número de arcos x->d tales que d dom x
		
		public IdentificacionBucles(Grafo grafoFuncion) {
			this.grafoBloquesBasicos = grafoFuncion;
			ejecutar();
		}
		
		public void print() {
			for(int id = 1; id <= this.nh; id++) {
				System.out.println();
				System.out.println(" ---> Bucle " + id + " <--- ");
				List<BloqueBasico> bb = this.tablaBLC.get(id);
				for(BloqueBasico b: bb) {
					System.out.println(b.toString());
				}
				System.out.println(" ------------------ ");
				System.out.println();
			}
		}

		public void ejecutar() {
			// Se monta tabla BB
			grafoBloquesBasicos.getBloquesBasicos().forEach(b->tablaBB.add(b));
			
			// Se monta tabla Dominadores
			rellenarTablaDominadores();
			
			// Se monta tabla DI
			rellenarTablaDI();
			
			// Algoritmo de identificación de bucles (Rellenado de tablas IL, H, AXD)
			identificacionBucles();
			
			// Algoritmo de identificación de bucles (Rellenado de tabla BLC)
			determinacionDeBucles();
		}
		
		private void determinacionDeBucles() {
			for(int ib = 1; ib <= this.nh; ib++) {
				this.tablaBLC.put(ib, new ArrayList<>());
			}
			for(int ixd = 1; ixd <= this.naxd; ixd++) {
				Arco arco = this.tablaAXD.get(ixd);
				List<BloqueBasico> l = L(arco);
				if(!l.isEmpty()) {
					int ib = this.tablaIL.get(arco.getB());
					List<BloqueBasico> aux = this.tablaBLC.get(ib);
					for(BloqueBasico bb: l) {
						if(!aux.contains(bb)) {
							aux.add(bb);
						}
					}
				}
			}
		}

		private List<BloqueBasico> L(Arco arco) {
			List<BloqueBasico> L = new ArrayList<>(); L.add(arco.getA()); L.add(arco.getB());
			List<BloqueBasico> PND = new ArrayList<>(); PND.add(arco.getA());
			while(!PND.isEmpty()) {
				BloqueBasico y = PND.get(0);
				PND.remove(0);
				if(!this.grafoBloquesBasicos.getPredecesores(y).isEmpty()) {
					for(BloqueBasico z: this.grafoBloquesBasicos.getPredecesores(y)) {
						if(!L.contains(z)) {
							L.add(z);
							if(!PND.contains(z)) {
								PND.add(z);
							}
						}
					}
				}
			}
			return L;
		}

		private void rellenarTablaDI() { 
			// DI(E) = E
			this.tablaDI.put(getBloqueE(), getBloqueE());
			
			// Para el resto de bloques
			List<BloqueBasico> tablaBBsinE = getSubListaSinBloque(this.tablaBB, getBloqueE());
			for(BloqueBasico x: tablaBBsinE) {
				List<BloqueBasico> dominadoresDeXsinX = getSubListaSinBloque(this.dominadores(x), x);
				for(BloqueBasico d: dominadoresDeXsinX) {
					Boolean d_esDominadorInmediatoDe_x = true;
					for(BloqueBasico c: getSubListaSinBloque(getSubListaSinBloque(this.dominadores(x), x), d)) {
						if(this.dominadores(c).contains(d)) {
							d_esDominadorInmediatoDe_x = false;
							break;
						}
					}
					if(d_esDominadorInmediatoDe_x) {
						tablaDI.put(x, d);
						break;
					}
				}
			}
			
		}
		
		private List<BloqueBasico> getSubListaSinBloque(List<BloqueBasico> dominadores, BloqueBasico b){
			if(dominadores == null || dominadores.isEmpty())
				return new ArrayList<>();
			return dominadores.stream().filter(x->!x.equals(b)).collect(Collectors.toList());
		}

		private void rellenarTablaDominadores() {
			
			List<BloqueBasico> PND = new ArrayList<>();
			tablaBB.stream().forEach(b->PND.add(b));
			
			// Inicialización
			List<BloqueBasico> todosLosBloquesBasicos = new ArrayList<>();
			tablaBB.stream().forEach(b->todosLosBloquesBasicos.add(b));
			for(BloqueBasico b: tablaBB) {
				tablaDom.put(b, todosLosBloquesBasicos);
			}
			
			// Dominador de E
			List<BloqueBasico> domE = tablaDom.get(getBloqueE());
			domE.clear();
			domE.add(getBloqueE());
			PND.remove(getBloqueE());
			
			// Mientras queden bloques pendientes
			while(!PND.isEmpty()) {
				// Escoger un Bloque Básico de los pendientes, y sacarlo de PND
				BloqueBasico a = PND.get(0);
				PND.remove(a);
				
				// D = BB
				List<BloqueBasico> conjuntoD = new ArrayList<>();
				tablaBB.stream().forEach(b->conjuntoD.add(b));
				
				if(this.grafoBloquesBasicos.getPredecesores(a) != null) {
					this.grafoBloquesBasicos.getPredecesores(a).stream().forEach(c->{
						List<BloqueBasico> new_conjuntoD = interseccion(conjuntoD, dominadores(c));
						conjuntoD.clear();
						new_conjuntoD.stream().forEach(ncd -> conjuntoD.add(ncd));
					});
				}
				
				// D = D U {a}
				if(!conjuntoD.contains(a))
					conjuntoD.add(a);
				
				// if D != dom(a)
				if(!sonIguales(conjuntoD, dominadores(a))) {
					if(this.grafoBloquesBasicos.getSucesores(a) != null) {
						this.grafoBloquesBasicos.getSucesores(a).stream().forEach(c->{
							if(!PND.contains(c))
								PND.add(c);
						});
					}
					tablaDom.put(a, conjuntoD);
				}
				
			}
			
		}
		
		private Boolean sonIguales(List<BloqueBasico> a, List<BloqueBasico> b){
			if(a.size() != b.size())
				return false;
			
			for(BloqueBasico aa: a) {
				if(!b.contains(aa)) {
					return false;
				}
			}
			
			return true;
		}
		
		private List<BloqueBasico> interseccion(List<BloqueBasico> a, List<BloqueBasico> b){
			List<BloqueBasico> interseccion = new ArrayList<>();
			for(BloqueBasico aa: a) {
				if(b.contains(aa)) {
					interseccion.add(aa);
				}
			}
			return interseccion;
		}
		
		private List<BloqueBasico> dominadores(BloqueBasico n) {
			if(tablaDom.containsKey(n)) {
				return tablaDom.get(n);
			}
			return new ArrayList<>();
		}
		
		private BloqueBasico getBloqueE() {
			return this.tablaBB.stream().filter(p->p.getEs_E()).findFirst().get();
		}

		private void identificacionBucles() {
			nh = 0;
			naxd = 0;
			List<Arco> arcosGrafo = this.grafoBloquesBasicos.getArcos();
			for(Arco arco: arcosGrafo) {
				if(domina(arco.getB(), arco.getA())) {
					if(!this.tablaH.containsValue(arco.getB())) {
						this.nh++;
						this.tablaH.put(nh, arco.getB());
						this.tablaIL.put(arco.getB(), nh);
					}
					this.naxd++;
					this.tablaAXD.put(naxd, arco);
				}
			}
		}

		private boolean domina(BloqueBasico a, BloqueBasico b) {
			BloqueBasico actual = tablaDI.get(b);
			Boolean fin = false;
			while(!actual.equals(a) && !fin) {
				BloqueBasico siguiente = tablaDI.get(actual);
				fin = siguiente.equals(actual);
				actual = siguiente;
			}
			return actual.equals(a);
		}
		
	}
	
	public static class Arco {

		private BloqueBasico a;
		private BloqueBasico b;
		
		public Arco(BloqueBasico a, BloqueBasico b) {
			this.a = a;
			this.b = b;
		}

		public BloqueBasico getA() {
			return a;
		}

		public void setA(BloqueBasico a) {
			this.a = a;
		}

		public BloqueBasico getB() {
			return b;
		}

		public void setB(BloqueBasico b) {
			this.b = b;
		}
		
		@Override
		public int hashCode() {
			return (a.hashCode() + " " + b.hashCode()).hashCode();
		}
		
	}
	
}
