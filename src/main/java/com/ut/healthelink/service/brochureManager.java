package com.ut.healthelink.service;

import com.ut.healthelink.model.Brochure;

public interface brochureManager {

    Integer createBrochure(Brochure brochure) throws Exception;

    void updateBrochure(Brochure brochure) throws Exception;

    void deleteBrochure(int brochureId);

    Brochure getBrochureById(int brochureId);

}
