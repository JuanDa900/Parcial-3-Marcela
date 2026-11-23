package tienda.dominio.descuentos;

import java.util.ArrayList;
import java.util.List;
import tienda.dominio.modelo.Carrito;
import tienda.dominio.modelo.Cliente;

public class CalculadorDescuentos {
    private List<ReglaDescuento> reglas = new ArrayList<>();

    public void registrarRegla(ReglaDescuento regla) {
        reglas.add(regla);
    }

    public static class ResultadoDescuento {
        public final double montoTotal;
        public final String detalle;

        public ResultadoDescuento(double montoTotal, String detalle) {
            this.montoTotal = montoTotal;
            this.detalle = detalle;
        }
    }

    public ResultadoDescuento calcularTotalDescuentos(Carrito carrito, Cliente cliente) {
        double acumulado = 0;
        StringBuilder detalleAcumulado = new StringBuilder();

        double mejorNoAcumulable = 0;
        String detalleNoAcumulable = "";

        for (ReglaDescuento regla : reglas) {
            if (regla.aplica(carrito, cliente)) {
                double valor = regla.calcular(carrito, cliente);
                if (regla.esAcumulable()) {
                    acumulado += valor;
                    if (detalleAcumulado.length() > 0) detalleAcumulado.append(" + ");
                    detalleAcumulado.append(regla.getNombre());
                } else if (valor > mejorNoAcumulable) {
                    mejorNoAcumulable = valor;
                    detalleNoAcumulable = regla.getNombre();
                }
            }
        }

        if (mejorNoAcumulable > acumulado) {
            return new ResultadoDescuento(mejorNoAcumulable, detalleNoAcumulable);
        }
        return new ResultadoDescuento(acumulado, detalleAcumulado.toString());
    }
}