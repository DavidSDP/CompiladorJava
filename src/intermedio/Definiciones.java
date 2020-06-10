package intermedio;

import java.util.HashSet;
import java.util.Set;

public class Definiciones {

    private HashSet<InstruccionTresDirecciones> definiciones;
    public Definiciones() {
        definiciones = new HashSet<>();
    }

    public void add(InstruccionTresDirecciones definicion) {
        this.definiciones.add(definicion);
    }

    public Set<InstruccionTresDirecciones> getDefiniciones() {
        return this.definiciones;
    }

    private static Definiciones instance;
    public static Definiciones getInstance() {
        if (instance == null) {
            instance = new Definiciones();
        }
        return instance;
    }
}
