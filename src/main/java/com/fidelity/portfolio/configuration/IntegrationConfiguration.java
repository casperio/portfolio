package com.fidelity.portfolio.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.MessageChannel;

import java.io.File;

/**
 * Spring-integration java config class:
 *
 * Implements spring-integration graph for portfolio file processing
 * [Inbound file channel adapter] -> [fileChannel] -> [file message to JobRequest transformer]
 * -> [jobLaunchChannel] -> [spring-batch service activator]
 */
@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    @Autowired
    @Qualifier("input")
    private File inboundDirectoryFile;

    @Autowired
    private JobLauncher jobLauncher;

    @Bean
    public MessageChannel fileChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel jobLaunchChannel() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "fileChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource reader = new FileReadingMessageSource();
        reader.setDirectory(inboundDirectoryFile);
        reader.setFilter(new SimplePatternFileListFilter("customers.csv"));
        return reader;
    }

    @Bean
    public FileMessageToJobRequest fileMessageToJobRequest() {
        return new FileMessageToJobRequest();
    }

    @ServiceActivator(inputChannel = "jobLaunchChannel")
    public void launch(JobLaunchRequest request) throws JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException {
        // JobExecution
        Job job = request.getJob();
        JobParameters jobParameters = request.getJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
