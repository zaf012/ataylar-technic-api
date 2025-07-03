package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InstantAccountServiceImpl {
    @Transactional
    InstantAccount createAccount(InstantAccountDto accountData);

    @Transactional
    InstantAccount updateAccount(String accountId, InstantAccountDto updatedData, String updatedBy);

    @Transactional
    InstantAccount toggleAccountStatus(String accountId, String updatedBy);

    @Transactional
    InstantAccount toggleUserStatus(String accountId, String updatedBy);

    @Transactional
    void deleteAccount(String accountId);

    @Transactional
    InstantAccount deactivateAccount(String accountId, String updatedBy);

    @Transactional
    InstantAccount activateAccount(String accountId, String updatedBy);

    Optional<InstantAccount> getAccountById(String accountId);

    Optional<InstantAccount> getActiveAccountById(String accountId);

    List<InstantAccount> getAllActiveAccounts();

    List<InstantAccount> getAccountsByGroup(String groupId);

    @Transactional
    InstantAccount updatePassword(String accountId, String newPassword, String updatedBy);

    @Transactional
    List<InstantAccount> createDummyAccounts(int count, String createdBy);
}
