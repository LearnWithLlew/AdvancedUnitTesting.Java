package org.samples;

public class ColumnInfo {

    private final String columnName;
    private final String typeName;
    private final Boolean nullable;

    public ColumnInfo(String columnName, String typeName, Boolean nullable) {

        this.columnName = columnName;
        this.typeName = typeName;
        this.nullable = nullable;
    }
    public String getColumnName() {
        return columnName;
    }

    public String getTypeName() {
        return typeName;
    }

    public Boolean getNullable() {
        return nullable;
    }

    public String print() {
        return  String.format("%s: %s[%s]", getColumnName(), getTypeName(), getNullable() ? "Nullable": "Not Null");
    }
}
