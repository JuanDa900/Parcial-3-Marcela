package tienda.adaptadores.salida;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import tienda.dominio.modelo.*;
import tienda.puertos.salida.PedidoRepository;

public class ArchivoPedidoRepository implements PedidoRepository {

    private final String rutaHistorial = "datos/pedidos.txt";
    private List<Pedido> pedidosEnMemoria = new ArrayList<>();

    public ArchivoPedidoRepository() {
        cargarHistorial();
    }

    private void cargarHistorial() {
        File archivo = new File(rutaHistorial);
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";");
                int id = Integer.parseInt(p[0]);
                Cliente c = new Cliente(p[1], p[2], Boolean.parseBoolean(p[3]));
                double total = Double.parseDouble(p[4]);
                String pago = p[5];
                EstadoEnvio estado = EstadoEnvio.valueOf(p[6]);

                Pedido pedido = new Pedido(id, c, new ArrayList<>(), total, 0, "", 0, 0, total, pago);
                pedido.setEstadoEnvio(estado);
                pedidosEnMemoria.add(pedido);
            }
        } catch (IOException e) {
            System.out.println("Error cargando historial: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Pedido pedido) {
        pedidosEnMemoria.add(pedido);
        actualizarTodos(pedidosEnMemoria);

        new File("datos").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter("datos/factura_" + pedido.getId() + ".txt"))) {
            pw.print(pedido.generarFactura());
        } catch (IOException e) {
            System.out.println("Error guardando archivo de factura: " + e.getMessage());
        }
    }

    @Override
    public List<Pedido> obtenerTodos() {
        return pedidosEnMemoria;
    }

    @Override
    public void actualizarTodos(List<Pedido> pedidos) {
        this.pedidosEnMemoria = pedidos;
        new File("datos").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaHistorial))) {
            for (Pedido p : pedidos) {
                pw.println(p.getId() + ";" + p.getCliente().getNombre() + ";"
                        + p.getCliente().getDocumento() + ";" + p.getCliente().isFrecuente() + ";"
                        + p.getTotalPagado() + ";" + p.getMetodoPago() + ";" + p.getEstadoEnvio().name());
            }
        } catch (IOException e) {
            System.out.println("Error actualizando historial: " + e.getMessage());
        }
    }
}
