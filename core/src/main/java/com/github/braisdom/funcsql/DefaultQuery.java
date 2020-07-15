package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultQuery<T> extends AbstractQuery<T> {

    public DefaultQuery(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public List<T> execute(Relation... relations) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        String sql = sqlGenerator.createQuerySQL(getTableName(domainModelClass), projection, filter, groupBy,
                having, orderBy, offset, limit);

        List<T> rows = executeInternally(connection, domainModelClass, sql);

        if (relations.length > 0) {
            for(Relation relation : relations)
                processRelation(connection, rows, relation);
        }

        return rows;
    }

    @Override
    public List<Row> executeCrudely() throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> executeCrudely(C relevantDomainClass, Relation... relations) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String sql = sqlGenerator.createQuerySQL(getTableName(relevantDomainClass), projection, filter, groupBy,
                having, orderBy, offset, limit);

        return null;
    }
}