/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import za.ac.tut.database.manager.DatabaseManager;
import za.ac.tut.ejb.EmailSessionBean;
import za.ac.tut.enums.SkillFields;

/**
 *
 * @author T Kujwane
 */
public class SkillHandler extends Handler {

    public SkillHandler(DatabaseManager dbManager, EmailSessionBean emailSessionBean) throws ClassNotFoundException, SQLException {
        super(dbManager, emailSessionBean);
    }

    public List getSkills() throws SQLException {
        List<String> skillsList = new ArrayList<>();

        String query = "SELECT skill FROM skill ORDER BY skill ASC;";

        ResultSet resultSet = executeQuery(query);

        while (resultSet.next()) {
            skillsList.add(getDatabaseManager().getData(SkillFields.SKILL, resultSet));
        }
        
        return skillsList;
    }
}
