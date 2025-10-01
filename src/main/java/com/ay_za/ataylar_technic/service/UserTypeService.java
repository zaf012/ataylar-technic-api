package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.UserTypeDto;
import com.ay_za.ataylar_technic.entity.UserType;
import com.ay_za.ataylar_technic.mapper.UserTypeMapper;
import com.ay_za.ataylar_technic.repository.UserTypeRepository;
import com.ay_za.ataylar_technic.service.base.UserTypeServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserTypeService implements UserTypeServiceImpl {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper;

    public UserTypeService(UserTypeRepository userTypeRepository, UserTypeMapper userTypeMapper) {
        this.userTypeRepository = userTypeRepository;
        this.userTypeMapper = userTypeMapper;
    }

    /**
     * Tüm kullancı tiplerini getir
     */
    @Override
    public List<UserTypeDto> getAllUserTypes() {
        List<UserType> groups = userTypeRepository.findAll();
        return userTypeMapper.convertAllToDTO(groups);
    }

    /**
     * Yeni kullanıcı tipi oluştur
     * userTypeName boş bırakılırsa id = 0 olur
     */
    @Transactional
    @Override
    public UserTypeDto createUserType(String userTypeName) {
        UserType userType = new UserType();

        if (userTypeName == null || userTypeName.trim().isEmpty()) {
            // Boş bırakılırsa id = 0
            userType.setId(0);
            userType.setUserTypeName("Belirtilmemiş");
        } else {
            // Aynı isimde tip var mı kontrol et
            if (userTypeRepository.existsByUserTypeName(userTypeName.trim())) {
                throw new IllegalArgumentException("Bu isimde bir kullanıcı tipi zaten mevcut: " + userTypeName);
            }

            // Yeni ID oluştur (en yüksek ID + 1)
            Optional<Integer> maxId = userTypeRepository.findMaxId();
            Integer newId = maxId.orElse(0) + 1;

            userType.setId(newId);
            userType.setUserTypeName(userTypeName.trim());
        }

        UserType savedUserType = userTypeRepository.save(userType);
        return userTypeMapper.convertToDTO(savedUserType);
    }

    /**
     * Default kullanıcı tiplerini oluştur
     * Ana Kullanıcı, Site Sakini, Personel, Cari Hesap
     */
    @Transactional
    @Override
    public List<UserTypeDto> createDefaultUserTypes() {
        List<String> defaultTypes = Arrays.asList(
                "Ana Kullanıcı",
                "Site Sakini",
                "Personel",
                "Cari Hesap"
        );

        List<UserTypeDto> createdTypes = new ArrayList<>();

        for (String typeName : defaultTypes) {
            try {
                // Var olan tipleri tekrar oluşturmaya çalışma
                if (!userTypeRepository.existsByUserTypeName(typeName)) {
                    UserTypeDto created = createUserType(typeName);
                    createdTypes.add(created);
                }
            } catch (Exception e) {
                // Hata durumunda devam et
                System.err.println("UserType oluşturulurken hata: " + typeName + " - " + e.getMessage());
            }
        }

        return createdTypes;
    }

    /**
     * ID'ye göre kullanıcı tipi getir
     */
    @Override
    public Optional<UserTypeDto> getUserTypeById(Integer id) {
        Optional<UserType> userType = userTypeRepository.findById(id);
        return userType.map(userTypeMapper::convertToDTO);
    }

    /**
     * Kullanıcı tipi adının varlığını kontrol et
     */
    @Override
    public boolean existsByUserTypeName(String userTypeName) {
        if (userTypeName == null || userTypeName.trim().isEmpty()) {
            return false;
        }
        return userTypeRepository.existsByUserTypeName(userTypeName.trim());
    }

    /**
     * Rastgele bir UserType getir (güvenli yaklaşım)
     */
    @Override
    public Optional<UserType> getRandomUserType() {
        List<UserType> allUserTypes = userTypeRepository.findAll();
        if (allUserTypes.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(allUserTypes.get(random.nextInt(allUserTypes.size())));
    }
}
