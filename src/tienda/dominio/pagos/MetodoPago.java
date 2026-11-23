package tienda.dominio.pagos;

public interface MetodoPago {
    String getNombre();
    boolean validar(double monto);
    double calcularAjuste(double monto);
    String getDescripcionRegla();
}