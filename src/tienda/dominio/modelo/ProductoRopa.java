package tienda.dominio.modelo;

public class ProductoRopa extends Producto {
    private String talla;
    private String material;

    public ProductoRopa(String codigo, String nombre, double precio, int stock, String talla, String material) {
        super(codigo, nombre, precio, stock);
        this.talla = talla;
        this.material = material;
    }

    @Override public String getCategoria() { return "Ropa"; }
    @Override public String getAtributosEspecificos() { return String.format("Talla: %s, Material: %s", talla, material); }
    @Override public String serializarEspecificos() { return String.format("%s,%s", talla, material); }
}
