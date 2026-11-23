package tienda.dominio.modelo;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public abstract String getCategoria();
    public abstract String getAtributosEspecificos();
    public abstract String serializarEspecificos();

    public boolean hayStock(int cantidad) {
        return cantidad > 0 && this.stock >= cantidad;
    }

    public void descontarStock(int cantidad) {
        this.stock -= cantidad;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    @Override
    public String toString() {
        return String.format("[%s] %-22s | $%7.2f | Stock: %-3d | %s (%s)",
                codigo, nombre, precio, stock, getCategoria(), getAtributosEspecificos());
    }
}