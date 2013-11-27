package com.ut.dph.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "MACRO_NAMES")
public class Macros {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;

    @Column(name = "CATEGORY", nullable = false)
    private String category;

    @NotEmpty
    @Column(name = "MACRO_NAME", nullable = false)
    private String macroName;

    @Column(name = "MACRO_SHORT_NAME", nullable = false)
    private String macroShortName;

    @Column(name = "REF_NUMBER", nullable = false)
    private int refNumber;

    @Column(name = "DATE_DISPLAY", nullable = true)
    private String dateDisplay;

    @Column(name = "FORMULA", nullable = true)
    private String formula = null;

    @Column(name = "INVALID_WHEN", nullable = true)
    private String invalidWhen = null;

    @Column(name = "FIELDA_QUESTION", nullable = true)
    private String fieldAQuestion = null;

    @Column(name = "FIELDB_QUESTION", nullable = true)
    private String fieldBQuestion = null;

    @Column(name = "CON1_QUESTION", nullable = true)
    private String con1Question = null;

    @Column(name = "CON2_QUESTION", nullable = true)
    private String con2Question = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmacroName() {
        return macroName;
    }

    public void setmacroName(String macroName) {
        this.macroName = macroName;
    }

    public String getmacroShortName() {
        return macroShortName;
    }

    public void setmacroShortName(String macroShortName) {
        this.macroShortName = macroShortName;
    }

    public int getrefNumber() {
        return id;
    }

    public void setrefNumber(int refNumber) {
        this.refNumber = refNumber;
    }
    
    public String getdateDisplay() {
        return dateDisplay;
    }

    public void setdateDisplay(String dateDisplay) {
        this.dateDisplay = dateDisplay;
    }
    
    public String getformula() {
        return formula;
    }

    public void setformula(String formula) {
        this.formula = formula;
    }
    
    public String getinvalidWhen() {
        return invalidWhen;
    }

    public void setinvalidWhen(String invalidWhen) {
        this.invalidWhen = invalidWhen;
    }
    
    public String getfieldAQuestion() {
        return fieldAQuestion;
    }

    public void setfieldAQuestion(String fieldAQuestion) {
        this.fieldAQuestion = fieldAQuestion;
    }
    
    public String getfieldBQuestion() {
        return fieldBQuestion;
    }

    public void setfieldBQuestion(String fieldBQuestion) {
        this.fieldBQuestion = fieldBQuestion;
    }      
    
    public String getcon1Question() {
        return con1Question;
    }

    public void setcon1Question(String con1Question) {
        this.con1Question = con1Question;
    }      
    
    public String getcon2Question() {
        return con2Question;
    }

    public void setcon2Question(String con2Question) {
        this.con2Question = con2Question;
    } 

}
