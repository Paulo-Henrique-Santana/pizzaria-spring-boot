package com.unip.pizzaria.model.db;

import org.springframework.data.repository.CrudRepository;

import com.unip.pizzaria.model.Pizza;

public interface PizzaRepository extends CrudRepository<Pizza, Integer> {

}
