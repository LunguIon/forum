package md.forum.forum.services;

import md.forum.forum.security.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
public class DBConnectivityTest {

    @Autowired
    private DataSource dataSource;

    @MockBean
    private JwtService jwtService;

    @Test
    public void testConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                System.out.println("Success: Database connection is valid.");
            } else {
                System.out.println("Failure: Database connection is NOT valid.");
            }
        } catch (Exception e) {
            System.out.println("Error: Unable to connect to the database.");
            throw e;
        }
    }
}
