package tienda.puertos.entrada;

import java.util.List;
import tienda.dominio.modelo.*;
import tienda.dominio.pagos.MetodoPago;

public interface TiendaUseCase {

    List<Producto> listarCatalogo();

    List<Producto> listarPorCategoria(String categoria);

    Producto buscarProducto(String codigo);

    boolean agregarAlCarrito(String codigoProducto, int cantidad);

    boolean modificarCantidadCarrito(String codigoProducto, int nuevaCantidad);

    boolean eliminarDelCarrito(String codigoProducto);

    Carrito obtenerCarrito();

    Pedido confirmarCompra(Cliente cliente, String cupon, boolean envioExpress, MetodoPago metodoPago);

    List<Pedido> consultarHistorialPedidos();

    boolean avanzarEstadoEnvioPedido(int idPedido);
}
