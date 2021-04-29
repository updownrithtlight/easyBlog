package com.technerd.easyblog.web.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.technerd.easyblog.config.annotation.SystemLog;
import com.technerd.easyblog.entity.Log;
import com.technerd.easyblog.entity.Permission;
import com.technerd.easyblog.entity.RolePermissionRef;
import com.technerd.easyblog.entity.UserRoleRef;
import com.technerd.easyblog.model.dto.JsonResult;
import com.technerd.easyblog.model.enums.LogTypeEnum;
import com.technerd.easyblog.model.enums.ResultCodeEnum;
import com.technerd.easyblog.service.*;
import com.technerd.easyblog.utils.LocaleMessageUtil;
import com.technerd.easyblog.utils.PageUtil;
import com.technerd.easyblog.web.controller.common.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.management.*;
import java.util.List;

/**
 * <pre>
 *     后台首页控制器
 * </pre>
 *
 * @author : saysky
 * @date : 2017/12/5
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin")
@Api(value = "后台首页控制器")
public class AdminController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleRefService userRoleRefService;
    @Autowired
    private RolePermissionRefService rolePermissionRefService;
    @Autowired
    private LocaleMessageUtil localeMessageUtil;

    /**
     *
     * @param permission
     * @return
     */
    @PostMapping(value = "/permissionOf")
    @SystemLog(description = "保存权限与角色关联", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "新增/修改权限")
    public JsonResult savePermission(@RequestBody RolePermissionRef permission) {
        super.save(permission);
        rolePermissionRefService.saveByRolePermissionRef(permission);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }

    /**
     *
     * @param roleRef
     * @return
     */
    @PostMapping(value = "/roleOf")
    @SystemLog(description = "保存用户角色关联", type = LogTypeEnum.OPERATION)
    @ApiOperation(value = "新增/修改用户角色关联")
    public JsonResult saveUseroleRef(@RequestBody UserRoleRef roleRef) {
        super.save(roleRef);
        userRoleRefService.insert(roleRef);
        return new JsonResult(ResultCodeEnum.SUCCESS.getCode(), localeMessageUtil.getMessage("code.admin.common.save-success"));
    }

    /**
     * 查看memory
     *
     * @return
     */
    @GetMapping("/memory")
    @ResponseBody
    public String memory() {

        StringBuilder sb = new StringBuilder();

        MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memorymbean.getHeapMemoryUsage();
        sb.append("INIT HEAP: " + usage.getInit() / 1024 / 2024 + "MB\n");
        sb.append("MAX HEAP: " + usage.getMax() / 1024 / 2024 + "MB\n");
        sb.append("USE HEAP: " + usage.getUsed() / 1024 / 2024 + "MB\n");
        sb.append("\nFull Information:");
        sb.append("Heap Memory Usage: "
                + memorymbean.getHeapMemoryUsage() + "\n");
        sb.append("Non-Heap Memory Usage: "
                + memorymbean.getNonHeapMemoryUsage() + "\n");

        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        sb.append("===================java options=============== \n");
        sb.append(inputArguments + "\n");


        sb.append("=======================通过java来获取相关系统状态============================ \n");
        //Java 虚拟机中的内存总量,以字节为单位
        int i = (int) Runtime.getRuntime().totalMemory() / 1024 / 1024;
        sb.append("总的内存量：" + i + "MB\n");
        //Java 虚拟机中的空闲内存量
        int j = (int) Runtime.getRuntime().freeMemory() / 1024 / 1024;
        sb.append("空闲内存量：" + j + "MB\n");
        sb.append("最大内存量： " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB\n");

        sb.append("=======================OperatingSystemMXBean============================ \n");
        OperatingSystemMXBean osm = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        //获取操作系统相关信息
        sb.append("osm.getArch() " + osm.getArch() + "\n");
        sb.append("osm.getAvailableProcessors() " + osm.getAvailableProcessors() + "\n");
        sb.append("osm.getName() " + osm.getName() + "\n");
        sb.append("osm.getVersion() " + osm.getVersion() + "\n");
        //获取整个虚拟机内存使用情况
        sb.append("=======================MemoryMXBean============================ \n");
        MemoryMXBean mm = (MemoryMXBean) ManagementFactory.getMemoryMXBean();
        sb.append("getHeapMemoryUsage " + mm.getHeapMemoryUsage() + "\n");
        sb.append("getNonHeapMemoryUsage " + mm.getNonHeapMemoryUsage() + "\n");
        //获取各个线程的各种状态，CPU 占用情况，以及整个系统中的线程状况
        sb.append("=======================ThreadMXBean============================ \n");
        ThreadMXBean tm = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        sb.append("getThreadCount " + tm.getThreadCount() + "\n");
        sb.append("getPeakThreadCount " + tm.getPeakThreadCount() + "\n");
        sb.append("getCurrentThreadCpuTime " + tm.getCurrentThreadCpuTime() + "\n");
        sb.append("getDaemonThreadCount " + tm.getDaemonThreadCount() + "\n");
        sb.append("getCurrentThreadUserTime " + tm.getCurrentThreadUserTime() + "\n");

        //当前编译器情况
        sb.append("=======================CompilationMXBean============================ \n");
        CompilationMXBean gm = (CompilationMXBean) ManagementFactory.getCompilationMXBean();
        sb.append("getName " + gm.getName() + "\n");
        sb.append("getTotalCompilationTime " + gm.getTotalCompilationTime() + "\n");

        //获取多个内存池的使用情况
        sb.append("=======================MemoryPoolMXBean============================ \n");
        List<MemoryPoolMXBean> mpmList = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mpm : mpmList) {
            sb.append("getUsage " + mpm.getUsage() + "\n");
            sb.append("getMemoryManagerNames " + mpm.getMemoryManagerNames().toString() + "\n");
        }
        //获取GC的次数以及花费时间之类的信息
        sb.append("=======================MemoryPoolMXBean============================ \n");
        List<GarbageCollectorMXBean> gcmList = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcm : gcmList) {
            sb.append(gcm.getName() + "\n");
        }
        //获取运行时信息
        sb.append("=======================RuntimeMXBean============================ \n");
        RuntimeMXBean rmb = (RuntimeMXBean) ManagementFactory.getRuntimeMXBean();
        sb.append("getClassPath " + rmb.getClassPath() + "\n");
        sb.append("getLibraryPath " + rmb.getLibraryPath() + "\n");
        sb.append("getVmVersion " + rmb.getVmVersion() + "\n");

        return sb.toString();
    }
}
