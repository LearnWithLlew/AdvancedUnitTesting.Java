package org.samples;


import com.spun.util.ObjectUtils;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTests
{
  @Test
  void testConnection() throws SQLException {
    assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getConnection()));
  }

  @Test
  void testVersion() throws SQLException {
    assertEquals(1, SqlUtilites.executeNumericQuery("Select value from metadata where name='schema_version'", getConnection()));
  }

  @Test
  void testSchema() throws SQLException {
    var schema = SqlUtilites.loadSchema(getConnection());
    Approvals.verify(SqlUtilites.print(schema));
  }

  private Connection getConnection()  {
    try {
      return  DriverManager.getConnection("jdbc:mariadb://localhost:3306/sakila", "root", "");
    } catch (SQLException e) {
      throw ObjectUtils.throwAsError(e);
    }
  }
}
