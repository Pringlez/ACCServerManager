package org.accmanager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.springframework.util.StringUtils.isEmpty;

@SpringBootApplication
public class ACCManagerApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACCManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ACCManagerApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = getProtocol(env);
        String serverPort = getPort(env);
        String contextPath = getContextPath(env);
        String hostAddress = getAddress();
        LOGGER.info("\n----------------------------------------------------------\n\t" +
            "Application '{}' is running! Access URLs:\n\t" +
            "Local: \t\t{}://localhost:{}{}\n\t" +
            "External: \t{}://{}:{}{}\n\t" +
            "Profile(s): \t{}\n----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles());
    }

    private static String getProtocol(Environment env) {
        return env.getProperty("server.ssl.key-store") != null ? "https" : "http";
    }

    private static String getPort(Environment env) {
        return env.getProperty("server.port");
    }

    private static String getContextPath(Environment env) {
        String contextPath = env.getProperty("server.servlet.context-path");
        return isEmpty(contextPath) ? "/" : contextPath;
    }

    private static String getAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.warn("The host name could not be determined, using `localhost` as fallback");
            return "localhost";
        }
    }
}
