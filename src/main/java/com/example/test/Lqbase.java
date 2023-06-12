package com.example.test;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class Lqbase {
    private static final String lqBaseCfg = "liquidbase/db.changelog.xml";

    private DataSource ds;

    private DataSource getDS() throws NamingException {
        if (ds == null) {
            Context ctx = new InitialContext();
            this.ds = (DataSource) ctx.lookup("java:/MySqlDS");
        }
        return this.ds;
    }

    @PostConstruct
    private void init() {
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(getClass().getClassLoader());

        try (Connection con = getDS().getConnection();) {
            JdbcConnection jc = new JdbcConnection(con);
            Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jc);
            Liquibase liquibase = new Liquibase(lqBaseCfg, resourceAccessor, db);
            liquibase.update("development");
        } catch (SQLException | NamingException | LiquibaseException e) {
            throw new RuntimeException(e);
        }

    }
}

