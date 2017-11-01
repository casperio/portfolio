package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

/**
 * FlatFileItemReader to parse CSV file into a POJO
 * The bean has a spring-batch step scope to make sure that
 * a new instance is injected for each new step
 */
@Component
@Scope("step")
public class CustomerFlatFileItemReader extends FlatFileItemReader<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerFlatFileItemReader.class);

    @Value("#{jobParameters['fileParameterName']}")
    public void setFileName(final String fileName) {
        LOG.info("Setting the resource to [" + fileName + "]");
        this.setResource(new FileSystemResource(fileName));
    }
}
