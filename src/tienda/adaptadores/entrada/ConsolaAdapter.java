package tienda.adaptadores.entrada;

import java.util.List;
import java.util.Scanner;
import tienda.dominio.modelo.*;
import tienda.dominio.pagos.*;
import tienda.puertos.entrada.TiendaUseCase;

public class ConsolaAdapter {

    private final TiendaUseCase tiendaUseCase;
    private final Scanner scanner;
    private Cliente clienteActual;

    public ConsolaAdapter(TiendaUseCase tiendaUseCase) {
        this.tiendaUseCase = tiendaUseCase;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("==========================================");
        System.out.println("      TIENDA VIRTUAL - MODO CONSOLA       ");
        System.out.println("==========================================");
        registrarCliente();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1 ->
                    verCatalogo();
                case 2 ->
                    filtrarPorCategoria();
                case 3 ->
                    agregarAlCarrito();
                case 4 ->
                    gestionarCarrito();
                case 5 ->
                    cerrarPedido();
                case 6 ->
                    verHistorialYSeguimiento();
                case 0 ->
                    System.out.println("Saliendo del sistema...");
                default ->
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void registrarCliente() {
        System.out.print("Nombre del cliente: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Documento: ");
        String doc = scanner.nextLine().trim();
        System.out.print("¿Es cliente frecuente? (s/n): ");
        boolean frecuente = scanner.nextLine().trim().equalsIgnoreCase("s");
        this.clienteActual = new Cliente(nombre, doc, frecuente);
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Ver catálogo completo");
        System.out.println("2. Filtrar catálogo por categoría");
        System.out.println("3. Agregar producto al carrito");
        System.out.println("4. Ver / modificar / eliminar del carrito");
        System.out.println("5. Finalizar compra y pagar");
        System.out.println("6. Historial de pedidos y seguimiento de envío");
        System.out.println("0. Salir");
    }

    private void verCatalogo() {
        System.out.println("\n--- CATÁLOGO DE PRODUCTOS ---");
        for (Producto p : tiendaUseCase.listarCatalogo()) {
            System.out.println(p);
        }
    }

    private void filtrarPorCategoria() {
        System.out.print("Ingrese categoría (Ropa / Tecnologia / Alimento): ");
        String cat = scanner.nextLine().trim();
        List<Producto> lista = tiendaUseCase.listarPorCategoria(cat);
        if (lista.isEmpty()) {
            System.out.println("No se encontraron productos en esa categoría.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void agregarAlCarrito() {
        verCatalogo();
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine().trim();
        int cantidad = leerEntero("Cantidad: ");

        if (tiendaUseCase.agregarAlCarrito(codigo, cantidad)) {
            System.out.println("Producto agregado correctamente.");
        } else {
            System.out.println("No se pudo agregar (código inexistente o stock insuficiente).");
        }
    }

    private void gestionarCarrito() {
        Carrito carrito = tiendaUseCase.obtenerCarrito();
        if (carrito.estaVacio()) {
            System.out.println("\nEl carrito está vacío.");
            return;
        }

        System.out.println("\n--- TU CARRITO ---");
        for (ItemCarrito item : carrito.getItems()) {
            System.out.printf("[%s] %-22s | Cant: %d | Subtotal: $%.2f\n",
                    item.getProducto().getCodigo(), item.getProducto().getNombre(),
                    item.getCantidad(), item.getSubtotal());
        }
        System.out.printf("Subtotal acumulado: $%.2f\n", carrito.calcularSubtotal());

        System.out.println("\n1. Cambiar cantidad de un producto");
        System.out.println("2. Eliminar un producto");
        System.out.println("0. Volver");
        int op = leerEntero("Elige: ");

        if (op == 1) {
            System.out.print("Código del producto a modificar: ");
            String cod = scanner.nextLine().trim();
            int nuevaCant = leerEntero("Nueva cantidad (0 para eliminar): ");
            if (tiendaUseCase.modificarCantidadCarrito(cod, nuevaCant)) {
                System.out.println("Carrito actualizado.");
            } else {
                System.out.println("No se pudo actualizar (verifica código o stock).");
            }
        } else if (op == 2) {
            System.out.print("Código del producto a eliminar: ");
            String cod = scanner.nextLine().trim();
            if (tiendaUseCase.eliminarDelCarrito(cod)) {
                System.out.println("Producto eliminado del carrito.");
            } else {
                System.out.println("Producto no encontrado en el carrito.");
            }
        }
    }

    private void cerrarPedido() {
        Carrito carrito = tiendaUseCase.obtenerCarrito();
        if (carrito.estaVacio()) {
            System.out.println("\nEl carrito está vacío.");
            return;
        }

        System.out.print("\nCódigo de cupón (ej. PROMO20) o Enter para omitir: ");
        String cupon = scanner.nextLine().trim();

        System.out.print("¿Agregar Envío Express (+$15.00)? (s/n): ");
        boolean express = scanner.nextLine().trim().equalsIgnoreCase("s");

        System.out.println("\nMétodo de pago:");
        System.out.println("1. Tarjeta (1 cuota sin interés; +1 cuota 3% recargo)");
        System.out.println("2. Transferencia bancaria (5% descuento extra)");
        System.out.println("3. Contra entrega (+$10 fijo, máx $500)");
        int opPago = leerEntero("Seleccione medio de pago: ");

        MetodoPago metodoPago;
        switch (opPago) {
            case 1 -> {
                int cuotas = leerEntero("Número de cuotas (1 a 12): ");
                metodoPago = new PagoTarjeta(cuotas);
            }
            case 2 ->
                metodoPago = new PagoTransferencia();
            case 3 ->
                metodoPago = new PagoContraEntrega();
            default -> {
                System.out.println("Método inválido.");
                return;
            }
        }

        System.out.println("Condición aplicada: " + metodoPago.getDescripcionRegla());
        Pedido pedido = tiendaUseCase.confirmarCompra(clienteActual, cupon, express, metodoPago);

        if (pedido == null) {
            System.out.println("Error: El pago no cumple con las validaciones del método elegido.");
        } else {
            System.out.println(pedido.generarFactura());
            System.out.println("Factura guardada localmente en: datos/factura_" + pedido.getId() + ".txt");
        }
    }

    private void verHistorialYSeguimiento() {
        List<Pedido> historial = tiendaUseCase.consultarHistorialPedidos();
        if (historial.isEmpty()) {
            System.out.println("\nNo hay pedidos registrados.");
            return;
        }

        System.out.println("\n--- HISTORIAL DE PEDIDOS ---");
        for (Pedido p : historial) {
            System.out.printf("Pedido #%d | Cliente: %s | Total: $%.2f | Pago: %s | Estado Envío: %s (%s)\n",
                    p.getId(), p.getCliente().getNombre(), p.getTotalPagado(),
                    p.getMetodoPago(), p.getEstadoEnvio().name(), p.getEstadoEnvio().getDescripcion());
        }

        System.out.print("\n¿Deseas simular el avance de envío de algún pedido? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            int id = leerEntero("Ingresa el # de Pedido: ");
            if (tiendaUseCase.avanzarEstadoEnvioPedido(id)) {
                System.out.println("Estado de envío actualizado correctamente.");
            } else {
                System.out.println("Pedido no encontrado.");
            }
        }
    }

    private int leerEntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        }
    }
}
