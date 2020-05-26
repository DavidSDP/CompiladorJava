package optimizacion;

import java.util.ArrayList;

import intermedio.InstruccionTresDirecciones;

public class Optimizacion {

	public static InstruccionTresDirecciones getSiguiente(ArrayList<InstruccionTresDirecciones> instrucciones, int idx) {
        int numElementos = instrucciones.size();
        if (idx < numElementos) {
            return instrucciones.get(idx + 1);
        }
        return null;
    }
}
