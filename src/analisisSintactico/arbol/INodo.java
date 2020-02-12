package analisisSintactico.arbol;

import java.util.List;

public interface INodo {
	List<INodo> getChildren();
	String getName();
	Integer getIdentificadorUnico();
}
