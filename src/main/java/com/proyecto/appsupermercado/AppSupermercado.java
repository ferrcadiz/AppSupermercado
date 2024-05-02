/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.proyecto.appsupermercado;

/**
 *
 * @author S
 */
import java.util.Scanner;
import java.util.List;
import java.util.InputMismatchException;
//import java.io.IOException;
//import java.nio.file.Paths;

public class AppSupermercado {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventario inventario = new Inventario();

        
        String txtProveedores = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\proveedores.txt";
        String txtProductos = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\productos.txt";
        String txtReporte = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\reporte.txt";

        inventario.cargarProveedores(txtProveedores);
        System.out.println("Proovedores cargados! :D");
        inventario.cargarProductos(txtProductos);
        System.out.println("Productos cargados! :D");
        
        inventario.generarReporte(txtReporte);
        System.out.println("Reporte cargado! :D");

        Principal ventana = new Principal(inventario);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        int idProveedor;

        while (true) {
            try {
                //Primer menú
                System.out.println("Seleccione un menu de los siguientes:");
                System.out.println("1. Menu de Ventas.");
                System.out.println("2. Menu Administracion (inventario).");
                System.out.println("0. Cerrar aplicacion.");

                int opcion = scanner.nextInt();
                scanner.nextLine();

                //String opcionStr = scanner.nextLine();
                //int opcion = Integer.parseInt(opcionStr);
                switch (opcion) {
                    case 0:
                        // Guarda los datos antes de salir de la aplicación
                        System.out.println("Saliendo...");

                        //guardar datos
                        txtProveedores = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\proveedores.txt";
                        txtProductos = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\productos.txt";
                        System.out.println("Guardando datos antes de salir ...");
                        inventario.guardarDatos(txtProveedores, txtProductos);
                        System.out.println("Proovedores guardados! :D");
                        System.out.println("Productos guardados! :D");
                        txtReporte = "C:\\Users\\bpcab\\Desktop\\AppSupermercado(N)\\reporte.txt";
                        inventario.generarReporte("reporte.txt");
                        System.exit(0);
                    case 1:
                        System.out.println("Nota: Tienen que haber productos añadidos con anterioridad.");
                        System.out.println("Seleccione la categoría del producto");
                        List<String> categorias = inventario.obtenerTodasLasCategorias();
                        for (int i = 0; i < categorias.size(); i++) {
                            System.out.println((i + 1) + ". " + categorias.get(i));
                        }

                        int indiceCategoriaElegida = scanner.nextInt() - 1;
                        scanner.nextLine();
                        String categoriaElegida = categorias.get(indiceCategoriaElegida);

                        System.out.println("Productos disponibles en la categoría " + categoriaElegida + ":");
                        inventario.mostrarTodosLosProductos(categoriaElegida); // Llamada al método sobrecargado

                        int indiceProductoElegido = scanner.nextInt() - 1;
                        scanner.nextLine();
                        Producto productoElegido = inventario.obtenerProductosPorCategoria(categoriaElegida).get(indiceProductoElegido);

                        System.out.println("Introduzca la cantidad que desea comprar:");
                        int cantidad = scanner.nextInt();
                        scanner.nextLine();

                        if (productoElegido.getStock() > 0 && productoElegido.getStock() >= cantidad) {
                            //Actualizar el stock del producto.
                            productoElegido.setStock(productoElegido.getStock() - cantidad);
                            System.out.println("¡Venta realizada con exito! Generando boleta...");

                            System.out.println("Boleta:");
                            System.out.println("Producto: " + productoElegido.getNombre());
                            System.out.println("Cantidad: " + cantidad);
                            System.out.println("Precio unitario: $" + productoElegido.getPrecio());
                            System.out.println("Total: $" + (productoElegido.getPrecio() * cantidad));
                        } else {
                            System.out.println("No hay suficiente stock para realizar la venta.");
                        }
                        break;

                    case 2:
                        System.out.println("Menu de administracion");
                        System.out.println("Seleccione una opción:");
                        System.out.println("1. Añadir producto (Deben existir proveedores)");
                        System.out.println("2. Añadir proveedor");
                        System.out.println("3. Mostrar productos");
                        System.out.println("4. Mostrar proveedores");
                        System.out.println("5. Mostrar productos con stock bajo (<20)");
                        System.out.println("6. Editar producto");
                        System.out.println("7. Eliminar producto");
                        System.out.println("8. Cargar Casos de Prueba");
                        System.out.println("0. Salir");

                        int opcion2 = scanner.nextInt();
                        scanner.nextLine(); // Problemas con lineas del espacio en java.

                        switch (opcion2) {
                            case 1:
                                System.out.println("Introduzca el nombre del producto:");
                                String nombreProducto = scanner.nextLine();
                                System.out.println("Introduzca el codigo:");
                                int codigo = scanner.nextInt();

                                // Verificar si el codigo ya existe
                                while (inventario.existeCodigoProducto(codigo)) {
                                    System.out.println("Este codigo ya esta en uso. Por favor, introduzca un nuevo codigo:");
                                    codigo = scanner.nextInt();
                                }

                                System.out.println("Introduzca la categoria");
                                String categoria = scanner.next();
                                scanner.nextLine(); // Consume newline left-over

                                Producto producto = new Producto(nombreProducto, categoria, 0, codigo, 0, 0); // Inicializar con precio y stock 0

                                // Pedir al usuario que ingrese un precio válido
                                int precio;
                                do {
                                    System.out.println("Introduzca el precio:");
                                    precio = scanner.nextInt();
                                    try {
                                        producto.setPrecio(precio);
                                    } catch (PrecioNegativoException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } while (precio < 0);

                                // Pedir al usuario que ingrese un stock válido
                                int stock;
                                do {
                                    System.out.println("Introduzca el stock:");
                                    stock = scanner.nextInt();
                                    try {
                                        producto.setStock(stock);
                                    } catch (StockNegativoException e) {
                                        System.out.println(e.getMessage());
                                    }
                                } while (stock < 0);

                                System.out.println("Producto añadido con exito.");
                                System.out.println("Introduzca el ID del proveedor para este producto:");
                                idProveedor = scanner.nextInt();
                                scanner.nextLine(); // Consume newline left-over

                                Proveedor proveedorAsociado = inventario.buscarProveedorPorId(idProveedor);
                                if (proveedorAsociado != null) {
                                    try {
                                        inventario.agregarProductoAProveedor(producto, proveedorAsociado);
                                        System.out.println("Producto añadido al proveedor con exito.");
                                    } catch (PrecioNegativoException | StockNegativoException e) {
                                        // Este bloque catch no debería ser necesario ahora, pero se deja por si acaso.
                                        System.out.println(e.getMessage());
                                    }
                                } else {
                                    System.out.println("Proveedor no encontrado. Asegurate de añadir el proveedor antes de asociarlo con productos.");
                                }
                                break;

                            case 2:
                                System.out.println("Introduzca el nombre del proveedor:");
                                String nombreProveedor = scanner.nextLine();
                                System.out.println("Introduzca el nombre de la empresa:");
                                String empresa = scanner.nextLine();
                                System.out.println("Introduzca la categoria del proveedor:");
                                String categoriaProveedor = scanner.nextLine();
                                System.out.println("Introduzca el ID del proveedor:");
                                idProveedor = scanner.nextInt();
                                scanner.nextLine(); // Consume newline left-over

                                Proveedor nuevoProveedor = new Proveedor(nombreProveedor, empresa, categoriaProveedor, idProveedor);

                                inventario.añadirProveedor(nuevoProveedor);

                                System.out.println("Proveedor añadido con exito.");
                                break;

                            case 3:
                                System.out.println("Productos disponibles:");
                                inventario.mostrarTodosLosProductos();
                                break;
                            case 4:
                                System.out.println("Proovedores registrados:");
                                inventario.mostrarTodosLosProveedores();
                                break;

                            case 5:
                                inventario.mostrarProductosBajosDeStock();
                                break;

                            case 6:
                                System.out.println("Introduzca el codigo del producto que desea editar:");
                                int codigoProductoEditar = scanner.nextInt();
                                scanner.nextLine(); // Consume newline left-over
                                Producto productoEditar = inventario.buscarProductoPorCodigo(codigoProductoEditar);
                                if (productoEditar != null) {
                                    System.out.println("Introduzca el nuevo precio del producto:");
                                    int nuevoPrecio = scanner.nextInt();
                                    scanner.nextLine(); // Consume newline left-over
                                    productoEditar.setPrecio(nuevoPrecio);
                                    System.out.println("Producto editado con exito.");
                                } else {
                                    System.out.println("No se encontro un producto con el codigo proporcionado");
                                }
                                break;

                            case 7:
                                System.out.println("Introduzca el codigo del producto que desea eliminar:");
                                int codigoProductoAEliminar = scanner.nextInt();
                                scanner.nextLine();
                                Producto productoAEliminar = inventario.buscarProductoPorCodigo(codigoProductoAEliminar);
                                if (productoAEliminar != null) {
                                    //Eliminar el producto de todos los proveedores
                                    List<Proveedor> proveedoresDelProducto = inventario.obtenerProveedoresDelProducto(productoAEliminar);
                                    for (Proveedor proveedor : proveedoresDelProducto) {
                                        proveedor.getProductos().remove(productoAEliminar);
                                    }
                                    //Eliminar el producto del inventario
                                    inventario.getProveedoresPorProducto().remove(productoAEliminar);
                                    System.out.println("Producto eliminado con exito.");
                                } else {
                                    System.out.println("No se encontro un producto con el codigo proporcionado");
                                }
                                break;

                            case 8:
                                //añadirDatosDePrueba(inventario);
                                break;

                            case 0:
                                System.out.println("Saliendo...");

                                //guardar datos
                                txtProveedores = "C:\\Users\\sofia\\Downloads\\AppSupermercado(N)\\proveedores.txt";
                                txtProductos = "C:\\Users\\sofia\\Downloads\\AppSupermercado(N)\\productos.txt";
                                System.out.println("Guardando datos antes de salir ...");
                                inventario.guardarDatos(txtProveedores, txtProductos);
                                System.out.println("Proovedores guardados! :D");
                                System.out.println("Productos guardados! :D");

                                System.exit(0);

                            default:
                                System.out.println("Opción no valida. Por favor, intente de nuevo.");
                                break;
                        }
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, introduce un numero valido.");
                scanner.nextLine(); // Descarta la entrada incorrecta
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        }

    }

}
