package com.formacionbdi.springboot.app.item.clientes;

import com.formacionbdi.springboot.app.item.models.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "servicio-productos")
public interface IProductoClienteFeing {

    @GetMapping("/")
    public List<Producto> listar();

    @GetMapping("/{id}")
    public Producto detalle(@PathVariable Long id);
}