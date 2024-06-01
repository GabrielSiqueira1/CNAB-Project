package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public record Transaction(
    Integer type,
    Date date,
    BigDecimal value,
    Long cpf,
    String card,
    Time hour,
    String storeOwner,
    String nameStore){
        
    }
