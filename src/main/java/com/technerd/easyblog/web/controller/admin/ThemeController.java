package com.technerd.easyblog.web.controller.admin;

import com.technerd.easyblog.model.enums.TrueFalseEnum;
import com.technerd.easyblog.service.OptionsService;
import com.technerd.easyblog.web.controller.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *     后台主题管理控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/16
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/theme")
public class ThemeController extends BaseController {

    @Autowired
    private OptionsService optionsService;



    /**
     * 跳转到主题设置
     *
     * @param theme theme名称
     */
    @GetMapping(value = "/options")
    public String setting(Model model,
                          @RequestParam("theme") String theme,
                          @RequestParam("hasUpdate") String hasUpdate) {
        model.addAttribute("themeDir", theme);
        if (StringUtils.equals(hasUpdate, TrueFalseEnum.TRUE.getValue())) {
            model.addAttribute("hasUpdate", true);
        } else {
            model.addAttribute("hasUpdate", false);
        }
        return "themes/" + theme + "/module/options";
    }


}
