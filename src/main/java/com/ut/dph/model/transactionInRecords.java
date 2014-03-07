/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ut.dph.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author chadmccue
 */
@Entity
@Table(name = "TRANSACTIONINRECORDS")
public class transactionInRecords {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private int id;
    
    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;
    
    @Column(name = "F1", nullable = true)
    private String f1 = null;
    
    @Column(name = "F2", nullable = true)
    private String f2 = null;
    
    @Column(name = "F3", nullable = true)
    private String f3 = null;
    
    @Column(name = "F4", nullable = true)
    private String f4 = null;
    
    @Column(name = "F5", nullable = true)
    private String f5 = null;
    
    @Column(name = "F6", nullable = true)
    private String f6 = null;
    
    @Column(name = "F7", nullable = true)
    private String f7 = null;
    
    @Column(name = "F8", nullable = true)
    private String f8 = null;
    
    @Column(name = "F9", nullable = true)
    private String f9 = null;
    
    @Column(name = "F10", nullable = true)
    private String f10 = null;
    
    @Column(name = "F11", nullable = true)
    private String f11 = null;
    
    @Column(name = "F12", nullable = true)
    private String f12 = null;
    
    @Column(name = "F13", nullable = true)
    private String f13 = null;
    
    @Column(name = "F14", nullable = true)
    private String f14 = null;
    
    @Column(name = "F15", nullable = true)
    private String f15 = null;
    
    @Column(name = "F16", nullable = true)
    private String f16 = null;
    
    @Column(name = "F17", nullable = true)
    private String f17 = null;
    
    @Column(name = "F18", nullable = true)
    private String f18 = null;
    
    @Column(name = "F19", nullable = true)
    private String f19 = null;
    
    @Column(name = "F20", nullable = true)
    private String f20 = null;
    
    @Column(name = "F21", nullable = true)
    private String f21 = null;
    
    @Column(name = "F22", nullable = true)
    private String f22 = null;
    
    @Column(name = "F23", nullable = true)
    private String f23 = null;
    
    @Column(name = "F24", nullable = true)
    private String f24 = null;
    
    @Column(name = "F25", nullable = true)
    private String f25 = null;
    
    @Column(name = "F26", nullable = true)
    private String f26 = null;
    
    @Column(name = "F27", nullable = true)
    private String f27 = null;
    
    @Column(name = "F28", nullable = true)
    private String f28 = null;
    
    @Column(name = "F29", nullable = true)
    private String f29 = null;
    
    @Column(name = "F30", nullable = true)
    private String f30 = null;
    
    @Column(name = "F31", nullable = true)
    private String f31 = null;
    
    @Column(name = "F32", nullable = true)
    private String f32 = null;
    
    @Column(name = "F33", nullable = true)
    private String f33 = null;
    
    @Column(name = "F34", nullable = true)
    private String f34 = null;
    
    @Column(name = "F35", nullable = true)
    private String f35 = null;
    
    @Column(name = "F36", nullable = true)
    private String f36 = null;
    
    @Column(name = "F37", nullable = true)
    private String f37 = null;
    
    @Column(name = "F38", nullable = true)
    private String f38 = null;
    
    @Column(name = "F39", nullable = true)
    private String f39 = null;
    
    @Column(name = "F40", nullable = true)
    private String f40 = null;
    
    @Column(name = "F41", nullable = true)
    private String f41 = null;
    
    @Column(name = "F42", nullable = true)
    private String f42 = null;
    
    @Column(name = "F43", nullable = true)
    private String f43 = null;
    
    @Column(name = "F44", nullable = true)
    private String f44 = null;
    
    @Column(name = "F45", nullable = true)
    private String f45 = null;
    
    @Column(name = "F46", nullable = true)
    private String f46 = null;
    
    @Column(name = "F47", nullable = true)
    private String f47 = null;
    
    @Column(name = "F48", nullable = true)
    private String f48 = null;
    
    @Column(name = "F49", nullable = true)
    private String f49 = null;
    
    @Column(name = "F50", nullable = true)
    private String f50 = null;
    
    @Column(name = "F51", nullable = true)
    private String f51 = null;
    
    @Column(name = "F52", nullable = true)
    private String f52 = null;
    
    @Column(name = "F53", nullable = true)
    private String f53 = null;
    
    @Column(name = "F54", nullable = true)
    private String f54 = null;
    
    @Column(name = "F55", nullable = true)
    private String f55 = null;
    
    @Column(name = "F56", nullable = true)
    private String f56 = null;
    
    @Column(name = "F57", nullable = true)
    private String f57 = null;
    
    @Column(name = "F58", nullable = true)
    private String f58 = null;
    
    @Column(name = "F59", nullable = true)
    private String f59 = null;
    
    @Column(name = "F60", nullable = true)
    private String f60 = null;
    
    @Column(name = "F61", nullable = true)
    private String f61 = null;
    
    @Column(name = "F62", nullable = true)
    private String f62 = null;
    
    @Column(name = "F63", nullable = true)
    private String f63 = null;
    
    @Column(name = "F64", nullable = true)
    private String f64 = null;
    
    @Column(name = "F65", nullable = true)
    private String f65 = null;
    
    @Column(name = "F66", nullable = true)
    private String f66 = null;
    
    @Column(name = "F67", nullable = true)
    private String f67 = null;
    
    @Column(name = "F68", nullable = true)
    private String f68 = null;
    
    @Column(name = "F69", nullable = true)
    private String f69 = null;
    
    @Column(name = "F70", nullable = true)
    private String f70 = null;
    
    @Column(name = "F71", nullable = true)
    private String f71 = null;
    
    @Column(name = "F72", nullable = true)
    private String f72 = null;
    
    @Column(name = "F73", nullable = true)
    private String f73 = null;
    
    @Column(name = "F74", nullable = true)
    private String f74 = null;
    
    @Column(name = "F75", nullable = true)
    private String f75 = null;
    
    @Column(name = "F76", nullable = true)
    private String f76 = null;
    
    @Column(name = "F77", nullable = true)
    private String f77 = null;
    
    @Column(name = "F78", nullable = true)
    private String f78 = null;
    
    @Column(name = "F79", nullable = true)
    private String f79 = null;
    
    @Column(name = "F80", nullable = true)
    private String f80 = null;
    
    @Column(name = "F81", nullable = true)
    private String f81 = null;
    
    @Column(name = "F82", nullable = true)
    private String f82 = null;
    
    @Column(name = "F83", nullable = true)
    private String f83 = null;
    
    @Column(name = "F84", nullable = true)
    private String f84 = null;
    
    @Column(name = "F85", nullable = true)
    private String f85 = null;
    
    @Column(name = "F86", nullable = true)
    private String f86 = null;
    
    @Column(name = "F87", nullable = true)
    private String f87 = null;
    
    @Column(name = "F88", nullable = true)
    private String f88 = null;
    
    @Column(name = "F89", nullable = true)
    private String f89 = null;
    
    @Column(name = "F90", nullable = true)
    private String f90 = null;
    
    @Column(name = "F91", nullable = true)
    private String f91 = null;
    
    @Column(name = "F92", nullable = true)
    private String f92 = null;
    
    @Column(name = "F93", nullable = true)
    private String f93 = null;
    
    @Column(name = "F94", nullable = true)
    private String f94 = null;
    
    @Column(name = "F95", nullable = true)
    private String f95 = null;
    
    @Column(name = "F96", nullable = true)
    private String f96 = null;
    
    @Column(name = "F97", nullable = true)
    private String f97 = null;
    
    @Column(name = "F98", nullable = true)
    private String f98 = null;
    
    @Column(name = "F99", nullable = true)
    private String f99 = null;
    
    @Column(name = "F100", nullable = true)
    private String f100 = null;
    
    @Column(name = "F101", nullable = true)
    private String f101 = null;
    
    @Column(name = "F102", nullable = true)
    private String f102 = null;
    
    @Column(name = "F103", nullable = true)
    private String f103 = null;
    
    @Column(name = "F104", nullable = true)
    private String f104 = null;
    
    @Column(name = "F105", nullable = true)
    private String f105 = null;
    
    @Column(name = "F106", nullable = true)
    private String f106 = null;
    
    @Column(name = "F107", nullable = true)
    private String f107 = null;
    
    @Column(name = "F108", nullable = true)
    private String f108 = null;
    
    @Column(name = "F109", nullable = true)
    private String f109 = null;
    
    @Column(name = "F110", nullable = true)
    private String f110 = null;
    
    @Column(name = "F111", nullable = true)
    private String f111 = null;
    
    @Column(name = "F112", nullable = true)
    private String f112 = null;
    
    @Column(name = "F113", nullable = true)
    private String f113 = null;
    
    @Column(name = "F114", nullable = true)
    private String f114 = null;
    
    @Column(name = "F115", nullable = true)
    private String f115 = null;
    
    @Column(name = "F116", nullable = true)
    private String f116 = null;
    
    @Column(name = "F117", nullable = true)
    private String f117 = null;
    
    @Column(name = "F118", nullable = true)
    private String f118 = null;
    
    @Column(name = "F119", nullable = true)
    private String f119 = null;
    
    @Column(name = "F120", nullable = true)
    private String f120 = null;
    
    @Column(name = "F121", nullable = true)
    private String f121 = null;
    
    @Column(name = "F122", nullable = true)
    private String f122 = null;
    
    @Column(name = "F123", nullable = true)
    private String f123 = null;
    
    @Column(name = "F124", nullable = true)
    private String f124 = null;
    
    @Column(name = "F125", nullable = true)
    private String f125 = null;
    
    @Column(name = "F126", nullable = true)
    private String f126 = null;
    
    @Column(name = "F127", nullable = true)
    private String f127 = null;
    
    @Column(name = "F128", nullable = true)
    private String f128 = null;
    
    @Column(name = "F129", nullable = true)
    private String f129 = null;
    
    @Column(name = "F130", nullable = true)
    private String f130 = null;
    
    @Column(name = "F131", nullable = true)
    private String f131 = null;
    
    @Column(name = "F132", nullable = true)
    private String f132 = null;
    
    @Column(name = "F133", nullable = true)
    private String f133 = null;
    
    @Column(name = "F134", nullable = true)
    private String f134 = null;
    
    @Column(name = "F135", nullable = true)
    private String f135 = null;
    
    @Column(name = "F136", nullable = true)
    private String f136 = null;
    
    @Column(name = "F137", nullable = true)
    private String f137 = null;
    
    @Column(name = "F138", nullable = true)
    private String f138 = null;
    
    @Column(name = "F139", nullable = true)
    private String f139 = null;
    
    @Column(name = "F140", nullable = true)
    private String f140 = null;
    
    @Column(name = "F141", nullable = true)
    private String f141 = null;
    
    @Column(name = "F142", nullable = true)
    private String f142 = null;
    
    @Column(name = "F143", nullable = true)
    private String f143 = null;
    
    @Column(name = "F144", nullable = true)
    private String f144 = null;
    
    @Column(name = "F145", nullable = true)
    private String f145 = null;
    
    @Column(name = "F146", nullable = true)
    private String f146 = null;
    
    @Column(name = "F147", nullable = true)
    private String f147 = null;
    
    @Column(name = "F148", nullable = true)
    private String f148 = null;
    
    @Column(name = "F149", nullable = true)
    private String f149 = null;
    
    @Column(name = "F150", nullable = true)
    private String f150 = null;
    
    @Column(name = "F151", nullable = true)
    private String f151 = null;
    
    @Column(name = "F152", nullable = true)
    private String f152 = null;
    
    @Column(name = "F153", nullable = true)
    private String f153 = null;
    
    @Column(name = "F154", nullable = true)
    private String f154 = null;
    
    @Column(name = "F155", nullable = true)
    private String f155 = null;
    
    @Column(name = "F156", nullable = true)
    private String f156 = null;
    
    @Column(name = "F157", nullable = true)
    private String f157 = null;
    
    @Column(name = "F158", nullable = true)
    private String f158 = null;
    
    @Column(name = "F159", nullable = true)
    private String f159 = null;
    
    @Column(name = "F160", nullable = true)
    private String f160 = null;
    
    @Column(name = "F161", nullable = true)
    private String f161 = null;
    
    @Column(name = "F162", nullable = true)
    private String f162 = null;
    
    @Column(name = "F163", nullable = true)
    private String f163 = null;
    
    @Column(name = "F164", nullable = true)
    private String f164 = null;
    
    @Column(name = "F165", nullable = true)
    private String f165 = null;
    
    @Column(name = "F166", nullable = true)
    private String f166 = null;
    
    @Column(name = "F167", nullable = true)
    private String f167 = null;
    
    @Column(name = "F168", nullable = true)
    private String f168 = null;
    
    @Column(name = "F169", nullable = true)
    private String f169 = null;
    
    @Column(name = "F170", nullable = true)
    private String f170 = null;
    
    @Column(name = "F171", nullable = true)
    private String f171 = null;
    
    @Column(name = "F172", nullable = true)
    private String f172 = null;
    
    @Column(name = "F173", nullable = true)
    private String f173 = null;
    
    @Column(name = "F174", nullable = true)
    private String f174 = null;
    
    @Column(name = "F175", nullable = true)
    private String f175 = null;
    
    @Column(name = "F176", nullable = true)
    private String f176 = null;
    
    @Column(name = "F177", nullable = true)
    private String f177 = null;
    
    @Column(name = "F178", nullable = true)
    private String f178 = null;
    
    @Column(name = "F179", nullable = true)
    private String f179 = null;
    
    @Column(name = "F180", nullable = true)
    private String f180 = null;
    
    @Column(name = "F181", nullable = true)
    private String f181 = null;
    
    @Column(name = "F182", nullable = true)
    private String f182 = null;
    
    @Column(name = "F183", nullable = true)
    private String f183 = null;
    
    @Column(name = "F184", nullable = true)
    private String f184 = null;
    
    @Column(name = "F185", nullable = true)
    private String f185 = null;
    
    @Column(name = "F186", nullable = true)
    private String f186 = null;
    
    @Column(name = "F187", nullable = true)
    private String f187 = null;
    
    @Column(name = "F188", nullable = true)
    private String f188 = null;
    
    @Column(name = "F189", nullable = true)
    private String f189 = null;
    
    @Column(name = "F190", nullable = true)
    private String f190 = null;
    
    @Column(name = "F191", nullable = true)
    private String f191 = null;
    
    @Column(name = "F192", nullable = true)
    private String f192 = null;
    
    @Column(name = "F193", nullable = true)
    private String f193 = null;
    
    @Column(name = "F194", nullable = true)
    private String f194 = null;
    
    @Column(name = "F195", nullable = true)
    private String f195 = null;
    
    @Column(name = "F196", nullable = true)
    private String f196 = null;
    
    @Column(name = "F197", nullable = true)
    private String f197 = null;
    
    @Column(name = "F198", nullable = true)
    private String f198 = null;
    
    @Column(name = "F199", nullable = true)
    private String f199 = null;
    
    @Column(name = "F200", nullable = true)
    private String f200 = null;
    
    @Column(name = "F201", nullable = true)
    private String f201 = null;
    
    @Column(name = "F202", nullable = true)
    private String f202 = null;
    
    @Column(name = "F203", nullable = true)
    private String f203 = null;
    
    @Column(name = "F204", nullable = true)
    private String f204 = null;
    
    @Column(name = "F205", nullable = true)
    private String f205 = null;
    
    @Column(name = "F206", nullable = true)
    private String f206 = null;
    
    @Column(name = "F207", nullable = true)
    private String f207 = null;
    
    @Column(name = "F208", nullable = true)
    private String f208 = null;
    
    @Column(name = "F209", nullable = true)
    private String f209 = null;
    
    @Column(name = "F210", nullable = true)
    private String f210 = null;
    
    @Column(name = "F211", nullable = true)
    private String f211 = null;
    
    @Column(name = "F212", nullable = true)
    private String f212 = null;
    
    @Column(name = "F213", nullable = true)
    private String f213 = null;
    
    @Column(name = "F214", nullable = true)
    private String f214 = null;
    
    @Column(name = "F215", nullable = true)
    private String f215 = null;
    
    @Column(name = "F216", nullable = true)
    private String f216 = null;
    
    @Column(name = "F217", nullable = true)
    private String f217 = null;
    
    @Column(name = "F218", nullable = true)
    private String f218 = null;
    
    @Column(name = "F219", nullable = true)
    private String f219 = null;
    
    @Column(name = "F220", nullable = true)
    private String f220 = null;
    
    @Column(name = "F221", nullable = true)
    private String f221 = null;
    
    @Column(name = "F222", nullable = true)
    private String f222 = null;
    
    @Column(name = "F223", nullable = true)
    private String f223 = null;
    
    @Column(name = "F224", nullable = true)
    private String f224 = null;
    
    @Column(name = "F225", nullable = true)
    private String f225 = null;
    
    @Column(name = "F226", nullable = true)
    private String f226 = null;
    
    @Column(name = "F227", nullable = true)
    private String f227 = null;
    
    @Column(name = "F228", nullable = true)
    private String f228 = null;
    
    @Column(name = "F229", nullable = true)
    private String f229 = null;
    
    @Column(name = "F230", nullable = true)
    private String f230 = null;
    
    @Column(name = "F231", nullable = true)
    private String f231 = null;
    
    @Column(name = "F232", nullable = true)
    private String f232 = null;
    
    @Column(name = "F233", nullable = true)
    private String f233 = null;
    
    @Column(name = "F234", nullable = true)
    private String f234 = null;
    
    @Column(name = "F235", nullable = true)
    private String f235 = null;
    
    @Column(name = "F236", nullable = true)
    private String f236 = null;
    
    @Column(name = "F237", nullable = true)
    private String f237 = null;
    
    @Column(name = "F238", nullable = true)
    private String f238 = null;
    
    @Column(name = "F239", nullable = true)
    private String f239 = null;
    
    @Column(name = "F240", nullable = true)
    private String f240 = null;
    
    @Column(name = "F241", nullable = true)
    private String f241 = null;
    
    @Column(name = "F242", nullable = true)
    private String f242 = null;
    
    @Column(name = "F243", nullable = true)
    private String f243 = null;
    
    @Column(name = "F244", nullable = true)
    private String f244 = null;
    
    @Column(name = "F245", nullable = true)
    private String f245 = null;
    
    @Column(name = "F246", nullable = true)
    private String f246 = null;
    
    @Column(name = "F247", nullable = true)
    private String f247 = null;
    
    @Column(name = "F248", nullable = true)
    private String f248 = null;
    
    @Column(name = "F249", nullable = true)
    private String f249 = null;
    
    @Column(name = "F250", nullable = true)
    private String f250 = null;
    
    @Column(name = "F251", nullable = true)
    private String f251 = null;
    
    @Column(name = "F252", nullable = true)
    private String f252 = null;
    
    @Column(name = "F253", nullable = true)
    private String f253 = null;
    
    @Column(name = "F254", nullable = true)
    private String f254 = null;
    
    @Column(name = "F255", nullable = true)
    private String f255 = null;
    
    @Column(name = "dateCreated", nullable = true)
    private Date dateCreated = new Date();
    
    @Column(name = "loadTableId", nullable = true)
    private String loadTableId = null;

    public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLoadTableId() {
		return loadTableId;
	}

	public void setLoadTableId(String loadTableId) {
		this.loadTableId = loadTableId;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionInId() {
        return transactionInId;
    }

    public void setTransactionInId(int transactionInId) {
        this.transactionInId = transactionInId;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2;
    }

    public String getF3() {
        return f3;
    }

    public void setF3(String f3) {
        this.f3 = f3;
    }
    
    public String getF4() {
        return f4;
    }

    public void setF4(String f4) {
        this.f4 = f4;
    }


    public String getF5() {
        return f5;
    }

    public void setF5(String f5) {
        this.f5 = f5;
    }

    public String getF6() {
        return f6;
    }

    public void setF6(String f6) {
        this.f6 = f6;
    }

    public String getF7() {
        return f7;
    }

    public void setF7(String f7) {
        this.f7 = f7;
    }

    public String getF8() {
        return f8;
    }

    public void setF8(String f8) {
        this.f8 = f8;
    }

    public String getF9() {
        return f9;
    }

    public void setF9(String f9) {
        this.f9 = f9;
    }

    public String getF10() {
        return f10;
    }

    public void setF10(String f10) {
        this.f10 = f10;
    }

    public String getF11() {
        return f11;
    }

    public void setF11(String f11) {
        this.f11 = f11;
    }

    public String getF12() {
        return f12;
    }

    public void setF12(String f12) {
        this.f12 = f12;
    }

    public String getF13() {
        return f13;
    }

    public void setF13(String f13) {
        this.f13 = f13;
    }

    public String getF14() {
        return f14;
    }

    public void setF14(String f14) {
        this.f14 = f14;
    }

    public String getF15() {
        return f15;
    }

    public void setF15(String f15) {
        this.f15 = f15;
    }

    public String getF16() {
        return f16;
    }

    public void setF16(String f16) {
        this.f16 = f16;
    }

    public String getF17() {
        return f17;
    }

    public void setF17(String f17) {
        this.f17 = f17;
    }

    public String getF18() {
        return f18;
    }

    public void setF18(String f18) {
        this.f18 = f18;
    }

    public String getF19() {
        return f19;
    }

    public void setF19(String f19) {
        this.f19 = f19;
    }

    public String getF20() {
        return f20;
    }

    public void setF20(String f20) {
        this.f20 = f20;
    }

    public String getF21() {
        return f21;
    }

    public void setF21(String f21) {
        this.f21 = f21;
    }

    public String getF22() {
        return f22;
    }

    public void setF22(String f22) {
        this.f22 = f22;
    }

    public String getF23() {
        return f23;
    }

    public void setF23(String f23) {
        this.f23 = f23;
    }

    public String getF24() {
        return f24;
    }

    public void setF24(String f24) {
        this.f24 = f24;
    }

    public String getF25() {
        return f25;
    }

    public void setF25(String f25) {
        this.f25 = f25;
    }

    public String getF26() {
        return f26;
    }

    public void setF26(String f26) {
        this.f26 = f26;
    }

    public String getF27() {
        return f27;
    }

    public void setF27(String f27) {
        this.f27 = f27;
    }

    public String getF28() {
        return f28;
    }

    public void setF28(String f28) {
        this.f28 = f28;
    }

    public String getF29() {
        return f29;
    }

    public void setF29(String f29) {
        this.f29 = f29;
    }

    public String getF30() {
        return f30;
    }

    public void setF30(String f30) {
        this.f30 = f30;
    }

    public String getF31() {
        return f31;
    }

    public void setF31(String f31) {
        this.f31 = f31;
    }

    public String getF32() {
        return f32;
    }

    public void setF32(String f32) {
        this.f32 = f32;
    }

    public String getF33() {
        return f33;
    }

    public void setF33(String f33) {
        this.f33 = f33;
    }

    public String getF34() {
        return f34;
    }

    public void setF34(String f34) {
        this.f34 = f34;
    }

    public String getF35() {
        return f35;
    }

    public void setF35(String f35) {
        this.f35 = f35;
    }

    public String getF36() {
        return f36;
    }

    public void setF36(String f36) {
        this.f36 = f36;
    }

    public String getF37() {
        return f37;
    }

    public void setF37(String f37) {
        this.f37 = f37;
    }

    public String getF38() {
        return f38;
    }

    public void setF38(String f38) {
        this.f38 = f38;
    }

    public String getF39() {
        return f39;
    }

    public void setF39(String f39) {
        this.f39 = f39;
    }

    public String getF40() {
        return f40;
    }

    public void setF40(String f40) {
        this.f40 = f40;
    }

    public String getF41() {
        return f41;
    }

    public void setF41(String f41) {
        this.f41 = f41;
    }

    public String getF42() {
        return f42;
    }

    public void setF42(String f42) {
        this.f42 = f42;
    }

    public String getF43() {
        return f43;
    }

    public void setF43(String f43) {
        this.f43 = f43;
    }

    public String getF44() {
        return f44;
    }

    public void setF44(String f44) {
        this.f44 = f44;
    }

    public String getF45() {
        return f45;
    }

    public void setF45(String f45) {
        this.f45 = f45;
    }

    public String getF46() {
        return f46;
    }

    public void setF46(String f46) {
        this.f46 = f46;
    }

    public String getF47() {
        return f47;
    }

    public void setF47(String f47) {
        this.f47 = f47;
    }

    public String getF48() {
        return f48;
    }

    public void setF48(String f48) {
        this.f48 = f48;
    }

    public String getF49() {
        return f49;
    }

    public void setF49(String f49) {
        this.f49 = f49;
    }

    public String getF50() {
        return f50;
    }

    public void setF50(String f50) {
        this.f50 = f50;
    }

    public String getF51() {
        return f51;
    }

    public void setF51(String f51) {
        this.f51 = f51;
    }

    public String getF52() {
        return f52;
    }

    public void setF52(String f52) {
        this.f52 = f52;
    }

    public String getF53() {
        return f53;
    }

    public void setF53(String f53) {
        this.f53 = f53;
    }

    public String getF54() {
        return f54;
    }

    public void setF54(String f54) {
        this.f54 = f54;
    }

    public String getF55() {
        return f55;
    }

    public void setF55(String f55) {
        this.f55 = f55;
    }

    public String getF56() {
        return f56;
    }

    public void setF56(String f56) {
        this.f56 = f56;
    }

    public String getF57() {
        return f57;
    }

    public void setF57(String f57) {
        this.f57 = f57;
    }

    public String getF58() {
        return f58;
    }

    public void setF58(String f58) {
        this.f58 = f58;
    }

    public String getF59() {
        return f59;
    }

    public void setF59(String f59) {
        this.f59 = f59;
    }

    public String getF60() {
        return f60;
    }

    public void setF60(String f60) {
        this.f60 = f60;
    }

    public String getF61() {
        return f61;
    }

    public void setF61(String f61) {
        this.f61 = f61;
    }

    public String getF62() {
        return f62;
    }

    public void setF62(String f62) {
        this.f62 = f62;
    }

    public String getF63() {
        return f63;
    }

    public void setF63(String f63) {
        this.f63 = f63;
    }

    public String getF64() {
        return f64;
    }

    public void setF64(String f64) {
        this.f64 = f64;
    }

    public String getF65() {
        return f65;
    }

    public void setF65(String f65) {
        this.f65 = f65;
    }

    public String getF66() {
        return f66;
    }

    public void setF66(String f66) {
        this.f66 = f66;
    }

    public String getF67() {
        return f67;
    }

    public void setF67(String f67) {
        this.f67 = f67;
    }

    public String getF68() {
        return f68;
    }

    public void setF68(String f68) {
        this.f68 = f68;
    }

    public String getF69() {
        return f69;
    }

    public void setF69(String f69) {
        this.f69 = f69;
    }

    public String getF70() {
        return f70;
    }

    public void setF70(String f70) {
        this.f70 = f70;
    }

    public String getF71() {
        return f71;
    }

    public void setF71(String f71) {
        this.f71 = f71;
    }

    public String getF72() {
        return f72;
    }

    public void setF72(String f72) {
        this.f72 = f72;
    }

    public String getF73() {
        return f73;
    }

    public void setF73(String f73) {
        this.f73 = f73;
    }

    public String getF74() {
        return f74;
    }

    public void setF74(String f74) {
        this.f74 = f74;
    }

    public String getF75() {
        return f75;
    }

    public void setF75(String f75) {
        this.f75 = f75;
    }

    public String getF76() {
        return f76;
    }

    public void setF76(String f76) {
        this.f76 = f76;
    }

    public String getF77() {
        return f77;
    }

    public void setF77(String f77) {
        this.f77 = f77;
    }

    public String getF78() {
        return f78;
    }

    public void setF78(String f78) {
        this.f78 = f78;
    }

    public String getF79() {
        return f79;
    }

    public void setF79(String f79) {
        this.f79 = f79;
    }

    public String getF80() {
        return f80;
    }

    public void setF80(String f80) {
        this.f80 = f80;
    }

    public String getF81() {
        return f81;
    }

    public void setF81(String f81) {
        this.f81 = f81;
    }

    public String getF82() {
        return f82;
    }

    public void setF82(String f82) {
        this.f82 = f82;
    }

    public String getF83() {
        return f83;
    }

    public void setF83(String f83) {
        this.f83 = f83;
    }

    public String getF84() {
        return f84;
    }

    public void setF84(String f84) {
        this.f84 = f84;
    }

    public String getF85() {
        return f85;
    }

    public void setF85(String f85) {
        this.f85 = f85;
    }

    public String getF86() {
        return f86;
    }

    public void setF86(String f86) {
        this.f86 = f86;
    }

    public String getF87() {
        return f87;
    }

    public void setF87(String f87) {
        this.f87 = f87;
    }

    public String getF88() {
        return f88;
    }

    public void setF88(String f88) {
        this.f88 = f88;
    }

    public String getF89() {
        return f89;
    }

    public void setF89(String f89) {
        this.f89 = f89;
    }

    public String getF90() {
        return f90;
    }

    public void setF90(String f90) {
        this.f90 = f90;
    }

    public String getF91() {
        return f91;
    }

    public void setF91(String f91) {
        this.f91 = f91;
    }

    public String getF92() {
        return f92;
    }

    public void setF92(String f92) {
        this.f92 = f92;
    }

    public String getF93() {
        return f93;
    }

    public void setF93(String f93) {
        this.f93 = f93;
    }

    public String getF94() {
        return f94;
    }

    public void setF94(String f94) {
        this.f94 = f94;
    }

    public String getF95() {
        return f95;
    }

    public void setF95(String f95) {
        this.f95 = f95;
    }

    public String getF96() {
        return f96;
    }

    public void setF96(String f96) {
        this.f96 = f96;
    }

    public String getF97() {
        return f97;
    }

    public void setF97(String f97) {
        this.f97 = f97;
    }

    public String getF98() {
        return f98;
    }

    public void setF98(String f98) {
        this.f98 = f98;
    }

    public String getF99() {
        return f99;
    }

    public void setF99(String f99) {
        this.f99 = f99;
    }

    public String getF100() {
        return f100;
    }

    public void setF100(String f100) {
        this.f100 = f100;
    }

    public String getF101() {
        return f101;
    }

    public void setF101(String f101) {
        this.f101 = f101;
    }

    public String getF102() {
        return f102;
    }

    public void setF102(String f102) {
        this.f102 = f102;
    }

    public String getF103() {
        return f103;
    }

    public void setF103(String f103) {
        this.f103 = f103;
    }

    public String getF104() {
        return f104;
    }

    public void setF104(String f104) {
        this.f104 = f104;
    }

    public String getF105() {
        return f105;
    }

    public void setF105(String f105) {
        this.f105 = f105;
    }

    public String getF106() {
        return f106;
    }

    public void setF106(String f106) {
        this.f106 = f106;
    }

    public String getF107() {
        return f107;
    }

    public void setF107(String f107) {
        this.f107 = f107;
    }

    public String getF108() {
        return f108;
    }

    public void setF108(String f108) {
        this.f108 = f108;
    }

    public String getF109() {
        return f109;
    }

    public void setF109(String f109) {
        this.f109 = f109;
    }

    public String getF110() {
        return f110;
    }

    public void setF110(String f110) {
        this.f110 = f110;
    }

    public String getF111() {
        return f111;
    }

    public void setF111(String f111) {
        this.f111 = f111;
    }

    public String getF112() {
        return f112;
    }

    public void setF112(String f112) {
        this.f112 = f112;
    }

    public String getF113() {
        return f113;
    }

    public void setF113(String f113) {
        this.f113 = f113;
    }

    public String getF114() {
        return f114;
    }

    public void setF114(String f114) {
        this.f114 = f114;
    }

    public String getF115() {
        return f115;
    }

    public void setF115(String f115) {
        this.f115 = f115;
    }

    public String getF116() {
        return f116;
    }

    public void setF116(String f116) {
        this.f116 = f116;
    }

    public String getF117() {
        return f117;
    }

    public void setF117(String f117) {
        this.f117 = f117;
    }

    public String getF118() {
        return f118;
    }

    public void setF118(String f118) {
        this.f118 = f118;
    }

    public String getF119() {
        return f119;
    }

    public void setF119(String f119) {
        this.f119 = f119;
    }

    public String getF120() {
        return f120;
    }

    public void setF120(String f120) {
        this.f120 = f120;
    }

    public String getF121() {
        return f121;
    }

    public void setF121(String f121) {
        this.f121 = f121;
    }

    public String getF122() {
        return f122;
    }

    public void setF122(String f122) {
        this.f122 = f122;
    }

    public String getF123() {
        return f123;
    }

    public void setF123(String f123) {
        this.f123 = f123;
    }

    public String getF124() {
        return f124;
    }

    public void setF124(String f124) {
        this.f124 = f124;
    }

    public String getF125() {
        return f125;
    }

    public void setF125(String f125) {
        this.f125 = f125;
    }

    public String getF126() {
        return f126;
    }

    public void setF126(String f126) {
        this.f126 = f126;
    }

    public String getF127() {
        return f127;
    }

    public void setF127(String f127) {
        this.f127 = f127;
    }

    public String getF128() {
        return f128;
    }

    public void setF128(String f128) {
        this.f128 = f128;
    }

    public String getF129() {
        return f129;
    }

    public void setF129(String f129) {
        this.f129 = f129;
    }

    public String getF130() {
        return f130;
    }

    public void setF130(String f130) {
        this.f130 = f130;
    }

    public String getF131() {
        return f131;
    }

    public void setF131(String f131) {
        this.f131 = f131;
    }

    public String getF132() {
        return f132;
    }

    public void setF132(String f132) {
        this.f132 = f132;
    }

    public String getF133() {
        return f133;
    }

    public void setF133(String f133) {
        this.f133 = f133;
    }

    public String getF134() {
        return f134;
    }

    public void setF134(String f134) {
        this.f134 = f134;
    }

    public String getF135() {
        return f135;
    }

    public void setF135(String f135) {
        this.f135 = f135;
    }

    public String getF136() {
        return f136;
    }

    public void setF136(String f136) {
        this.f136 = f136;
    }

    public String getF137() {
        return f137;
    }

    public void setF137(String f137) {
        this.f137 = f137;
    }

    public String getF138() {
        return f138;
    }

    public void setF138(String f138) {
        this.f138 = f138;
    }

    public String getF139() {
        return f139;
    }

    public void setF139(String f139) {
        this.f139 = f139;
    }

    public String getF140() {
        return f140;
    }

    public void setF140(String f140) {
        this.f140 = f140;
    }

    public String getF141() {
        return f141;
    }

    public void setF141(String f141) {
        this.f141 = f141;
    }

    public String getF142() {
        return f142;
    }

    public void setF142(String f142) {
        this.f142 = f142;
    }

    public String getF143() {
        return f143;
    }

    public void setF143(String f143) {
        this.f143 = f143;
    }

    public String getF144() {
        return f144;
    }

    public void setF144(String f144) {
        this.f144 = f144;
    }

    public String getF145() {
        return f145;
    }

    public void setF145(String f145) {
        this.f145 = f145;
    }

    public String getF146() {
        return f146;
    }

    public void setF146(String f146) {
        this.f146 = f146;
    }

    public String getF147() {
        return f147;
    }

    public void setF147(String f147) {
        this.f147 = f147;
    }

    public String getF148() {
        return f148;
    }

    public void setF148(String f148) {
        this.f148 = f148;
    }

    public String getF149() {
        return f149;
    }

    public void setF149(String f149) {
        this.f149 = f149;
    }

    public String getF150() {
        return f150;
    }

    public void setF150(String f150) {
        this.f150 = f150;
    }

    public String getF151() {
        return f151;
    }

    public void setF151(String f151) {
        this.f151 = f151;
    }

    public String getF152() {
        return f152;
    }

    public void setF152(String f152) {
        this.f152 = f152;
    }

    public String getF153() {
        return f153;
    }

    public void setF153(String f153) {
        this.f153 = f153;
    }

    public String getF154() {
        return f154;
    }

    public void setF154(String f154) {
        this.f154 = f154;
    }

    public String getF155() {
        return f155;
    }

    public void setF155(String f155) {
        this.f155 = f155;
    }

    public String getF156() {
        return f156;
    }

    public void setF156(String f156) {
        this.f156 = f156;
    }

    public String getF157() {
        return f157;
    }

    public void setF157(String f157) {
        this.f157 = f157;
    }

    public String getF158() {
        return f158;
    }

    public void setF158(String f158) {
        this.f158 = f158;
    }

    public String getF159() {
        return f159;
    }

    public void setF159(String f159) {
        this.f159 = f159;
    }

    public String getF160() {
        return f160;
    }

    public void setF160(String f160) {
        this.f160 = f160;
    }

    public String getF161() {
        return f161;
    }

    public void setF161(String f161) {
        this.f161 = f161;
    }

    public String getF162() {
        return f162;
    }

    public void setF162(String f162) {
        this.f162 = f162;
    }

    public String getF163() {
        return f163;
    }

    public void setF163(String f163) {
        this.f163 = f163;
    }

    public String getF164() {
        return f164;
    }

    public void setF164(String f164) {
        this.f164 = f164;
    }

    public String getF165() {
        return f165;
    }

    public void setF165(String f165) {
        this.f165 = f165;
    }

    public String getF166() {
        return f166;
    }

    public void setF166(String f166) {
        this.f166 = f166;
    }

    public String getF167() {
        return f167;
    }

    public void setF167(String f167) {
        this.f167 = f167;
    }

    public String getF168() {
        return f168;
    }

    public void setF168(String f168) {
        this.f168 = f168;
    }

    public String getF169() {
        return f169;
    }

    public void setF169(String f169) {
        this.f169 = f169;
    }

    public String getF170() {
        return f170;
    }

    public void setF170(String f170) {
        this.f170 = f170;
    }

    public String getF171() {
        return f171;
    }

    public void setF171(String f171) {
        this.f171 = f171;
    }

    public String getF172() {
        return f172;
    }

    public void setF172(String f172) {
        this.f172 = f172;
    }

    public String getF173() {
        return f173;
    }

    public void setF173(String f173) {
        this.f173 = f173;
    }

    public String getF174() {
        return f174;
    }

    public void setF174(String f174) {
        this.f174 = f174;
    }

    public String getF175() {
        return f175;
    }

    public void setF175(String f175) {
        this.f175 = f175;
    }

    public String getF176() {
        return f176;
    }

    public void setF176(String f176) {
        this.f176 = f176;
    }

    public String getF177() {
        return f177;
    }

    public void setF177(String f177) {
        this.f177 = f177;
    }

    public String getF178() {
        return f178;
    }

    public void setF178(String f178) {
        this.f178 = f178;
    }

    public String getF179() {
        return f179;
    }

    public void setF179(String f179) {
        this.f179 = f179;
    }

    public String getF180() {
        return f180;
    }

    public void setF180(String f180) {
        this.f180 = f180;
    }

    public String getF181() {
        return f181;
    }

    public void setF181(String f181) {
        this.f181 = f181;
    }

    public String getF182() {
        return f182;
    }

    public void setF182(String f182) {
        this.f182 = f182;
    }

    public String getF183() {
        return f183;
    }

    public void setF183(String f183) {
        this.f183 = f183;
    }

    public String getF184() {
        return f184;
    }

    public void setF184(String f184) {
        this.f184 = f184;
    }

    public String getF185() {
        return f185;
    }

    public void setF185(String f185) {
        this.f185 = f185;
    }

    public String getF186() {
        return f186;
    }

    public void setF186(String f186) {
        this.f186 = f186;
    }

    public String getF187() {
        return f187;
    }

    public void setF187(String f187) {
        this.f187 = f187;
    }

    public String getF188() {
        return f188;
    }

    public void setF188(String f188) {
        this.f188 = f188;
    }

    public String getF189() {
        return f189;
    }

    public void setF189(String f189) {
        this.f189 = f189;
    }

    public String getF190() {
        return f190;
    }

    public void setF190(String f190) {
        this.f190 = f190;
    }

    public String getF191() {
        return f191;
    }

    public void setF191(String f191) {
        this.f191 = f191;
    }

    public String getF192() {
        return f192;
    }

    public void setF192(String f192) {
        this.f192 = f192;
    }

    public String getF193() {
        return f193;
    }

    public void setF193(String f193) {
        this.f193 = f193;
    }

    public String getF194() {
        return f194;
    }

    public void setF194(String f194) {
        this.f194 = f194;
    }

    public String getF195() {
        return f195;
    }

    public void setF195(String f195) {
        this.f195 = f195;
    }

    public String getF196() {
        return f196;
    }

    public void setF196(String f196) {
        this.f196 = f196;
    }

    public String getF197() {
        return f197;
    }

    public void setF197(String f197) {
        this.f197 = f197;
    }

    public String getF198() {
        return f198;
    }

    public void setF198(String f198) {
        this.f198 = f198;
    }

    public String getF199() {
        return f199;
    }

    public void setF199(String f199) {
        this.f199 = f199;
    }

    public String getF200() {
        return f200;
    }

    public void setF200(String f200) {
        this.f200 = f200;
    }

    public String getF201() {
        return f201;
    }

    public void setF201(String f201) {
        this.f201 = f201;
    }

    public String getF202() {
        return f202;
    }

    public void setF202(String f202) {
        this.f202 = f202;
    }

    public String getF203() {
        return f203;
    }

    public void setF203(String f203) {
        this.f203 = f203;
    }

    public String getF204() {
        return f204;
    }

    public void setF204(String f204) {
        this.f204 = f204;
    }

    public String getF205() {
        return f205;
    }

    public void setF205(String f205) {
        this.f205 = f205;
    }

    public String getF206() {
        return f206;
    }

    public void setF206(String f206) {
        this.f206 = f206;
    }

    public String getF207() {
        return f207;
    }

    public void setF207(String f207) {
        this.f207 = f207;
    }

    public String getF208() {
        return f208;
    }

    public void setF208(String f208) {
        this.f208 = f208;
    }

    public String getF209() {
        return f209;
    }

    public void setF209(String f209) {
        this.f209 = f209;
    }

    public String getF210() {
        return f210;
    }

    public void setF210(String f210) {
        this.f210 = f210;
    }

    public String getF211() {
        return f211;
    }

    public void setF211(String f211) {
        this.f211 = f211;
    }

    public String getF212() {
        return f212;
    }

    public void setF212(String f212) {
        this.f212 = f212;
    }

    public String getF213() {
        return f213;
    }

    public void setF213(String f213) {
        this.f213 = f213;
    }

    public String getF214() {
        return f214;
    }

    public void setF214(String f214) {
        this.f214 = f214;
    }

    public String getF215() {
        return f215;
    }

    public void setF215(String f215) {
        this.f215 = f215;
    }

    public String getF216() {
        return f216;
    }

    public void setF216(String f216) {
        this.f216 = f216;
    }

    public String getF217() {
        return f217;
    }

    public void setF217(String f217) {
        this.f217 = f217;
    }

    public String getF218() {
        return f218;
    }

    public void setF218(String f218) {
        this.f218 = f218;
    }

    public String getF219() {
        return f219;
    }

    public void setF219(String f219) {
        this.f219 = f219;
    }

    public String getF220() {
        return f220;
    }

    public void setF220(String f220) {
        this.f220 = f220;
    }

    public String getF221() {
        return f221;
    }

    public void setF221(String f221) {
        this.f221 = f221;
    }

    public String getF222() {
        return f222;
    }

    public void setF222(String f222) {
        this.f222 = f222;
    }

    public String getF223() {
        return f223;
    }

    public void setF223(String f223) {
        this.f223 = f223;
    }

    public String getF224() {
        return f224;
    }

    public void setF224(String f224) {
        this.f224 = f224;
    }

    public String getF225() {
        return f225;
    }

    public void setF225(String f225) {
        this.f225 = f225;
    }

    public String getF226() {
        return f226;
    }

    public void setF226(String f226) {
        this.f226 = f226;
    }

    public String getF227() {
        return f227;
    }

    public void setF227(String f227) {
        this.f227 = f227;
    }

    public String getF228() {
        return f228;
    }

    public void setF228(String f228) {
        this.f228 = f228;
    }

    public String getF229() {
        return f229;
    }

    public void setF229(String f229) {
        this.f229 = f229;
    }

    public String getF230() {
        return f230;
    }

    public void setF230(String f230) {
        this.f230 = f230;
    }

    public String getF231() {
        return f231;
    }

    public void setF231(String f231) {
        this.f231 = f231;
    }

    public String getF232() {
        return f232;
    }

    public void setF232(String f232) {
        this.f232 = f232;
    }

    public String getF233() {
        return f233;
    }

    public void setF233(String f233) {
        this.f233 = f233;
    }

    public String getF234() {
        return f234;
    }

    public void setF234(String f234) {
        this.f234 = f234;
    }

    public String getF235() {
        return f235;
    }

    public void setF235(String f235) {
        this.f235 = f235;
    }

    public String getF236() {
        return f236;
    }

    public void setF236(String f236) {
        this.f236 = f236;
    }

    public String getF237() {
        return f237;
    }

    public void setF237(String f237) {
        this.f237 = f237;
    }

    public String getF238() {
        return f238;
    }

    public void setF238(String f238) {
        this.f238 = f238;
    }

    public String getF239() {
        return f239;
    }

    public void setF239(String f239) {
        this.f239 = f239;
    }

    public String getF240() {
        return f240;
    }

    public void setF240(String f240) {
        this.f240 = f240;
    }

    public String getF241() {
        return f241;
    }

    public void setF241(String f241) {
        this.f241 = f241;
    }

    public String getF242() {
        return f242;
    }

    public void setF242(String f242) {
        this.f242 = f242;
    }

    public String getF243() {
        return f243;
    }

    public void setF243(String f243) {
        this.f243 = f243;
    }

    public String getF244() {
        return f244;
    }

    public void setF244(String f244) {
        this.f244 = f244;
    }

    public String getF245() {
        return f245;
    }

    public void setF245(String f245) {
        this.f245 = f245;
    }

    public String getF246() {
        return f246;
    }

    public void setF246(String f246) {
        this.f246 = f246;
    }

    public String getF247() {
        return f247;
    }

    public void setF247(String f247) {
        this.f247 = f247;
    }

    public String getF248() {
        return f248;
    }

    public void setF248(String f248) {
        this.f248 = f248;
    }

    public String getF249() {
        return f249;
    }

    public void setF249(String f249) {
        this.f249 = f249;
    }

    public String getF250() {
        return f250;
    }

    public void setF250(String f250) {
        this.f250 = f250;
    }

    public String getF251() {
        return f251;
    }

    public void setF251(String f251) {
        this.f251 = f251;
    }

    public String getF252() {
        return f252;
    }

    public void setF252(String f252) {
        this.f252 = f252;
    }

    public String getF253() {
        return f253;
    }

    public void setF253(String f253) {
        this.f253 = f253;
    }

    public String getF254() {
        return f254;
    }

    public void setF254(String f254) {
        this.f254 = f254;
    }

    public String getF255() {
        return f255;
    }

    public void setF255(String f255) {
        this.f255 = f255;
    }
}
