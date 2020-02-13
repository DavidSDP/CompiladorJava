package Checkers;

import Errores.ErrorSemantico;

public enum Tipo {
	
	Integer, String, Boolean, Class, Void, Identificador, Token, Comparable, IF, WHILE, ELSE;
	
	public static Tipo getTipo(String s) throws ErrorSemantico {
		switch(s) {
			case "void":
				return Tipo.Void;
			case "if":
				return Tipo.IF;
			case "else":
				return Tipo.ELSE;
			case "while":
				return Tipo.WHILE;
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
