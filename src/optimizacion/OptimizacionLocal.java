package optimizacion;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Procesador.AlmacenVariables;
import Procesador.Declaracion;
import intermedio.BloqueBasico;
import intermedio.InstruccionTresDirecciones;
import optimizacion.local.Grafo;

public class OptimizacionLocal implements Optimizador{
	
	/**
     * Las optimizaciones locales solo se aplican sobre las funciones. AsÃ­ que, para cada una de las funciones
     * se genera un grafo independiente sobre el cual se puede optimizar el cÃ³digo
     * @param secuenciaInstrucciones
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
        	IdentificacionBucles idBucles = new IdentificacionBucles(grafosFunciones.get(rangoInstruccionesFuncion), secuenciaInstrucciones);
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

		// TODO Temporal, hasta que saquemos el codigo de esta clase
		private SecuenciaInstrucciones secuenciaInstrucciones;
		private DefinicionesAccesibles definicionesAccesibles;
		
		public IdentificacionBucles(Grafo grafoFuncion, SecuenciaInstrucciones instrucciones) {
			this.grafoBloquesBasicos = grafoFuncion;
			// TODO Temporary code
			this.secuenciaInstrucciones = instrucciones;
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

			// TODO Esto definitivamente no va aquí, pero de momento me va bien para poder
			//  mantener htodo el código recogido en una sola parte.
			for(Map.Entry<Integer, List<BloqueBasico>> e : this.tablaBLC.entrySet()) {

				this.definicionesAccesibles = new DefinicionesAccesibles(
						e.getValue(),
						secuenciaInstrucciones,
						grafoBloquesBasicos
				);
				identificacionDeInvariantes(e.getKey(), e.getValue());
				// TODO identificacion de invariantes trasladables
				// TODO traslado de invariantes
			}
		}

		private void identificacionDeInvariantes(int indiceBucle, List<BloqueBasico> bloques) {
			AlmacenVariables almacenVariables = AlmacenVariables.getInstance();
			// Por si acaso reseteamos el conteo de las variables ( aunque se supone
			// que ya deberían haberse inicializado )
			almacenVariables.resetAsignaciones();

			// TODO Esta tabla de modos debería estar en algún otro lado que no fuera aquí.
			//  Por no hablar de que es bastante explicito. Alomejor una estructura alrededor de esto no nos haría daño
			HashMap<InstruccionTresDirecciones, Integer> modos = new HashMap<>();

			for (BloqueBasico bloqueBasico : bloques) {
				// TODO Transformar esta mierda en un iterador
				for(int idx = bloqueBasico.getInicio(); idx <= bloqueBasico.getFin(); idx++) {
					InstruccionTresDirecciones i3d = secuenciaInstrucciones.get(idx);
					Declaracion destino = i3d.getDestino();
					if (destino != null) {
						almacenVariables.incrementaAsignaciones(destino);
						modos.put(i3d, 0);
					}
				}
			}

			ArrayList<InstruccionTresDirecciones> invariantes = new ArrayList<>();
			boolean hayCambios = true;
			while(hayCambios) {
				hayCambios = false;
				for (BloqueBasico bloqueBasico : bloques) {
					// TODO Transformar esta mierda en un iterador
					for(int idx = bloqueBasico.getInicio(); idx <= bloqueBasico.getFin(); idx++) {
						InstruccionTresDirecciones i3d = secuenciaInstrucciones.get(idx);
						Declaracion destino = i3d.getDestino();
						if (almacenVariables.getAsignaciones(destino) == 1 && modos.get(i3d) <= 0) {
							int nuevoModo = examinaInvariancia(i3d, destino, modos, bloques);
							modos.put(i3d, nuevoModo);
							if (nuevoModo > 0) {
								hayCambios = true;
								invariantes.add(i3d);
							}
						}
					}
				}
			}
		}

		/**
		 * Tabla de modos para las condiciones de invarianza:
		 * 		1  -> La instruccion es invariante
		 * 		0  -> La instruccion tiene todos los argumentos variantes
		 * 		-1 -> El argumento izquierda no es invariante, pero el derecho si
		 * 		-2 -> El argumento derecho no es invariante, pero el izquierdo si
		 *
		 * 	Ojo! Según esta definción el código estaría mal, pero vamos a ver si realmente está mal o no.
		 *
		 */
		private int examinaInvariancia(InstruccionTresDirecciones i3d, Declaracion variable, HashMap<InstruccionTresDirecciones, Integer> modos, List<BloqueBasico> bloquesBucle) {
			// Como reza el comentario de arriba, -2 significa que el argumento izquierda no es invariante. Esto tiene pinta
			// de estar mal.
			boolean argIzquierdaInvariante = (modos.get(i3d) == 0 || modos.get(i3d) == -2);
			boolean argDerechaInvariante = (modos.get(i3d) == 0 || modos.get(i3d) == -1);

			if (!argIzquierdaInvariante) {
				argIzquierdaInvariante = i3d.primeroEsConstante();
				if (!argIzquierdaInvariante) {
					boolean extraer = true;
					// TODO No se debería conocer la estructura del operando aquí fuera
					for (InstruccionTresDirecciones definicion : definicionesAccesibles.getDefiniciones(i3d, i3d.getPrimero().getValor())) {
						// TODO Ahora mismo no tenemos una forma de identificar si una instrucción pertenece al bloque basico.
						boolean declaracionPerteneceABucle = true;
						if (declaracionPerteneceABucle) {
							extraer = false;
						}
					}
					argIzquierdaInvariante = extraer;
				}

				// Si despues de mirar los usos definicion la variable sigue siendo invariante, tenemos que mirar si
				// la definicion es única  y si esta es invariante.
				// Si no se redefine en ningún punto y la definición es invariante, entonces podemos sacarla fuera.
				if (!argIzquierdaInvariante) {
					if (definicionesAccesibles.getDefiniciones(i3d, i3d.getPrimero().getValor()).size() == 1) {
						for (InstruccionTresDirecciones declaracion : definicionesAccesibles.getDefiniciones(i3d, i3d.getPrimero().getValor())) {
							argIzquierdaInvariante = modos.get(declaracion) > 0;
						}
					}
				}
			}

			// Cuidado! En las instrucciones de copia segundo es el destino, no uno de los parámetros, así
			// que esto depende de la instrucción. Ojito 2! para las funciones esto tampoco funciona ( menudo chiste )
			if (!argDerechaInvariante) {
				argDerechaInvariante = i3d.segundoEsConstante();
				if (!argDerechaInvariante) {
					boolean extraer = true;
					// TODO No se debería conocer la estructura del operando aquí fuera
					for (InstruccionTresDirecciones definicion : definicionesAccesibles.getDefiniciones(i3d, i3d.getSegundo().getValor())) {
						// TODO Ahora mismo no tenemos una forma de identificar si una instrucción pertenece al bloque basico.
						boolean declaracionPerteneceABucle = true;
						if (declaracionPerteneceABucle) {
							extraer = false;
						}
					}
					argDerechaInvariante = extraer;
				}

				// Si despues de mirar los usos definicion la variable sigue siendo invariante, tenemos que mirar si
				// la definicion es única  y si esta es invariante.
				// Si no se redefine en ningún punto y la definición es invariante, entonces podemos sacarla fuera.
				if (!argDerechaInvariante) {
					if (definicionesAccesibles.getDefiniciones(i3d, i3d.getSegundo().getValor()).size() == 1) {
						for (InstruccionTresDirecciones declaracion : definicionesAccesibles.getDefiniciones(i3d, i3d.getSegundo().getValor())) {
							argDerechaInvariante = modos.get(declaracion) > 0;
						}
					}
				}
			}

			if (argIzquierdaInvariante && argDerechaInvariante) {
				return 1; // La instruccion es invariante
			} else if (!argIzquierdaInvariante && !argDerechaInvariante) {
				return 0; // La instruccion es variante
			} else if (argIzquierdaInvariante) {
				return -1;
			} else { // Despues de htodo esto si llegamos aqui, singifica que el derecho es invariante
				return -2;
			}
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
