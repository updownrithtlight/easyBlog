package com.technerd.easyblog.web.controller.portal;

import com.technerd.easyblog.entity.Category;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.vo.CommonEnum;
import com.technerd.easyblog.service.CategoryService;
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
 *     前台分类管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/portal/category")
@Api(value = "前台分类管理控制器")
public class CategoryPortalController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/list")
    @ApiOperation("查询所有分类")
    public JsonResult<List<Category>> categories(@RequestParam Long userId) {
        List<Category> categories = categoryService.findByUserId(userId);
        return new JsonResult<List<Category>>(CommonEnum.SUCCESS.getCode(),categories) ;
    }


    /**
     * 查询详情
     *
     * @param cateId
     */
    @GetMapping(value = "/getOne/{cateId}")
    @ApiOperation("查询详情")
    public JsonResult<Category> getCategory( @PathVariable("cateId") Long cateId) {
        Category category = categoryService.get(cateId);
      return new JsonResult<Category>(CommonEnum.SUCCESS.getCode(),category) ;
    }
}
