package com.formacionbdi.springboot.app.item.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Item implements Serializable {
    private static final long serialVersionUID = 8830490770508445240L;

    private Producto producto;
    private Integer cantidad;

    public Item(){

    }
    public Item(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Double getTotal(){
        return producto.getPrecio() * cantidad.doubleValue();
    }

}
