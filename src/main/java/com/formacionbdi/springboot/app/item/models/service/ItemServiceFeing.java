package com.formacionbdi.springboot.app.item.models.service;

import com.formacionbdi.springboot.app.item.clientes.IProductoClienteFeing;
import com.formacionbdi.springboot.app.item.models.Item;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("serviceFeing")
@Primary
public class ItemServiceFeing implements IItemService{

    private IProductoClienteFeing serviceFeing;

    public ItemServiceFeing(IProductoClienteFeing serviceFeing) {
        this.serviceFeing = serviceFeing;
    }

    @Override
    public List<Item> findAll() {
        return serviceFeing.listar().stream()
                .map(producto -> new Item(producto, 1)).collect(Collectors.toList());
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        return new Item(serviceFeing.detalle(id), cantidad);
    }
}