package com.ay_za.ataylar_technic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.*;

@Component
@Profile("dev")
@ConditionalOnProperty(name = "data-loader.enabled", havingValue = "true")
public class MySQLToH2DataLoader implements ApplicationRunner {

    private final JdbcTemplate h2JdbcTemplate;

    @Value("${data-loader.mysql.url}")
    private String mysqlUrl;

    @Value("${data-loader.mysql.username}")
    private String mysqlUsername;

    @Value("${data-loader.mysql.password}")
    private String mysqlPassword;

    // Kopyalanacak tablolar (error_logs HARİÇ)
    private static final List<String> TABLES_TO_COPY = Arrays.asList(
            "user_type",
            "sites_info",
            "blocks_info",
            "squares_info",
            "instant_group",
            "instant_accounts",
            "system_info",
            "product_inventory_category",
            "product_inventory_detail",
            "site_product_inventory_detail",
            "maintenance_pdf_records"
    );

    public MySQLToH2DataLoader(JdbcTemplate h2JdbcTemplate) {
        this.h2JdbcTemplate = h2JdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n🔄 MySQL'den H2'ye veri kopyalama başlıyor...");
        System.out.println("📋 Kopyalanacak tablo sayısı: " + TABLES_TO_COPY.size());
        System.out.println("🔗 MySQL URL: " + mysqlUrl);
        System.out.println("👤 MySQL User: " + mysqlUsername + "\n");

        try (Connection mysqlConn = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword)) {

            int totalRecords = 0;

            for (String tableName : TABLES_TO_COPY) {
                try {
                    int count = copyTable(mysqlConn, tableName);
                    totalRecords += count;
                    System.out.println("✅ " + String.format("%-35s", tableName) + ": " + count + " kayıt kopyalandı");
                } catch (Exception e) {
                    System.err.println("❌ " + tableName + " kopyalanırken hata: " + e.getMessage());
                }
            }

            System.out.println("\n🎉 Toplam " + totalRecords + " kayıt başarıyla kopyalandı!\n");

        } catch (Exception e) {
            System.err.println("❌ MySQL bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int copyTable(Connection mysqlConn, String tableName) throws SQLException {
        // 1. H2'deki mevcut verileri temizle
        h2JdbcTemplate.execute("DELETE FROM " + tableName);

        // 2. MySQL'den tüm kayıtları çek
        Statement stmt = mysqlConn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

        // 3. ResultSet metadata'sını al
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // 4. INSERT query'sini oluştur
        String insertQuery = buildInsertQuery(tableName, metaData, columnCount);

        // 5. Verileri H2'ye kopyala
        int count = 0;
        while (rs.next()) {
            Object[] values = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                values[i - 1] = rs.getObject(i);
            }
            h2JdbcTemplate.update(insertQuery, values);
            count++;
        }

        rs.close();
        stmt.close();

        return count;
    }

    private String buildInsertQuery(String tableName, ResultSetMetaData metaData, int columnCount) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 1; i <= columnCount; i++) {
            if (i > 1) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(metaData.getColumnName(i));
            placeholders.append("?");
        }

        return String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, columns, placeholders);
    }
}
