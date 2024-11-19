package com.ocmaker.server.service;

import com.ocmaker.dto.OcDTO;
import java.util.List;
import com.ocmaker.server.mapper.OcMapper;
import com.ocmaker.vo.OcBaseInfoVO;
import com.ocmaker.vo.OcDetailVO;
import org.springframework.beans.factory.annotation.Autowired;

public interface OcService {
    public void addNewOc(OcDTO ocInfo);

    public List<OcBaseInfoVO> listAllOcBaseInfo(int user_uid);

    public OcDetailVO selectOcDetail(Integer ocId, Integer userUId);

    public Boolean updateOcDetailInfo(OcDetailVO ocDetailVO, Integer ocId);

    public Boolean deleteOc(Integer ocId, Integer userUid);
}
