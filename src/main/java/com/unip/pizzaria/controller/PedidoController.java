package com.unip.pizzaria.controller;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.unip.pizzaria.model.Cliente;
import com.unip.pizzaria.model.ItemPedido;
import com.unip.pizzaria.model.Pedido;
import com.unip.pizzaria.model.Produto;
import com.unip.pizzaria.model.db.ClienteRepository;
import com.unip.pizzaria.model.db.ItemPedidoRepository;
import com.unip.pizzaria.model.db.PedidoRepository;
import com.unip.pizzaria.model.db.ProdutoRepository;

@Controller
@SessionAttributes("itemPedido")

public class PedidoController {
  @Autowired
  ProdutoRepository produtoRepository;
  @Autowired
  ClienteRepository clienteRepository;
  @Autowired
  PedidoRepository pedidoRepository;
  @Autowired
  ItemPedidoRepository itemPedidoRepository;

  Pedido pedido = new Pedido();
  Cliente cliente;

  public float getValorTotalPedido() {
    float valorTotalPedido = 0;

    for (ItemPedido itemPedido : pedido.getItensPedido()) {
      valorTotalPedido += itemPedido.getProduto().getValor() * itemPedido.getQuantidade();
    }

    return valorTotalPedido;
  }

  @RequestMapping("/listarProdutos")
  public ModelAndView listarProdutos() {
    ModelAndView modelAndView = new ModelAndView("selecionarProduto");

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("produtos", produtoRepository.findAll());

    return modelAndView;
  }

  @RequestMapping("/selecionaProduto")
  public ModelAndView selecionaProduto(@RequestParam("codigo") int codigo) {
    ModelAndView modelAndView = new ModelAndView("selecionarProduto");

    ItemPedido itemPedido = new ItemPedido();
    itemPedido.setQuantidade(1);

    Produto produto = produtoRepository.findById(Integer.valueOf(codigo)).get();
    itemPedido.setProduto(produto);

    modelAndView.addObject("produtoSelecionado", produto);
    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("produtos", produtoRepository.findAll());
    modelAndView.addObject("itemPedido", itemPedido);

    return modelAndView;
  }

  @PostMapping("/incluirNoPedido")
  public String incluirNoPedido(
      @ModelAttribute("itemPedido") ItemPedido itemPedido) {

    pedido.adicionarItem(itemPedido);
    System.out.println(pedido.getItensPedido());

    return "redirect:/listarProdutos";
  }

  @RequestMapping("/confirmarPedido")
  public ModelAndView confirmarPedido() {
    ModelAndView modelAndView = new ModelAndView("confirmarPedido");

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("valorTotalPedido", getValorTotalPedido());

    return modelAndView;
  }

  @RequestMapping("/confirmarDados")
  public ModelAndView confirmarDados() {
    ModelAndView modelAndView = new ModelAndView("confirmarDados");

    cliente = clienteRepository.findById(1).get();

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("cliente", cliente);

    return modelAndView;
  }

  @RequestMapping("/formaDePagamento")
  public ModelAndView formaDePagamento() {
    ModelAndView modelAndView = new ModelAndView("formaDePagamento");

    modelAndView.addObject("pedido", pedido);
    modelAndView.addObject("formaPagamento", "debito");

    return modelAndView;
  }

  @RequestMapping("/registrarPedido")
  public ModelAndView registrarPedido(@ModelAttribute("pedido") Pedido pedido) {
    ModelAndView modelAndView = new ModelAndView("pedidoRegistradoSucesso");

    Date date = new Date();
    Timestamp timestamp = new Timestamp(date.getTime());

    this.pedido.setValor(getValorTotalPedido());
    this.pedido.setCliente(cliente);
    this.pedido.setMomento(timestamp);
    this.pedido.setStatus("em andamento");
    this.pedido.setPagamento(pedido.getPagamento());

    pedidoRepository.save(this.pedido);

    this.pedido = new Pedido();

    return modelAndView;
  }

  @RequestMapping("/cancelarPedido")
  public ModelAndView cancelarPedido() {
    ModelAndView modelAndView = new ModelAndView("pedidoCancelado");

    pedido.getItensPedido().clear();

    return modelAndView;
  }

  @RequestMapping("/alterarEndereco")
  public ModelAndView alterarEndereco() {
    ModelAndView modelAndView = new ModelAndView("alterarEndereco");

    modelAndView.addObject("cliente", cliente);
    modelAndView.addObject("pedido", pedido);

    return modelAndView;
  }

  @RequestMapping("/alteraEndereco")
  public String alteraEndereco(@ModelAttribute("cliente") Cliente cliente) {
    this.cliente.setEnderecoEntrega(cliente.getEnderecoEntrega());
    clienteRepository.save(this.cliente);

    return "redirect:/confirmarDados";
  }
}
