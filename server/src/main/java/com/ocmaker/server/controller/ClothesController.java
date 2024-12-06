package com.ocmaker.server.controller;

import com.ocmaker.common.result.Result;
import com.ocmaker.server.mapper.ClothesMapper;
import com.ocmaker.server.service.ClothesService;
import com.ocmaker.vo.ClothesBaseInfoVO;
import java.util.List;

import com.ocmaker.vo.ClothesDetailVO;
import com.ocmaker.vo.CollectClothesVO;
import com.ocmaker.vo.SetFavouriteVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clothes")
@Slf4j
public class ClothesController {
    @Autowired
    private ClothesService clothesService;

    /**
     * 查询ocId的所有服装的基本信息
     * @param ocId
     * @param user_uid
     * @return
     */
    @GetMapping("/base-info/{ocId}")
    public Result<List<ClothesBaseInfoVO>> listAllClothesBaseInfo(@PathVariable Integer ocId, @RequestAttribute Integer user_uid) {
        log.info("查询ocId为{}的所有服装的基本信息", ocId);
        List<ClothesBaseInfoVO> baseInfoVOS = clothesService.listAllClothesBaseInfo(ocId, user_uid);
        return Result.success(baseInfoVOS);
    }

    /**
     * 为ocId新增服装
     * @param vo
     * @return
     */
    @PostMapping("/new")
    public Result<?> addNewClothes(@RequestBody ClothesDetailVO vo) {
        log.info("新增ocid为{}的服装：{}", vo.getClothesOcId(), vo);
        clothesService.addNewClothes(vo);
        return Result.success();
    }

    /**
     * 根据BaseInfoVO查询详细信息
     * @param vo
     * @return
     */
    @PostMapping("/detail")
    public Result<ClothesDetailVO> selectClothesDetail(@RequestBody ClothesBaseInfoVO vo) {
        log.info("查询ocId为{}的clothesId为{}的详细信息", vo.getClothesOcId(), vo.getClothesId());
        ClothesDetailVO detailVO = clothesService.selectClothesDetail(vo);
        return Result.success(detailVO);
    }

    /**
     * 根据clothesID删除服装
     * @param vo
     * @return
     */
    @PostMapping("/update")
    public Result<?> updateClothesDetailInfo(@RequestBody ClothesDetailVO vo) {
        log.info("将ocid为{}的clothesid为{}的服装更新为{}", vo.getClothesOcId(), vo.getClothesId(), vo);

        return clothesService.updateClothesDetailInfo(vo) ? Result.success() : Result.error("");
    }

    /**
     * 收藏或者取消收藏
     * @param vo
     * @return
     */
    @PostMapping("/collect")
    public Result<?> collectOrUncollectClothes(@RequestBody CollectClothesVO vo) {
        log.info("将ocid为{}的clothesid为{}的收藏属性更新为{}", vo.getClothesOcId(), vo.getClothesId(), vo.isCollect());
        return clothesService.updateIsCollect(vo.isCollect(), vo.getClothesId()) ? Result.success() : Result.error("");
    }

    /**
     * 修改招牌服装
     * @param vo
     * @return
     */
    @PostMapping("/setAsFavourite")
    public Result<?> setAsFavourite(@RequestBody SetFavouriteVO vo) {
        log.info("将ocid为{}的招牌服装修改为{}", vo.getOcId(), vo.getClothesId());
        return clothesService.setFavouriteClothes(vo.getOcId(), vo.getClothesId()) ? Result.success() : Result.error("");
    }

    /**
     * 根据clothesID删除服装
     * @param vo
     * @return
     */
    @PostMapping("/delete")
    public Result<?> deleteClothes(@RequestBody ClothesBaseInfoVO vo) throws Exception {
        log.info("将ocid为{}的clothesId为{}的服装删除", vo.getClothesOcId(), vo.getClothesId());
        return clothesService.deleteClothes(vo) ? Result.success() : Result.error("");
    }

}
