package com.ocmaker.server.mapper;

import com.ocmaker.entity.Clothes;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClothesMapper {
    @Select("select * from oc_maker.clothes where clothes_oc_id = #{ocId}")
    public List<Clothes> selectAllClothesByOcId(Integer ocId);

    @Select("insert into oc_maker.clothes(name, `describe`, hat, face_decorate, uppers, belt, bottoms, leg_decorate, shoes, other_decorate, create_time, update_time, clothes_oc_id) " +
            "values (#{name}, #{describe}, #{hat}, #{faceDecorate}, #{uppers}, #{belt}, #{bottoms}, #{legDecorate}, #{shoes}, #{otherDecorate}, #{createTime}, #{updateTime}, #{clothesOcId})")
    public void insertNewClothes(Clothes clothes);

    @Select("select clothes_oc_id from oc_maker.clothes where clothes_id = #{clothesId}")
    public Integer selectClothesOcIdByClothesId(Integer clothesId);

    @Select("select * from oc_maker.clothes where clothes_id = #{clothesId}")
    public Clothes selectClothesByClothesId(Integer clothesId);

    @Update("update oc_maker.clothes set " +
            "name = #{name}," +
            "`describe` = #{describe}," +
            "hat = #{hat}," +
            "face_decorate = #{faceDecorate}," +
            "uppers = #{uppers}," +
            "belt = #{belt}," +
            "bottoms = #{bottoms}," +
            "leg_decorate = #{legDecorate}," +
            "shoes = #{shoes}," +
            "other_decorate = #{otherDecorate}," +
            "update_time = #{updateTime} " +
            "where clothes_id = #{clothesId}")
    public void updateClothesDetailInfo(Clothes clothes);

    @Delete("delete from oc_maker.clothes where clothes_id = #{clothesId}")
    public void deleteClothesByClothesId(Integer clothesId);

    @Update("update oc_maker.clothes set img_url = null where clothes_id = #{clothesId}")
    public void makeImgUrlNullByClothesId(Integer clothesId);

    @Update("update oc_maker.clothes set img_url = #{imgUrl} where clothes_id = #{clothesId}")
    public void updateImgUrl(String imgUrl, Integer clothesId);

    @Select("select img_url from oc_maker.clothes where clothes_id = #{clothesId}")
    public String selectImgUrlByClothesId(Integer clothesId);

    @Update("update oc_maker.clothes set abb_img_url = #{abbImgUrl} where clothes_id = #{clothesId}")
    public void updateAbbImgUrl(String abbImgUrl, Integer clothesId);

    @Update("update oc_maker.clothes set img_url = null where clothes_id = #{clothesId}")
    public void makeAbbImgUrlNullByClothesId(Integer clothesId);

    @Select("select abb_img_url from oc_maker.clothes where clothes_id = #{clothesId}")
    public String selectAbbImgUrlByClothesId(Integer clothesId);
}
