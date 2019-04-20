/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.model;

import com.ut.healthelink.validator.NoHtml;
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
    
    @Column(name = "batchId", nullable = false)
    private int batchId;

    @Column(name = "TRANSACTIONINID", nullable = false)
    private int transactionInId;

    @NoHtml
    @Column(name = "F1", nullable = true)
    private String f1 = null;

    @NoHtml
    @Column(name = "F2", nullable = true)
    private String f2 = null;

    @NoHtml
    @Column(name = "F3", nullable = true)
    private String f3 = null;

    @NoHtml
    @Column(name = "F4", nullable = true)
    private String f4 = null;

    @NoHtml
    @Column(name = "F5", nullable = true)
    private String f5 = null;

    @NoHtml
    @Column(name = "F6", nullable = true)
    private String f6 = null;

    @NoHtml
    @Column(name = "F7", nullable = true)
    private String f7 = null;

    @NoHtml
    @Column(name = "F8", nullable = true)
    private String f8 = null;

    @NoHtml
    @Column(name = "F9", nullable = true)
    private String f9 = null;

    @NoHtml
    @Column(name = "F10", nullable = true)
    private String f10 = null;

    @NoHtml
    @Column(name = "F11", nullable = true)
    private String f11 = null;

    @NoHtml
    @Column(name = "F12", nullable = true)
    private String f12 = null;

    @NoHtml
    @Column(name = "F13", nullable = true)
    private String f13 = null;

    @NoHtml
    @Column(name = "F14", nullable = true)
    private String f14 = null;

    @NoHtml
    @Column(name = "F15", nullable = true)
    private String f15 = null;

    @NoHtml
    @Column(name = "F16", nullable = true)
    private String f16 = null;

    @NoHtml
    @Column(name = "F17", nullable = true)
    private String f17 = null;

    @NoHtml
    @Column(name = "F18", nullable = true)
    private String f18 = null;

    @NoHtml
    @Column(name = "F19", nullable = true)
    private String f19 = null;

    @NoHtml
    @Column(name = "F20", nullable = true)
    private String f20 = null;

    @NoHtml
    @Column(name = "F21", nullable = true)
    private String f21 = null;

    @NoHtml
    @Column(name = "F22", nullable = true)
    private String f22 = null;

    @NoHtml
    @Column(name = "F23", nullable = true)
    private String f23 = null;

    @NoHtml
    @Column(name = "F24", nullable = true)
    private String f24 = null;

    @NoHtml
    @Column(name = "F25", nullable = true)
    private String f25 = null;

    @NoHtml
    @Column(name = "F26", nullable = true)
    private String f26 = null;

    @NoHtml
    @Column(name = "F27", nullable = true)
    private String f27 = null;

    @NoHtml
    @Column(name = "F28", nullable = true)
    private String f28 = null;

    @NoHtml
    @Column(name = "F29", nullable = true)
    private String f29 = null;

    @NoHtml
    @Column(name = "F30", nullable = true)
    private String f30 = null;

    @NoHtml
    @Column(name = "F31", nullable = true)
    private String f31 = null;

    @NoHtml
    @Column(name = "F32", nullable = true)
    private String f32 = null;

    @NoHtml
    @Column(name = "F33", nullable = true)
    private String f33 = null;

    @NoHtml
    @Column(name = "F34", nullable = true)
    private String f34 = null;

    @NoHtml
    @Column(name = "F35", nullable = true)
    private String f35 = null;

    @NoHtml
    @Column(name = "F36", nullable = true)
    private String f36 = null;

    @NoHtml
    @Column(name = "F37", nullable = true)
    private String f37 = null;

    @NoHtml
    @Column(name = "F38", nullable = true)
    private String f38 = null;

    @NoHtml
    @Column(name = "F39", nullable = true)
    private String f39 = null;

    @NoHtml
    @Column(name = "F40", nullable = true)
    private String f40 = null;

    @NoHtml
    @Column(name = "F41", nullable = true)
    private String f41 = null;

    @NoHtml
    @Column(name = "F42", nullable = true)
    private String f42 = null;

    @NoHtml
    @Column(name = "F43", nullable = true)
    private String f43 = null;

    @NoHtml
    @Column(name = "F44", nullable = true)
    private String f44 = null;

    @NoHtml
    @Column(name = "F45", nullable = true)
    private String f45 = null;

    @NoHtml
    @Column(name = "F46", nullable = true)
    private String f46 = null;

    @NoHtml
    @Column(name = "F47", nullable = true)
    private String f47 = null;

    @NoHtml
    @Column(name = "F48", nullable = true)
    private String f48 = null;

    @NoHtml
    @Column(name = "F49", nullable = true)
    private String f49 = null;

    @NoHtml
    @Column(name = "F50", nullable = true)
    private String f50 = null;

    @NoHtml
    @Column(name = "F51", nullable = true)
    private String f51 = null;

    @NoHtml
    @Column(name = "F52", nullable = true)
    private String f52 = null;

    @NoHtml
    @Column(name = "F53", nullable = true)
    private String f53 = null;

    @NoHtml
    @Column(name = "F54", nullable = true)
    private String f54 = null;

    @NoHtml
    @Column(name = "F55", nullable = true)
    private String f55 = null;

    @NoHtml
    @Column(name = "F56", nullable = true)
    private String f56 = null;

    @NoHtml
    @Column(name = "F57", nullable = true)
    private String f57 = null;

    @NoHtml
    @Column(name = "F58", nullable = true)
    private String f58 = null;

    @NoHtml
    @Column(name = "F59", nullable = true)
    private String f59 = null;

    @NoHtml
    @Column(name = "F60", nullable = true)
    private String f60 = null;

    @NoHtml
    @Column(name = "F61", nullable = true)
    private String f61 = null;

    @NoHtml
    @Column(name = "F62", nullable = true)
    private String f62 = null;

    @NoHtml
    @Column(name = "F63", nullable = true)
    private String f63 = null;

    @NoHtml
    @Column(name = "F64", nullable = true)
    private String f64 = null;

    @NoHtml
    @Column(name = "F65", nullable = true)
    private String f65 = null;

    @NoHtml
    @Column(name = "F66", nullable = true)
    private String f66 = null;

    @NoHtml
    @Column(name = "F67", nullable = true)
    private String f67 = null;

    @NoHtml
    @Column(name = "F68", nullable = true)
    private String f68 = null;

    @NoHtml
    @Column(name = "F69", nullable = true)
    private String f69 = null;

    @NoHtml
    @Column(name = "F70", nullable = true)
    private String f70 = null;

    @NoHtml
    @Column(name = "F71", nullable = true)
    private String f71 = null;

    @NoHtml
    @Column(name = "F72", nullable = true)
    private String f72 = null;

    @NoHtml
    @Column(name = "F73", nullable = true)
    private String f73 = null;

    @NoHtml
    @Column(name = "F74", nullable = true)
    private String f74 = null;

    @NoHtml
    @Column(name = "F75", nullable = true)
    private String f75 = null;

    @NoHtml
    @Column(name = "F76", nullable = true)
    private String f76 = null;

    @NoHtml
    @Column(name = "F77", nullable = true)
    private String f77 = null;

    @NoHtml
    @Column(name = "F78", nullable = true)
    private String f78 = null;

    @NoHtml
    @Column(name = "F79", nullable = true)
    private String f79 = null;

    @NoHtml
    @Column(name = "F80", nullable = true)
    private String f80 = null;

    @NoHtml
    @Column(name = "F81", nullable = true)
    private String f81 = null;

    @NoHtml
    @Column(name = "F82", nullable = true)
    private String f82 = null;

    @NoHtml
    @Column(name = "F83", nullable = true)
    private String f83 = null;

    @NoHtml
    @Column(name = "F84", nullable = true)
    private String f84 = null;

    @NoHtml
    @Column(name = "F85", nullable = true)
    private String f85 = null;

    @NoHtml
    @Column(name = "F86", nullable = true)
    private String f86 = null;

    @NoHtml
    @Column(name = "F87", nullable = true)
    private String f87 = null;

    @NoHtml
    @Column(name = "F88", nullable = true)
    private String f88 = null;

    @NoHtml
    @Column(name = "F89", nullable = true)
    private String f89 = null;

    @NoHtml
    @Column(name = "F90", nullable = true)
    private String f90 = null;

    @NoHtml
    @Column(name = "F91", nullable = true)
    private String f91 = null;

    @NoHtml
    @Column(name = "F92", nullable = true)
    private String f92 = null;

    @NoHtml
    @Column(name = "F93", nullable = true)
    private String f93 = null;

    @NoHtml
    @Column(name = "F94", nullable = true)
    private String f94 = null;

    @NoHtml
    @Column(name = "F95", nullable = true)
    private String f95 = null;

    @NoHtml
    @Column(name = "F96", nullable = true)
    private String f96 = null;

    @NoHtml
    @Column(name = "F97", nullable = true)
    private String f97 = null;

    @NoHtml
    @Column(name = "F98", nullable = true)
    private String f98 = null;

    @NoHtml
    @Column(name = "F99", nullable = true)
    private String f99 = null;

    @NoHtml
    @Column(name = "F100", nullable = true)
    private String f100 = null;

    @NoHtml
    @Column(name = "F101", nullable = true)
    private String f101 = null;

    @NoHtml
    @Column(name = "F102", nullable = true)
    private String f102 = null;

    @NoHtml
    @Column(name = "F103", nullable = true)
    private String f103 = null;

    @NoHtml
    @Column(name = "F104", nullable = true)
    private String f104 = null;

    @NoHtml
    @Column(name = "F105", nullable = true)
    private String f105 = null;

    @NoHtml
    @Column(name = "F106", nullable = true)
    private String f106 = null;

    @NoHtml
    @Column(name = "F107", nullable = true)
    private String f107 = null;

    @NoHtml
    @Column(name = "F108", nullable = true)
    private String f108 = null;

    @NoHtml
    @Column(name = "F109", nullable = true)
    private String f109 = null;

    @NoHtml
    @Column(name = "F110", nullable = true)
    private String f110 = null;

    @NoHtml
    @Column(name = "F111", nullable = true)
    private String f111 = null;

    @NoHtml
    @Column(name = "F112", nullable = true)
    private String f112 = null;

    @NoHtml
    @Column(name = "F113", nullable = true)
    private String f113 = null;

    @NoHtml
    @Column(name = "F114", nullable = true)
    private String f114 = null;

    @NoHtml
    @Column(name = "F115", nullable = true)
    private String f115 = null;

    @NoHtml
    @Column(name = "F116", nullable = true)
    private String f116 = null;

    @NoHtml
    @Column(name = "F117", nullable = true)
    private String f117 = null;

    @NoHtml
    @Column(name = "F118", nullable = true)
    private String f118 = null;

    @NoHtml
    @Column(name = "F119", nullable = true)
    private String f119 = null;

    @NoHtml
    @Column(name = "F120", nullable = true)
    private String f120 = null;

    @NoHtml
    @Column(name = "F121", nullable = true)
    private String f121 = null;

    @NoHtml
    @Column(name = "F122", nullable = true)
    private String f122 = null;

    @NoHtml
    @Column(name = "F123", nullable = true)
    private String f123 = null;

    @NoHtml
    @Column(name = "F124", nullable = true)
    private String f124 = null;

    @NoHtml
    @Column(name = "F125", nullable = true)
    private String f125 = null;

    @NoHtml
    @Column(name = "F126", nullable = true)
    private String f126 = null;

    @NoHtml
    @Column(name = "F127", nullable = true)
    private String f127 = null;

    @NoHtml
    @Column(name = "F128", nullable = true)
    private String f128 = null;

    @NoHtml
    @Column(name = "F129", nullable = true)
    private String f129 = null;

    @NoHtml
    @Column(name = "F130", nullable = true)
    private String f130 = null;

    @NoHtml
    @Column(name = "F131", nullable = true)
    private String f131 = null;

    @NoHtml
    @Column(name = "F132", nullable = true)
    private String f132 = null;

    @NoHtml
    @Column(name = "F133", nullable = true)
    private String f133 = null;

    @NoHtml
    @Column(name = "F134", nullable = true)
    private String f134 = null;

    @NoHtml
    @Column(name = "F135", nullable = true)
    private String f135 = null;

    @NoHtml
    @Column(name = "F136", nullable = true)
    private String f136 = null;

    @NoHtml
    @Column(name = "F137", nullable = true)
    private String f137 = null;

    @NoHtml
    @Column(name = "F138", nullable = true)
    private String f138 = null;

    @NoHtml
    @Column(name = "F139", nullable = true)
    private String f139 = null;

    @NoHtml
    @Column(name = "F140", nullable = true)
    private String f140 = null;

    @NoHtml
    @Column(name = "F141", nullable = true)
    private String f141 = null;

    @NoHtml
    @Column(name = "F142", nullable = true)
    private String f142 = null;

    @NoHtml
    @Column(name = "F143", nullable = true)
    private String f143 = null;

    @NoHtml
    @Column(name = "F144", nullable = true)
    private String f144 = null;

    @NoHtml
    @Column(name = "F145", nullable = true)
    private String f145 = null;

    @NoHtml
    @Column(name = "F146", nullable = true)
    private String f146 = null;

    @NoHtml
    @Column(name = "F147", nullable = true)
    private String f147 = null;

    @NoHtml
    @Column(name = "F148", nullable = true)
    private String f148 = null;

    @NoHtml
    @Column(name = "F149", nullable = true)
    private String f149 = null;

    @NoHtml
    @Column(name = "F150", nullable = true)
    private String f150 = null;

    @NoHtml
    @Column(name = "F151", nullable = true)
    private String f151 = null;

    @NoHtml
    @Column(name = "F152", nullable = true)
    private String f152 = null;

    @NoHtml
    @Column(name = "F153", nullable = true)
    private String f153 = null;

    @NoHtml
    @Column(name = "F154", nullable = true)
    private String f154 = null;

    @NoHtml
    @Column(name = "F155", nullable = true)
    private String f155 = null;

    @NoHtml
    @Column(name = "F156", nullable = true)
    private String f156 = null;

    @NoHtml
    @Column(name = "F157", nullable = true)
    private String f157 = null;

    @NoHtml
    @Column(name = "F158", nullable = true)
    private String f158 = null;

    @NoHtml
    @Column(name = "F159", nullable = true)
    private String f159 = null;

    @NoHtml
    @Column(name = "F160", nullable = true)
    private String f160 = null;

    @NoHtml
    @Column(name = "F161", nullable = true)
    private String f161 = null;

    @NoHtml
    @Column(name = "F162", nullable = true)
    private String f162 = null;

    @NoHtml
    @Column(name = "F163", nullable = true)
    private String f163 = null;

    @NoHtml
    @Column(name = "F164", nullable = true)
    private String f164 = null;

    @NoHtml
    @Column(name = "F165", nullable = true)
    private String f165 = null;

    @NoHtml
    @Column(name = "F166", nullable = true)
    private String f166 = null;

    @NoHtml
    @Column(name = "F167", nullable = true)
    private String f167 = null;

    @NoHtml
    @Column(name = "F168", nullable = true)
    private String f168 = null;

    @NoHtml
    @Column(name = "F169", nullable = true)
    private String f169 = null;

    @NoHtml
    @Column(name = "F170", nullable = true)
    private String f170 = null;

    @NoHtml
    @Column(name = "F171", nullable = true)
    private String f171 = null;

    @NoHtml
    @Column(name = "F172", nullable = true)
    private String f172 = null;

    @NoHtml
    @Column(name = "F173", nullable = true)
    private String f173 = null;

    @NoHtml
    @Column(name = "F174", nullable = true)
    private String f174 = null;

    @NoHtml
    @Column(name = "F175", nullable = true)
    private String f175 = null;

    @NoHtml
    @Column(name = "F176", nullable = true)
    private String f176 = null;

    @NoHtml
    @Column(name = "F177", nullable = true)
    private String f177 = null;

    @NoHtml
    @Column(name = "F178", nullable = true)
    private String f178 = null;

    @NoHtml
    @Column(name = "F179", nullable = true)
    private String f179 = null;

    @NoHtml
    @Column(name = "F180", nullable = true)
    private String f180 = null;

    @NoHtml
    @Column(name = "F181", nullable = true)
    private String f181 = null;

    @NoHtml
    @Column(name = "F182", nullable = true)
    private String f182 = null;

    @NoHtml
    @Column(name = "F183", nullable = true)
    private String f183 = null;

    @NoHtml
    @Column(name = "F184", nullable = true)
    private String f184 = null;

    @NoHtml
    @Column(name = "F185", nullable = true)
    private String f185 = null;

    @NoHtml
    @Column(name = "F186", nullable = true)
    private String f186 = null;

    @NoHtml
    @Column(name = "F187", nullable = true)
    private String f187 = null;

    @NoHtml
    @Column(name = "F188", nullable = true)
    private String f188 = null;

    @NoHtml
    @Column(name = "F189", nullable = true)
    private String f189 = null;

    @NoHtml
    @Column(name = "F190", nullable = true)
    private String f190 = null;

    @NoHtml
    @Column(name = "F191", nullable = true)
    private String f191 = null;

    @NoHtml
    @Column(name = "F192", nullable = true)
    private String f192 = null;

    @NoHtml
    @Column(name = "F193", nullable = true)
    private String f193 = null;

    @NoHtml
    @Column(name = "F194", nullable = true)
    private String f194 = null;

    @NoHtml
    @Column(name = "F195", nullable = true)
    private String f195 = null;

    @NoHtml
    @Column(name = "F196", nullable = true)
    private String f196 = null;

    @NoHtml
    @Column(name = "F197", nullable = true)
    private String f197 = null;

    @NoHtml
    @Column(name = "F198", nullable = true)
    private String f198 = null;

    @NoHtml
    @Column(name = "F199", nullable = true)
    private String f199 = null;

    @NoHtml
    @Column(name = "F200", nullable = true)
    private String f200 = null;

    @NoHtml
    @Column(name = "F201", nullable = true)
    private String f201 = null;

    @NoHtml
    @Column(name = "F202", nullable = true)
    private String f202 = null;

    @NoHtml
    @Column(name = "F203", nullable = true)
    private String f203 = null;

    @NoHtml
    @Column(name = "F204", nullable = true)
    private String f204 = null;

    @NoHtml
    @Column(name = "F205", nullable = true)
    private String f205 = null;

    @NoHtml
    @Column(name = "F206", nullable = true)
    private String f206 = null;

    @NoHtml
    @Column(name = "F207", nullable = true)
    private String f207 = null;

    @NoHtml
    @Column(name = "F208", nullable = true)
    private String f208 = null;

    @NoHtml
    @Column(name = "F209", nullable = true)
    private String f209 = null;

    @NoHtml
    @Column(name = "F210", nullable = true)
    private String f210 = null;

    @NoHtml
    @Column(name = "F211", nullable = true)
    private String f211 = null;

    @NoHtml
    @Column(name = "F212", nullable = true)
    private String f212 = null;

    @NoHtml
    @Column(name = "F213", nullable = true)
    private String f213 = null;

    @NoHtml
    @Column(name = "F214", nullable = true)
    private String f214 = null;

    @NoHtml
    @Column(name = "F215", nullable = true)
    private String f215 = null;

    @NoHtml
    @Column(name = "F216", nullable = true)
    private String f216 = null;

    @NoHtml
    @Column(name = "F217", nullable = true)
    private String f217 = null;

    @NoHtml
    @Column(name = "F218", nullable = true)
    private String f218 = null;

    @NoHtml
    @Column(name = "F219", nullable = true)
    private String f219 = null;

    @NoHtml
    @Column(name = "F220", nullable = true)
    private String f220 = null;

    @NoHtml
    @Column(name = "F221", nullable = true)
    private String f221 = null;

    @NoHtml
    @Column(name = "F222", nullable = true)
    private String f222 = null;

    @NoHtml
    @Column(name = "F223", nullable = true)
    private String f223 = null;

    @NoHtml
    @Column(name = "F224", nullable = true)
    private String f224 = null;

    @NoHtml
    @Column(name = "F225", nullable = true)
    private String f225 = null;

    @NoHtml
    @Column(name = "F226", nullable = true)
    private String f226 = null;

    @NoHtml
    @Column(name = "F227", nullable = true)
    private String f227 = null;

    @NoHtml
    @Column(name = "F228", nullable = true)
    private String f228 = null;

    @NoHtml
    @Column(name = "F229", nullable = true)
    private String f229 = null;

    @NoHtml
    @Column(name = "F230", nullable = true)
    private String f230 = null;

    @NoHtml
    @Column(name = "F231", nullable = true)
    private String f231 = null;

    @NoHtml
    @Column(name = "F232", nullable = true)
    private String f232 = null;

    @NoHtml
    @Column(name = "F233", nullable = true)
    private String f233 = null;

    @NoHtml
    @Column(name = "F234", nullable = true)
    private String f234 = null;

    @NoHtml
    @Column(name = "F235", nullable = true)
    private String f235 = null;

    @NoHtml
    @Column(name = "F236", nullable = true)
    private String f236 = null;

    @NoHtml
    @Column(name = "F237", nullable = true)
    private String f237 = null;

    @NoHtml
    @Column(name = "F238", nullable = true)
    private String f238 = null;

    @NoHtml
    @Column(name = "F239", nullable = true)
    private String f239 = null;

    @NoHtml
    @Column(name = "F240", nullable = true)
    private String f240 = null;

    @NoHtml
    @Column(name = "F241", nullable = true)
    private String f241 = null;

    @NoHtml
    @Column(name = "F242", nullable = true)
    private String f242 = null;

    @NoHtml
    @Column(name = "F243", nullable = true)
    private String f243 = null;

    @NoHtml
    @Column(name = "F244", nullable = true)
    private String f244 = null;

    @NoHtml
    @Column(name = "F245", nullable = true)
    private String f245 = null;

    @NoHtml
    @Column(name = "F246", nullable = true)
    private String f246 = null;

    @NoHtml
    @Column(name = "F247", nullable = true)
    private String f247 = null;

    @NoHtml
    @Column(name = "F248", nullable = true)
    private String f248 = null;

    @NoHtml
    @Column(name = "F249", nullable = true)
    private String f249 = null;

    @NoHtml
    @Column(name = "F250", nullable = true)
    private String f250 = null;

    @NoHtml
    @Column(name = "F251", nullable = true)
    private String f251 = null;

    @NoHtml
    @Column(name = "F252", nullable = true)
    private String f252 = null;

    @NoHtml
    @Column(name = "F253", nullable = true)
    private String f253 = null;

    @NoHtml
    @Column(name = "F254", nullable = true)
    private String f254 = null;

    @NoHtml
    @Column(name = "F255", nullable = true)
    private String f255 = null;
    
    @NoHtml
    @Column(name = "F256", nullable = true)
    private String f256 = null;

    @NoHtml
    @Column(name = "F257", nullable = true)
    private String f257 = null;

    @NoHtml
    @Column(name = "F258", nullable = true)
    private String f258 = null;

    @NoHtml
    @Column(name = "F259", nullable = true)
    private String f259 = null;

    @NoHtml
    @Column(name = "F260", nullable = true)
    private String f260 = null;

    @NoHtml
    @Column(name = "F261", nullable = true)
    private String f261 = null;

    @NoHtml
    @Column(name = "F262", nullable = true)
    private String f262 = null;

    @NoHtml
    @Column(name = "F263", nullable = true)
    private String f263 = null;

    @NoHtml
    @Column(name = "F264", nullable = true)
    private String f264 = null;

    @NoHtml
    @Column(name = "F265", nullable = true)
    private String f265 = null;

    @NoHtml
    @Column(name = "F266", nullable = true)
    private String f266 = null;

    @NoHtml
    @Column(name = "F267", nullable = true)
    private String f267 = null;

    @NoHtml
    @Column(name = "F268", nullable = true)
    private String f268 = null;

    @NoHtml
    @Column(name = "F269", nullable = true)
    private String f269 = null;

    @NoHtml
    @Column(name = "F270", nullable = true)
    private String f270 = null;

    @NoHtml
    @Column(name = "F271", nullable = true)
    private String f271 = null;

    @NoHtml
    @Column(name = "F272", nullable = true)
    private String f272 = null;

    @NoHtml
    @Column(name = "F273", nullable = true)
    private String f273 = null;

    @NoHtml
    @Column(name = "F274", nullable = true)
    private String f274 = null;

    @NoHtml
    @Column(name = "F275", nullable = true)
    private String f275 = null;

    @NoHtml
    @Column(name = "F276", nullable = true)
    private String f276 = null;

    @NoHtml
    @Column(name = "F277", nullable = true)
    private String f277 = null;

    @NoHtml
    @Column(name = "F278", nullable = true)
    private String f278 = null;

    @NoHtml
    @Column(name = "F279", nullable = true)
    private String f279 = null;

    @NoHtml
    @Column(name = "F280", nullable = true)
    private String f280 = null;

    @NoHtml
    @Column(name = "F281", nullable = true)
    private String f281 = null;

    @NoHtml
    @Column(name = "F282", nullable = true)
    private String f282 = null;

    @NoHtml
    @Column(name = "F283", nullable = true)
    private String f283 = null;

    @NoHtml
    @Column(name = "F284", nullable = true)
    private String f284 = null;

    @NoHtml
    @Column(name = "F285", nullable = true)
    private String f285 = null;

    @NoHtml
    @Column(name = "F286", nullable = true)
    private String f286 = null;

    @NoHtml
    @Column(name = "F287", nullable = true)
    private String f287 = null;

    @NoHtml
    @Column(name = "F288", nullable = true)
    private String f288 = null;

    @NoHtml
    @Column(name = "F289", nullable = true)
    private String f289 = null;

    @NoHtml
    @Column(name = "F290", nullable = true)
    private String f290 = null;

    @NoHtml
    @Column(name = "F291", nullable = true)
    private String f291 = null;

    @NoHtml
    @Column(name = "F292", nullable = true)
    private String f292 = null;

    @NoHtml
    @Column(name = "F293", nullable = true)
    private String f293 = null;

    @NoHtml
    @Column(name = "F294", nullable = true)
    private String f294 = null;

    @NoHtml
    @Column(name = "F295", nullable = true)
    private String f295 = null;

    @NoHtml
    @Column(name = "F296", nullable = true)
    private String f296 = null;

    @NoHtml
    @Column(name = "F297", nullable = true)
    private String f297 = null;

    @NoHtml
    @Column(name = "F298", nullable = true)
    private String f298 = null;

    @NoHtml
    @Column(name = "F299", nullable = true)
    private String f299 = null;

    @NoHtml
    @Column(name = "F300", nullable = true)
    private String f300 = null;
    
    @NoHtml
    @Column(name = "F301", nullable = true)
    private String f301 = null;

    @NoHtml
    @Column(name = "F302", nullable = true)
    private String f302 = null;

    @NoHtml
    @Column(name = "F303", nullable = true)
    private String f303 = null;

    @NoHtml
    @Column(name = "F304", nullable = true)
    private String f304 = null;

    @NoHtml
    @Column(name = "F305", nullable = true)
    private String f305 = null;

    @NoHtml
    @Column(name = "F306", nullable = true)
    private String f306 = null;

    @NoHtml
    @Column(name = "F307", nullable = true)
    private String f307 = null;

    @NoHtml
    @Column(name = "F308", nullable = true)
    private String f308 = null;

    @NoHtml
    @Column(name = "F309", nullable = true)
    private String f309 = null;

    @NoHtml
    @Column(name = "F310", nullable = true)
    private String f310 = null;

    @NoHtml
    @Column(name = "F311", nullable = true)
    private String f311 = null;

    @NoHtml
    @Column(name = "F312", nullable = true)
    private String f312 = null;

    @NoHtml
    @Column(name = "F313", nullable = true)
    private String f313 = null;

    @NoHtml
    @Column(name = "F314", nullable = true)
    private String f314 = null;

    @NoHtml
    @Column(name = "F315", nullable = true)
    private String f315 = null;

    @NoHtml
    @Column(name = "F316", nullable = true)
    private String f316 = null;

    @NoHtml
    @Column(name = "F317", nullable = true)
    private String f317 = null;

    @NoHtml
    @Column(name = "F318", nullable = true)
    private String f318 = null;

    @NoHtml
    @Column(name = "F319", nullable = true)
    private String f319 = null;

    @NoHtml
    @Column(name = "F320", nullable = true)
    private String f320 = null;

    @NoHtml
    @Column(name = "F321", nullable = true)
    private String f321 = null;

    @NoHtml
    @Column(name = "F322", nullable = true)
    private String f322 = null;

    @NoHtml
    @Column(name = "F323", nullable = true)
    private String f323 = null;

    @NoHtml
    @Column(name = "F324", nullable = true)
    private String f324 = null;

    @NoHtml
    @Column(name = "F325", nullable = true)
    private String f325 = null;

    @NoHtml
    @Column(name = "F326", nullable = true)
    private String f326 = null;

    @NoHtml
    @Column(name = "F327", nullable = true)
    private String f327 = null;

    @NoHtml
    @Column(name = "F328", nullable = true)
    private String f328 = null;

    @NoHtml
    @Column(name = "F329", nullable = true)
    private String f329 = null;

    @NoHtml
    @Column(name = "F330", nullable = true)
    private String f330 = null;

    @NoHtml
    @Column(name = "F331", nullable = true)
    private String f331 = null;

    @NoHtml
    @Column(name = "F332", nullable = true)
    private String f332 = null;

    @NoHtml
    @Column(name = "F333", nullable = true)
    private String f333 = null;

    @NoHtml
    @Column(name = "F334", nullable = true)
    private String f334 = null;

    @NoHtml
    @Column(name = "F335", nullable = true)
    private String f335 = null;

    @NoHtml
    @Column(name = "F336", nullable = true)
    private String f336 = null;

    @NoHtml
    @Column(name = "F337", nullable = true)
    private String f337 = null;

    @NoHtml
    @Column(name = "F338", nullable = true)
    private String f338 = null;

    @NoHtml
    @Column(name = "F339", nullable = true)
    private String f339 = null;

    @NoHtml
    @Column(name = "F340", nullable = true)
    private String f340 = null;

    @NoHtml
    @Column(name = "F341", nullable = true)
    private String f341 = null;

    @NoHtml
    @Column(name = "F342", nullable = true)
    private String f342 = null;

    @NoHtml
    @Column(name = "F343", nullable = true)
    private String f343 = null;

    @NoHtml
    @Column(name = "F344", nullable = true)
    private String f344 = null;

    @NoHtml
    @Column(name = "F345", nullable = true)
    private String f345 = null;

    @NoHtml
    @Column(name = "F346", nullable = true)
    private String f346 = null;

    @NoHtml
    @Column(name = "F347", nullable = true)
    private String f347 = null;

    @NoHtml
    @Column(name = "F348", nullable = true)
    private String f348 = null;

    @NoHtml
    @Column(name = "F349", nullable = true)
    private String f349 = null;

    @NoHtml
    @Column(name = "F350", nullable = true)
    private String f350 = null;

    @NoHtml
    @Column(name = "F351", nullable = true)
    private String f351 = null;

    @NoHtml
    @Column(name = "F352", nullable = true)
    private String f352 = null;

    @NoHtml
    @Column(name = "F353", nullable = true)
    private String f353 = null;

    @NoHtml
    @Column(name = "F354", nullable = true)
    private String f354 = null;

    @NoHtml
    @Column(name = "F355", nullable = true)
    private String f355 = null;

    @NoHtml
    @Column(name = "F356", nullable = true)
    private String f356 = null;

    @NoHtml
    @Column(name = "F357", nullable = true)
    private String f357 = null;

    @NoHtml
    @Column(name = "F358", nullable = true)
    private String f358 = null;

    @NoHtml
    @Column(name = "F359", nullable = true)
    private String f359 = null;

    @NoHtml
    @Column(name = "F360", nullable = true)
    private String f360 = null;

    @NoHtml
    @Column(name = "F361", nullable = true)
    private String f361 = null;

    @NoHtml
    @Column(name = "F362", nullable = true)
    private String f362 = null;

    @NoHtml
    @Column(name = "F363", nullable = true)
    private String f363 = null;

    @NoHtml
    @Column(name = "F364", nullable = true)
    private String f364 = null;

    @NoHtml
    @Column(name = "F365", nullable = true)
    private String f365 = null;

    @NoHtml
    @Column(name = "F366", nullable = true)
    private String f366 = null;

    @NoHtml
    @Column(name = "F367", nullable = true)
    private String f367 = null;

    @NoHtml
    @Column(name = "F368", nullable = true)
    private String f368 = null;

    @NoHtml
    @Column(name = "F369", nullable = true)
    private String f369 = null;

    @NoHtml
    @Column(name = "F370", nullable = true)
    private String f370 = null;

    @NoHtml
    @Column(name = "F371", nullable = true)
    private String f371 = null;

    @NoHtml
    @Column(name = "F372", nullable = true)
    private String f372 = null;

    @NoHtml
    @Column(name = "F373", nullable = true)
    private String f373 = null;

    @NoHtml
    @Column(name = "F374", nullable = true)
    private String f374 = null;

    @NoHtml
    @Column(name = "F375", nullable = true)
    private String f375 = null;

    @NoHtml
    @Column(name = "F376", nullable = true)
    private String f376 = null;

    @NoHtml
    @Column(name = "F377", nullable = true)
    private String f377 = null;

    @NoHtml
    @Column(name = "F378", nullable = true)
    private String f378 = null;

    @NoHtml
    @Column(name = "F379", nullable = true)
    private String f379 = null;

    @NoHtml
    @Column(name = "F380", nullable = true)
    private String f380 = null;

    @NoHtml
    @Column(name = "F381", nullable = true)
    private String f381 = null;

    @NoHtml
    @Column(name = "F382", nullable = true)
    private String f382 = null;

    @NoHtml
    @Column(name = "F383", nullable = true)
    private String f383 = null;

    @NoHtml
    @Column(name = "F384", nullable = true)
    private String f384 = null;

    @NoHtml
    @Column(name = "F385", nullable = true)
    private String f385 = null;

    @NoHtml
    @Column(name = "F386", nullable = true)
    private String f386 = null;

    @NoHtml
    @Column(name = "F387", nullable = true)
    private String f387 = null;

    @NoHtml
    @Column(name = "F388", nullable = true)
    private String f388 = null;

    @NoHtml
    @Column(name = "F389", nullable = true)
    private String f389 = null;

    @NoHtml
    @Column(name = "F390", nullable = true)
    private String f390 = null;

    @NoHtml
    @Column(name = "F391", nullable = true)
    private String f391 = null;

    @NoHtml
    @Column(name = "F392", nullable = true)
    private String f392 = null;

    @NoHtml
    @Column(name = "F393", nullable = true)
    private String f393 = null;

    @NoHtml
    @Column(name = "F394", nullable = true)
    private String f394 = null;

    @NoHtml
    @Column(name = "F395", nullable = true)
    private String f395 = null;

    @NoHtml
    @Column(name = "F396", nullable = true)
    private String f396 = null;

    @NoHtml
    @Column(name = "F397", nullable = true)
    private String f397 = null;

    @NoHtml
    @Column(name = "F398", nullable = true)
    private String f398 = null;

    @NoHtml
    @Column(name = "F399", nullable = true)
    private String f399 = null;

    @NoHtml
    @Column(name = "F400", nullable = true)
    private String f400 = null;
    
    @NoHtml
    @Column(name = "F401", nullable = true)
    private String f401 = null;

    @NoHtml
    @Column(name = "F402", nullable = true)
    private String f402 = null;

    @NoHtml
    @Column(name = "F403", nullable = true)
    private String f403 = null;

    @NoHtml
    @Column(name = "F404", nullable = true)
    private String f404 = null;

    @NoHtml
    @Column(name = "F405", nullable = true)
    private String f405 = null;

    @NoHtml
    @Column(name = "F406", nullable = true)
    private String f406 = null;

    @NoHtml
    @Column(name = "F407", nullable = true)
    private String f407 = null;

    @NoHtml
    @Column(name = "F408", nullable = true)
    private String f408 = null;

    @NoHtml
    @Column(name = "F409", nullable = true)
    private String f409 = null;

    @NoHtml
    @Column(name = "F410", nullable = true)
    private String f410 = null;

    @NoHtml
    @Column(name = "F411", nullable = true)
    private String f411 = null;

    @NoHtml
    @Column(name = "F412", nullable = true)
    private String f412 = null;

    @NoHtml
    @Column(name = "F413", nullable = true)
    private String f413 = null;

    @NoHtml
    @Column(name = "F414", nullable = true)
    private String f414 = null;

    @NoHtml
    @Column(name = "F415", nullable = true)
    private String f415 = null;

    @NoHtml
    @Column(name = "F416", nullable = true)
    private String f416 = null;

    @NoHtml
    @Column(name = "F417", nullable = true)
    private String f417 = null;

    @NoHtml
    @Column(name = "F418", nullable = true)
    private String f418 = null;

    @NoHtml
    @Column(name = "F419", nullable = true)
    private String f419 = null;

    @NoHtml
    @Column(name = "F420", nullable = true)
    private String f420 = null;

    @NoHtml
    @Column(name = "F421", nullable = true)
    private String f421 = null;

    @NoHtml
    @Column(name = "F422", nullable = true)
    private String f422 = null;

    @NoHtml
    @Column(name = "F423", nullable = true)
    private String f423 = null;

    @NoHtml
    @Column(name = "F424", nullable = true)
    private String f424 = null;

    @NoHtml
    @Column(name = "F425", nullable = true)
    private String f425 = null;

    @NoHtml
    @Column(name = "F426", nullable = true)
    private String f426 = null;

    @NoHtml
    @Column(name = "F427", nullable = true)
    private String f427 = null;

    @NoHtml
    @Column(name = "F428", nullable = true)
    private String f428 = null;

    @NoHtml
    @Column(name = "F429", nullable = true)
    private String f429 = null;

    @NoHtml
    @Column(name = "F430", nullable = true)
    private String f430 = null;

    @NoHtml
    @Column(name = "F431", nullable = true)
    private String f431 = null;

    @NoHtml
    @Column(name = "F432", nullable = true)
    private String f432 = null;

    @NoHtml
    @Column(name = "F433", nullable = true)
    private String f433 = null;

    @NoHtml
    @Column(name = "F434", nullable = true)
    private String f434 = null;

    @NoHtml
    @Column(name = "F435", nullable = true)
    private String f435 = null;

    @NoHtml
    @Column(name = "F436", nullable = true)
    private String f436 = null;

    @NoHtml
    @Column(name = "F437", nullable = true)
    private String f437 = null;

    @NoHtml
    @Column(name = "F438", nullable = true)
    private String f438 = null;

    @NoHtml
    @Column(name = "F439", nullable = true)
    private String f439 = null;

    @NoHtml
    @Column(name = "F440", nullable = true)
    private String f440 = null;

    @NoHtml
    @Column(name = "F441", nullable = true)
    private String f441 = null;

    @NoHtml
    @Column(name = "F442", nullable = true)
    private String f442 = null;

    @NoHtml
    @Column(name = "F443", nullable = true)
    private String f443 = null;

    @NoHtml
    @Column(name = "F444", nullable = true)
    private String f444 = null;

    @NoHtml
    @Column(name = "F445", nullable = true)
    private String f445 = null;

    @NoHtml
    @Column(name = "F446", nullable = true)
    private String f446 = null;

    @NoHtml
    @Column(name = "F447", nullable = true)
    private String f447 = null;

    @NoHtml
    @Column(name = "F448", nullable = true)
    private String f448 = null;

    @NoHtml
    @Column(name = "F449", nullable = true)
    private String f449 = null;

    @NoHtml
    @Column(name = "F450", nullable = true)
    private String f450 = null;

    @NoHtml
    @Column(name = "F451", nullable = true)
    private String f451 = null;

    @NoHtml
    @Column(name = "F452", nullable = true)
    private String f452 = null;

    @NoHtml
    @Column(name = "F453", nullable = true)
    private String f453 = null;

    @NoHtml
    @Column(name = "F454", nullable = true)
    private String f454 = null;

    @NoHtml
    @Column(name = "F455", nullable = true)
    private String f455 = null;

    @NoHtml
    @Column(name = "F456", nullable = true)
    private String f456 = null;

    @NoHtml
    @Column(name = "F457", nullable = true)
    private String f457 = null;

    @NoHtml
    @Column(name = "F458", nullable = true)
    private String f458 = null;

    @NoHtml
    @Column(name = "F459", nullable = true)
    private String f459 = null;

    @NoHtml
    @Column(name = "F460", nullable = true)
    private String f460 = null;

    @NoHtml
    @Column(name = "F461", nullable = true)
    private String f461 = null;

    @NoHtml
    @Column(name = "F462", nullable = true)
    private String f462 = null;

    @NoHtml
    @Column(name = "F463", nullable = true)
    private String f463 = null;

    @NoHtml
    @Column(name = "F464", nullable = true)
    private String f464 = null;

    @NoHtml
    @Column(name = "F465", nullable = true)
    private String f465 = null;

    @NoHtml
    @Column(name = "F466", nullable = true)
    private String f466 = null;

    @NoHtml
    @Column(name = "F467", nullable = true)
    private String f467 = null;

    @NoHtml
    @Column(name = "F468", nullable = true)
    private String f468 = null;

    @NoHtml
    @Column(name = "F469", nullable = true)
    private String f469 = null;

    @NoHtml
    @Column(name = "F470", nullable = true)
    private String f470 = null;

    @NoHtml
    @Column(name = "F471", nullable = true)
    private String f471 = null;

    @NoHtml
    @Column(name = "F472", nullable = true)
    private String f472 = null;

    @NoHtml
    @Column(name = "F473", nullable = true)
    private String f473 = null;

    @NoHtml
    @Column(name = "F474", nullable = true)
    private String f474 = null;

    @NoHtml
    @Column(name = "F475", nullable = true)
    private String f475 = null;

    @NoHtml
    @Column(name = "F476", nullable = true)
    private String f476 = null;

    @NoHtml
    @Column(name = "F477", nullable = true)
    private String f477 = null;

    @NoHtml
    @Column(name = "F478", nullable = true)
    private String f478 = null;

    @NoHtml
    @Column(name = "F479", nullable = true)
    private String f479 = null;

    @NoHtml
    @Column(name = "F480", nullable = true)
    private String f480 = null;

    @NoHtml
    @Column(name = "F481", nullable = true)
    private String f481 = null;

    @NoHtml
    @Column(name = "F482", nullable = true)
    private String f482 = null;

    @NoHtml
    @Column(name = "F483", nullable = true)
    private String f483 = null;

    @NoHtml
    @Column(name = "F484", nullable = true)
    private String f484 = null;

    @NoHtml
    @Column(name = "F485", nullable = true)
    private String f485 = null;

    @NoHtml
    @Column(name = "F486", nullable = true)
    private String f486 = null;

    @NoHtml
    @Column(name = "F487", nullable = true)
    private String f487 = null;

    @NoHtml
    @Column(name = "F488", nullable = true)
    private String f488 = null;

    @NoHtml
    @Column(name = "F489", nullable = true)
    private String f489 = null;

    @NoHtml
    @Column(name = "F490", nullable = true)
    private String f490 = null;

    @NoHtml
    @Column(name = "F491", nullable = true)
    private String f491 = null;

    @NoHtml
    @Column(name = "F492", nullable = true)
    private String f492 = null;

    @NoHtml
    @Column(name = "F493", nullable = true)
    private String f493 = null;

    @NoHtml
    @Column(name = "F494", nullable = true)
    private String f494 = null;

    @NoHtml
    @Column(name = "F495", nullable = true)
    private String f495 = null;

    @NoHtml
    @Column(name = "F496", nullable = true)
    private String f496 = null;

    @NoHtml
    @Column(name = "F497", nullable = true)
    private String f497 = null;

    @NoHtml
    @Column(name = "F498", nullable = true)
    private String f498 = null;

    @NoHtml
    @Column(name = "F499", nullable = true)
    private String f499 = null;

    @NoHtml
    @Column(name = "F500", nullable = true)
    private String f500 = null;

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

    public String getF256() {
        return f256;
    }

    public void setF256(String f256) {
        this.f256 = f256;
    }

    public String getF257() {
        return f257;
    }

    public void setF257(String f257) {
        this.f257 = f257;
    }

    public String getF258() {
        return f258;
    }

    public void setF258(String f258) {
        this.f258 = f258;
    }

    public String getF259() {
        return f259;
    }

    public void setF259(String f259) {
        this.f259 = f259;
    }

    public String getF260() {
        return f260;
    }

    public void setF260(String f260) {
        this.f260 = f260;
    }

    public String getF261() {
        return f261;
    }

    public void setF261(String f261) {
        this.f261 = f261;
    }

    public String getF262() {
        return f262;
    }

    public void setF262(String f262) {
        this.f262 = f262;
    }

    public String getF263() {
        return f263;
    }

    public void setF263(String f263) {
        this.f263 = f263;
    }

    public String getF264() {
        return f264;
    }

    public void setF264(String f264) {
        this.f264 = f264;
    }

    public String getF265() {
        return f265;
    }

    public void setF265(String f265) {
        this.f265 = f265;
    }

    public String getF266() {
        return f266;
    }

    public void setF266(String f266) {
        this.f266 = f266;
    }

    public String getF267() {
        return f267;
    }

    public void setF267(String f267) {
        this.f267 = f267;
    }

    public String getF268() {
        return f268;
    }

    public void setF268(String f268) {
        this.f268 = f268;
    }

    public String getF269() {
        return f269;
    }

    public void setF269(String f269) {
        this.f269 = f269;
    }

    public String getF270() {
        return f270;
    }

    public void setF270(String f270) {
        this.f270 = f270;
    }

    public String getF271() {
        return f271;
    }

    public void setF271(String f271) {
        this.f271 = f271;
    }

    public String getF272() {
        return f272;
    }

    public void setF272(String f272) {
        this.f272 = f272;
    }

    public String getF273() {
        return f273;
    }

    public void setF273(String f273) {
        this.f273 = f273;
    }

    public String getF274() {
        return f274;
    }

    public void setF274(String f274) {
        this.f274 = f274;
    }

    public String getF275() {
        return f275;
    }

    public void setF275(String f275) {
        this.f275 = f275;
    }

    public String getF276() {
        return f276;
    }

    public void setF276(String f276) {
        this.f276 = f276;
    }

    public String getF277() {
        return f277;
    }

    public void setF277(String f277) {
        this.f277 = f277;
    }

    public String getF278() {
        return f278;
    }

    public void setF278(String f278) {
        this.f278 = f278;
    }

    public String getF279() {
        return f279;
    }

    public void setF279(String f279) {
        this.f279 = f279;
    }

    public String getF280() {
        return f280;
    }

    public void setF280(String f280) {
        this.f280 = f280;
    }

    public String getF281() {
        return f281;
    }

    public void setF281(String f281) {
        this.f281 = f281;
    }

    public String getF282() {
        return f282;
    }

    public void setF282(String f282) {
        this.f282 = f282;
    }

    public String getF283() {
        return f283;
    }

    public void setF283(String f283) {
        this.f283 = f283;
    }

    public String getF284() {
        return f284;
    }

    public void setF284(String f284) {
        this.f284 = f284;
    }

    public String getF285() {
        return f285;
    }

    public void setF285(String f285) {
        this.f285 = f285;
    }

    public String getF286() {
        return f286;
    }

    public void setF286(String f286) {
        this.f286 = f286;
    }

    public String getF287() {
        return f287;
    }

    public void setF287(String f287) {
        this.f287 = f287;
    }

    public String getF288() {
        return f288;
    }

    public void setF288(String f288) {
        this.f288 = f288;
    }

    public String getF289() {
        return f289;
    }

    public void setF289(String f289) {
        this.f289 = f289;
    }

    public String getF290() {
        return f290;
    }

    public void setF290(String f290) {
        this.f290 = f290;
    }

    public String getF291() {
        return f291;
    }

    public void setF291(String f291) {
        this.f291 = f291;
    }

    public String getF292() {
        return f292;
    }

    public void setF292(String f292) {
        this.f292 = f292;
    }

    public String getF293() {
        return f293;
    }

    public void setF293(String f293) {
        this.f293 = f293;
    }

    public String getF294() {
        return f294;
    }

    public void setF294(String f294) {
        this.f294 = f294;
    }

    public String getF295() {
        return f295;
    }

    public void setF295(String f295) {
        this.f295 = f295;
    }

    public String getF296() {
        return f296;
    }

    public void setF296(String f296) {
        this.f296 = f296;
    }

    public String getF297() {
        return f297;
    }

    public void setF297(String f297) {
        this.f297 = f297;
    }

    public String getF298() {
        return f298;
    }

    public void setF298(String f298) {
        this.f298 = f298;
    }

    public String getF299() {
        return f299;
    }

    public void setF299(String f299) {
        this.f299 = f299;
    }

    public String getF300() {
        return f300;
    }

    public void setF300(String f300) {
        this.f300 = f300;
    }

    public String getF301() {
        return f301;
    }

    public void setF301(String f301) {
        this.f301 = f301;
    }

    public String getF302() {
        return f302;
    }

    public void setF302(String f302) {
        this.f302 = f302;
    }

    public String getF303() {
        return f303;
    }

    public void setF303(String f303) {
        this.f303 = f303;
    }

    public String getF304() {
        return f304;
    }

    public void setF304(String f304) {
        this.f304 = f304;
    }

    public String getF305() {
        return f305;
    }

    public void setF305(String f305) {
        this.f305 = f305;
    }

    public String getF306() {
        return f306;
    }

    public void setF306(String f306) {
        this.f306 = f306;
    }

    public String getF307() {
        return f307;
    }

    public void setF307(String f307) {
        this.f307 = f307;
    }

    public String getF308() {
        return f308;
    }

    public void setF308(String f308) {
        this.f308 = f308;
    }

    public String getF309() {
        return f309;
    }

    public void setF309(String f309) {
        this.f309 = f309;
    }

    public String getF310() {
        return f310;
    }

    public void setF310(String f310) {
        this.f310 = f310;
    }

    public String getF311() {
        return f311;
    }

    public void setF311(String f311) {
        this.f311 = f311;
    }

    public String getF312() {
        return f312;
    }

    public void setF312(String f312) {
        this.f312 = f312;
    }

    public String getF313() {
        return f313;
    }

    public void setF313(String f313) {
        this.f313 = f313;
    }

    public String getF314() {
        return f314;
    }

    public void setF314(String f314) {
        this.f314 = f314;
    }

    public String getF315() {
        return f315;
    }

    public void setF315(String f315) {
        this.f315 = f315;
    }

    public String getF316() {
        return f316;
    }

    public void setF316(String f316) {
        this.f316 = f316;
    }

    public String getF317() {
        return f317;
    }

    public void setF317(String f317) {
        this.f317 = f317;
    }

    public String getF318() {
        return f318;
    }

    public void setF318(String f318) {
        this.f318 = f318;
    }

    public String getF319() {
        return f319;
    }

    public void setF319(String f319) {
        this.f319 = f319;
    }

    public String getF320() {
        return f320;
    }

    public void setF320(String f320) {
        this.f320 = f320;
    }

    public String getF321() {
        return f321;
    }

    public void setF321(String f321) {
        this.f321 = f321;
    }

    public String getF322() {
        return f322;
    }

    public void setF322(String f322) {
        this.f322 = f322;
    }

    public String getF323() {
        return f323;
    }

    public void setF323(String f323) {
        this.f323 = f323;
    }

    public String getF324() {
        return f324;
    }

    public void setF324(String f324) {
        this.f324 = f324;
    }

    public String getF325() {
        return f325;
    }

    public void setF325(String f325) {
        this.f325 = f325;
    }

    public String getF326() {
        return f326;
    }

    public void setF326(String f326) {
        this.f326 = f326;
    }

    public String getF327() {
        return f327;
    }

    public void setF327(String f327) {
        this.f327 = f327;
    }

    public String getF328() {
        return f328;
    }

    public void setF328(String f328) {
        this.f328 = f328;
    }

    public String getF329() {
        return f329;
    }

    public void setF329(String f329) {
        this.f329 = f329;
    }

    public String getF330() {
        return f330;
    }

    public void setF330(String f330) {
        this.f330 = f330;
    }

    public String getF331() {
        return f331;
    }

    public void setF331(String f331) {
        this.f331 = f331;
    }

    public String getF332() {
        return f332;
    }

    public void setF332(String f332) {
        this.f332 = f332;
    }

    public String getF333() {
        return f333;
    }

    public void setF333(String f333) {
        this.f333 = f333;
    }

    public String getF334() {
        return f334;
    }

    public void setF334(String f334) {
        this.f334 = f334;
    }

    public String getF335() {
        return f335;
    }

    public void setF335(String f335) {
        this.f335 = f335;
    }

    public String getF336() {
        return f336;
    }

    public void setF336(String f336) {
        this.f336 = f336;
    }

    public String getF337() {
        return f337;
    }

    public void setF337(String f337) {
        this.f337 = f337;
    }

    public String getF338() {
        return f338;
    }

    public void setF338(String f338) {
        this.f338 = f338;
    }

    public String getF339() {
        return f339;
    }

    public void setF339(String f339) {
        this.f339 = f339;
    }

    public String getF340() {
        return f340;
    }

    public void setF340(String f340) {
        this.f340 = f340;
    }

    public String getF341() {
        return f341;
    }

    public void setF341(String f341) {
        this.f341 = f341;
    }

    public String getF342() {
        return f342;
    }

    public void setF342(String f342) {
        this.f342 = f342;
    }

    public String getF343() {
        return f343;
    }

    public void setF343(String f343) {
        this.f343 = f343;
    }

    public String getF344() {
        return f344;
    }

    public void setF344(String f344) {
        this.f344 = f344;
    }

    public String getF345() {
        return f345;
    }

    public void setF345(String f345) {
        this.f345 = f345;
    }

    public String getF346() {
        return f346;
    }

    public void setF346(String f346) {
        this.f346 = f346;
    }

    public String getF347() {
        return f347;
    }

    public void setF347(String f347) {
        this.f347 = f347;
    }

    public String getF348() {
        return f348;
    }

    public void setF348(String f348) {
        this.f348 = f348;
    }

    public String getF349() {
        return f349;
    }

    public void setF349(String f349) {
        this.f349 = f349;
    }

    public String getF350() {
        return f350;
    }

    public void setF350(String f350) {
        this.f350 = f350;
    }

    public String getF351() {
        return f351;
    }

    public void setF351(String f351) {
        this.f351 = f351;
    }

    public String getF352() {
        return f352;
    }

    public void setF352(String f352) {
        this.f352 = f352;
    }

    public String getF353() {
        return f353;
    }

    public void setF353(String f353) {
        this.f353 = f353;
    }

    public String getF354() {
        return f354;
    }

    public void setF354(String f354) {
        this.f354 = f354;
    }

    public String getF355() {
        return f355;
    }

    public void setF355(String f355) {
        this.f355 = f355;
    }

    public String getF356() {
        return f356;
    }

    public void setF356(String f356) {
        this.f356 = f356;
    }

    public String getF357() {
        return f357;
    }

    public void setF357(String f357) {
        this.f357 = f357;
    }

    public String getF358() {
        return f358;
    }

    public void setF358(String f358) {
        this.f358 = f358;
    }

    public String getF359() {
        return f359;
    }

    public void setF359(String f359) {
        this.f359 = f359;
    }

    public String getF360() {
        return f360;
    }

    public void setF360(String f360) {
        this.f360 = f360;
    }

    public String getF361() {
        return f361;
    }

    public void setF361(String f361) {
        this.f361 = f361;
    }

    public String getF362() {
        return f362;
    }

    public void setF362(String f362) {
        this.f362 = f362;
    }

    public String getF363() {
        return f363;
    }

    public void setF363(String f363) {
        this.f363 = f363;
    }

    public String getF364() {
        return f364;
    }

    public void setF364(String f364) {
        this.f364 = f364;
    }

    public String getF365() {
        return f365;
    }

    public void setF365(String f365) {
        this.f365 = f365;
    }

    public String getF366() {
        return f366;
    }

    public void setF366(String f366) {
        this.f366 = f366;
    }

    public String getF367() {
        return f367;
    }

    public void setF367(String f367) {
        this.f367 = f367;
    }

    public String getF368() {
        return f368;
    }

    public void setF368(String f368) {
        this.f368 = f368;
    }

    public String getF369() {
        return f369;
    }

    public void setF369(String f369) {
        this.f369 = f369;
    }

    public String getF370() {
        return f370;
    }

    public void setF370(String f370) {
        this.f370 = f370;
    }

    public String getF371() {
        return f371;
    }

    public void setF371(String f371) {
        this.f371 = f371;
    }

    public String getF372() {
        return f372;
    }

    public void setF372(String f372) {
        this.f372 = f372;
    }

    public String getF373() {
        return f373;
    }

    public void setF373(String f373) {
        this.f373 = f373;
    }

    public String getF374() {
        return f374;
    }

    public void setF374(String f374) {
        this.f374 = f374;
    }

    public String getF375() {
        return f375;
    }

    public void setF375(String f375) {
        this.f375 = f375;
    }

    public String getF376() {
        return f376;
    }

    public void setF376(String f376) {
        this.f376 = f376;
    }

    public String getF377() {
        return f377;
    }

    public void setF377(String f377) {
        this.f377 = f377;
    }

    public String getF378() {
        return f378;
    }

    public void setF378(String f378) {
        this.f378 = f378;
    }

    public String getF379() {
        return f379;
    }

    public void setF379(String f379) {
        this.f379 = f379;
    }

    public String getF380() {
        return f380;
    }

    public void setF380(String f380) {
        this.f380 = f380;
    }

    public String getF381() {
        return f381;
    }

    public void setF381(String f381) {
        this.f381 = f381;
    }

    public String getF382() {
        return f382;
    }

    public void setF382(String f382) {
        this.f382 = f382;
    }

    public String getF383() {
        return f383;
    }

    public void setF383(String f383) {
        this.f383 = f383;
    }

    public String getF384() {
        return f384;
    }

    public void setF384(String f384) {
        this.f384 = f384;
    }

    public String getF385() {
        return f385;
    }

    public void setF385(String f385) {
        this.f385 = f385;
    }

    public String getF386() {
        return f386;
    }

    public void setF386(String f386) {
        this.f386 = f386;
    }

    public String getF387() {
        return f387;
    }

    public void setF387(String f387) {
        this.f387 = f387;
    }

    public String getF388() {
        return f388;
    }

    public void setF388(String f388) {
        this.f388 = f388;
    }

    public String getF389() {
        return f389;
    }

    public void setF389(String f389) {
        this.f389 = f389;
    }

    public String getF390() {
        return f390;
    }

    public void setF390(String f390) {
        this.f390 = f390;
    }

    public String getF391() {
        return f391;
    }

    public void setF391(String f391) {
        this.f391 = f391;
    }

    public String getF392() {
        return f392;
    }

    public void setF392(String f392) {
        this.f392 = f392;
    }

    public String getF393() {
        return f393;
    }

    public void setF393(String f393) {
        this.f393 = f393;
    }

    public String getF394() {
        return f394;
    }

    public void setF394(String f394) {
        this.f394 = f394;
    }

    public String getF395() {
        return f395;
    }

    public void setF395(String f395) {
        this.f395 = f395;
    }

    public String getF396() {
        return f396;
    }

    public void setF396(String f396) {
        this.f396 = f396;
    }

    public String getF397() {
        return f397;
    }

    public void setF397(String f397) {
        this.f397 = f397;
    }

    public String getF398() {
        return f398;
    }

    public void setF398(String f398) {
        this.f398 = f398;
    }

    public String getF399() {
        return f399;
    }

    public void setF399(String f399) {
        this.f399 = f399;
    }

    public String getF400() {
        return f400;
    }

    public void setF400(String f400) {
        this.f400 = f400;
    }

    public String getF401() {
        return f401;
    }

    public void setF401(String f401) {
        this.f401 = f401;
    }

    public String getF402() {
        return f402;
    }

    public void setF402(String f402) {
        this.f402 = f402;
    }

    public String getF403() {
        return f403;
    }

    public void setF403(String f403) {
        this.f403 = f403;
    }

    public String getF404() {
        return f404;
    }

    public void setF404(String f404) {
        this.f404 = f404;
    }

    public String getF405() {
        return f405;
    }

    public void setF405(String f405) {
        this.f405 = f405;
    }

    public String getF406() {
        return f406;
    }

    public void setF406(String f406) {
        this.f406 = f406;
    }

    public String getF407() {
        return f407;
    }

    public void setF407(String f407) {
        this.f407 = f407;
    }

    public String getF408() {
        return f408;
    }

    public void setF408(String f408) {
        this.f408 = f408;
    }

    public String getF409() {
        return f409;
    }

    public void setF409(String f409) {
        this.f409 = f409;
    }

    public String getF410() {
        return f410;
    }

    public void setF410(String f410) {
        this.f410 = f410;
    }

    public String getF411() {
        return f411;
    }

    public void setF411(String f411) {
        this.f411 = f411;
    }

    public String getF412() {
        return f412;
    }

    public void setF412(String f412) {
        this.f412 = f412;
    }

    public String getF413() {
        return f413;
    }

    public void setF413(String f413) {
        this.f413 = f413;
    }

    public String getF414() {
        return f414;
    }

    public void setF414(String f414) {
        this.f414 = f414;
    }

    public String getF415() {
        return f415;
    }

    public void setF415(String f415) {
        this.f415 = f415;
    }

    public String getF416() {
        return f416;
    }

    public void setF416(String f416) {
        this.f416 = f416;
    }

    public String getF417() {
        return f417;
    }

    public void setF417(String f417) {
        this.f417 = f417;
    }

    public String getF418() {
        return f418;
    }

    public void setF418(String f418) {
        this.f418 = f418;
    }

    public String getF419() {
        return f419;
    }

    public void setF419(String f419) {
        this.f419 = f419;
    }

    public String getF420() {
        return f420;
    }

    public void setF420(String f420) {
        this.f420 = f420;
    }

    public String getF421() {
        return f421;
    }

    public void setF421(String f421) {
        this.f421 = f421;
    }

    public String getF422() {
        return f422;
    }

    public void setF422(String f422) {
        this.f422 = f422;
    }

    public String getF423() {
        return f423;
    }

    public void setF423(String f423) {
        this.f423 = f423;
    }

    public String getF424() {
        return f424;
    }

    public void setF424(String f424) {
        this.f424 = f424;
    }

    public String getF425() {
        return f425;
    }

    public void setF425(String f425) {
        this.f425 = f425;
    }

    public String getF426() {
        return f426;
    }

    public void setF426(String f426) {
        this.f426 = f426;
    }

    public String getF427() {
        return f427;
    }

    public void setF427(String f427) {
        this.f427 = f427;
    }

    public String getF428() {
        return f428;
    }

    public void setF428(String f428) {
        this.f428 = f428;
    }

    public String getF429() {
        return f429;
    }

    public void setF429(String f429) {
        this.f429 = f429;
    }

    public String getF430() {
        return f430;
    }

    public void setF430(String f430) {
        this.f430 = f430;
    }

    public String getF431() {
        return f431;
    }

    public void setF431(String f431) {
        this.f431 = f431;
    }

    public String getF432() {
        return f432;
    }

    public void setF432(String f432) {
        this.f432 = f432;
    }

    public String getF433() {
        return f433;
    }

    public void setF433(String f433) {
        this.f433 = f433;
    }

    public String getF434() {
        return f434;
    }

    public void setF434(String f434) {
        this.f434 = f434;
    }

    public String getF435() {
        return f435;
    }

    public void setF435(String f435) {
        this.f435 = f435;
    }

    public String getF436() {
        return f436;
    }

    public void setF436(String f436) {
        this.f436 = f436;
    }

    public String getF437() {
        return f437;
    }

    public void setF437(String f437) {
        this.f437 = f437;
    }

    public String getF438() {
        return f438;
    }

    public void setF438(String f438) {
        this.f438 = f438;
    }

    public String getF439() {
        return f439;
    }

    public void setF439(String f439) {
        this.f439 = f439;
    }

    public String getF440() {
        return f440;
    }

    public void setF440(String f440) {
        this.f440 = f440;
    }

    public String getF441() {
        return f441;
    }

    public void setF441(String f441) {
        this.f441 = f441;
    }

    public String getF442() {
        return f442;
    }

    public void setF442(String f442) {
        this.f442 = f442;
    }

    public String getF443() {
        return f443;
    }

    public void setF443(String f443) {
        this.f443 = f443;
    }

    public String getF444() {
        return f444;
    }

    public void setF444(String f444) {
        this.f444 = f444;
    }

    public String getF445() {
        return f445;
    }

    public void setF445(String f445) {
        this.f445 = f445;
    }

    public String getF446() {
        return f446;
    }

    public void setF446(String f446) {
        this.f446 = f446;
    }

    public String getF447() {
        return f447;
    }

    public void setF447(String f447) {
        this.f447 = f447;
    }

    public String getF448() {
        return f448;
    }

    public void setF448(String f448) {
        this.f448 = f448;
    }

    public String getF449() {
        return f449;
    }

    public void setF449(String f449) {
        this.f449 = f449;
    }

    public String getF450() {
        return f450;
    }

    public void setF450(String f450) {
        this.f450 = f450;
    }

    public String getF451() {
        return f451;
    }

    public void setF451(String f451) {
        this.f451 = f451;
    }

    public String getF452() {
        return f452;
    }

    public void setF452(String f452) {
        this.f452 = f452;
    }

    public String getF453() {
        return f453;
    }

    public void setF453(String f453) {
        this.f453 = f453;
    }

    public String getF454() {
        return f454;
    }

    public void setF454(String f454) {
        this.f454 = f454;
    }

    public String getF455() {
        return f455;
    }

    public void setF455(String f455) {
        this.f455 = f455;
    }

    public String getF456() {
        return f456;
    }

    public void setF456(String f456) {
        this.f456 = f456;
    }

    public String getF457() {
        return f457;
    }

    public void setF457(String f457) {
        this.f457 = f457;
    }

    public String getF458() {
        return f458;
    }

    public void setF458(String f458) {
        this.f458 = f458;
    }

    public String getF459() {
        return f459;
    }

    public void setF459(String f459) {
        this.f459 = f459;
    }

    public String getF460() {
        return f460;
    }

    public void setF460(String f460) {
        this.f460 = f460;
    }

    public String getF461() {
        return f461;
    }

    public void setF461(String f461) {
        this.f461 = f461;
    }

    public String getF462() {
        return f462;
    }

    public void setF462(String f462) {
        this.f462 = f462;
    }

    public String getF463() {
        return f463;
    }

    public void setF463(String f463) {
        this.f463 = f463;
    }

    public String getF464() {
        return f464;
    }

    public void setF464(String f464) {
        this.f464 = f464;
    }

    public String getF465() {
        return f465;
    }

    public void setF465(String f465) {
        this.f465 = f465;
    }

    public String getF466() {
        return f466;
    }

    public void setF466(String f466) {
        this.f466 = f466;
    }

    public String getF467() {
        return f467;
    }

    public void setF467(String f467) {
        this.f467 = f467;
    }

    public String getF468() {
        return f468;
    }

    public void setF468(String f468) {
        this.f468 = f468;
    }

    public String getF469() {
        return f469;
    }

    public void setF469(String f469) {
        this.f469 = f469;
    }

    public String getF470() {
        return f470;
    }

    public void setF470(String f470) {
        this.f470 = f470;
    }

    public String getF471() {
        return f471;
    }

    public void setF471(String f471) {
        this.f471 = f471;
    }

    public String getF472() {
        return f472;
    }

    public void setF472(String f472) {
        this.f472 = f472;
    }

    public String getF473() {
        return f473;
    }

    public void setF473(String f473) {
        this.f473 = f473;
    }

    public String getF474() {
        return f474;
    }

    public void setF474(String f474) {
        this.f474 = f474;
    }

    public String getF475() {
        return f475;
    }

    public void setF475(String f475) {
        this.f475 = f475;
    }

    public String getF476() {
        return f476;
    }

    public void setF476(String f476) {
        this.f476 = f476;
    }

    public String getF477() {
        return f477;
    }

    public void setF477(String f477) {
        this.f477 = f477;
    }

    public String getF478() {
        return f478;
    }

    public void setF478(String f478) {
        this.f478 = f478;
    }

    public String getF479() {
        return f479;
    }

    public void setF479(String f479) {
        this.f479 = f479;
    }

    public String getF480() {
        return f480;
    }

    public void setF480(String f480) {
        this.f480 = f480;
    }

    public String getF481() {
        return f481;
    }

    public void setF481(String f481) {
        this.f481 = f481;
    }

    public String getF482() {
        return f482;
    }

    public void setF482(String f482) {
        this.f482 = f482;
    }

    public String getF483() {
        return f483;
    }

    public void setF483(String f483) {
        this.f483 = f483;
    }

    public String getF484() {
        return f484;
    }

    public void setF484(String f484) {
        this.f484 = f484;
    }

    public String getF485() {
        return f485;
    }

    public void setF485(String f485) {
        this.f485 = f485;
    }

    public String getF486() {
        return f486;
    }

    public void setF486(String f486) {
        this.f486 = f486;
    }

    public String getF487() {
        return f487;
    }

    public void setF487(String f487) {
        this.f487 = f487;
    }

    public String getF488() {
        return f488;
    }

    public void setF488(String f488) {
        this.f488 = f488;
    }

    public String getF489() {
        return f489;
    }

    public void setF489(String f489) {
        this.f489 = f489;
    }

    public String getF490() {
        return f490;
    }

    public void setF490(String f490) {
        this.f490 = f490;
    }

    public String getF491() {
        return f491;
    }

    public void setF491(String f491) {
        this.f491 = f491;
    }

    public String getF492() {
        return f492;
    }

    public void setF492(String f492) {
        this.f492 = f492;
    }

    public String getF493() {
        return f493;
    }

    public void setF493(String f493) {
        this.f493 = f493;
    }

    public String getF494() {
        return f494;
    }

    public void setF494(String f494) {
        this.f494 = f494;
    }

    public String getF495() {
        return f495;
    }

    public void setF495(String f495) {
        this.f495 = f495;
    }

    public String getF496() {
        return f496;
    }

    public void setF496(String f496) {
        this.f496 = f496;
    }

    public String getF497() {
        return f497;
    }

    public void setF497(String f497) {
        this.f497 = f497;
    }

    public String getF498() {
        return f498;
    }

    public void setF498(String f498) {
        this.f498 = f498;
    }

    public String getF499() {
        return f499;
    }

    public void setF499(String f499) {
        this.f499 = f499;
    }

    public String getF500() {
        return f500;
    }

    public void setF500(String f500) {
        this.f500 = f500;
    }

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
    
    
}
