package tienda.puertos.salida;

import java.util.List;
import tienda.dominio.modelo.Pedido;

public interface PedidoRepository {

    void guardar(Pedido pedido);

    List<Pedido> obtenerTodos();

    void actualizarTodos(List<Pedido> pedidos);
}
