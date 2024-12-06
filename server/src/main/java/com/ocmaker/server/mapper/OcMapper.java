package com.ocmaker.server.mapper;

import com.ocmaker.entity.Oc;
import java.util.List;

import com.ocmaker.vo.OcDetailVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OcMapper {

    @Insert("INSERT INTO oc_maker.oc(name, gender, age, height, weight, skin_color, hair, hair_color, eyes_color, body, face, personality, skill, hobby, create_time, update_time, user_uid) " +
            "VALUES(#{name}, #{gender}, #{age}, #{height}, #{weight}, #{skinColor}, #{hair}, #{hairColor}, #{eyesColor}, #{body}, #{face}, #{personality}, #{skill}, #{hobby}, #{createTime}, #{updateTime}, #{userUid})")
    public void addNewOc(Oc newOcInfo);

    @Select("select user_uid from oc_maker.oc where oc_id = #{ocId}")
    public Integer selectUserUidByOcId(Integer ocId);

    @Select("select * from oc_maker.oc where user_uid = #{userUid}")
    public List<Oc> listAllOcBaseInfo(int userUid);

    @Select("select * from oc_maker.oc where oc_id = #{ocID}")
    public Oc selectOcDetail(Integer ocId);

    @Update("UPDATE oc_maker.oc SET " +
            "name = #{oc.name}, " +
            "gender = #{oc.gender}, " +
            "age = #{oc.age}, " +
            "height = #{oc.height}, " +
            "weight = #{oc.weight}, " +
            "skin_color = #{oc.skinColor}, " +
            "hair = #{oc.hair}, " +
            "hair_color = #{oc.hairColor}, " +
            "eyes_color = #{oc.eyesColor}, " +
            "body = #{oc.body}, " +
            "face = #{oc.face}, " +
            "personality = #{oc.personality}, " +
            "skill = #{oc.skill}, " +
            "update_time = #{oc.updateTime}," +
            "hobby = #{oc.hobby} " +
            "WHERE oc_id = #{ocId}")
    public void updateOcDetailInfo(Oc oc, Integer ocId);

    @Update("update oc_maker.oc set favourite_clothes_id = #{clothesId} where oc_id = #{ocId}")
    public void setFavouriteClothesById(Integer clothesId, Integer ocId);

    @Delete("delete from oc_maker.oc where oc_id = #{ocId}")
    public void deleteOc(Integer ocId);
}
