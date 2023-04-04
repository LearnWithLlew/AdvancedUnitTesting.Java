package org.samples;

import com.spun.util.ObjectUtils;
import com.spun.util.io.FileUtils;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductionDatabase {
    protected static void createDatabase(Connection connection, int version) {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            String sql = FileUtils.readFromClassPath(ProductionDatabase.class, "database_schema_01.sql");
            InputStream resourceAsStream = ProductionDatabase.class.getResourceAsStream("database_schema_01.sql");
            Reader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            sr.runScript(reader);
        } catch (Exception e) {
            throw ObjectUtils.throwAsError(e);
        }
    }

    static int getSchemaVersion(Connection connection) throws SQLException {
        return SqlUtilites.executeNumericQuery("Select data_value from metadata where data_name='schema_version'", connection);
    }
}
