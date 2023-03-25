package org.samples;


import com.spun.util.ObjectUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTests
{
  @Test
  void testConnection() throws SQLException {
    assertEquals(42, executeNumericQuery("Select 42;"));
  }

  @Test
  void testVersion() throws SQLException {
    assertEquals(1, executeNumericQuery("Select value from metadata where name='schema_version'"));
  }

  private int executeNumericQuery(String query) throws SQLException {
    var connection = getConnection();
    var result = connection.createStatement().executeQuery(query);
    result.next();
    int theAnswer = result.getInt(1);
    return theAnswer;
  }

  private Connection getConnection()  {
    try {
      return  DriverManager.getConnection("jdbc:mariadb://localhost:3306/sakila", "root", "");
    } catch (SQLException e) {
      throw ObjectUtils.throwAsError(e);
    }
  }
}
