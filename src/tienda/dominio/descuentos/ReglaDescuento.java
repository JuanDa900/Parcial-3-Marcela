package tienda.dominio.descuentos;

import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public interface ReglaDescuento {
    boolean aplica(Carrito carrito, Cliente cliente);
    double calcular(Carrito carrito, Cliente cliente);
    boolean esAcumulable();
    String getNombre();
}