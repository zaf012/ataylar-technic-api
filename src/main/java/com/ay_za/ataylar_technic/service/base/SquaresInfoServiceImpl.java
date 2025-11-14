package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.SquaresInfoDto;
import com.ay_za.ataylar_technic.entity.SquaresInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SquaresInfoServiceImpl {
    SquaresInfoDto createSquare(SquaresInfoDto squaresInfoDto);

    @Transactional
    SquaresInfoDto updateSquare(String id, SquaresInfoDto squaresInfoDto);

    @Transactional
    void deleteSquare(String id);

    List<SquaresInfoDto> getAllSquares();

    Optional<SquaresInfo> getRandomSquare();
}
