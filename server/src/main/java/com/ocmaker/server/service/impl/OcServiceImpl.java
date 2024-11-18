package com.ocmaker.server.service.impl;

import com.ocmaker.dto.OcDTO;
import com.ocmaker.entity.Oc;
import com.ocmaker.server.mapper.OcMapper;
import com.ocmaker.server.service.OcService;
import com.ocmaker.vo.OcBaseInfoVO;
import com.ocmaker.vo.OcDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OcServiceImpl implements OcService {

    @Autowired
    private OcMapper ocMapper;

    /*
    增加新的OC条目
    传入：OcDTO
    返回：void
     */
    @Override
    public void addNewOc(OcDTO ocInfo) {
        Oc newOc = Oc.builder()
                .name(ocInfo.getName())
                .gender(ocInfo.getGender())
                .age(ocInfo.getAge())
                .height(ocInfo.getHeight())
                .weight(ocInfo.getWeight())
                .skinColor(ocInfo.getSkinColor())
                .hair(ocInfo.getHair())
                .hairColor(ocInfo.getHairColor())
                .eyesColor(ocInfo.getEyesColor())
                .body(ocInfo.getBody())
                .face(ocInfo.getFace())
                .personality(ocInfo.getPersonality())
                .skill(ocInfo.getSkill())
                .hobby(ocInfo.getHobby())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .userUid(ocInfo.getUserUid())
                .build();
        ocMapper.addNewOc(newOc);
    }

    /**
     * 查询所有oc的基本信息
     * @param userUid
     * @return
     */
    @Override
    public List<OcBaseInfoVO> listAllOcBaseInfo(int userUid) {
        List<Oc> ocs = ocMapper.listAllOcBaseInfo(userUid);
        List<OcBaseInfoVO> ocBaseInfoVOS = new ArrayList<>();
        for (Oc oc:ocs
             ) {
            OcBaseInfoVO v0 = OcBaseInfoVO.builder()
                    .ocId(oc.getOcId())
                    .name(oc.getName())
                    .gender(oc.getGender())
                    .age(oc.getAge())
                    .build();
            ocBaseInfoVOS.add(v0);
        }
        return ocBaseInfoVOS;
    }

    /**
     * 查询单个oc的详细信息
     * @param ocId
     * @return
     */
    @Override
    public OcDetailVO selectOcDetail(Integer ocId) {
        Oc oc = ocMapper.selectOcDetail(ocId);
        OcDetailVO ocDetailVO = OcDetailVO.builder()
                .name(oc.getName())
                .gender(oc.getGender())
                .age(oc.getAge())
                .height(oc.getHeight())
                .weight(oc.getWeight())
                .skinColor(oc.getSkinColor())
                .hair(oc.getHair())
                .hairColor(oc.getHairColor())
                .eyesColor(oc.getEyesColor())
                .body(oc.getBody())
                .face(oc.getFace())
                .personality(oc.getPersonality())
                .skill(oc.getSkill())
                .hobby(oc.getHobby())
                .build();
        return  ocDetailVO;
    }
}
