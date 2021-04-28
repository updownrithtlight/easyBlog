package com.technerd.easyblog.web.controller.portal;

import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/portal/home")
@Api(value = "前台标签管理控制器")
public class HomePortalController extends BaseController {


}
