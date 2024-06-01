package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;

public record Transaction(
    Long id,
    Integer type,
    Date date,
    BigDecimal value,
    Long cpf,
    String card,
    Time hour,
    String storeOwner,
    String nameStore){
        // Centraliza possíveis alterações na variável valor
        public Transaction withValue(BigDecimal value){
            return new Transaction(this.id(), this.type(), this.date(), value, this.cpf(), this.card(), this.hour(), this.storeOwner(), this.nameStore());
        }
        public Transaction withDate(String data){
            var dateFormat = new SimpleDateFormat("yyyyMMdd");
            var date = dateFormat.parse(data);
        }

    }
