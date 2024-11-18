package com.ocmaker.server.controller;

import java.util.List;
import com.ocmaker.common.result.Result;
import com.ocmaker.dto.OcDTO;
import com.ocmaker.server.service.OcService;
import com.ocmaker.vo.OcBaseInfoVO;
import com.ocmaker.vo.OcDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/oc")
@Slf4j
public class OcController {
    @Autowired
    private OcService ocService;

    @GetMapping("/base-info")
    public Result<List<OcBaseInfoVO>> listAllOcBaseInfo(@RequestAttribute Integer user_uid) {
        log.info("列出所有oc的基本信息");
        List<OcBaseInfoVO> ocs = ocService.listAllOcBaseInfo(user_uid);
        return Result.success(ocs);
    }

    @GetMapping("/detail/{ocId}")
    public Result<OcDetailVO> selectOcDetail(@PathVariable Integer ocId) {
        log.info("查询oc详细信息：{}", ocId);
        OcDetailVO ocDetailVO = ocService.selectOcDetail(ocId);
        return Result.success(ocDetailVO);
    }

    @PostMapping("/new")
    public Result<Integer> addNewOc(@RequestBody OcDTO ocInfo, @RequestAttribute Integer user_uid) {
        ocInfo.setUserUid(user_uid);
        log.info("创建新的oc" + ocInfo);

        ocService.addNewOc(ocInfo);
        return Result.success();
    }
}
