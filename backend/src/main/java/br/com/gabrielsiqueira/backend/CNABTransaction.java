package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;

public record CNABTransaction (
    Integer tipo,
    String data,
    BigDecimal valor,
    Long cpf,
    String cartao,
    String hora,
    String donoLoja,
    String nomeLoja){
        
    }
