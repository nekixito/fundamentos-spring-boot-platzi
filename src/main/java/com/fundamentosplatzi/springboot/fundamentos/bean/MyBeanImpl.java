package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBeanImpl implements MyBean{
    @Override
    public void print() {
        System.out.println("Hola desde mi impplementacion propia del bean");
    }
}
