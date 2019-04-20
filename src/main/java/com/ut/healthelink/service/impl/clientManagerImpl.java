/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ut.healthelink.service.impl;

import com.ut.healthelink.model.clients;
import com.ut.healthelink.service.clientManager;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ut.healthelink.dao.clientDAO;

/**
 *
 * @author chadmccue
 */
@Service
public class clientManagerImpl implements clientManager {
    
    @Autowired
    private clientDAO clientDAO;
    
    @Override
    @Transactional
    public List<clients> searchClients(int orgId, String firstName, String lastName, String DOB, String postalCode) throws Exception {
        
        List<clients> clients = clientDAO.searchClients(orgId, firstName, lastName, DOB, postalCode);
        
        return clients;
        
    }
    
    @Override
    @Transactional
    public clients getClientById(int clientId) throws Exception {
        return clientDAO.getClientById(clientId);
    }
    
}
