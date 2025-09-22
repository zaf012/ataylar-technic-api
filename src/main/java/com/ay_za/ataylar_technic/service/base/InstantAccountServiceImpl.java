package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InstantAccountDto;
import com.ay_za.ataylar_technic.entity.InstantAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InstantAccountServiceImpl {
    @Transactional
    InstantAccountDto createAccount(InstantAccountDto accountData);

    @Transactional
    InstantAccountDto updateAccount(String accountId, InstantAccountDto updatedData, String updatedBy);

    @Transactional
    InstantAccountDto toggleAccountStatus(String accountId, String updatedBy);

    @Transactional
    InstantAccountDto toggleUserStatus(String accountId, String updatedBy);

    @Transactional
    void deleteAccount(String accountId);

    @Transactional
    InstantAccountDto deactivateAccount(String accountId, String updatedBy);

    @Transactional
    InstantAccountDto activateAccount(String accountId, String updatedBy);

    Optional<InstantAccount> getAccountById(String accountId);

    Optional<InstantAccount> getActiveAccountById(String accountId);

    List<InstantAccountDto> getAllActiveAccounts();

    List<InstantAccountDto> getAccountsByGroup(String groupId);

    @Transactional
    InstantAccountDto updatePassword(String accountId, String newPassword, String updatedBy);

    @Transactional
    List<InstantAccountDto> createDummyAccounts(int count, String createdBy);
}
