package com.app.studentManagerment.services;

import com.app.studentManagerment.entity.Account;
import com.app.studentManagerment.enumPack.enumRole;

import java.util.List;


public interface AccountService {
    List<String> findallEmailNoConnected();

    Account createAccount(String email, String password, List<enumRole> roles);

    Account updateAccount(String oldEmail, String email, String password, List<enumRole> roles);

    boolean deleteAccount(String email);

    Account autoCreateAccount(String name, String ms, enumRole role);
}
