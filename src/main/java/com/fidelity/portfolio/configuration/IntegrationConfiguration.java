package com.fidelity.portfolio.configuration;

import com.fidelity.portfolio.utils.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.batch.integration.launch.JobLaunchingMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.io.IOException;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    @Autowired
    @Qualifier("input")
    private File inboundDirectoryFile;

    @Autowired
    @Qualifier("output")
    private File outboundDirectoryFile;

    @Autowired
    private JobLauncher jobLauncher;

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
        FileMessageToJobRequest transformer = new FileMessageToJobRequest();
        return transformer;
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
