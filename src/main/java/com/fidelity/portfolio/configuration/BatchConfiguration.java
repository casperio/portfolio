package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import com.fidelity.portfolio.service.AgeAssignmentStrategy;
import com.fidelity.portfolio.service.AssignmentStrategy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Spring-batch java config class:
 * Defines the batch job that processes input portfolio files and
 * generates an output CSV file that lists customers and their
 * assigned portfolios
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("outputPortfolioFile")
    private String outputPortfolioFile;

    @Bean
    @StepScope
    public CustomDateEditor customDateEditor() {
        return new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false);
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
        CustomerFlatFileItemReader reader = new CustomerFlatFileItemReader();
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
    @StepScope
    public FlatFileItemWriter<Customer> flatFileWriter() {
        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(new FileSystemResource(outputPortfolioFile));
        DelimitedLineAggregator<Customer> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(",");
        CustomerFieldExtractor extractor = new CustomerFieldExtractor();
        delimitedLineAggregator.setFieldExtractor(extractor);
        flatFileItemWriter.setLineAggregator(delimitedLineAggregator);
        return flatFileItemWriter;
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
    public Job processCustomersJob() {
        return jobBuilderFactory.get("processCustomerJob")
                .incrementer(new RunIdIncrementer())
                .flow(writeToFileStep())
                .end()
                .build();
    }
}
