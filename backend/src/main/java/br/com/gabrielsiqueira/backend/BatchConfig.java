package br.com.gabrielsiqueira.backend;

import org.springframework.transaction.PlatformTransactionManager;

import io.micrometer.core.instrument.distribution.StepBucketHistogram;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration // Configuração de componentes em lote
public class BatchConfig {
    // Para trabalhar com persistência em banco de dados
    private PlatformTransactionManager transactionManager;
    private JobRepository jobRepository;

    public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository){
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

    // Tarefa a ser executada
    // Baseado em máquina de estados: O que já foi escrito, o que já foi lido e seus estados são salvos no jobRepository
    // .start inicializa o processo
    // .incrementer permite que o processo rode mais de uma vez
    // .build cria o componente
    @Bean
    Job job(Step step){
        return new JobBuilder("job", jobRepository).start(step).incrementer(new RunIdIncrementer()).build();
    }

    // Descreve a leitura, o processamento e a escrita
    // Chunk é o pedaço que será processado
    // Depende da criação de tipos
    @Bean
    Step step(ItemReader<CNABTransaction> itemReader, ItemProcessor<CNABTransaction, Transaction> itemProcessor, ItemWriter<Transaction> itemWriter){
        return new StepBuilder("step", jobRepository).<CNABTransaction, Transaction>chunk(1000, transactionManager).reader(itemReader).processor(itemProcessor).writer(itemWriter).build();
    }

    // Para arquivos que são sintaticamente indefinidos
    @Bean
    FlatFileItemReader<CNABTransaction> reader(){
        return new FlatFileItemReaderBuilder<CNABTransaction>().name("reader").resource(new FileSystemResource("backend/files/CNAB.txt")).fixedLength().columns(
            new Range(1,1), new Range(2,9),
            new Range(10,19), new Range(20,30),
            new Range(31,42), new Range(43,48),
            new Range(49,62), new Range(63,80)
        ).names("tipo", "data", "valor", "cpf", "cartao", "hora", "donoLoja", "nomeLoja").targetType(CNABTransaction.class).build();
    }

    // Transformações de objetos
    // Data, Hora e Valor devem ser normalizados de maneiras distintas aos que são obtidas diretamente do arquivo
    // Valor já está multiplicado por 100
    // Data está no formato yyyymmdd
    // Hora está no formato HHmmss
    @Bean
    ItemProcessor<CNABTransaction, Transaction> processor(){
        // Wither pattern 
        return item -> {
            var transaction = new Transaction(
                null, item.tipo(), null, null, item.cpf(), item.cartao(), null, item.donoLoja().trim(), item.nomeLoja().trim()
                ).withValue(item.valor().divide(BigDecimal.valueOf(100))).withDate(item.data()).withHour(item.hora());
            return transaction;
        };
    }

    // Conexão do schema.sql com o Transcation.java
    @Bean
    JdbcBatchItemWriter<Transaction> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Transaction>().dataSource(dataSource).sql(
            """
                    INSERT INTO TRANSACTION (
                        tipo, data, valor, cpf, cartao, hora, dono_loja, nome_loja
                    ) VALUES (
                        :tipo, :data, :valor, :cpf, :cartao, :hora, :donoLoja, :nomeLoja
                    )
                    """).beanMapped().build();
    }
}
