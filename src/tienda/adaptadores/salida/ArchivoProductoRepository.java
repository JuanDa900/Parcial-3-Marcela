package tienda.adaptadores.salida;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import tienda.dominio.catalogo.*;
import tienda.dominio.modelo.Producto;
import tienda.puertos.salida.ProductoRepository;

public class ArchivoProductoRepository implements ProductoRepository {

    private final String rutaArchivo = "datos/productos.txt";
    private List<Producto> cacheProductos = new ArrayList<>();

    public ArchivoProductoRepository() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            inicializarCatalogoPorDefecto();
            guardarTodos(cacheProductos);
        } else {
            cargarDesdeArchivo();
        }
    }

    private void inicializarCatalogoPorDefecto() {
        CreadorProducto ropaFactory = new ProductoRopaFactory();
        CreadorProducto tecnoFactory = new ProductoTecnologiaFactory();
        CreadorProducto alimentoFactory = new ProductoAlimentoFactory();

        cacheProductos.add(ropaFactory.crearProducto("R01", "Chaqueta Impermeable", 85.00, 10, "L", "Poliester"));
        cacheProductos.add(ropaFactory.crearProducto("R02", "Camiseta Algodon", 22.00, 25, "M", "Algodon"));
        cacheProductos.add(tecnoFactory.crearProducto("T01", "Audifonos Inalambricos", 120.00, 8, "Sony", "12"));
        cacheProductos.add(tecnoFactory.crearProducto("T02", "Teclado Mecanico", 65.00, 14, "Logitech", "24"));
        cacheProductos.add(alimentoFactory.crearProducto("A01", "Cafe Especial 500g", 18.00, 30, "15/08/2027", "false"));
        cacheProductos.add(alimentoFactory.crearProducto("A02", "Yogurt Griego Pack", 12.00, 15, "30/11/2026", "true"));
    }

    private void cargarDesdeArchivo() {
        cacheProductos.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                String categoria = partes[0];
                String codigo = partes[1];
                String nombre = partes[2];
                double precio = Double.parseDouble(partes[3]);
                int stock = Integer.parseInt(partes[4]);
                String dato1 = partes[5];
                String dato2 = partes[6];

                CreadorProducto factory = switch (categoria) {
                    case "Ropa" ->
                        new ProductoRopaFactory();
                    case "Tecnologia" ->
                        new ProductoTecnologiaFactory();
                    case "Alimento" ->
                        new ProductoAlimentoFactory();
                    default ->
                        null;
                };

                if (factory != null) {
                    cacheProductos.add(factory.crearProducto(codigo, nombre, precio, stock, dato1, dato2));
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo productos: " + e.getMessage());
        }
    }

    @Override
    public List<Producto> obtenerTodos() {
        return cacheProductos;
    }

    @Override
    public void guardarTodos(List<Producto> productos) {
        this.cacheProductos = productos;
        new File("datos").mkdirs();
        try (PrintWriter pw = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (Producto p : productos) {
                pw.println(p.getCategoria() + ";" + p.getCodigo() + ";" + p.getNombre() + ";"
                        + p.getPrecio() + ";" + p.getStock() + ";" + p.serializarEspecificos());
            }
        } catch (IOException e) {
            System.out.println("Error guardando productos: " + e.getMessage());
        }
    }
}
