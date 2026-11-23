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
            opcion = leerOpcionMenu("Seleccione una opcion: ", 0, 6);
            switch (opcion) {
                case 1 -> verCatalogo();
                case 2 -> filtrarPorCategoria();
                case 3 -> agregarAlCarrito();
                case 4 -> gestionarCarrito();
                case 5 -> cerrarPedido();
                case 6 -> verHistorialYSeguimiento();
                case 0 -> System.out.println("Saliendo del sistema...");
            }
        } while (opcion != 0);
    }

    private void registrarCliente() {
        String nombre;
        do {
            System.out.print("Nombre del cliente: ");
            nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("El nombre no puede estar vacio.");
            }
        } while (nombre.isEmpty());

        String doc;
        boolean docValido;
        do {
            System.out.print("Documento: ");
            doc = scanner.nextLine().trim();
            docValido = !doc.isEmpty() && doc.matches("\\d+");
            if (doc.isEmpty()) {
                System.out.println("El documento no puede estar vacio.");
            } else if (!doc.matches("\\d+")) {
                System.out.println("Error: El documento solo debe contener numeros enteros.");
            }
        } while (!docValido);

        boolean frecuente = leerConfirmacion("¿Es cliente frecuente? (s/n): ");
        this.clienteActual = new Cliente(nombre, doc, frecuente);
    }

    private void mostrarMenu() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Ver catalogo completo");
        System.out.println("2. Filtrar catalogo por categoria");
        System.out.println("3. Agregar producto al carrito");
        System.out.println("4. Ver / modificar / eliminar del carrito");
        System.out.println("5. Finalizar compra y pagar");
        System.out.println("6. Historial de pedidos y seguimiento de envio");
        System.out.println("0. Salir");
    }

    private void verCatalogo() {
        System.out.println("\n--- CATALOGO DE PRODUCTOS ---");
        List<Producto> catalogo = tiendaUseCase.listarCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("No hay productos disponibles en el catalogo.");
            return;
        }
        for (Producto p : catalogo) {
            System.out.println(p);
        }
    }

    private void filtrarPorCategoria() {
        System.out.println("\n--- SELECCIONE UNA CATEGORÍA ---");
        System.out.println("1. Ropa");
        System.out.println("2. Tecnologia");
        System.out.println("3. Alimento");
        System.out.println("0. Volver");

        int op = leerOpcionMenu("Opcion: ", 0, 3);
        if (op == 0) {
            return;
        }

        String categoria = switch (op) {
            case 1 -> "Ropa";
            case 2 -> "Tecnologia";
            case 3 -> "Alimento";
            default -> null;
        };

        List<Producto> lista = tiendaUseCase.listarPorCategoria(categoria);
        System.out.println("\n--- PRODUCTOS EN " + categoria.toUpperCase() + " ---");
        if (lista.isEmpty()) {
            System.out.println("No se encontraron productos en esa categoria.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void agregarAlCarrito() {
        verCatalogo();
        String codigo = leerTextoNoVacio("\nCódigo del producto: ");
        int cantidad = leerEnteroPositivo("Cantidad: ");

        if (tiendaUseCase.agregarAlCarrito(codigo, cantidad)) {
            System.out.println("Producto agregado correctamente.");
        } else {
            System.out.println("No se pudo agregar (codigo inexistente o stock insuficiente).");
        }
    }

    private void gestionarCarrito() {
        Carrito carrito = tiendaUseCase.obtenerCarrito();
        if (carrito.estaVacio()) {
            System.out.println("\nEl carrito esta vacio.");
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
        int op = leerOpcionMenu("Elige: ", 0, 2);

        if (op == 1) {
            String cod = leerTextoNoVacio("Código del producto a modificar: ");
            int nuevaCant = leerOpcionMenu("Nueva cantidad (0 para eliminar): ", 0, 1000);
            if (tiendaUseCase.modificarCantidadCarrito(cod, nuevaCant)) {
                System.out.println("Carrito actualizado.");
            } else {
                System.out.println("No se pudo actualizar (verifica código o stock).");
            }
        } else if (op == 2) {
            String cod = leerTextoNoVacio("Codigo del producto a eliminar: ");
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

        System.out.print("\nCodigo de cupon (ej. PROMO20) o Enter para omitir: ");
        String cupon = scanner.nextLine().trim();

        boolean express = leerConfirmacion("¿Agregar Envio Express (+$15.00)? (s/n): ");

        System.out.println("\nMétodo de pago:");
        System.out.println("1. Tarjeta (1 cuota sin interés; +1 cuota 3% recargo)");
        System.out.println("2. Transferencia bancaria (5% descuento extra)");
        System.out.println("3. Contra entrega (+$10 fijo, máx $500)");
        System.out.println("0. Cancelar compra");
        int opPago = leerOpcionMenu("Seleccione medio de pago: ", 0, 3);

        if (opPago == 0) {
            System.out.println("Compra cancelada.");
            return;
        }

        MetodoPago metodoPago;
        switch (opPago) {
            case 1 -> {
                int cuotas = leerOpcionMenu("Número de cuotas (1 a 12): ", 1, 12);
                metodoPago = new PagoTarjeta(cuotas);
            }
            case 2 -> metodoPago = new PagoTransferencia();
            case 3 -> metodoPago = new PagoContraEntrega();
            default -> {
                return;
            }
        }

        System.out.println("Condición aplicada: " + metodoPago.getDescripcionRegla());
        Pedido pedido = tiendaUseCase.confirmarCompra(clienteActual, cupon, express, metodoPago);

        if (pedido == null) {
            System.out.println("Error: El pago no cumple con las validaciones del metodo elegido.");
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
            System.out.printf("Pedido #%d | Cliente: %s | Total: $%.2f | Pago: %s | Estado Envio: %s (%s)\n",
                    p.getId(), p.getCliente().getNombre(), p.getTotalPagado(),
                    p.getMetodoPago(), p.getEstadoEnvio().name(), p.getEstadoEnvio().getDescripcion());
        }

        if (leerConfirmacion("\n¿Deseas simular el avance de envio de algun pedido? (s/n): ")) {
            int id = leerEnteroPositivo("Ingresa el # de Pedido: ");
            if (tiendaUseCase.avanzarEstadoEnvioPedido(id)) {
                System.out.println("Estado de envio actualizado correctamente.");
            } else {
                System.out.println("Pedido no encontrado.");
            }
        }
    }

    // --- MÉTODOS DE VALIDACIÓN DE ENTRADA ---

    private int leerEntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un numero entero valido.");
            }
        }
    }

    private int leerOpcionMenu(String msg, int min, int max) {
        while (true) {
            int opcion = leerEntero(msg);
            if (opcion >= min && opcion <= max) {
                return opcion;
            }
            System.out.printf("Opción fuera de rango. Ingresa un número entre %d y %d.\n", min, max);
        }
    }

    private int leerEnteroPositivo(String msg) {
        while (true) {
            int num = leerEntero(msg);
            if (num > 0) {
                return num;
            }
            System.out.println("Error: El valor debe ser mayor a 0.");
        }
    }

    private String leerTextoNoVacio(String msg) {
        while (true) {
            System.out.print(msg);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Error: Este campo no puede estar vacio.");
        }
    }

    private boolean leerConfirmacion(String msg) {
        while (true) {
            System.out.print(msg);
            String respuesta = scanner.nextLine().trim();
            if (respuesta.equalsIgnoreCase("s")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Respuesta inválida. Por favor ingresa 's' para sí o 'n' para no.");
        }
    }
}