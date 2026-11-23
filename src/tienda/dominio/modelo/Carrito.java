package tienda.dominio.modelo;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private List<ItemCarrito> items = new ArrayList<>();

    public boolean agregar(Producto producto, int cantidad) {
        if (cantidad <= 0) return false;
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (!producto.hayStock(nuevaCantidad)) return false;
                item.setCantidad(nuevaCantidad);
                return true;
            }
        }
        if (!producto.hayStock(cantidad)) return false;
        items.add(new ItemCarrito(producto, cantidad));
        return true;
    }

    public boolean modificarCantidad(String codigoProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return eliminar(codigoProducto);
        }
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                if (!item.getProducto().hayStock(nuevaCantidad)) return false;
                item.setCantidad(nuevaCantidad);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(String codigoProducto) {
        return items.removeIf(i -> i.getProducto().getCodigo().equalsIgnoreCase(codigoProducto));
    }

    public double calcularSubtotal() {
        double total = 0;
        for (ItemCarrito item : items) total += item.getSubtotal();
        return total;
    }

    public int contarUnidades() {
        int total = 0;
        for (ItemCarrito item : items) total += item.getCantidad();
        return total;
    }

    public boolean estaVacio() { return items.isEmpty(); }
    public List<ItemCarrito> getItems() { return new ArrayList<>(items); }
    public void vaciar() { items.clear(); }
}