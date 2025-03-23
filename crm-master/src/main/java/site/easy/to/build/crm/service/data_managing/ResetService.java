package site.easy.to.build.crm.service.data_managing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

@Service
public class ResetService {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Transactional
    public void resetDatabase() {
        String[] sqlCommands = {
            "SET FOREIGN_KEY_CHECKS = 0;",
            "DELETE FROM contract_settings;",
            "DELETE FROM customer;",
            "DELETE FROM customer_login_info;",
            "DELETE FROM email_template;",
            "DELETE FROM employee;",
            "DELETE FROM file;",
            "DELETE FROM google_drive_file;",
            "DELETE FROM lead_action;",
            "DELETE FROM lead_settings;",
            "DELETE FROM ticket_settings;",
            "DELETE FROM trigger_contract;",
            "DELETE FROM trigger_ticket;",
            "DELETE FROM trigger_lead;",
            "SET FOREIGN_KEY_CHECKS = 1;",
        };

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {

            for (String sql : sqlCommands) {
                statement.execute(sql);
            }

            System.out.println("Database cleared successfully.");

        } catch (Exception e) {
        }
    }
}
