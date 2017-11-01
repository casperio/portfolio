package com.fidelity.portfolio.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class PortfolioController {

    private static final Logger LOG = LoggerFactory.getLogger(PortfolioController.class);

    @Autowired
    private String portfolioCsvFilename;

    /**
     * Handles requests to the /v1/portfolio/all endpoint
     *
     * @param response  Servlet response object associated with the request
     * @return  StreamingResponseBody that allows the portfolio file to be streamed asynchronously
     * from the servlet thread
     */
    @RequestMapping(value = "/v1/portfolio/all", method = RequestMethod.GET, produces = "application/json")
    public StreamingResponseBody handleRequest(HttpServletResponse response) {
        response.setContentType("application/json");
        return out -> {
            long start = System.currentTimeMillis();
            processCsvFile(out);
            out.flush();
            LOG.info("Request took " + (System.currentTimeMillis() - start) + " ms" );
        };
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "File not found")
    @ExceptionHandler(FileNotFoundException.class)
    public void fileNotFound() {
    }

    /**
     * Writes an array of JSON objects by reading the CSV file line-by-line
     * and transforming each row to a JSON object
     * @param out Output stream into which the JSON array will be written
     * @throws IOException thrown if the CSV file could not be open or if
     * an error occurs while writing to the output stream
     */
    private void processCsvFile(OutputStream out) throws IOException {
        JsonFactory factory = new JsonFactory();
        File filePath = new File(portfolioCsvFilename);

        if (!filePath.exists()) {
            throw new FileNotFoundException();
        }

        try (BufferedReader reader = new BufferedReader((new InputStreamReader(new FileInputStream(filePath))));
             JsonGenerator generator = factory.createGenerator(out)) {
            generator.writeStartArray();
            reader.lines().forEach(line -> writeJsonObject(line, generator));
            generator.writeEndArray();
        } catch (IOException ex) {
            LOG.error("Error processing csv file", ex);
            throw ex;
        }

    }

    /**
     * Transforms a csv row into JSON and writes it to a JSON generator
     * @param line CSV row
     * @param generator JSON generator
     */
    private void writeJsonObject(String line, JsonGenerator generator) {
        try {
            generator.writeStartObject();
            String[] values = line.split(",");
            generator.writeStringField("firstName", values[0]);
            generator.writeStringField("lastName", values[1]);
            generator.writeStringField("portfolioName", values[2]);
            generator.writeEndObject();
        } catch (IOException ex) {
            LOG.error("Error writing JSON for line: " + line, ex);
            throw new RuntimeException(ex);
        }
    }
}
