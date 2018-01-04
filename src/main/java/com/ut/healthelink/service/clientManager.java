/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service;

import com.ut.healthelink.model.clients;
import java.util.List;

/**
 *
 * @author chadmccue
 */
public interface clientManager {
    
    List<clients> searchClients(int orgId, String firstName, String lastName, String DOB, String postalCode) throws Exception;
    
    clients getClientById(int clientId) throws Exception;
}
