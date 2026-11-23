package tienda.dominio.modelo;

public class ProductoTecnologia extends Producto {
    private String marca;
    private int mesesGarantia;

    public ProductoTecnologia(String codigo, String nombre, double precio, int stock, String marca, int mesesGarantia) {
        super(codigo, nombre, precio, stock);
        this.marca = marca;
        this.mesesGarantia = mesesGarantia;
    }

    @Override public String getCategoria() { return "Tecnologia"; }
    @Override public String getAtributosEspecificos() { return "Marca: " + marca + ", Garantía: " + mesesGarantia + " meses"; }
    @Override public String serializarEspecificos() { return marca + ";" + mesesGarantia; }
}