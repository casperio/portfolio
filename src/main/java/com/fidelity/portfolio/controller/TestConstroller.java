package com.fidelity.portfolio.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class TestConstroller {

    private static final Logger LOG = LoggerFactory.getLogger(TestConstroller.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public StreamingResponseBody handleRequest(HttpServletResponse response) {
        response.setContentType("application/json");
        return out -> {
            long start = System.currentTimeMillis();
            processCsvFile(out);
            out.flush();
            LOG.info("Streaming took " + (System.currentTimeMillis() - start) + " ms" );
        };
    }

    private void processCsvFile(OutputStream out) throws IOException {
        File filePath = new File("C:\\fidelity\\out\\portfolio_customers.csv");
        JsonFactory factory = new JsonFactory();

        try (BufferedReader reader = new BufferedReader((new InputStreamReader(new FileInputStream(filePath))));
                JsonGenerator generator = factory.createGenerator(out)) {
            generator.writeStartArray();
            reader.lines().forEach(line -> writeLine(line, generator));
            generator.writeEndArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    private void writeLine(String line, JsonGenerator generator) {
        try {
            generator.writeStartObject();
            String[] values = line.split(",");
            generator.writeStringField("firstName", values[0]);
            generator.writeStringField("lastName", values[1]);
            generator.writeStringField("portfolioName", values[2]);
            generator.writeEndObject();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private void read(OutputStream out) throws IOException {
        File filePath = new File("C:\\fidelity\\out\\portfolio_customers.csv");
        try (BufferedReader reader = new BufferedReader((new InputStreamReader(new FileInputStream(filePath))))) {
            reader.lines().forEach(line -> writeLine(line, out));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private void writeLine(String line, OutputStream out) {
        try {
            out.write((line + "\n").getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
