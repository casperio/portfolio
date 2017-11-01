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

/**
 * Implements the transformer component of the spring integration graph
 */
public class FileMessageToJobRequest {

    private static final Logger LOG = LoggerFactory.getLogger(FileMessageToJobRequest.class);

    @Autowired
    private Job job;

    private RandomStringGenerator generator;

    FileMessageToJobRequest() {
        generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
    }

    /**
     * Transforms a File message to a JobLaunchRequest message
     * This method also renames the input file to make sure the File message
     * does not get sent back again by the input-file adapter
     *
     * @param message Input file message
     * @return JobLaunch request
     */
    @Transformer(inputChannel = "fileChannel", outputChannel = "jobLaunchChannel")
    public JobLaunchRequest toRequest(Message<File> message) {
        LOG.info("Spring Integration graph transformer toRequest invoked:  " + message.getPayload().getAbsolutePath());
        File newFile = new File(message.getPayload().getAbsolutePath() + "_" + generator.generate(8));
        File outputFile = new File(message.getPayload().getAbsolutePath());
        if (outputFile.renameTo(newFile)) {
            LOG.debug("[" + message.getPayload().getAbsolutePath() + "] -> [" + outputFile.getAbsolutePath() + "]");
        } else {
            LOG.error("Failed to rename file [" + message.getPayload().getAbsolutePath()
                    + "] to [" + newFile.getAbsolutePath() + "]");
            throw new RuntimeException("Failed to rename file [" + message.getPayload().getAbsolutePath()
                    + "] to [" + newFile.getAbsolutePath() + "]");
        }
        LOG.info("Spring Integration graph transformer toRequest new filename is: " + newFile.getAbsolutePath());
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("fileParameterName", newFile.getAbsolutePath());
        return new JobLaunchRequest(job, jobParametersBuilder.toJobParameters());
    }
}
