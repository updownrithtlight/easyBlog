package com.technerd.easyblog.web.controller.portal;

import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Slide;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.enums.SlideTypeEnum;
import com.technerd.easyblog.service.SlideService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *     前台台幻灯片管理控制器
 * </pre>
 *
 * @author : technerd
 * @date : 2018/1/30
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/slide")
@Api(value = "前台幻灯片管理控制器")
public class SlidePortalController extends BaseController {

    @Autowired
    private SlideService slideService;

    @Autowired
    LocaleMessageUtil localeMessageUtil;
    /**
     * 幻灯片列表
     * @return
     */
    @GetMapping
    @ApiOperation(value = "幻灯片列表")
    public JsonResult<List<Slide>> slides() {
        //前台主要幻灯片
        List<Slide> slides = slideService.findBySlideType(SlideTypeEnum.INDEX_SLIDE.getCode());
        return new JsonResult<List<Slide>>(ResultCodeEnum.SUCCESS.getCode(),  localeMessageUtil.getMessage("code.admin.common.query-success"),slides);
    }



    /**
     * 查询详情
     * @param slideId
     * @return
     */
    @GetMapping(value = "/get")
    @ApiOperation("查询详情")
    public JsonResult<Slide> get(@RequestParam("id") Long slideId) {
        Slide slide = slideService.get(slideId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(),  localeMessageUtil.getMessage("code.admin.common.query-success"),slide);
    }


}
