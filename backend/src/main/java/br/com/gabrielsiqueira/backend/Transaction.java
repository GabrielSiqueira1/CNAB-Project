package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
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
        public Transaction withDate(String date) throws ParseException{
            var dateFormat = new SimpleDateFormat("yyyyMMdd");
            var dateParse = dateFormat.parse(date);

            return new Transaction(this.id(), this.type(), new Date(dateParse.getTime()), this.value(), this.cpf(), this.card(), this.hour(), this.storeOwner(), this.nameStore());
        }
        public Transaction withHour(String hour) throws ParseException{
            var hourFormat = new SimpleDateFormat("HHmmss");
            var hourParse = hourFormat.parse(hour);

            return new Transaction(this.id(), this.type(), this.date(), this.value(), this.cpf(), this.card(), new Time(hourParse.getTime()), this.storeOwner(), this.nameStore());
        }
    }
