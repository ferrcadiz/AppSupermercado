/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.appsupermercado;

import javax.swing.JTextField;

/**
 *
 * @author S
 */
public class Producto {

    private String nombre;
    private int precio;
    private int codigo; //Codigo de barras unico. Sirve para hashmap.
    private int stock;  //Ver si es necesario acá o en inventario
    private String categoria;
    private int idProveedor;

    Producto(String nombreProd, String categoriaProd, int precioProd, int stockProd, int codigoProd, JTextField idProveedor) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
   
    //Getter y Setter
    public String getNombre() {
        return nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public int getCodigo() {
        return codigo;
    }

    public int getStock() {
        return stock;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(int precio) throws PrecioNegativoException {
         if (precio < 0) {
            throw new PrecioNegativoException("El precio no puede ser negativo.");
        }
        this.precio = precio;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
        
    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public void setStock(int stock) throws StockNegativoException {
        if (stock < 0) {
            throw new StockNegativoException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Producto(String nombre, String categoria, int precio, int codigo, int stock, int idProveedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.codigo = codigo;
        this.stock = stock;
        this.categoria = categoria;
        this.idProveedor = idProveedor;
    }
    
    
 
}





