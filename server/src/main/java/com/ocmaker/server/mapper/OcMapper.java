package com.ocmaker.server.mapper;

import com.ocmaker.entity.Oc;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OcMapper {
    @Insert("INSERT INTO oc_maker.oc(name, gender, age, height, weight, skin_color, hair, hair_color, eyes_color, body, face, personality, skill, hobby, create_time, update_time, user_uid) " +
            "VALUES(#{name}, #{gender}, #{age}, #{height}, #{weight}, #{skinColor}, #{hair}, #{hairColor}, #{eyesColor}, #{body}, #{face}, #{personality}, #{skill}, #{hobby}, #{createTime}, #{updateTime}, #{userUid})")
    public void addNewOc(Oc newOcInfo);

    @Select("select * from oc_maker.oc where user_uid = #{userUid}")
    public List<Oc> listAllOcBaseInfo(int userUid);

    @Select("select * from oc_maker.oc where oc_id = #{ocID}")
    public Oc selectOcDetail(Integer ocId);
}
