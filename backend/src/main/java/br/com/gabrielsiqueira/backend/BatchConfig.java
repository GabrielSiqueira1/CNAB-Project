package br.com.gabrielsiqueira.backend;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Configuração de componentes em lote
public class BatchConfig {
    // Para trabalhar com persistência em banco de dados
    private PlatformTransactionManager transactionManager;

    public BatchConfig(PlatformTransactionManager transactionManager){
        this.transactionManager = transactionManager;
    }

    // Tarefa a ser executada
    // Baseado em máquina de estados: O que já foi escrito e o que já foi lido e seus estados são salvos no jobRepository
    @Bean
    Job job(Step step, JobRepository jobRepository){
        return new JobBuilder("job", jobRepository).start(step).incrementer(new RunIdIncrementer()).build();
    }
}
