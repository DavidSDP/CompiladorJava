package Checkers;

public class TipoObject {
    protected Tipo tipo;

    // Tamano ocupado en memoria medido en Bytes.
    protected int size;

    public TipoObject(Tipo tipo, int size) {
        this.tipo = tipo;
        this.size = size;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoObject) {
            TipoObject other = (TipoObject) obj;
            return other.getTipo().equals(this.getTipo());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.tipo.name();
    }
}
