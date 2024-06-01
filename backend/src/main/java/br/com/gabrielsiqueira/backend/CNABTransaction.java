package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;

public record CNABTransaction (
    Integer type,
    String date,
    BigDecimal value,
    Long cpf,
    String card,
    String hour,
    String storeOwner,
    String nameStore){
        
    }