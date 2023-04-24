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
import za.ac.tut.enums.CourseFields;
import za.ac.tut.enums.QualificationTypeFields;

/**
 *
 * @author T Kujwane
 */
public class QualificationHandler extends Handler {

    public QualificationHandler(DatabaseManager dbManager, EmailSessionBean emailSessionBean) throws ClassNotFoundException, SQLException {
        super(dbManager, emailSessionBean);
    }

    public List getQualificationTypes() throws SQLException {
        List<String> qualificationTypes = new ArrayList<>();

        String query = "SELECT type_name FROM qualification_type;";

        ResultSet resultSet = executeQuery(query);

        if (getDatabaseManager().hasData(resultSet)) {
            while (resultSet.next()) {
                qualificationTypes.add(getDatabaseManager().getData(QualificationTypeFields.TYPE_NAME, resultSet));
            }
        }

        return qualificationTypes;
    }

    public List getCourses() throws SQLException {
        List<String> coursesList = new ArrayList<>();

        String query = "SELECT course_name FROM course ORDER BY course_name ASC;";

        ResultSet resultSet = executeQuery(query);

        if (getDatabaseManager().hasData(resultSet)) {
            while (resultSet.next()) {
                coursesList.add(getDatabaseManager().getData(CourseFields.COURSE_NAME, resultSet));
            }
        }
        
        return coursesList;
    }
}
