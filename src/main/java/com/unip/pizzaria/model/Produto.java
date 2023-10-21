package com.unip.pizzaria.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int codigo;
  private String nome;
  private String descricao;
  private float valor;
}
