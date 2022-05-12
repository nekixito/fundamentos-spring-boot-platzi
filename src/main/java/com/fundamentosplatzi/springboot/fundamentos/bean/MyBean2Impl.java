package com.fundamentosplatzi.springboot.fundamentos.bean;

public class MyBean2Impl implements MyBean{
    @Override
    public void print() {
        System.out.println("Hola desde mi impplementacion propia del bean 2");
    }
}
