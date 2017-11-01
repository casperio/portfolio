package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.utils.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * Common spring configuration class
 */
@Configuration
public class CommonConfiguration {

    @Bean(name = "inboundDirectory")
    @Qualifier("input")
    public File inboundDirectory(@Value("${directory.input}") String path) throws IOException {
        return FileUtils.makeDirectory(path);
    }

    @Bean(name = "outboundDirectory")
    @Qualifier("output")
    public File outboundDirectory(@Value("${directory.output}") String path) {
        return FileUtils.makeDirectory(path);
    }

    @Bean(name = "outputPortfolioFile")
    @Qualifier("outputPortfolioFile")
    public String outputPortfolioFile(@Value("${directory.output}") String path) {
        return path + File.separator + "portfolio_customers.csv";
    }
}
