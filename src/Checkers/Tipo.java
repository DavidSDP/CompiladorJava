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
     */
    Integer, String, Boolean, Class, Void, Array, Final, Identificador, Token, Comparable, IF, WHILE, ELSE;

    public static TipoObject getTipo(Tipo t) throws ErrorSemantico {
        switch (t) {
            case Integer:
                return new TipoObject(Tipo.Integer, 2);
            case String:
                // Esto es un poco diferente. Probablemente este elemento sea un valor en memoria dinamica
                // y el footprint deberia ser por caracter.
                return new TipoObject(Tipo.String, 2);
            case Boolean:
                return new TipoObject(Tipo.Boolean, 2);
            case Class:
                return new TipoObject(Tipo.Class, 0);
            case Void:
                return new TipoObject(Tipo.Void, 0);
            case Array:
                // TODO Este tipo deberia ser algo especial que calcule el tamaño en base al tipo
				// basico del elemento
                return new TipoObject(Tipo.Array, 0);
            case Final:
				return new TipoObject(Tipo.Final, 0);
            case IF:
				return new TipoObject(Tipo.IF, 0);
            case WHILE:
				return new TipoObject(Tipo.WHILE, 0);
            case ELSE:
				return new TipoObject(Tipo.ELSE, 0);
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
            case "array":
                // TODO Este tipo deberia ser algo especial que calcule el tamaño en base al tipo
                // basico del elemento
                return new TipoObject(Tipo.Array, 0);
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
            case "int":
                return new TipoObject(Tipo.Integer, 2);
            case "String":
                // Esto es un poco diferente. Probablemente este elemento sea un valor en memoria dinamica
                // y el footprint deberia ser por caracter.
                return new TipoObject(Tipo.String, 2);
            case "boolean":
                return new TipoObject(Tipo.Boolean, 2);
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
