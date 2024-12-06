package com.ocmaker.server.service;

import com.ocmaker.vo.ClothesBaseInfoVO;
import com.ocmaker.vo.ClothesDetailVO;

import java.util.List;

public interface ClothesService {
    public List<ClothesBaseInfoVO> listAllClothesBaseInfo(Integer ocId, Integer userUid);

    public void addNewClothes(ClothesDetailVO clothesDetail);

    public ClothesDetailVO selectClothesDetail(ClothesBaseInfoVO vo);

    public boolean updateClothesDetailInfo(ClothesDetailVO vo);

    public boolean deleteClothes(ClothesBaseInfoVO vo) throws Exception;

    public boolean updateIsCollect(boolean isCollect, Integer clothesId);

    public boolean setFavouriteClothes(Integer ocId, Integer clothesId);

    public void updateUrl(String imgUrl, Integer clothesId);

    public void updateAbbImgUrl(String addImgUrl, Integer clothesId);

    public String selectImgUrlByClothesId(Integer clothesId);

    public String selectAbbImgUrlByClothesId(Integer clothesId);

    public void makeImgUrlNullByClothesId(Integer clothesId);

    public void makeAbbImgUrlNullByClothesId(Integer clothesId);

}
