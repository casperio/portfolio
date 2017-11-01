package com.fidelity.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Expected two arguments got " + args.length + " instead");
            printHelp();
            System.exit(128);
        }
        for (String arg : args) {
            if (!arg.startsWith("--inputDirectory=") && !arg.startsWith("--outputDirectory=")) {
                System.err.println("Argument [" + arg + "] not recognised");
                printHelp();
                System.exit(128);
            }
        }

        SpringApplication.run(Application.class, args);
    }

    private static void printHelp() {
        System.err.println("Exactly two arguments should be specified --inputDirectory=<dirName1>" +
                " --outputDirectory=<dirName2>");
        System.err.println("e.g. --inputDirectory=c:\\in --outputDirectory=c:\\out");
        System.err.println("Bailing out...");
    }
}
