package intermedio;

public class BloqueBasico {
    private int id, inicio, fin;

    public BloqueBasico(int id, int inicio, int fin) {
        this(id, inicio);
        this.fin = fin;
    }

    public BloqueBasico(int id) {
        this(id, -1);
    }

    public BloqueBasico(int id, int inicio) {
        this.id = id;
        this.inicio = inicio;
        this.fin = -1;
    }

    public int getInicio() {
        return inicio;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    @Override
    public int hashCode() {
        String str = "" + inicio + "" + fin;
        return str.hashCode();
    }
}
