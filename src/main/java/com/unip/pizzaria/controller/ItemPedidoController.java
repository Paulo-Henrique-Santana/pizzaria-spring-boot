package com.unip.pizzaria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.unip.pizzaria.model.Bebida;
import com.unip.pizzaria.model.ItemPedido;
import com.unip.pizzaria.model.Pedido;
import com.unip.pizzaria.model.Pizza;
import com.unip.pizzaria.model.db.BebidaRepository;
import com.unip.pizzaria.model.db.PizzaRepository;

@Controller
@SessionAttributes("itemPedido")
public class ItemPedidoController {
  @Autowired
  PizzaRepository pizzaRepository;
  @Autowired
  BebidaRepository bebidaRepository;

  Pedido pedido = new Pedido();

  @RequestMapping("/novoPedido")
  public ModelAndView novoPedido() {
    ModelAndView modelAndView = new ModelAndView("novoPedido");

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("pizzas", pizzaRepository.findAll());
    modelAndView.addObject("bebidas", bebidaRepository.findAll());

    return modelAndView;
  }

  @RequestMapping("/selecionaProduto")
  public ModelAndView selecionaProduto(@RequestParam("codigo") int codigo,
      @RequestParam("tipoProduto") String tipoProduto) {
    ModelAndView modelAndView = new ModelAndView("novoPedido");

    ItemPedido itemPedido = new ItemPedido();
    itemPedido.setQuantidade(1);

    if (tipoProduto.equals("pizza")) {
      Pizza pizza = pizzaRepository.findById(Integer.valueOf(codigo)).get();
      itemPedido.setPizza(pizza);
      modelAndView.addObject("produtoSelecionado", pizza);
    } else if (tipoProduto.equals("bebida")) {
      Bebida bebida = bebidaRepository.findById(Integer.valueOf(codigo)).get();
      itemPedido.setBebida(bebida);
      modelAndView.addObject("produtoSelecionado", bebida);
    }

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("pizzas", pizzaRepository.findAll());
    modelAndView.addObject("bebidas", bebidaRepository.findAll());
    modelAndView.addObject("tipoProduto", tipoProduto);
    modelAndView.addObject("itemPedido", itemPedido);

    return modelAndView;
  }

  @PostMapping("/incluirNoPedido")
  public String incluirNoPedido(
      @ModelAttribute("itemPedido") ItemPedido itemPedido) {

    pedido.adicionarItem(itemPedido);

    for (ItemPedido item : pedido.getItensPedido()) {
      System.out.println(item.getPizza().toString());
    }

    return "redirect:/novoPedido";
  }

  @RequestMapping("/finalizarPedido")
  public ModelAndView finalizarPedido() {

    ModelAndView modelAndView = new ModelAndView("novoPedido");

    return modelAndView;
  }
}
