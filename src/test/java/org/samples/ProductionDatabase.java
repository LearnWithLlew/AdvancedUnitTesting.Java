package org.samples;

import com.spun.util.ObjectUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

public class ProductionDatabase {
    protected static void createDatabase(Connection connection, int version) {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            InputStream resourceAsStream = ProductionDatabase.class.getResourceAsStream(String.format("database_schema_0%s.sql",version));
            Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            sr.runScript(reader);
        } catch (Exception e) {
            throw ObjectUtils.throwAsError(e);
        }
    }

    static int getSchemaVersion(Connection connection) throws SQLException {
        return SqlUtilites.executeNumericQuery("Select data_value from metadata where data_name='schema_version'", connection);
    }

    public static void updateDatabaseVersionTo(int version, Connection connection) {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            InputStream resourceAsStream = ProductionDatabase.class.getResourceAsStream(String.format("update_database_schema_00_0%s.sql",version));
            Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            sr.runScript(reader);
        } catch (Exception e) {
            throw ObjectUtils.throwAsError(e);
        }
    }
}
