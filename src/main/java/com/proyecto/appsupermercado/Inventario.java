/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.appsupermercado;

/**
 *
 * @author S
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.BufferedReader; //se utilizan para leer datos de un archivo.
import java.io.FileReader; //se utilizan para leer datos de un archivo.
import java.io.FileWriter;
import java.io.IOException; //es una excepción que se puede lanzar cuando ocurre un error de entrada/salida.
import java.io.PrintWriter;

public class Inventario {

    private Map<Producto, List<Proveedor>> proveedoresPorProducto;
    private Map<Proveedor, List<Producto>> productosPorProveedor;
    private Map<String, Producto> productosPorNombre;
    private Map<Integer, Producto> productosPorCodigo;
    private Map<Integer, Proveedor> proveedoresPorId; // Mapa para buscar proveedores por ID
    private Set<String> categorias; //Conjunto para almacenar todas las categorias.

    public Inventario() {
        this.productosPorProveedor = new HashMap<>();
        this.proveedoresPorProducto = new HashMap<>();
        this.proveedoresPorId = new HashMap<>(); // Inicialización del mapa
        this.categorias = new HashSet<>(); //Inicialización del conjunto
        this.productosPorNombre = new HashMap<>();
        this.productosPorCodigo = new HashMap<>();
    }

    
    // Agrega un producto a un proveedor y viceversa
    public void agregarProductoAProveedor(Producto producto, Proveedor proveedor) throws PrecioNegativoException, StockNegativoException {
        if (producto.getPrecio() < 0) {
            throw new PrecioNegativoException("El precio no puede ser negativo.");
        }
        if (producto.getStock() < 0) {
            throw new StockNegativoException("El stock no puede ser negativo.");
        }
        proveedoresPorProducto.computeIfAbsent(producto, k -> new ArrayList<>()).add(proveedor);
        productosPorProveedor.computeIfAbsent(proveedor, k -> new ArrayList<>()).add(producto);

        // Asegurarse de que el proveedor está en el mapa de proveedoresPorId
        if (!proveedoresPorId.containsKey(proveedor.getId())) {
            proveedoresPorId.put(proveedor.getId(), proveedor);
        }
        categorias.add(producto.getCategoria());
    }

    // Obtiene la lista de proveedores para un producto específico
    public List<Proveedor> obtenerProveedoresDeProducto(Producto producto) {
        return proveedoresPorProducto.getOrDefault(producto, new ArrayList<>());
    }

    // Obtiene la lista de productos ofrecidos por un proveedor específico
    public List<Producto> obtenerProductosDeProveedor(Proveedor proveedor) {
        return productosPorProveedor.getOrDefault(proveedor, new ArrayList<>());
    }

    // Obtiene todos los proveedores registrados en el inventario
    public List<Proveedor> obtenerTodosLosProveedores() {
        return new ArrayList<>(proveedoresPorId.values());
    }

    // Buscar un proveedor por su ID
    public Proveedor buscarProveedorPorId(int id) {
        return proveedoresPorId.get(id);
    }

    //Añadir un proveedor a la instancia de inventario
    public void añadirProveedor(Proveedor proveedor) {
        // Asegurarse de que el proveedor no sea nulo y que no esté ya presente en el inventario
        if (proveedor != null && !proveedoresPorId.containsKey(proveedor.getId())) {
            proveedoresPorId.put(proveedor.getId(), proveedor);
        }
    }
    
    // Otiene todos los productos del inventario
    public List<Producto> obtenerTodosLosProductos(){
        
        List<Producto> listaProductos = new ArrayList<>();
        for (Producto producto : proveedoresPorProducto.keySet()) {
            listaProductos.add(producto);
        }
        return listaProductos;
    }

    //Método para no repetir el codigo en un producto.
    public boolean existeCodigoProducto(int codigo) {
        for (Proveedor proveedor : this.productosPorProveedor.keySet()) {
            for (Producto producto : this.productosPorProveedor.get(proveedor)) {
                if (producto.getCodigo() == codigo) {
                    return true;
                }
            }
        }
        return false;
    }

    //Metodo mostrar productos
    public void mostrarTodosLosProductos() {
        if (proveedoresPorProducto.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }

        System.out.println("Lista de todos los productos:");
        for (Producto producto : proveedoresPorProducto.keySet()) {
            System.out.println("- " + producto.getNombre() + ", Precio: $" + producto.getPrecio() + ", Codigo: " + producto.getCodigo() + ", Stock: " + producto.getStock() + ", IdProveedor:" + producto.getIdProveedor());
        }
    }

    // Método sobrecargado para mostrar todos los productos de una categoría específica ordenados por precio
    public void mostrarTodosLosProductos(String categoria) {
        List<Producto> productosCategoria = obtenerProductosPorCategoria(categoria);
        productosCategoria.sort((Producto p1, Producto p2) -> p1.getPrecio() - p2.getPrecio()); // Ordena los productos por precio

        if (productosCategoria.isEmpty()) {
            System.out.println("No hay productos en la categoría " + categoria + ".");
            return;
        }

        System.out.println("Lista de todos los productos en la categoría " + categoria + ":");
        int contador = 1;
        for (Producto producto : productosCategoria) {
            System.out.println(contador + ". " + producto.getNombre() + " Precio: $" + producto.getPrecio() + ", Codigo: " + producto.getCodigo() + ", Stock: " + producto.getStock());
            contador++;
        }
    }

    //Metodo para mostrar proveedores
    public void mostrarTodosLosProveedores() {
        if (proveedoresPorId.isEmpty()) {
            System.out.println("No hay proveedores en el inventario.");
            return;
        }

        System.out.println("Lista de todos los proveedores:");
        for (Proveedor proveedor : proveedoresPorId.values()) {
            System.out.println("ID: " + proveedor.getId() + ", Nombre: " + proveedor.getNombre() + ", Empresa: " + proveedor.getEmpresa() + ", Categoria: " + proveedor.getCategoria());
            // Si también quieres mostrar los productos que ofrece cada proveedor, puedes hacerlo aquí.
        }
    }

    //Buscar y mostrar todos los productos bajo de stock (actualmente -20)
    public void mostrarProductosBajosDeStock() {
        boolean hayProductosBajosDeStock = false;

        for (Producto producto : proveedoresPorProducto.keySet()) {
            if (producto.getStock() < 20) {
                hayProductosBajosDeStock = true;
                List<Proveedor> proveedores = obtenerProveedoresDeProducto(producto);
                System.out.println("Producto bajo de stock: " + producto.getNombre() + ", Stock actual: " + producto.getStock());
                System.out.println("Puede solicitar a los siguientes proveedores:");
                for (Proveedor proveedor : proveedores) {
                    System.out.println("- " + proveedor.getNombre() + " (Empresa: " + proveedor.getEmpresa() + ", Categoria: " + proveedor.getCategoria() + ")");
                }
                System.out.println(); // Agrega una línea en blanco para separar los productos.
            }
        }

        if (!hayProductosBajosDeStock) {
            System.out.println("Todos los productos tienen un stock suficiente.");
        }
    }

    public List<String> obtenerCategorias() {
        List<String> categoriasLocales = new ArrayList<>();
        for (List<Producto> productos : this.productosPorProveedor.values()) {
            for (Producto producto : productos) {
                if (!categoriasLocales.contains(producto.getCategoria())) {
                    categoriasLocales.add(producto.getCategoria());
                }
            }
        }
        return categoriasLocales;
    }

    //Método para obtener todas las categorías.
    public List<String> obtenerTodasLasCategorias() {
        return new ArrayList<>(categorias);
    }

    //Método para obtener todos los productos de una categoría especifica
    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return proveedoresPorProducto.keySet().stream().filter(producto -> producto.getCategoria().equals(categoria)).collect(Collectors.toList());

    }

    //Método para buscar producto por código.
    public Producto buscarProductoPorCodigo(int codigo) {
        for (Producto producto : proveedoresPorProducto.keySet()) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    // Este método devuelve una lista de proveedores para un producto específico
    public List<Proveedor> obtenerProveedoresDelProducto(Producto producto) {
        return proveedoresPorProducto.getOrDefault(producto, new ArrayList<>());
    }

    // Este método devuelve el mapa de proveedores por producto
    public Map<Producto, List<Proveedor>> getProveedoresPorProducto() {
        return proveedoresPorProducto;
    }

    public void cargarProveedores(String proveedores) {
        try (BufferedReader leerProveedores = new BufferedReader(new FileReader(proveedores))) {
            String line;
            while ((line = leerProveedores.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String nombre = parts[0].trim();
                    String empresa = parts[1].trim();
                    String categoria = parts[2].trim();
                    int id = Integer.parseInt(parts[3].trim());
                    Proveedor proveedor = new Proveedor(nombre, empresa, categoria, id);

                    List<Producto> productosCategoria = obtenerProductosPorCategoria(categoria);

                    for (Producto producto : productosCategoria) {
                        proveedor.agregarProducto(producto);
                    }

                    añadirProveedor(proveedor);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void añadirProducto(Producto producto) {
        // Añade el producto a los mapas de productos
        productosPorNombre.put(producto.getNombre(), producto);
        productosPorCodigo.put(producto.getCodigo(), producto);

        // Busca el proveedor por su ID
        Proveedor proveedor = proveedoresPorId.get(producto.getIdProveedor());
        if (proveedor != null) {
            try {
                // Añade el producto al proveedor y viceversa
                agregarProductoAProveedor(producto, proveedor);
            } catch (PrecioNegativoException | StockNegativoException e) {
                e.printStackTrace();
            }
        }
    }

    public void cargarProductos(String archivo) {
        try (BufferedReader leerProductos = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = leerProductos.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String nombre = parts[0].trim();
                    int precio = Integer.parseInt(parts[1].trim());
                    int codigo = Integer.parseInt(parts[2].trim());
                    int stock = Integer.parseInt(parts[3].trim());
                    String categoria = parts[4].trim();
                    int idProveedor = Integer.parseInt(parts[5].trim());
                    Producto producto = new Producto(nombre, categoria, precio, codigo, stock, idProveedor);
                    añadirProducto(producto);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    public void guardarDatos(String archivoProveedores, String archivoProductos) {
    try (PrintWriter guardarProveedores = new PrintWriter(new FileWriter(archivoProveedores));
         PrintWriter guardarProductos = new PrintWriter(new FileWriter(archivoProductos))) {

        // Guardar proveedores
        for (Proveedor proveedor : this.proveedoresPorId.values()) {
            guardarProveedores.println(proveedor.getNombre() + "," + proveedor.getEmpresa() + "," + proveedor.getCategoria() + "," + proveedor.getId());
        }

        // Guardar productos
        for (Producto producto : this.productosPorCodigo.values()) {
            guardarProductos.println(producto.getNombre() + "," + producto.getPrecio() + "," + producto.getCodigo() + "," + producto.getStock() + "," + producto.getCategoria() + "," + producto.getIdProveedor());
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public void generarReporte(String nombreArchivo) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
        // Escribe los datos de los productos
        writer.println("Datos de los productos:");
        for (Producto producto : this.productosPorCodigo.values()) {
            writer.println(producto.getNombre() + "," + producto.getPrecio() + "," + producto.getCodigo() + "," + producto.getStock() + "," + producto.getCategoria() + "," + producto.getIdProveedor());
        }

        // Escribe los datos de los proveedores
        writer.println("\nDatos de los proveedores:");
        for (Proveedor proveedor : this.proveedoresPorId.values()) {
            writer.println(proveedor.getId() + "," + proveedor.getNombre() + "," + proveedor.getEmpresa() + "," + proveedor.getCategoria());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public static void añadirDatosDePrueba(Inventario inventario) {
        // Crear algunos productos
        Producto producto1 = new Producto("Leche", "Lacteos", 1000, 1001, 50, 1);
        Producto producto2 = new Producto("Pan", "Panaderia", 2000, 1002, 30, 2);
        Producto producto3 = new Producto("Queso", "Lacteos", 4500, 1003, 20, 1);
        Producto producto4 = new Producto("Pan de molde", "Panaderia", 2500, 1004, 12, 2);
        Producto producto5 = new Producto("Comida de perro", "Mascotas", 15000, 1005, 35, 3);
        Producto producto6 = new Producto("Limpia piso", "Limpieza", 3750, 1006, 25, 4);

        // Crear algunos proveedores
        Proveedor proveedor1 = new Proveedor("Carlos", "Lacteos S.A.", "Lacteos", 1);
        Proveedor proveedor2 = new Proveedor("Maria", "Panaderia El Trigo", "Panaderia", 2);
        Proveedor proveedor3 = new Proveedor("Nicolas", "Master dog", "Mascotas", 3);
        Proveedor proveedor4 = new Proveedor("Juana", "Poet", "Limpieza", 4);

        // Añadir productos a proveedores (y viceversa) en el inventario
        try {
            inventario.agregarProductoAProveedor(producto1, proveedor1);
            inventario.agregarProductoAProveedor(producto2, proveedor2);
            inventario.agregarProductoAProveedor(producto3, proveedor1);
            inventario.agregarProductoAProveedor(producto4, proveedor2);
            inventario.agregarProductoAProveedor(producto5, proveedor3);
            inventario.agregarProductoAProveedor(producto6, proveedor4);
        } catch (PrecioNegativoException | StockNegativoException e) {
            e.printStackTrace();
        }

        System.out.println("Datos de prueba añadidos al inventario.");
    }
}
