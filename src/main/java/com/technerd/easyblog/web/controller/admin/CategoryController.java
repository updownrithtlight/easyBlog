package com.technerd.easyblog.web.controller.admin;
import com.technerd.easyblog.service.CategoryService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * <pre>
 *     后台分类管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/10
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/category")
@Api(value = "分类")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     * 查询所有分类并渲染category页面
     *
     * @return 模板路径admin/admin_category
     */
    @GetMapping
    @ApiOperation(value = "v")
    public String categories() {


        return "admin/admin_category";
    }







}
