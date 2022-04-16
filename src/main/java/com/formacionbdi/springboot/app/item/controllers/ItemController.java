package com.formacionbdi.springboot.app.item.controllers;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.IItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log
@RefreshScope
@RestController @AllArgsConstructor
public class ItemController {

    @Qualifier("serviceFeing")
    private IItemService itemService;

    private CircuitBreakerFactory breakerFactory;

    @GetMapping("/")
    public List<Item> listar(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestHeader(name = "token-request", required = false) String token){
        log.info("Param: ".concat(nombre));
        log.info("token: ".concat(token));
        return itemService.findAll();
    }

    //@HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("/{id}/cantidad/{cantidad}")
    public Item detalleItem(@PathVariable Long id, @PathVariable Integer cantidad){
        return breakerFactory.create("items")
                .run(() -> itemService.findById(id, cantidad), error -> metodoAlternativo(id, cantidad, error));
    }

    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    @GetMapping("ver/{id}/cantidad/{cantidad}")
    public Item detalleItem2(@PathVariable Long id, @PathVariable Integer cantidad){
        return itemService.findById(id, cantidad);
    }

    @TimeLimiter(name = "items")
    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
    @GetMapping("ver3/{id}/cantidad/{cantidad}")
    public CompletableFuture<Item> detalleItem3(@PathVariable Long id, @PathVariable Integer cantidad){
        return CompletableFuture.supplyAsync( () -> itemService.findById(id, cantidad));
    }

    public Item metodoAlternativo(Long id, Integer cantidad, Throwable error){
        log.warning(error.getMessage());
        Item item = new Item();
        item.setCantidad(cantidad);

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Camara Sony");
        producto.setPrecio(500.00);

        item.setProducto(producto);
        return item;
    }

    public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable error){
        log.warning(error.getMessage());
        Item item = new Item();
        item.setCantidad(cantidad);

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Camara Sony");
        producto.setPrecio(500.00);

        item.setProducto(producto);
        return CompletableFuture.supplyAsync( () -> item);
    }
}
