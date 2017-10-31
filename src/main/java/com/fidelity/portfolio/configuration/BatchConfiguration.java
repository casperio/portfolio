package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import com.fidelity.portfolio.service.AgeAssignmentStrategy;
import com.fidelity.portfolio.service.AssignmentStrategy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource datasource;

    @Bean
    @StepScope
    public CustomDateEditor customDateEditor() {
        CustomDateEditor customDateEditor = new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false);
        return customDateEditor;
    }

    @Bean
    @StepScope
    public AssignmentStrategy assignmentStrategy() {
        return new AgeAssignmentStrategy();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> flatFileReader() {
        HashMap<Object, CustomDateEditor> map = new HashMap<>();
        map.put(Date.class, customDateEditor());
        // FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        CustomerFlatFileItemReader reader = new CustomerFlatFileItemReader();
        // reader.setResource(new ClassPathResource("customers.csv"));
        reader.setLineMapper(new DefaultLineMapper<Customer>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"firstName", "lastName", "dateOfBirth", "assets"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                setTargetType(Customer.class);
                setCustomEditors(map);
            }});
        }});
        return reader;
    }

    @Bean
    @StepScope
    public CustomerItemProcessor processor() {
        return new CustomerItemProcessor(assignmentStrategy());
    }

    @Bean
    public FlatFileItemWriter<Customer> flatFileWriter() {
        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(new FileSystemResource("c:\\fidelity\\out\\portfolio_customers.csv"));
        DelimitedLineAggregator<Customer> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");
        CustomerFieldExtractor extractor = new CustomerFieldExtractor();
        delimitedLineAggregator.setFieldExtractor(extractor);
        flatFileItemWriter.setLineAggregator(delimitedLineAggregator);
        return flatFileItemWriter;
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Customer> jdbcBatchItemWriter() {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        writer.setSql("INSERT INTO customers (firstName, lastName, dateOfBirth, assets, portfolioName) " +
                "VALUES (:firstName, :lastName, :dateOfBirth, :assets, :portfolioName)");
        writer.setDataSource(datasource);
        return writer;
    }

    @Bean
    public Step writeToFileStep() {
        return stepBuilderFactory.get("step1").<Customer, Customer> chunk(5)
                .reader(flatFileReader())
                .processor(processor())
                .writer(flatFileWriter())
                .faultTolerant()
                .skipPolicy((Throwable throwable, int skipCount) -> true)
                .build();
    }

    @Bean
    public Step writeToDBStep() {
        return stepBuilderFactory.get("step2").<Customer, Customer> chunk(5)
                .reader(flatFileReader())
                .processor(processor())
                .writer(jdbcBatchItemWriter())
                .faultTolerant()
                .skipPolicy((Throwable throwable, int skipCount) -> true)
                .build();
    }

    @Bean
    public Job processCustomersJob() {
        // Create two parallel flows:
        //    + The first flow contains one step that writes to the flat file
        //    + The second flow contains one step that writes to the DB
        Flow flow1 = new FlowBuilder<Flow>("flow1").from(writeToFileStep()).end();
        Flow flow2 = new FlowBuilder<Flow>("flow2").from(writeToDBStep()).end();
        Flow splitFlow = new FlowBuilder<Flow>("splitFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow1, flow2)
                .build();
        return jobBuilderFactory.get("processCustomerJob")
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .end()
                .build();
    }
}
