package tienda.aplicacion;

import java.util.ArrayList;
import java.util.List;
import tienda.dominio.descuentos.*;
import tienda.dominio.modelo.*;
import tienda.dominio.pagos.MetodoPago;
import tienda.puertos.entrada.TiendaUseCase;
import tienda.puertos.salida.PedidoRepository;
import tienda.puertos.salida.ProductoRepository;

public class TiendaService implements TiendaUseCase {

    private final ProductoRepository productoRepo;
    private final PedidoRepository pedidoRepo;
    private final Carrito carrito;
    private final boolean temporadaActiva = true;

    public TiendaService(ProductoRepository productoRepo, PedidoRepository pedidoRepo) {
        this.productoRepo = productoRepo;
        this.pedidoRepo = pedidoRepo;
        this.carrito = new Carrito();
    }

    @Override
    public List<Producto> listarCatalogo() {
        return productoRepo.obtenerTodos();
    }

    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        List<Producto> filtrados = new ArrayList<>();
        for (Producto p : productoRepo.obtenerTodos()) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                filtrados.add(p);
            }
        }
        return filtrados;
    }

    @Override
    public Producto buscarProducto(String codigo) {
        for (Producto p : productoRepo.obtenerTodos()) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean agregarAlCarrito(String codigoProducto, int cantidad) {
        Producto p = buscarProducto(codigoProducto);
        if (p == null) {
            return false;
        }
        return carrito.agregar(p, cantidad);
    }

    @Override
    public boolean modificarCantidadCarrito(String codigoProducto, int nuevaCantidad) {
        return carrito.modificarCantidad(codigoProducto, nuevaCantidad);
    }

    @Override
    public boolean eliminarDelCarrito(String codigoProducto) {
        return carrito.eliminar(codigoProducto);
    }

    @Override
    public Carrito obtenerCarrito() {
        return carrito;
    }

    @Override
    public Pedido confirmarCompra(Cliente cliente, String cupon, boolean envioExpress, MetodoPago metodoPago) {
        if (carrito.estaVacio() || metodoPago == null) {
            return null;
        }

        CalculadorDescuentos calculador = new CalculadorDescuentos();
        calculador.registrarRegla(new DescuentoPorCantidad());
        calculador.registrarRegla(new DescuentoClienteFrecuente());
        calculador.registrarRegla(new DescuentoTemporada(temporadaActiva));
        calculador.registrarRegla(new DescuentoPorCupon(cupon));

        double subtotal = carrito.calcularSubtotal();
        CalculadorDescuentos.ResultadoDescuento desc = calculador.calcularTotalDescuentos(carrito, cliente);

        double costoExpress = envioExpress ? 15.00 : 0.0;
        double montoAntesDePago = (subtotal - desc.montoTotal) + costoExpress;

        if (!metodoPago.validar(montoAntesDePago)) {
            return null;
        }

        double ajustePago = metodoPago.calcularAjuste(montoAntesDePago);
        double totalFinal = montoAntesDePago + ajustePago;

        List<Producto> catalogoActual = productoRepo.obtenerTodos();
        for (ItemCarrito item : carrito.getItems()) {
            for (Producto prodCatalogo : catalogoActual) {
                if (prodCatalogo.getCodigo().equalsIgnoreCase(item.getProducto().getCodigo())) {
                    prodCatalogo.descontarStock(item.getCantidad());
                }
            }
        }
        productoRepo.guardarTodos(catalogoActual);

        int nuevoId = 1000 + pedidoRepo.obtenerTodos().size() + 1;
        Pedido pedido = new Pedido(
                nuevoId, cliente, carrito.getItems(), subtotal,
                desc.montoTotal, desc.detalle, costoExpress,
                ajustePago, totalFinal, metodoPago.getNombre()
        );

        pedidoRepo.guardar(pedido);
        carrito.vaciar();
        return pedido;
    }

    @Override
    public List<Pedido> consultarHistorialPedidos() {
        return pedidoRepo.obtenerTodos();
    }

    @Override
    public boolean avanzarEstadoEnvioPedido(int idPedido) {
        List<Pedido> pedidos = pedidoRepo.obtenerTodos();
        for (Pedido p : pedidos) {
            if (p.getId() == idPedido) {
                p.avanzarEstadoEnvio();
                pedidoRepo.actualizarTodos(pedidos);
                return true;
            }
        }
        return false;
    }
}
