package tienda.dominio.catalogo;

import tienda.dominio.modelo.Producto;
import tienda.dominio.modelo.ProductoTecnologia;

public class ProductoTecnologiaFactory extends CreadorProducto {

    @Override
    public Producto crearProducto(String codigo, String nombre, double precio, int stock, String marca, String mesesGarantia) {
        return new ProductoTecnologia(codigo, nombre, precio, stock, marca, Integer.parseInt(mesesGarantia));
    }
}
