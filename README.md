Sistema de Compras en Línea (E-Commerce)

Aplicación de tienda virtual desarrollada en Java bajo Arquitectura Hexagonal y patrones de diseño orientados a objetos, diseñada para gestionar el ciclo completo de compra desde consola

Autores:
-Carlos Angulo
-Juan Arévalo

la documentacion completa esta en la rama Doc en github.

Instrucciones para Ejecutar:

Desde Visual Studio Code

Abre la carpeta raíz del proyecto en VS Code.
Navega hasta src/tienda/Main.java.
Haz clic en el botón Run situado en la parte superior derecha de visual estudio code o presiona F5.

Nota sobre persistencia: Al ejecutar el programa por primera vez, se creará automáticamente la carpeta datos/ en la raíz del proyecto con el catálogo inicial (productos.txt). Al confirmar compras, se almacenarán allí el historial (pedidos.txt) y los comprobantes individuales (factura_XXXX.txt).

Funcionalidades Implementadas:

Gestión y consulta de catálogo: Visualización de productos disponibles y filtrado por categorías (Ropa, Tecnología y Alimento), cada una con sus atributos específicos.
Administración del carrito de compras: Agregar productos validando stock disponible, modificar cantidades, eliminar ítems y calcular subtotales en tiempo real.
Sistema de descuentos con reglas de acumulación:
Descuento por cantidad: 10% al llevar 5 o más unidades (acumulable).
Descuento por cliente frecuente: 5% adicional (acumulable).
Descuento por temporada: 12% sobre el subtotal (acumulable).
Descuento por cupón (PROMO20): 20% exclusivo (no acumulable; el sistema aplica automáticamente el beneficio más favorable para el cliente).
Servicio adicional: Opción de Envío Express con recargo fijo de $15.00.
Medios de pago simulados con reglas propias:
Tarjeta: Sin recargo a 1 cuota; recargo del 3% entre 2 y 12 cuotas.
Transferencia bancaria: 5% de descuento adicional sobre el total.
Contra entrega: Recargo fijo de $10.00 (permitido únicamente para compras de hasta $500.00).
Cierre de pedido, facturación y seguimiento: Descuento automático de inventario, generación de factura detallada en consola y archivo .txt, consulta de historial de pedidos y simulación de estados de envío (Confirmado, En preparación, En camino y Entregado).

Alcance Excluido:

De acuerdo con la definición del proyecto, no se incluyen notificaciones de reposición de inventario, garantía extendida ni empaque de regalo.

Estructura del Proyecto:

El código está organizado separando las reglas de negocio (dominio y aplicación) de los detalles externos de entrada y persistencia (puertos y adaptadores):

src/
└── tienda/
    ├── Main.java                           # Punto de entrada y ensamblaje de dependencias
    ├── dominio/
    │   ├── modelo/                         # Entidades del negocio (Producto, Carrito, Pedido, Cliente, EstadoEnvio)
    │   ├── catalogo/                       # Creación de productos por categoría (Factory Method)
    │   ├── descuentos/                     # Reglas de descuento acumulables y exclusivas (Strategy)
    │   └── pagos/                          # Medios de pago y validaciones (Strategy)
    ├── puertos/
    │   ├── entrada/                        # Contratos de casos de uso (TiendaUseCase)
    │   └── salida/                         # Contratos de persistencia (ProductoRepository, PedidoRepository)
    ├── aplicacion/
    │   └── TiendaService.java              # Orquestación de la lógica de negocio
    └── adaptadores/
        ├── entrada/
        │   └── ConsolaAdapter.java         # Interfaz de usuario por consola
        └── salida/
            ├── ArchivoProductoRepository.java  # Persistencia local de inventario
            └── ArchivoPedidoRepository.java    # Persistencia local de historial y facturas


Requisitos Previos

Java Development Kit (JDK) versión 17 o superior.
Visual Studio Code con la extensión Extension Pack for Java (opcional si se ejecuta desde terminal).

