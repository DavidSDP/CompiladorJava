package Procesador;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import Checkers.Tipo;
import Checkers.TipoObject;
import Ejecucion.FicheroEntornos;
import Errores.ErrorSemantico;

public class EntornoClase extends Entorno {
    private static int classSequence = 0;

    private Hashtable<String, Declaracion> tablaClases;
    private Hashtable<String, List<DeclaracionFuncion>> tablaFunciones;

    private int profundidad;

    public EntornoClase(Entorno entornoPadre, Declaracion identificador) {
        super(entornoPadre, identificador);
        this.tablaFunciones = new Hashtable<>();
        this.tablaClases = new Hashtable<>();

        // Notese que este es el único punto donde se puede crear un entorno sin entorno padre.
        // Tal como esta montada la gramatica, las funciones no pueden estar fuera de las clases.
        if (entornoPadre == null) {
            this.profundidad = 0;
        } else {
            this.profundidad = entornoPadre.getProfundidad() + 1;
        }
    }

    ////////*	IDENTIFICADORES DE FUNCIONES	*////////

    // Introduce nuevo ID de Función en el entorno actual
    public DeclaracionFuncion putFuncion(TipoObject tipo, String s, String etiqueta) throws ErrorSemantico {
    	// Esta comprobación ya no se puede hacer aquí. Puede que nos de problemas en el caso de las
		// funciones que no tengan parametros. Si es el caso hay que mirar si en el equals hay que ponerle si la
		// función está en proceso de construcción.
//        if (this.containsFuncion(s))
//            throw new ErrorSemantico("El identificador de función '" + s + "' se ha declarado por duplicado");

        DeclaracionFuncion decl = new DeclaracionFuncion(new Identificador(s, s), tipo, etiqueta);
        // Bucket para funciones con el mismo nombre. Después tendremos que filtrar por parámetros para poder
        // permitir la sobrecarga de funciones
        List<DeclaracionFuncion> funciones = tablaFunciones.getOrDefault(s, new ArrayList<>());
        this.tablaFunciones.put(s, funciones);
        funciones.add(decl);
        return decl;
    }

    // Devuelve true si el ID de Función ha sido declarado en el entorno actual
    public Boolean containsFuncion(String s, ArrayList<Declaracion> declaracionParametros) {
        List<TipoObject> tipoParametros = declaracionParametros.stream().map(Declaracion::getTipo).collect(Collectors.toList());
        return findFuncion(s, tipoParametros) != null;
    }

    // Devuelve true si el ID de Función ha sido declarado en el entorno actual
    public Boolean containsFuncion(String s, List<TipoObject> tipoParametros) {
        return findFuncion(s, tipoParametros) != null;
    }

    // Devuelve el ID de Función especificado en el entorno actual
    public DeclaracionFuncion getFuncion(String s, ArrayList<Declaracion> declaracionParams) {
        List<TipoObject> tipoParametros = declaracionParams.stream().map(Declaracion::getTipo).collect(Collectors.toList());
        return findFuncion(s, tipoParametros);
    }

    // Devuelve el ID de Función especificado en el entorno actual
    public DeclaracionFuncion getFuncion(String s, List<TipoObject> tipoParams) {
        return findFuncion(s, tipoParams);
    }

    public DeclaracionFuncion findFuncion(String s, List<TipoObject> tipoParametros) {
		// Buscamos la función que concuerda con los parametros que nos pasan.
		// Esto nos permite decir que permitimos la sobrecarga de funciones
		List<DeclaracionFuncion> funciones = this.tablaFunciones.getOrDefault(s, null);
		boolean encontrada = false;
		DeclaracionFuncion funcion = null;
		if (funciones != null) {
			for(int idx = 0; idx < funciones.size() && !encontrada; idx++) {
				funcion = funciones.get(idx);
				encontrada = funcion.coincidenParams(tipoParametros);
			}

		}

		if (encontrada) {
			return funcion;
		} else {
			return null;
		}
	}

    ////////*	IDENTIFICADORES	DE CLASES	*////////

    // Introduce nuevo ID de Clase en el entorno actual
    public DeclaracionClase putClase(String s) throws ErrorSemantico {
        if (this.containsSoloPropioEntorno(s))
            throw new ErrorSemantico("El identificador de clase '" + s + "' se ha declarado por duplicado");
        DeclaracionClase decl = new DeclaracionClase(new Identificador(s, s), Tipo.getTipo(Tipo.Class.name().toLowerCase()));
        decl.setEtiquetaDeclaraciones(generateClassLabel());
        decl.setEtiquetaPostInicializacion(generateClassLabel());
        decl.setEtiquetaPreInicializacion(generateClassLabel());
        this.tablaClases.put(s, decl);
        return decl;
    }

    // Devuelve true si el ID de Clase ha sido declarado en el entorno actual
    public Boolean containsClase(String s) {
        return this.tablaClases.containsKey(s);
    }

    // Devuelve el ID de Clase especificado en el entorno actual
    public Declaracion getClase(String s) {
        if (!this.containsClase(s)) {
            return null;
        }
        return this.tablaClases.get(s);
    }

    @Override
    public int getProfundidad() {
        return this.profundidad;
    }

    /* Dibujando el Entorno */

    public void printEntorno() throws IOException {
        StringBuffer sb = new StringBuffer();
        sb.append("\n");
        if (this.getIdentificador() == null) {
            sb.append(" -> ENTORNO PROGRAMA PRINCIPAL " + this.get_identificador_entorno() + ", de nivel " + this.getNivel() + " <- ");
        } else {
            sb.append(" -> ENTORNO CLASE " + this.get_identificador_entorno() + ", de nivel " + this.getNivel() + " <- ");
        }
        sb.append("\n");

        sb.append("\n");
        sb.append(" VARIABLES: ");
        sb.append("\n");
        sb.append("\n");
        if (this.getTablaIDs().isEmpty()) {
            sb.append(" - no hay identificadores declarados - ");
            sb.append("\n");
        } else {
            Iterator<String> iterator = this.getTablaIDs().keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Declaracion id = this.getTablaIDs().get(key);
                if (id instanceof DeclaracionConstante) {
                    sb.append("CONSTANTE " + "ID: " + id.getId().getId() + " , TIPO: " + id.getTipo() + "");
                } else {
                    sb.append("VARIABLE " + "ID: " + id.getId().getId() + " , TIPO: " + id.getTipo() + "");
                }
                sb.append("\n");
                sb.append("\n");
            }
        }

        sb.append("\n");
        sb.append(" CLASES: ");
        sb.append("\n");
        sb.append("\n");
        if (tablaClases.isEmpty()) {
            sb.append(" - no hay clases declaradas - ");
            sb.append("\n");
            sb.append("\n");
        } else {
            Iterator<String> iterator = tablaClases.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Declaracion id = tablaClases.get(key);
                sb.append("ID: " + id.getId().getId() + " , TIPO: " + id.getTipo());
                sb.append("\n");
                sb.append("\n");
            }
        }

        sb.append("\n");
        sb.append(" FUNCIONES: ");
        sb.append("\n");
        sb.append("\n");

        if (tablaFunciones.isEmpty()) {
            sb.append("\n");
            sb.append(" - no hay funciones declaradas - ");
            sb.append("\n");
            sb.append("\n");
        } else {
			Iterator<Map.Entry<String, List<DeclaracionFuncion>>> iteratorFunciones = tablaFunciones.entrySet().iterator();
			for (Map.Entry<String, List<DeclaracionFuncion>> funciones : tablaFunciones.entrySet()) {
				for (DeclaracionFuncion funcion : funciones.getValue()) {
					sb.append("ID: " + funcion.getId().getId() + " , TIPO: " + funcion.getTipo());
					sb.append("\n");
					sb.append("     -> argumentos: ");
					sb.append("\n");
					sb.append("\n");
					List<String> argumentos = (funcion.getEntorno()).getArgs();
					if (argumentos == null || argumentos.isEmpty()) {
						sb.append("              -> sin argumentos <-");
						sb.append("\n");
						sb.append("\n");
					} else {
						for (String arg : argumentos) {
							Declaracion idArgumento = funcion.getEntorno().get(arg);
							sb.append("             -> id: " + idArgumento.getId().getId() + " , tipo: " + idArgumento.getTipo());
							sb.append("\n");
							sb.append("\n");
						}
					}
				}
			}
        }
        sb.append("\n");
        sb.append("\n");
        sb.append("_______________________________________");
        sb.append("\n");
        sb.append("\n");
        FicheroEntornos.almacenaEntorno(sb.toString());
    }

    protected String generateClassLabel() {
        return "c" + ++classSequence;
    }

    /**
     * La memoria necesaria para una clase es solo la acumlacion
     * del tamano de las variables.
     * <p>
     * Las funciones no utilizan ningun sistema que necesite almacenar la memoria en runtime ( vtable )
     */
    @Override
    public int getTamanoTotalVariables() {
        int tamano = 0;
        for (Declaracion decl : ids) {
            tamano += decl.getOcupacion();
        }
        return tamano;
    }

    public boolean existenDuplicidades(DeclaracionFuncion declaracion) {
        List<DeclaracionFuncion> funciones = this.tablaFunciones.get(declaracion.getId().getId());
        List<TipoObject> tipoParametros = declaracion.getTipoArgumentos();

        int coincidencias = 0;
        for (DeclaracionFuncion funcion : funciones) {
            if (funcion.coincidenParams(tipoParametros)) {
                coincidencias++;
            }
        }

        // Nuestra funcion debería ser la única que hace que coincidan los parámetros de la que buscamos.
        return coincidencias > 1;
    }
}
