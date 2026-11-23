package tienda.dominio.descuentos;

import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public class DescuentoTemporada implements ReglaDescuento {
    private boolean temporadaActiva;

    public DescuentoTemporada(boolean temporadaActiva) {
        this.temporadaActiva = temporadaActiva;
    }

    @Override
    public boolean aplica(Carrito carrito, Cliente cliente) {
        return temporadaActiva;
    }

    @Override
    public double calcular(Carrito carrito, Cliente cliente) {
        return aplica(carrito, cliente) ? carrito.calcularSubtotal() * 0.12 : 0.0;
    }

    @Override
    public boolean esAcumulable() { return true; }

    @Override
    public String getNombre() { return "Temporada especial (12%)"; }
}