package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class PartnerSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("codep", table, columnPrefix + "_codep"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("name", table, columnPrefix + "_name"));
        columns.add(Column.aliased("contact", table, columnPrefix + "_contact"));
        columns.add(Column.aliased("logo", table, columnPrefix + "_logo"));
        columns.add(Column.aliased("logo_content_type", table, columnPrefix + "_logo_content_type"));
        columns.add(Column.aliased("icon", table, columnPrefix + "_icon"));
        columns.add(Column.aliased("icon_content_type", table, columnPrefix + "_icon_content_type"));

        return columns;
    }
}
