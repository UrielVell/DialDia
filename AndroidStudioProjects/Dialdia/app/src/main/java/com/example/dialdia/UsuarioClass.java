package com.example.dialdia;

public class UsuarioClass {
    String nombreUsuario;
    String correo;
    int numRecambio;
    int milTotales;

    int milEntrada;

    public UsuarioClass(String nombreUsuario, String correo, int numRecambio, int milTotales, int milEntrada) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.numRecambio = numRecambio;
        this.milTotales = milTotales;
        this.milEntrada= milEntrada;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getNumRecambio() {
        return numRecambio;
    }

    public void setNumRecambio(int numRecambio) {
        this.numRecambio = numRecambio;
    }

    public int getMilTotales() {
        return milTotales;
    }

    public void setMilTotales(int milTotales) {
        this.milTotales = milTotales;
    }
}
