package SimbolosNoTerminales;

/**
* Clase generica para propagar una etiqueta en el flujo de analisis sintactico
*/
public class SimboloEtiqueta {
    private String etiqueta;

    public SimboloEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return this.etiqueta;
    }
}
