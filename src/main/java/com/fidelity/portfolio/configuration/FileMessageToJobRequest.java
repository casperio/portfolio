package com.fidelity.portfolio.configuration;

import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.integration.launch.JobLaunchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import java.io.File;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

public class FileMessageToJobRequest {

    private static final Logger LOG = LoggerFactory.getLogger(FileMessageToJobRequest.class);

    private RandomStringGenerator generator;

    public FileMessageToJobRequest() {
        generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
    }

    @Autowired
    private Job job;

    public void setJob(Job job) {
        this.job = job;
    }

    @Transformer(inputChannel = "fileChannel", outputChannel = "jobLaunchChannel")
    public JobLaunchRequest toRequest(Message<File> message) {
        LOG.info("Mehdi toRequest invoked:  " + message.getPayload().getAbsolutePath());
        File newFile = new File(message.getPayload().getAbsolutePath() + "_" + generator.generate(8));
        new File(message.getPayload().getAbsolutePath()).renameTo(newFile);
        LOG.info("Mehdi new filename is: " + newFile.getAbsolutePath());
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("fileParameterName", newFile.getAbsolutePath());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }
}
