package Procesador;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Almacen de variables para poder utilizarlas en la fase de optimización.
 * Probablemente esto podría ir en algún otro lado, pero como veo que te gusta
 * reordenar código pues te lo dejo a ti @David.
 */
public class AlmacenVariables {

    private ArrayList<Declaracion> variables;
    private HashMap<Declaracion, Integer> asignaciones;

    protected AlmacenVariables() {
        this.variables = new ArrayList<>();
        this.asignaciones = new HashMap<>();
    }


    // Adicion de variable al almacen de variables.
    public void add(Declaracion declaracion) {
        variables.add(declaracion);
        asignaciones.put(declaracion, 0);
    }

    public void incrementaAsignaciones(Declaracion declaracion) {
        int asignaciones = this.asignaciones.getOrDefault(declaracion, -1);
        assert asignaciones != -1;

        this.asignaciones.put(declaracion, ++asignaciones);
    }

    public int getAsignaciones(Declaracion declaracion) {
        return this.asignaciones.get(declaracion);
    }

    public void resetAsignaciones() {
        for(Declaracion variable: variables) {
            asignaciones.put(variable, 0);
        }
    }


    // Singleton access
    private static AlmacenVariables instance;
    public static AlmacenVariables getInstance() {
        if (instance == null) {
            instance = new AlmacenVariables();
        }
        return instance;
    }
}
