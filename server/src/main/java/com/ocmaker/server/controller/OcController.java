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

    /**
     * 在前端刷新时，列出所有oc的基本信息
     * @param user_uid
     * @return
     */
    @GetMapping("/base-info")
    public Result<List<OcBaseInfoVO>> listAllOcBaseInfo(@RequestAttribute Integer user_uid) {
        log.info("列出所有oc的基本信息");
        List<OcBaseInfoVO> ocs = ocService.listAllOcBaseInfo(user_uid);
        return Result.success(ocs);
    }

    /**
     * 使用ocid查询单个oc的详细信息
     * @param ocId
     * @param user_uid
     * @return
     */
    @GetMapping("/detail/{ocId}")
    public Result<OcDetailVO> selectOcDetail(@PathVariable Integer ocId, @RequestAttribute Integer user_uid) {
        log.info("查询oc详细信息：{}", ocId);
        OcDetailVO ocDetailVO = ocService.selectOcDetail(ocId, user_uid);
        return Result.success(ocDetailVO);
    }

    /**
     * 创建属于user_uid的新oc
     * @param ocInfo
     * @param user_uid
     * @return
     */
    @PostMapping("/new")
    public Result<Integer> addNewOc(@RequestBody OcDTO ocInfo, @RequestAttribute Integer user_uid) {
        ocInfo.setUserUid(user_uid);
        log.info("创建新的oc" + ocInfo);

        ocService.addNewOc(ocInfo);
        return Result.success();
    }

    /**
     * 将ocid的信息更新
     * @param ocDetailVO
     * @param ocId
     * @return
     */
    @PutMapping("/detail/{ocId}")
    public Result<?> updateOcDetailInfo(@RequestBody OcDetailVO ocDetailVO, @PathVariable Integer ocId) {
        log.info("ID为{}的Oc信息更新为：{}", ocId, ocDetailVO);

        return ocService.updateOcDetailInfo(ocDetailVO, ocId) ? Result.success() : Result.error("");
    }

    @DeleteMapping("/delete/{ocId}")
    public Result<?> deleteOc(@PathVariable Integer ocId, @RequestAttribute Integer user_uid) {
        log.info("删除id为{}的oc", ocId);
        return ocService.deleteOc(ocId, user_uid) ? Result.success() : Result.error("");
    }
}
