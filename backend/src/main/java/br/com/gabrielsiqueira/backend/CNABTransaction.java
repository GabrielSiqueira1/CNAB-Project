package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;

public record CNABTransaction (
    Integer type,
    String data,
    BigDecimal value,
    Long cpf,
    String card,
    String hour,
    String storeOwner,
    String nameStore){
        
    }
