package za.ac.tut.application;

import java.sql.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author My HP
 */
public class Application {

    private String appliaction_id;
    private String firstName;
    private String description;
    private Date dateQualified;
    private Date closingDate;
    private String vacancyType;
    private String vacancyReferenceNumber;

    public Application(String appliaction_id, String firstName, String description, Date dateQualified, Date closingDate, String vacancyType, String vacancyReferenceNumber) {
        this.appliaction_id = appliaction_id;
        this.firstName = firstName;
        this.description = description;
        this.dateQualified = dateQualified;
        this.closingDate = closingDate;
        this.vacancyType = vacancyType;
        this.vacancyReferenceNumber = vacancyReferenceNumber;
    }

    public void setVacancyReferenceNumber(String vacancyReferenceNumber) {
        this.vacancyReferenceNumber = vacancyReferenceNumber;
    }

    public String getVacancyReferenceNumber() {
        return vacancyReferenceNumber;
    }

    public String getAppliaction_id() {
        return appliaction_id;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public String getDescription() {
        return description;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getVacancyType() {
        return vacancyType;
    }

    public void setAppliaction_id(String appliaction_id) {
        this.appliaction_id = appliaction_id;
    }

    public void setVacancyType(String vacancyType) {
        this.vacancyType = vacancyType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setDateQualified(Date dateQualified) {
        this.dateQualified = dateQualified;
    }

    public Date getDateQualified() {
        return dateQualified;
    }

}
