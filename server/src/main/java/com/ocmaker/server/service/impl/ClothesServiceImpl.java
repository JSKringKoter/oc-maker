package com.ocmaker.server.service.impl;

import com.ocmaker.common.result.ErrorTypes;
import com.ocmaker.entity.Clothes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ocmaker.server.exception.NoSuchSourceException;
import com.ocmaker.server.exception.PermissionDeniedException;
import com.ocmaker.server.mapper.ClothesMapper;
import com.ocmaker.server.mapper.OcMapper;
import com.ocmaker.server.service.ClothesService;
import com.ocmaker.vo.ClothesBaseInfoVO;
import com.ocmaker.vo.ClothesDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClothesServiceImpl implements ClothesService {
    @Autowired
    private ClothesMapper clothesMapper;
    @Autowired
    private OcMapper ocMapper;

    /**
     * 根据ocid查询属于其的所有服装的基本信息
     * @param ocId
     * @param userUid
     * @return
     */
    @Override
    public List<ClothesBaseInfoVO> listAllClothesBaseInfo(Integer ocId, Integer userUid) {
        Integer OcUserId = ocMapper.selectUserUidByOcId(ocId);
        if (!Objects.equals(userUid, OcUserId)) {
            throw new PermissionDeniedException(ErrorTypes.PERMISSION_DENIED);
        }
        if (OcUserId == null) {
            throw new NoSuchSourceException(ErrorTypes.NO_SUCH_OC);
        }

        List<Clothes> clothes = clothesMapper.selectAllClothesByOcId(ocId);
        List<ClothesBaseInfoVO> baseInfoVOS = new ArrayList<>();
        for (Clothes clothes1:
             clothes) {
            ClothesBaseInfoVO baseInfoVO = ClothesBaseInfoVO.builder()
                    .clothesId(clothes1.getClothesId())
                    .name(clothes1.getName())
                    .imgUrl(clothes1.getImgUrl())
                    .describe(clothes1.getDescribe())
                    .build();
            baseInfoVOS.add(baseInfoVO);
        }
        return baseInfoVOS;
    }


    /**
     * 新增clothes
     * @param clothesDetail
     * @return
     */
    @Override
    public void addNewClothes(ClothesDetailVO clothesDetail) {
        Clothes clothes = Clothes.builder()
                .name(clothesDetail.getName())
                .describe(clothesDetail.getDescribe())
                .hat(clothesDetail.getHat())
                .faceDecorate(clothesDetail.getFaceDecorate())
                .uppers(clothesDetail.getUppers())
                .belt(clothesDetail.getBelt())
                .bottoms(clothesDetail.getBottoms())
                .legDecorate(clothesDetail.getLegDecorate())
                .shoes(clothesDetail.getShoes())
                .otherDecorate(clothesDetail.getOtherDecorate())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .clothesOcId(clothesDetail.getClothesOcId())
                .build();

        clothesMapper.insertNewClothes(clothes);
    }


    /**
     * 根据vo中的clothesId查询，并检查是否属于ocId
     * @param vo
     * @return
     */
    @Override
    public ClothesDetailVO selectClothesDetail(ClothesBaseInfoVO vo) {
        Integer OcId = clothesMapper.selectClothesOcIdByClothesId(vo.getClothesId());
        if (!Objects.equals(OcId, vo.getClothesOcId())) {
            throw new PermissionDeniedException(ErrorTypes.PERMISSION_DENIED);
        }
        Clothes clothes = clothesMapper.selectClothesByClothesId(vo.getClothesId());
        if (clothes == null) {
            throw new NoSuchSourceException(ErrorTypes.NO_SUCH_CLOTHES);
        }

        ClothesDetailVO detailVO = ClothesDetailVO.builder()
                .name(clothes.getName())
                .describe(clothes.getDescribe())
                .imgUrl(clothes.getImgUrl())
                .hat(clothes.getHat())
                .faceDecorate(clothes.getFaceDecorate())
                .uppers(clothes.getUppers())
                .belt(clothes.getBelt())
                .bottoms(clothes.getBottoms())
                .legDecorate(clothes.getLegDecorate())
                .shoes(clothes.getShoes())
                .otherDecorate(clothes.getOtherDecorate())
                .clothesOcId(clothes.getClothesOcId())
                .build();

        return detailVO;
    }
}
