package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBeanWithPropertiesImpl implements MyBeanWithProperties {

    private String nombre;
    private String apellido;

    public MyBeanWithPropertiesImpl(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    @Override
    public String function() {
        return nombre+"-"+apellido;
    }
}
