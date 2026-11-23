package tienda.dominio.descuentos;

import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public class DescuentoPorCupon implements ReglaDescuento {
    private String codigoIngresado;

    public DescuentoPorCupon(String codigoIngresado) {
        this.codigoIngresado = codigoIngresado;
    }

    @Override
    public boolean aplica(Carrito carrito, Cliente cliente) {
        return codigoIngresado != null && codigoIngresado.equalsIgnoreCase("PROMO20");
    }

    @Override
    public double calcular(Carrito carrito, Cliente cliente) {
        return aplica(carrito, cliente) ? carrito.calcularSubtotal() * 0.20 : 0.0;
    }

    @Override
    public boolean esAcumulable() { return false; }

    @Override
    public String getNombre() { return "Cupón PROMO20 (20% - No acumulable)"; }
}