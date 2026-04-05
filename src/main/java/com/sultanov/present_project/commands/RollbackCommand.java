package com.sultanov.present_project.commands;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class RollbackCommand {

    public static void main(String[] args) throws Exception {
        String profile = args.length > 0 ? args[0] : "local";
        int count = args.length > 1 ? Integer.parseInt(args[1]) : 1;

        Properties props = loadProperties(profile);

        String url      = resolve(props, "spring.datasource.url");
        String username = resolve(props, "spring.datasource.username");
        String password = resolve(props, "spring.datasource.password");

        System.out.println("Rolling back " + count + " changeset(s)...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            )) {
                liquibase.rollback(count, new Contexts(), new LabelExpression());
                System.out.println("Rollback completed successfully!");
            }
        } catch (Exception e) {
            System.err.println("Rollback failed: " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Properties loadProperties(String profile) throws IOException {
        Properties props = new Properties();

        try (InputStream base = RollbackCommand.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (base != null) props.load(base);
        }

        try (InputStream profileProps = RollbackCommand.class.getClassLoader()
                .getResourceAsStream("application-" + profile + ".properties")) {
            if (profileProps != null) props.load(profileProps);
        }

        return props;
    }

    private static String resolve(Properties props, String key) {
        String value = props.getProperty(key, "");
        if (value.startsWith("${") && value.endsWith("}")) {
            String inner = value.substring(2, value.length() - 1);
            String[] parts = inner.split(":", 2);
            String envValue = System.getenv(parts[0]);
            return envValue != null ? envValue : (parts.length > 1 ? parts[1] : "");
        }
        return value;
    }
}
