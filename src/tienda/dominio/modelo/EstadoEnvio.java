package tienda.dominio.modelo;

public enum EstadoEnvio {
    CONFIRMADO("Pedido confirmado y recibido"),
    EN_PREPARACION("Pedido en preparación"),
    EN_CAMINO("Pedido en camino"),
    ENTREGADO("Pedido entregado");

    private final String descripcion;

    EstadoEnvio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}