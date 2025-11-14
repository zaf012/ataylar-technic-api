package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.BlocksInfoDto;
import com.ay_za.ataylar_technic.entity.BlocksInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BlocksInfoServiceImpl {
    BlocksInfoDto createBlock(BlocksInfoDto blocksInfoDto);

    @Transactional
    BlocksInfoDto updateBlock(String id, BlocksInfoDto blocksInfoDto);

    @Transactional
    void deleteBlock(String id);

    List<BlocksInfoDto> getAllBlocks();

    Optional<BlocksInfo> getRandomBlock();
}
