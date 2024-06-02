package br.com.gabrielsiqueira.backend;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public record Transaction(
    Long id,
    Integer tipo,
    Date data,
    BigDecimal valor,
    Long cpf,
    String cartao,
    Time hora,
    String donoLoja,
    String nomeLoja){
        // Centraliza possíveis alterações na variável valor
        public Transaction withValue(BigDecimal valor){
            return new Transaction(this.id(), this.tipo(), this.data(), valor, this.cpf(), this.cartao(), this.hora(), this.donoLoja(), this.nomeLoja());
        }   
        public Transaction withDate(String date) throws ParseException{
            var dateFormat = new SimpleDateFormat("yyyyMMdd");
            var dateParse = dateFormat.parse(date);

            return new Transaction(this.id(), this.tipo(), new Date(dateParse.getTime()), this.valor(), this.cpf(), this.cartao(), this.hora(), this.donoLoja(), this.nomeLoja());
        }
        public Transaction withHour(String hour) throws ParseException{
            var hourFormat = new SimpleDateFormat("HHmmss");
            var hourParse = hourFormat.parse(hour);

            return new Transaction(this.id(), this.tipo(), this.data(), this.valor(), this.cpf(), this.cartao(), new Time(hourParse.getTime()), this.donoLoja(), this.nomeLoja());
        }
    }
