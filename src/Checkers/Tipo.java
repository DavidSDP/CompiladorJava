package Checkers;

import Errores.ErrorSemantico;

public enum Tipo {

    /**
     * TODO Aqui hay elementos que no son tipos, vease:
     * - Class
     * - Final
     * - Identificador
     * - Token
     * - Comparable
     * - IF
     * - While
     * - ELSE
     * <p>
     * Palabras reservadas: Class, Final, If, While, Else
     * Cosas sin categorizar: Identificador, Token, Comparable.
     * Por lo que se, Identificador, es cualquier palabra admitida como nombre de variable.
     * <p>
     * De las siguientes no estoy tan seguro, esta puedo comprender que se queden aqui
     * - Void
     * - Array
     *
     */
    Integer, Char, String, Boolean, Class, Void, Array, Final, Identificador, Token, Comparable, IF, WHILE, ELSE;

    public static TipoObject getTipo(Tipo tipo) throws ErrorSemantico {
        switch (tipo) {
	        case Integer:
	            return new TipoObject(Tipo.Integer, 2);
	        case Char:
	            return new TipoObject(Tipo.Char, 2);
            case String:
            case Array:
                // Tanto string como array ocupan dos palabras.
                // la primera contiene el puntero a memoria dinámica y el segundo el número
                // de elementos en el vector ( Spoiler: el string es un array de caracteres )
                return new TipoObject(tipo, 8);
            case Boolean:
                return new TipoObject(Tipo.Boolean, 2);
            case Void:
                return new TipoObject(Tipo.Void, 0);
            // Ojo!!! Esto no son tipos
            case Class:
            case Final:
            case IF:
            case WHILE:
            case ELSE:
				return new TipoObject(tipo, 0);
			default:
				throw new ErrorSemantico("No se ha encontrado el tipo especificado");
        }
    }

	public static TipoObject getTipoSafe(Tipo t) {
		try {
			return Tipo.getTipo(t);
		} catch (ErrorSemantico errorSemantico) {
			System.err.println(errorSemantico.toString());
			return null;
		}
	}

    public static TipoObject getTipo(String s) throws ErrorSemantico {
        switch (s) {
            case "void":
                return new TipoObject(Tipo.Void, 0);
            case "Char":
                return new TipoObject(Tipo.Char, 2);
            case "array":
                return new TipoObject(Tipo.Array, 8);
            case "String":
                return new TipoObject(Tipo.String, 8);
            case "int":
                return new TipoObject(Tipo.Integer, 2);
            case "boolean":
                return new TipoObject(Tipo.Boolean, 2);

            // Ojo! esto no son tipos
            case "final":
                return new TipoObject(Tipo.Final, 0);
            case "if":
                return new TipoObject(Tipo.IF, 0);
            case "else":
                return new TipoObject(Tipo.ELSE, 0);
            case "while":
                return new TipoObject(Tipo.WHILE, 0);
            case "class":
                return new TipoObject(Tipo.Class, 0);
            default:
                throw new ErrorSemantico("No se ha encontrado el tipo especificado");
        }
    }

    public static TipoObject getTipoSafe(String s) {
        try {
            return Tipo.getTipo(s);
        } catch (ErrorSemantico errorSemantico) {
            System.err.println(errorSemantico.toString());
            return null;
        }
    }

}
