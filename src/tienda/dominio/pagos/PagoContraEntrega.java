package tienda.dominio.pagos;

public class PagoContraEntrega implements MetodoPago {
    @Override
    public String getNombre() { return "Contra Entrega"; }

    @Override
    public boolean validar(double monto) {
        return monto > 0 && monto <= 500.0;
    }

    @Override
    public double calcularAjuste(double monto) {
        return 10.0;
    }

    @Override
    public String getDescripcionRegla() { return "Recargo fijo de $10.00 (Tope máximo de compra: $500.00)."; }
}