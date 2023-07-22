package org.accmanager.service.config.json;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonWriterConfig {

    @Bean
    public DefaultPrettyPrinter defaultPrettyPrinter() {
        DefaultPrettyPrinter.Indenter indenter = new DefaultIndenter();
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);
        return printer;
    }
}