package tienda.dominio.descuentos;

import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public class DescuentoPorCantidad implements ReglaDescuento {
    @Override
    public boolean aplica(Carrito carrito, Cliente cliente) {
        return carrito.contarUnidades() >= 5;
    }

    @Override
    public double calcular(Carrito carrito, Cliente cliente) {
        return aplica(carrito, cliente) ? carrito.calcularSubtotal() * 0.10 : 0.0;
    }

    @Override
    public boolean esAcumulable() { return true; }

    @Override
    public String getNombre() { return "Descuento por cantidad (10%)"; }
}