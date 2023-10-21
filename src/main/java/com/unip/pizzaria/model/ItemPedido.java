package com.unip.pizzaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int codigo;
  int quantidade;
  String tamanho;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "numero_pedido")
  private Pedido pedido;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codigo_pizza")
  private Pizza pizza;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codigo_bebida")
  private Bebida bebida;
}
