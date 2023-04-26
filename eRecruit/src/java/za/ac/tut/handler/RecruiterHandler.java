/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.RecruiterFields;
import za.ac.tut.recruiter.Recruiter;

/**
 *
 * @author T Kujwane
 */
public class RecruiterHandler extends Handler{

    public RecruiterHandler(DatabaseManager dbManager) throws ClassNotFoundException, SQLException {
        super(dbManager);
    }
    
    
    @Override
    public Recruiter getRecruiter(String enterpriseNr) throws SQLException{
        return super.getRecruiter(enterpriseNr);
    }
}
