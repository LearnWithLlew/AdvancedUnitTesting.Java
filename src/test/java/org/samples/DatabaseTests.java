package org.samples;


import com.spun.util.ObjectUtils;
import com.spun.util.StringUtils;
import org.approvaltests.Approvals;
import org.approvaltests.namer.NamerFactory;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class DatabaseTests
{
  @Test
  void testConnection() throws SQLException {
    skipTestOnCI();
    assertEquals(42, SqlUtilites.executeNumericQuery("Select 42;", getConnection()));
  }

  private void skipTestOnCI() {
    if (isCI()) {
       assumeTrue(true);
    }
  }

  private boolean isCI() {
    String[] environmentVariablesForCI = {
            // begin-snippet: supported_ci_env_vars
            "CI",
            "CONTINUOUS_INTEGRATION",
            "GITHUB_ACTIONS",
            "GO_SERVER_URL",
            "JENKINS_URL",
            "TEAMCITY_VERSION",
            "TF_BUILD"
            // end-snippet
    };

    for (var variable : environmentVariablesForCI)
    {
      if (StringUtils.isNonZero(System.getenv(variable)))
      {
        return true;
      }
    }
    return false;
  }


  @Test
  void testVersion() throws SQLException {
    skipTestOnCI();
    assertEquals(1, SqlUtilites.executeNumericQuery("Select value from metadata where name='schema_version'", getConnection()));
  }

  @Test
  void testSchema() throws SQLException {
    skipTestOnCI();
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
