package tienda.puertos.salida;

import java.util.List;
import tienda.dominio.modelo.Producto;

public interface ProductoRepository {

    List<Producto> obtenerTodos();

    void guardarTodos(List<Producto> productos);
}
