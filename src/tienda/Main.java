package tienda;

import tienda.adaptadores.entrada.ConsolaAdapter;
import tienda.adaptadores.salida.ArchivoPedidoRepository;
import tienda.adaptadores.salida.ArchivoProductoRepository;
import tienda.aplicacion.TiendaService;
import tienda.puertos.entrada.TiendaUseCase;
import tienda.puertos.salida.PedidoRepository;
import tienda.puertos.salida.ProductoRepository;

public class Main {

    public static void main(String[] args) {
        ProductoRepository productoRepo = new ArchivoProductoRepository();
        PedidoRepository pedidoRepo = new ArchivoPedidoRepository();

        TiendaUseCase tiendaService = new TiendaService(productoRepo, pedidoRepo);

        ConsolaAdapter consola = new ConsolaAdapter(tiendaService);
        consola.iniciar();
    }
}
