package tienda.dominio.modelo;

public class ProductoAlimento extends Producto {
    private String fechaCaducidad;
    private boolean refrigerado; 

    public ProductoAlimento(String codigo, String nombre, double precio, int stock, String fechaCaducidad, boolean refrigerado) {
        super(codigo, nombre, precio, stock);
        this.fechaCaducidad = fechaCaducidad;
        this.refrigerado = refrigerado;
    }

    @Override
    public String getCategoria() {
        return "Alimento";
    }

    @Override
    public String getAtributosEspecificos() {
        return String.format("Fecha de caducidad: %s, Refrigerado: %s", fechaCaducidad, refrigerado);
    }

    @Override
    public String serializarEspecificos() {
        return String.format("%s,%s", fechaCaducidad, refrigerado);
    }
}
