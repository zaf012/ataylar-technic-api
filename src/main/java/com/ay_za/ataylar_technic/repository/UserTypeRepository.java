package com.ay_za.ataylar_technic.repository;


import com.ay_za.ataylar_technic.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, String> {

    // Kullanıcı tiplerini getir
    List<UserType> findAll();

    // ID'ye göre UserType ara
    Optional<UserType> findById(Integer id);

    // UserType adına göre ara
    Optional<UserType> findByUserTypeName(String userTypeName);

    // UserType adının varlığını kontrol et
    boolean existsByUserTypeName(String userTypeName);

    // UserType ID'nin varlığını kontrol et
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserType u WHERE u.id = :id")
    boolean existsByUserTypeId(Integer id);

    // En yüksek ID'yi getir
    @Query("SELECT MAX(u.id) FROM UserType u")
    Optional<Integer> findMaxId();

    // Tüm grupları getir (veritabanından bağımsız)
    @Query("SELECT ut FROM UserType ut")
    List<UserType> findAllTypes();
}
