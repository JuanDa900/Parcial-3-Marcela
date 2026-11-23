package tienda.dominio.catalogo;

import tienda.dominio.modelo.Producto;
import tienda.dominio.modelo.ProductoAlimento;

public class ProductoAlimentoFactory extends CreadorProducto {
    @Override
    public Producto crearProducto(String codigo, String nombre, double precio, int stock, String fechaCaducidad, String esRefrigerado) {
        return new ProductoAlimento(codigo, nombre, precio, stock, fechaCaducidad, Boolean.parseBoolean(esRefrigerado));
    }
}
