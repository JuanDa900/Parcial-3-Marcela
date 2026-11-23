package tienda.dominio.pagos;

public class PagoTransferencia implements MetodoPago {
    @Override
    public String getNombre() { return "Transferencia"; }

    @Override
    public boolean validar(double monto) { return monto > 0; }

    @Override
    public double calcularAjuste(double monto) {
        return -(monto * 0.05);
    }

    @Override
    public String getDescripcionRegla() { return "5% de descuento adicional por transferencia bancaria."; }
}