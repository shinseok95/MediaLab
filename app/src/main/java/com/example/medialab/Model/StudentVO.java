package com.example.medialab.Model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class StudentVO implements Serializable {

    private String name;
    private int studentId;
    private String department;
    private String accessDay;
    private String entranceTime;
    private String exitTime;
    private String purpose;
    private String computerNumber;
    private boolean warning;
    private String warningReason;

    public StudentVO(){}

    public StudentVO(String name, int studentId, String department, String accessTime, String entranceTime, String exitTime){
        this.name = name;
        this.studentId = studentId;
        this.department = department;
        this.accessDay = accessTime;
        this.entranceTime = entranceTime;
        this.exitTime = exitTime;
        this.warning=false;
        this.warningReason="";
    }

    public StudentVO(String name, int studentId, String accessTime){
        this(name,studentId,null,accessTime,null,null);
    }

    public StudentVO(int studentId, String accessTime){
        this(null,studentId,null,accessTime,null,null);
    }

    public StudentVO(int studentId, String accessTime,String entranceTime){
        this(null,studentId,null,accessTime,entranceTime,null);
    }


    public String getName(){
        return name;
    }

    public int getStudentId(){
        return studentId;
    }

    public String getAccessDay(){
        return accessDay;
    }

    public String getEntranceTime(){
        return entranceTime;
    }

    public String getExitTime(){
        return exitTime;
    }

    public String getDepartment(){
        return department;
    }

    public String getPurpose(){
        return purpose;
    }

    public String getComputerNumber(){
        return computerNumber;
    }

    public boolean getWarning(){
        return warning;
    }

    public String getWarningReason(){return warningReason;}

    public void setName(String name){
        this.name = name;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setDepartment(String department){
        this.department = department;
    }

    public void setAccessDay(String accessTime){
        this.accessDay = accessTime;
    }

    public void setEntranceTime(String entranceTime){
        this.entranceTime = entranceTime;
    }

    public void setExitTime(String exitTime){
        this.exitTime = exitTime;
    }

    public void setPurpose(String purpose){
        this.purpose = purpose;
    }

    public void setComputerNumber(String computerNumber){
        this.computerNumber = computerNumber;
    }

    public void setWarning(boolean warning) {this.warning = warning;}

    public void setWarningReason(String warningPurpose) {this.warningReason = warningPurpose;}

    @NonNull
    @Override
    public String toString() {

        return name+"["+studentId+"]";
    }
}
