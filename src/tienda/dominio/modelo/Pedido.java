package tienda.dominio.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemCarrito> items;
    private double subtotal;
    private double totalDescuentos;
    private String detalleDescuentos;
    private double costoEnvioExpress;
    private double ajustePago;
    private double totalPagado;
    private String metodoPago;
    private EstadoEnvio estadoEnvio;
    private LocalDateTime fecha;

    public Pedido(int id, Cliente cliente, List<ItemCarrito> items, double subtotal,
                  double totalDescuentos, String detalleDescuentos, double costoEnvioExpress,
                  double ajustePago, double totalPagado, String metodoPago) {
        this.id = id;
        this.cliente = cliente;
        this.items = items;
        this.subtotal = subtotal;
        this.totalDescuentos = totalDescuentos;
        this.detalleDescuentos = detalleDescuentos;
        this.costoEnvioExpress = costoEnvioExpress;
        this.ajustePago = ajustePago;
        this.totalPagado = totalPagado;
        this.metodoPago = metodoPago;
        this.estadoEnvio = EstadoEnvio.CONFIRMADO;
        this.fecha = LocalDateTime.now();
    }

    public void avanzarEstadoEnvio() {
        switch (estadoEnvio) {
            case CONFIRMADO -> estadoEnvio = EstadoEnvio.EN_PREPARACION;
            case EN_PREPARACION -> estadoEnvio = EstadoEnvio.EN_CAMINO;
            case EN_CAMINO -> estadoEnvio = EstadoEnvio.ENTREGADO;
            case ENTREGADO -> {}
        }
    }

    public String generarFactura() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("\n==================================================\n");
        sb.append("               FACTURA DE VENTA #").append(id).append("\n");
        sb.append("==================================================\n");
        sb.append("Fecha:   ").append(fecha.format(fmt)).append("\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append(" (CC: ").append(cliente.getDocumento()).append(")\n");
        sb.append("Estado:  ").append(estadoEnvio.name()).append(" - ").append(estadoEnvio.getDescripcion()).append("\n");
        sb.append("--------------------------------------------------\n");
        for (ItemCarrito item : items) {
            sb.append(String.format("%-24s x%-3d  $%8.2f\n",
                    item.getProducto().getNombre(), item.getCantidad(), item.getSubtotal()));
        }
        sb.append("--------------------------------------------------\n");
        sb.append(String.format("Subtotal:                      $%8.2f\n", subtotal));
        sb.append(String.format("Descuentos:                   -$%8.2f\n", totalDescuentos));
        if (!detalleDescuentos.isEmpty()) {
            sb.append("  -> ").append(detalleDescuentos).append("\n");
        }
        if (costoEnvioExpress > 0) {
            sb.append(String.format("Envío Express:                +$%8.2f\n", costoEnvioExpress));
        }
        sb.append(String.format("Ajuste (%-16s):    %+8.2f\n", metodoPago, ajustePago));
        sb.append("==================================================\n");
        sb.append(String.format("TOTAL PAGADO:                  $%8.2f\n", totalPagado));
        sb.append("==================================================\n");
        return sb.toString();
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public double getTotalPagado() { return totalPagado; }
    public String getMetodoPago() { return metodoPago; }
    public EstadoEnvio getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(EstadoEnvio estadoEnvio) { this.estadoEnvio = estadoEnvio; }
}