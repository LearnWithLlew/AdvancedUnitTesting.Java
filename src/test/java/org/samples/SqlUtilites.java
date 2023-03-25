package org.samples;

import org.lambda.query.Queryable;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SqlUtilites {
    public static HashMap<String, List<ColumnInfo>> loadSchema(Connection connection) throws SQLException {
      DatabaseMetaData metaData = connection.getMetaData();
      ResultSet tables = metaData.getColumns(connection.getCatalog(), null, null, null);
      var schema = new HashMap<String, List<ColumnInfo>>();
      while (tables.next()){
        String tableName = tables.getString("TABLE_NAME");
        var columns = schema.getOrDefault(tableName, new ArrayList<>());
        String columnName = tables.getString("COLUMN_NAME");
        String typeName = tables.getString("TYPE_NAME");
        Boolean nullable = tables.getInt("NULLABLE") == 0;
        var columnInfo = new ColumnInfo(columnName, typeName, nullable);
        columns.add(columnInfo);
        schema.put(tableName, columns);
      }
      tables.close();
      connection.close();
      return schema;
    }

    static int executeNumericQuery(String query, Connection connection) throws SQLException {
      var result = connection.createStatement().executeQuery(query);
      result.next();
      int theAnswer = result.getInt(1);
      return theAnswer;
    }

    static String print(HashMap<String, List<ColumnInfo>> schema) {
      String output = "";
      Queryable<String> keys = Queryable.as(schema.keySet().toArray(new String[0])).orderBy(k -> k);
      for (String key : keys) {
        output += key + "\n--------------------\n";
        List<ColumnInfo> columnInfos = schema.get(key);
        for (ColumnInfo columnInfo : columnInfos) {
          output += "     " +  columnInfo.print() + "\n";
        }
        output += "\n\n";
      }
      return output;
    }
}
