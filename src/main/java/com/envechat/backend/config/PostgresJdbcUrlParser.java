package com.envechat.backend.config;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Converts Render/Heroku-style {@code postgres://} / {@code postgresql://} URLs into JDBC URLs.
 */
final class PostgresJdbcUrlParser {

    private PostgresJdbcUrlParser() {
    }

    record Parsed(String jdbcUrl, String username, String password) {
    }

    static Parsed parse(String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalArgumentException("Database URL is blank");
        }
        if (databaseUrl.startsWith("jdbc:")) {
            return new Parsed(databaseUrl, "", "");
        }

        URI uri;
        try {
            uri = new URI(databaseUrl);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid database URL", e);
        }

        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("postgres") || scheme.equalsIgnoreCase("postgresql"))) {
            throw new IllegalArgumentException("Database URL must use postgres or jdbc scheme");
        }

        String host = uri.getHost();
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("Database URL missing host");
        }

        int port = uri.getPort() > 0 ? uri.getPort() : 5432;
        String path = uri.getPath();
        if (path == null || path.length() <= 1) {
            throw new IllegalArgumentException("Database URL missing database name");
        }
        String database = path.substring(1);

        String user = "";
        String password = "";
        String userInfo = uri.getRawUserInfo();
        if (userInfo != null && !userInfo.isBlank()) {
            int colon = userInfo.indexOf(':');
            if (colon >= 0) {
                user = urlDecode(userInfo.substring(0, colon));
                password = urlDecode(userInfo.substring(colon + 1));
            } else {
                user = urlDecode(userInfo);
            }
        }

        StringBuilder jdbc = new StringBuilder();
        jdbc.append("jdbc:postgresql://").append(host).append(':').append(port).append('/').append(database);

        String query = uri.getRawQuery();
        if (query != null && !query.isBlank()) {
            jdbc.append('?').append(query);
        }

        boolean local = isLocalHost(host);
        if (!local && !containsSslMode(query)) {
            jdbc.append(query == null || query.isBlank() ? '?' : '&').append("sslmode=require");
        }

        return new Parsed(jdbc.toString(), user, password);
    }

    private static boolean isLocalHost(String host) {
        String h = host.toLowerCase(Locale.ROOT);
        return h.equals("localhost") || h.equals("127.0.0.1") || h.equals("::1");
    }

    private static boolean containsSslMode(String query) {
        if (query == null) {
            return false;
        }
        return query.toLowerCase(Locale.ROOT).contains("sslmode=");
    }

    private static String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}
