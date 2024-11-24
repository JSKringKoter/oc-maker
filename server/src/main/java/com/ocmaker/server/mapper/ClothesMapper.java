package com.ocmaker.server.mapper;

import com.ocmaker.entity.Clothes;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
