package Checkers;

import Errores.ErrorSemantico;

public enum Tipo {
	
	Integer, String, Boolean, Class, Void, Identificador, Token, Comparable;
	
	public static Tipo getTipo(String s) throws ErrorSemantico {
		switch(s) {
			case "void":
				return Tipo.Void;
			case "class":
				return Tipo.Class;
			case "int":
				return Tipo.Integer;
			case "String":
				return Tipo.String;
			case "boolean":
				return Tipo.Boolean;
			default:
				throw new ErrorSemantico("No se ha encontrado el tipo especificado");
		}
	}
	
}
