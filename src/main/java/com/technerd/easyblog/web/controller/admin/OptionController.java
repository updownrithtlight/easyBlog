package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.entity.Options;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.OptionsService;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <pre>
 *     后台设置选项控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/13
 */
@Slf4j
@RestController
@RequestMapping("/admin/option")
@Api(value = "后台设置选项控制器")
public class OptionController extends BaseController {

    @Autowired
    private OptionsService optionsService;


    @Autowired
    private LocaleMessageUtil localeMessageUtil;



    @GetMapping
    @ApiOperation("查询")
    public String options(@RequestBody Options options) {
        String oneOption = optionsService.findOneOption(options.getOptionName());

        return oneOption;
    }

   /**
    * 保存设置选项
    *
    * @param options options
    * @return JsonResult
    */
   @PostMapping(value = "/save")
   @ApiOperation(value = "保存设置选项")
   public JsonResult saveOptions(@RequestParam Map<String, String> options)  {
       optionsService.saveOptions(options);
       //刷新options
       log.info("所保存的设置选项列表：" + options);
       return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
   }
}
