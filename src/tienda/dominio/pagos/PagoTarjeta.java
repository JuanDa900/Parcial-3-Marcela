package tienda.dominio.pagos;

public class PagoTarjeta implements MetodoPago {
    private int cuotas;

    public PagoTarjeta(int cuotas) {
        this.cuotas = cuotas;
    }

    @Override
    public String getNombre() { return "Tarjeta (" + cuotas + "c)"; }

    @Override
    public boolean validar(double monto) { return monto > 0 && cuotas >= 1 && cuotas <= 12; }

    @Override
    public double calcularAjuste(double monto) {
        return (cuotas > 1) ? monto * 0.03 : 0.0;
    }

    @Override
    public String getDescripcionRegla() {
        return (cuotas > 1) ? "Recargo del 3% por diferir a cuotas." : "Sin recargo a 1 cuota.";
    }
}