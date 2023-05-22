/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.tut.recruiter;

import java.io.Serializable;

/**
 *
 * @author T Kujwane
 */
public class Recruiter implements Serializable{
    private final String enterpriseName, enterpriseEmail, enterprisePhoneNr, enterpriseNumber;

    public Recruiter(String enterpriseName, String enterpriseEmail, String enterprisePhoneNr, String enterpriseNumber) {
        this.enterpriseName = enterpriseName;
        this.enterpriseEmail = enterpriseEmail;
        this.enterprisePhoneNr = enterprisePhoneNr;
        this.enterpriseNumber = enterpriseNumber;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getEnterpriseEmail() {
        return enterpriseEmail;
    }

    public String getEnterprisePhoneNr() {
        return enterprisePhoneNr;
    }

    public String getEnterpriseNumber() {
        return enterpriseNumber;
    }
}
