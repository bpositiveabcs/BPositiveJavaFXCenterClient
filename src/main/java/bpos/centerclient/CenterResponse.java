package bpos.centerclient;

import bpos.common.model.Center;
import bpos.common.model.LogInfo;

public class CenterResponse {
    private int id;
    private String institutionDetails;
    private LogInfo logInfo;
    private String centerName;
    private String address;

    // Getters și setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInstitutionDetails() {
        return institutionDetails;
    }

    public void setInstitutionDetails(String institutionDetails) {
        this.institutionDetails = institutionDetails;
    }

    public LogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(LogInfo logInfo) {
        this.logInfo = logInfo;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public Center getCenter() {
        Center center= new Center(centerName, logInfo, institutionDetails, address);
        center.setId(id);
        return center;
    }
}
