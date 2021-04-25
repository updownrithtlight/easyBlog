package com.technerd.easyblog.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Category;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.model.vo.CommonEnum;
import com.technerd.easyblog.model.vo.SearchVo;
import com.technerd.easyblog.service.CategoryService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.utils.RedisUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <pre>
 *     后台分类管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/category")
@Api(value = "后台分类管理控制器")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     *
     * @param searchVo
     * @return
     */
    @PostMapping(value = "/list")
    @ApiOperation("查询所有分类")
    public JsonResult<Page<Category>> categories(@RequestBody SearchVo searchVo) {
        Long userId = getLoginUserId();
        Page page = PageUtil.initMpPage(searchVo);
        Page<Category> categoryPage = categoryService.findByUserIdWithCountAndLevel(userId, page);
        return new JsonResult<Page<Category>>(CommonEnum.SUCCESS.getCode(),categoryPage) ;
    }

    /**
     *
     * @param category
     * @return
     */
    @PostMapping(value = "/save")
    @SystemLog(description = "新增/修改分类目录", type = LogTypeEnum.OPERATION)
    @ApiOperation("新增/修改分类目录")
    public JsonResult saveCategory(@RequestBody Category category) {
//        if(category.getId() != null) {
//            //1.判断id是否为当前用户
//            Category checkId = categoryService.get(category.getId());
//            if (checkId != null && !Objects.equals(checkId.getUserId(), userId)) {
//                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
//            }
//
//            //2.判断pid是否为该用户
//            Category checkPid = categoryService.get(category.getCatePid());
//            if (checkPid != null && !Objects.equals(checkPid.getUserId(), userId)) {
//                return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
//            }
//        }
        //3.do
        categoryService.insertOrUpdate(category);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }

    /**
     * 删除分类
     *
     * @param cateId 分类Id
     * @return JsonResult
     */
    @GetMapping(value = "/delete")
    @SystemLog(description = "删除分类", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "删除分类")
    public JsonResult checkDelete(@RequestParam("id") Long cateId) {
        //1.判断这个分类是否属于该用户
        Long userId = getLoginUserId();
        Category category = categoryService.get(cateId);
        if(!Objects.equals(category.getUserId(), userId)) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.common.permission-denied"));
        }
//        //2.判断这个分类有没有文章
//        Integer postCount = categoryService.countPostByCateId(cateId);
//        if (postCount != 0) {
//            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.category.first-delete-post"));
//        }
        //3.判断这个分类有没有子分类
        Integer childCount = categoryService.selectChildCateId(cateId).size();
        if (childCount != 0) {
            return new JsonResult(ResultCodeEnum.FAIL.getCode(), localeMessageUtil.getMessage("code.admin.category.first-delete-child"));
        }
        //4.do
        categoryService.delete(cateId);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.delete-success"));
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
