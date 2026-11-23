package tienda.dominio.modelo;

public class Cliente {
    private String nombre;
    private String documento;
    private boolean frecuente;

    public Cliente(String nombre, String documento, boolean frecuente) {
        this.nombre = nombre;
        this.documento = documento;
        this.frecuente = frecuente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public boolean isFrecuente() {
        return frecuente;
    }
}
