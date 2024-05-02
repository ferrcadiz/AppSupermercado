/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto.appsupermercado;

/**
 *
 * @author sofia
 */
// Clase de excepción personalizada para manejar errores de stock negativo
public class StockNegativoException extends Exception {
    public StockNegativoException(String mensaje) {
        super(mensaje);
    }
}