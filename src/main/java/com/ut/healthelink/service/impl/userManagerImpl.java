package com.ut.healthelink.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.healthelink.dao.userDAO;
import com.ut.healthelink.model.User;
import com.ut.healthelink.service.userManager;
import com.ut.healthelink.model.UserActivity;
import com.ut.healthelink.model.configurationConnectionSenders;
import com.ut.healthelink.model.siteSections;
import com.ut.healthelink.model.userAccess;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
public class userManagerImpl implements userManager {

    @Autowired
    private userDAO userDAO;

    @Override
    @Transactional
    public Integer createUser(User user) {
        Integer lastId = null;
        lastId = (Integer) userDAO.createUser(user);
        return lastId;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    @Transactional
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    @Transactional
    public List<User> getUsersByOrganization(int orgId) {
        return userDAO.getUsersByOrganization(orgId);
    }

    @Override
    @Transactional
    public User getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    @Override
    @Transactional
    public Long findTotalLogins(int userId) {
        return userDAO.findTotalLogins(userId);
    }

    @Override
    @Transactional
    public void setLastLogin(String username) {
        userDAO.setLastLogin(username);
    }

    @Override
    @Transactional
    public List<siteSections> getSections() {
        return userDAO.getSections();
    }

    @Override
    @Transactional
    public List<userAccess> getuserSections(int userId) {
        return userDAO.getuserSections(userId);
    }

    @Override
    @Transactional
    public List<User> getOrganizationContact(int orgId, int mainContact) {
        return userDAO.getOrganizationContact(orgId, mainContact);
    }

    @Override
    @Transactional
    public Integer getUserByIdentifier(String identifier) {
        return userDAO.getUserByIdentifier(identifier);
    }

    @Override
    @Transactional
    public User getUserByResetCode(String resetCode) {
        return userDAO.getUserByResetCode(resetCode);
    }

    @Override
    @Transactional
    public void insertUserLog(UserActivity userActivity) {
        userDAO.insertUserLog(userActivity);
    }

    @Override
    @Transactional
    public UserActivity getUAById(Integer uaId) {
        return userDAO.getUAById(uaId);
    }

    @Override
    @Transactional
    public List<User> getUserByTypeByOrganization(int orgId) {
        return userDAO.getUserByTypeByOrganization(orgId);
    }

    @Override
    @Transactional
    public List<User> getSendersForConfig(List<Integer> configIds) {
        return userDAO.getSendersForConfig(configIds);
    }

    @Override
    @Transactional
    public List<User> getOrgUsersForConfig(List<Integer> configIds) {
        return userDAO.getOrgUsersForConfig(configIds);
    }
    
    @Override
    @Transactional
    public List<User> getUserConnectionListSending(Integer configId) {
        return userDAO.getUserConnectionListSending(configId);
    }
    
    @Override
    @Transactional
    public List<User> getUserConnectionListReceiving(Integer configId) {
        return userDAO.getUserConnectionListReceiving(configId);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    @Transactional
    public void updateUserActivity(UserActivity userActivity) {
        userDAO.updateUserActivity(userActivity);
    }

    @Override
    public byte[] generateSalt() throws NoSuchAlgorithmException {
        // VERY important to use SecureRandom instead of just Random
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        // Generate a 8 byte (64 bit) salt as recommended by RSA PKCS5
        byte[] salt = new byte[8];
        random.nextBytes(salt);

        return salt;
    }

    @Override
    public byte[] getEncryptedPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
    
        // PBKDF2 with SHA-1 as the hashing algorithm. Note that the NIST
        // specifically names SHA-1 as an acceptable hashing algorithm for PBKDF2
        String algorithm = "PBKDF2WithHmacSHA1";
       
        // SHA-1 generates 160 bit hashes, so that's what makes sense here
        int derivedKeyLength = 160;
	
        // Pick an iteration count that works for you. The NIST recommends at
        // least 1,000 iterations:
        // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
        // iOS 4.x reportedly uses 10,000:
        // http://blog.crackpassword.com/2010/09/smartphone-forensics-cracking-blackberry-backup-passwords/
        int iterations = 20000;
        
// byte[] b = string.getBytes(Charset.forName("UTF-8"));
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }

    @Override
    public boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
	
        // Encrypt the clear-text password using the same salt that was used to
        // encrypt the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);
        
	// Authentication succeeds if encrypted password that the user entered
        // is equal to the stored hash
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    @Override
    public User encryptPW(User user) throws Exception {
        //first we get salt
        byte[] salt = generateSalt();
        user.setRandomSalt(salt);

        byte[] encPW = getEncryptedPassword(user.getPassword(), salt);
        user.setEncryptedPw(encPW);
        // then we encrypt and send back pw
        return user;
    }

    @Override
    public List<String> getUserRoles(User user) throws Exception {
        return userDAO.getUserRoles(user);
    }

    @Override
    public void updateUserOnly(User user) throws Exception {
        userDAO.updateUserOnly(user);
    }

    @Override
    @Transactional
    public List<User> getUsersByStatuRolesAndOrg(boolean status, List<Integer> rolesToExclude, List<Integer> orgs, boolean include)
            throws Exception {
        return userDAO.getUsersByStatuRolesAndOrg(status, rolesToExclude, orgs, include);

    }
    
    @Override
    @Transactional
    public List<Integer> getUserAllowedTargets(int userId, List<configurationConnectionSenders> connections) throws Exception {
        return userDAO.getUserAllowedTargets(userId, connections);
    }
  
    @Override
    @Transactional
    public List<Integer> getUserAllowedMessageTypes(int userId, List<configurationConnectionSenders> connections) throws Exception {
        return userDAO.getUserAllowedMessageTypes(userId, connections);
    }
    
    @Override
    @Transactional
    public List<configurationConnectionSenders> configurationConnectionSendersByUserId(int userId) throws Exception {
        return userDAO.configurationConnectionSendersByUserId(userId);
    }

}
