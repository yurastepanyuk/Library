package com.stepanyuk.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private static InitialContext   initialContext;
    private static DataSource       dataSource;
    private static Connection       connection;

    public Database() {
    }

    public static Connection getConnection(){

        try {
            initialContext = new InitialContext();
            dataSource = (DataSource)initialContext.lookup("java:comp/env/DerbyPool");

//            if (connection == null) {
                connection = dataSource.getConnection();
//            }

        } catch (NamingException | SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE,null,e);
        }
        return connection;
    }

}
