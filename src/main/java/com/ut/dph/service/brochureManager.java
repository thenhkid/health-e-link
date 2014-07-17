package com.ut.dph.service;

import com.ut.healthelink.model.Brochure;

public interface brochureManager {

    Integer createBrochure(Brochure brochure);

    void updateBrochure(Brochure brochure);

    void deleteBrochure(int brochureId);

    Brochure getBrochureById(int brochureId);

}
