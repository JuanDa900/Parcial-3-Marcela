package tienda.dominio.descuentos;

import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public class DescuentoClienteFrecuente implements ReglaDescuento {
    @Override
    public boolean aplica(Carrito carrito, Cliente cliente) {
        return cliente != null && cliente.isFrecuente();
    }

    @Override
    public double calcular(Carrito carrito, Cliente cliente) {
        return aplica(carrito, cliente) ? carrito.calcularSubtotal() * 0.05 : 0.0;
    }

    @Override
    public boolean esAcumulable() { return true; }

    @Override
    public String getNombre() { return "Cliente frecuente (5%)"; }
}