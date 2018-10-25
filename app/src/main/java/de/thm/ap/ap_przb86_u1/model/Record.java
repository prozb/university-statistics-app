package de.thm.ap.ap_przb86_u1.model;

import java.io.Serializable;

public class Record implements Serializable {
    private int id;
    private String moduleNum;
    private String moduleName;
    private boolean summerTerm;
    private boolean halfWeighted;
    private int credits;
    private int note;
    private int year;

    public Record(){

    }

    public Record(String moduleNumber, String moduleName, int year, boolean summerTerm, boolean halfWeighted, int credits, int note){
        this.moduleNum    = moduleNumber;
        this.moduleName   = moduleName;
        this.summerTerm   = summerTerm;
        this.halfWeighted = halfWeighted;
        this.credits      = credits;
        this.note         = note;
    }


    @Override
    public String toString() {
        String result = "";

        if(moduleNum != null){
            result += (moduleNum.toUpperCase() + ":");
        }
        if(moduleName != null){
            result += (" " + moduleName);
        }
        if(note == -1){
            result += " null";
        }else{
            result += (" " + note + "%");
        }

        return result;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleNum(String moduleNum) {
        this.moduleNum = moduleNum;
    }

    public void setSummerTerm(boolean summerTerm) {
        this.summerTerm = summerTerm;
    }

    public void setCrp(int credits) {
        this.credits = credits;
    }

    public void setMark(int note) {
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public boolean isSummerTerm() {
        return summerTerm;
    }

    public int getCrp() {
        return credits;
    }

    public int getMark() {
        return note;
    }

    public boolean isHalfWeighted() {
        return halfWeighted;
    }

    public void setHalfWeighted(boolean halfWeighted) {
        this.halfWeighted = halfWeighted;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModuleNum() {
        return moduleNum;
    }
}
