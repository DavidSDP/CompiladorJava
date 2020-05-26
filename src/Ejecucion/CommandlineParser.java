package Ejecucion;

import java.util.ArrayList;

public class CommandlineParser {
    private String[] args;
    private ArrayList<String> tokens;
    private ArrayList<String> positionalParams;
    private Integer nivelOptimizacion;
    private String filepath;

    public CommandlineParser(String args[]) {
        for (String arg: args) {
            System.out.println(arg);
        }
        this.args = args;
        this.tokens = new ArrayList<>();
        this.positionalParams = new ArrayList<>();
    }

    public int getNivelOptimizacion() {
        return nivelOptimizacion;
    }

    public String getFilepath() {
        return filepath;
    }

    public void parse() throws Exception {
        for (String arg : args) {
            if (arg.startsWith("-")) {
                tokens.add(arg);
            } else {
                positionalParams.add(arg);
            }
        }

        // Rellenar los tokens opcionales
        for (String token: tokens) {
            if (token.startsWith("-O")) {
                nivelOptimizacion = Integer.parseInt(token.substring(2));
            }
        }

        // por defecto el nivel de optimización es sin optimización
        if (nivelOptimizacion == null) {
            nivelOptimizacion = 0;
        }

        // De momento los parametros obligatorios solo incluyen el fichero de entrada
        if (positionalParams.size() > 2) {
            throw new Exception("Demasiados parametros posicionales. Solo se admiten el fichero de entrada y el nivel de optimizaci�n");
        } else {
            filepath = positionalParams.get(0);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fichero a compilar: ").append(positionalParams.get(0)).append(System.lineSeparator());
        sb.append("Nivel de optimización: ").append(nivelOptimizacion);

        return sb.toString();
    }
}
