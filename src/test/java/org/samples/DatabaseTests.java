package org.samples;


import com.spun.util.ObjectUtils;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.samples.CiUtils.skipTestOnCI;

public class DatabaseTests {
    @Test
    void testConnection() throws SQLException {
        skipTestOnCI();
        assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getConnection()));
    }

    @Test
    void testVersion() throws SQLException {
        skipTestOnCI();
        assertEquals(1, getSchemaVersion(getConnection()));
    }
    @Test
    void testInMemoryVersion() throws SQLException {
        Connection connection = createInMemoryDatabase(1);
        assertEquals(1, getSchemaVersion(connection));
    }

    private static int getSchemaVersion(Connection connection) throws SQLException {
        return SqlUtilites.executeNumericQuery("Select data_value from metadata where data_name='schema_version'", connection);
    }

    private Connection createInMemoryDatabase(int version) throws SQLException {
        Connection connection = getInMemoryConnection();
        Statement statement = connection.createStatement();
        statement.execute(
                "DROP Table if exists metadata"
        );
        statement.execute("CREATE TABLE metadata as (SELECT 'schema_version' as data_name, 1 as data_value)");
       return connection;
    }

    @Test
    void testSchema() throws SQLException {
        skipTestOnCI();
        var schema = SqlUtilites.loadSchema(getConnection());
        Approvals.verify(SqlUtilites.print(schema));
    }
    @Test
    void testInMemoryConnection() throws SQLException {
        assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getInMemoryConnection()));
    }
    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mariadb://localhost:3306/sakila", "root", "");
        } catch (SQLException e) {
            throw ObjectUtils.throwAsError(e);
        }
    }

    private Connection getInMemoryConnection() {
        try {
             return DriverManager.getConnection("jdbc:h2:~/sakila", "sa", "");
        } catch (SQLException e) {
            throw ObjectUtils.throwAsError(e);
        }
    }
}
