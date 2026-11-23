package tienda.dominio.catalogo;

import tienda.dominio.modelo.Producto;

public abstract class CreadorProducto {
    public abstract Producto crearProducto(String codigo, String nombre, double precio, int stock, String dato1, String dato2);
}
