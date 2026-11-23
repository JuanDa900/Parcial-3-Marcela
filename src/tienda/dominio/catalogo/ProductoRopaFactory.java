package tienda.dominio.catalogo;

import tienda.dominio.modelo.Producto;
import tienda.dominio.modelo.ProductoRopa;

public class ProductoRopaFactory extends CreadorProducto {
    @Override
    public Producto crearProducto(String codigo, String nombre, double precio, int stock, String talla, String material) {
        return new ProductoRopa(codigo, nombre, precio, stock, talla, material);
    }
}
