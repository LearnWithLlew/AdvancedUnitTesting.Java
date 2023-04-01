package org.samples;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductionDatabase {
    protected static void createDatabase(Connection connection, int version) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(
                "DROP Table if exists metadata"
        );
        statement.execute("CREATE TABLE metadata as (SELECT 'schema_version' as data_name, 1 as data_value)");
    }

    static int getSchemaVersion(Connection connection) throws SQLException {
        return SqlUtilites.executeNumericQuery("Select data_value from metadata where data_name='schema_version'", connection);
    }
}
