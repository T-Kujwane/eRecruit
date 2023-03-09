/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.database.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is a database manager that allows an application to interact with a database using the JDBC framework
 * and the MySQL database management system (MySQL DBMS). <br>
 * An object created class will have a connection to the DBMS that can be used to perform queries using 
 * the PreparedStatement. <br>
 * Alternatively, objects of this class can be used to perform queries to the database.
 * @author Thato Keith Kujwane
 */
public class DatabaseManager {
    private final Connection connection;
    private final Statement statement;
    
    /**
     * This the constructor that creates a database manager object. 
     * @param url This is the Uniform Resource Locator (URL) pointing to the location of the database
     * @param userName This field represents the userName of a database administrator
     * @param password This field represents the password a database administrator uses to connect to the database
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public DatabaseManager(String url, String userName, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url, userName, password);
        this.statement = this.connection.createStatement();
    }
    
    /**
     * This method is invoked from an object of <strong>DatabaseManager</strong> to perform a query on the database. <br>
     * @param sqlQuery An SQL query statement in the form <code>SELECT 'column(s)' FROM 'table(s)' WHERE 'condition(s)'</code>
     * @return The ResultSet produced by the query
     * @throws SQLException 
     */
    public ResultSet executeQuery(String sqlQuery) throws SQLException{
        return this.statement.executeQuery(sqlQuery);
    }
    
    /**
     * This method is invoked from an object of <strong>DatabaseManager</strong> to perform insert, update or delete queries on the database
     * @param sqlUpdate A complete SQL query in the form <code>INSERT INTO 'table'[otional: columns] VALUES(mandatory: values)</code>
     * @throws SQLException 
     */
    public void executeUpdate(String sqlUpdate) throws SQLException{
        this.statement.executeUpdate(sqlUpdate);
    }

    /**
     * This method gets the connection to the database that an object of this class has.
     * @return <code>java.sql.Connection</code>
     */
    public Connection getConnection() {
        return connection;
    }
    
}
