package Procesador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import Checkers.Tipo;
import Checkers.TipoObject;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class Entorno {

    protected static int nameSeq = 0;

    private Integer nivel;

    protected Declaracion identificador;

    private Hashtable<String, Declaracion> tablaIDs;
    protected ArrayList<Declaracion> ids;

    private Entorno entornoPadre;
    protected ArrayList<Entorno> entornosHijo;

    private Integer _identificador_entorno;

    public Entorno(Entorno entornoPadre, TipoObject tipo) {
        this(entornoPadre, new Declaracion(new Identificador(tipo.toString(), tipo.toString()), tipo));
    }

    public Entorno(Entorno entornoPadre, Declaracion identificador) {
        if (entornoPadre == null) {
            this.setNivel(0);
        } else {
            this.setNivel(entornoPadre.getNivel() + 1);
        }
        this.set_identificador_entorno(GlobalVariables.getIdentificador());
        this.identificador = identificador;
        this.tablaIDs = new Hashtable<>();
        this.ids = new ArrayList<>();
        this.entornoPadre = entornoPadre;
        this.entornosHijo = new ArrayList<>();

        // Si de verdad tenemos un entorno padre, nos vinculamos al padre. Creando asi una estructura arborea.
        if (this.entornoPadre != null) {
            this.entornoPadre.registraEntornoHijo(this);
        }
    }

    public Declaracion getIdentificadorFuncionRetorno() {
        EntornoFuncion entornoFuncionSuperior = (EntornoFuncion) getEntornoFuncionSuperior();
        if (entornoFuncionSuperior == null)
            return null;
        return entornoFuncionSuperior.getIdentificador();
    }

    public Entorno getEntornoFuncionSuperior() {
        if (this instanceof EntornoFuncion)
            return this;
        return this.getEntornoPadre().getEntornoFuncionSuperior();
    }

    ////////*	IDENTIFICADORES		*////////
    
    public Boolean containsSoloPropioEntorno(String name) {
    	return this.tablaIDs.containsKey(name);
    }

    // Devuelve true si el ID ha sido declarado en el entorno actual
    public Boolean containsHastaFuncionPadre(String s) {
    	List<Entorno> listaEntornos = this.fullGetEntornos();
    	Iterator<Entorno> iterator = listaEntornos.iterator();
    	Boolean found = false;
    	Boolean encontradaFuncionPadre = false;
    	while(iterator.hasNext() && !found && encontradaFuncionPadre) {
    		Entorno entorno = iterator.next();
    		if(entorno instanceof EntornoFuncion)
    			encontradaFuncionPadre = true;
    		if(entorno.containsSoloPropioEntorno(s))
    			found = true;
    	}
    	return found;
    }
    
    // Introduce nuevo ID en el entorno actual
    public Declaracion put(TipoObject tipo, String s) throws ErrorSemantico {
        String name = s;
        if (name == null)
            name = getTempName();

        if (this.containsSoloPropioEntorno(name))
            throw new ErrorSemantico("El identificador '" + name + "' se ha declarado por duplicado");


        Declaracion nuevaDeclaracion = new Declaracion(new Identificador(name, name), tipo, this.getProfundidad());
        nuevaDeclaracion.setEntorno(this);
        this.tablaIDs.put(name, nuevaDeclaracion);
        this.ids.add(nuevaDeclaracion);
        return nuevaDeclaracion;
    }

    public DeclaracionArray putArray(String id, String tipo, Integer size) throws ErrorSemantico {
        if (this.containsSoloPropioEntorno(id))
            throw new ErrorSemantico("El identificador '" + id + "' se ha declarado por duplicado");


        DeclaracionArray declArray = new DeclaracionArray(new Identificador(id, id), Tipo.getTipo(tipo), size);
        declArray.setEntorno(this);
        this.tablaIDs.put(id, declArray);
        this.ids.add(declArray);
        return declArray;
    }

    // Introduce nuevo ID constante en el entorno actual
    public DeclaracionConstante putConstante(TipoObject tipo, String s, Object valor) throws ErrorSemantico {
        String name = s;
        if (name == null)
            name = getTempName();

        if (this.containsSoloPropioEntorno(name))
            throw new ErrorSemantico("La constante '" + name + "' se ha declarado por duplicado");

        DeclaracionConstante nuevoIdentificador = new DeclaracionConstante(new Identificador(name, name), tipo, valor, this.getProfundidad());
        nuevoIdentificador.setEntorno(this);
        this.tablaIDs.put(name, nuevoIdentificador);
        this.ids.add(nuevoIdentificador);
        return nuevoIdentificador;
    }

    // Devuelve el ID especificado en el entorno actual
    public Declaracion get(String s) {
        if (!this.containsSoloPropioEntorno(s)) {
            return null;
        }
        return this.tablaIDs.get(s);
    }

    // Devuelve el ID declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
    public Declaracion fullGet(String s) {
        for (Entorno e = this; e != null; e = e.getEntornoPadre()) {
            if (e.containsSoloPropioEntorno(s)) {
                return e.get(s);
            }
        }
        return null;
    }

    public List<Entorno> fullGetEntornos() {
    	List<Entorno> listaEntornos = new ArrayList<>();
        for (Entorno e = this; e != null; e = e.getEntornoPadre()) {
        	listaEntornos.add(e);
        }
        return listaEntornos;
    }

    // Devuelve el ID  de Función declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
    public DeclaracionFuncion fullGetFuncion(String s) {
        for (Entorno e = this; e != null; e = e.getEntornoPadre()) {
            if (e instanceof EntornoClase) {
                if (((EntornoClase) e).containsFuncion(s)) {
                    return ((EntornoClase) e).getFuncion(s);
                }
            }
        }
        return null;
    }

    // Devuelve el Entorno de Función declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
    public EntornoFuncion fullGetFuncionEntorno(String s) {
        for (Entorno e = this; e != null; e = e.getEntornoPadre()) {
            if (e instanceof EntornoClase) {
                if (((EntornoClase) e).containsFuncion(s)) {
                    return ((EntornoClase) e).getFuncionEntorno(s);
                }
            }
        }
        return null;
    }

    // Devuelve el ID de Clase declarado más cercano (hacia arriba por entornos), null si no ha sido declarado
    public Declaracion fullGetClase(String s) {
        for (Entorno e = this; e != null; e = e.getEntornoPadre()) {
            if (((EntornoClase) e).containsClase(s)) {
                return ((EntornoClase) e).getClase(s);
            }
        }
        return null;
    }

    /* Dibujando el Entorno */

    public void printEntorno() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        sb.append(" -> ENTORNO TIPO: " + this.getIdentificador().getTipo() + " " + this.get_identificador_entorno() + ", de nivel " + this.getNivel() + " <- ");
        sb.append("\n");

        sb.append("\n");
        sb.append(" VARIABLES: ");
        sb.append("\n");
        sb.append("\n");
        if (this.getTablaIDs().isEmpty()) {
            sb.append("\n");
            sb.append(" - no hay identificadores declarados - ");
            sb.append("\n");
            sb.append("\n");
        } else {
            Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Declaracion id = this.getTablaIDs().get(key);
                sb.append("\n");
                sb.append("\n");
                if (id instanceof DeclaracionConstante) {
                    sb.append("CONSTANTE " + "ID: " + id.getId() + " , TIPO: " + id.getTipo() + "");
                } else {
                    sb.append("VARIABLE " + "ID: " + id.getId() + " , TIPO: " + id.getTipo() + "");
                }
                sb.append("\n");
                sb.append("\n");
            }
        }
        sb.append("\n");
        sb.append("_______________________________________");
        sb.append("\n");
        FicheroEntornos.almacenaEntorno(sb.toString());
    }

    public Integer getNivel() {
        return this.nivel;
    }

    public Entorno getEntornoPadre() {
        return entornoPadre;
    }

    public Hashtable<String, Declaracion> getTablaIDs() {
        return tablaIDs;
    }

    public void setTablaIDs(Hashtable<String, Declaracion> tablaIDs) {
        this.tablaIDs = tablaIDs;
    }

    public Integer get_identificador_entorno() {
        return _identificador_entorno;
    }

    public void set_identificador_entorno(Integer _identificador_entorno) {
        this._identificador_entorno = _identificador_entorno;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Declaracion getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Declaracion identificador) {
        this.identificador = identificador;
    }

    /**
     * Los bucles y los condicionales ( son los que usan este tipo de entorno ) no generan un
     * aumento de profundidad en los bloques de activacion.
     * Es por eso que la profundidad viene dictada por el entorno padre.
     */
    public int getProfundidad() {
        return this.entornoPadre.getProfundidad();
    }

    private String getTempName() {
        return "t" + nameSeq++;
    }

    public int getDesplazamiento(Declaracion decl) {
        int elementIndex = this.ids.indexOf(decl);
        int desplazamiento = 0;
        for (int index = 0; index < elementIndex; index++) {
            desplazamiento += this.ids.get(index).getOcupacion();
        }
        // El +2 proviene de que necesitamos saltarnos el BP. Obviamente esto no debería
        // ir aqui pero estoy escaso de imaginacion ahora mismo
        return desplazamiento + 2;
    }

    /*
    * Tal como esta declarado el entorno para los bucles y los condicionales se declaran
    * sus variable en los scopes independientes.
    * Esto deberia cambiar para que los entornos funcion fueran los que contuvieran las declaraciones de dichas variables
     */
    public int getTamanoTotalVariables() {
        int tamano = 0;

        List<Declaracion> localIds = this.getLocalVariables();

        for (Declaracion decl : localIds) {
            tamano += decl.getOcupacion();
        }

        // Llegado el caso, si se modificaran los entornos para declarar todo el tema de identificadores
        // dentro de condicionales y bucles y se hiciera a nivel de función, podríamos eliminar esta recursividad
        // ya que todas las variables se comprobarían a nivel de EntornoFuncion.
        for (Entorno hijo: entornosHijo) {
            tamano += hijo.getTamanoTotalVariables();
        }
        return tamano;
    }

    protected List<Declaracion> getLocalVariables() {
        return this.ids.stream().filter( x -> !x.isParam()).collect(Collectors.toList());
    }

    protected void registraEntornoHijo(Entorno hijo) {
        this.entornosHijo.add(hijo);
    }
}
