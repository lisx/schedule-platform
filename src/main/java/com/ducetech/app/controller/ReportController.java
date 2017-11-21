package com.ducetech.app.controller;

import com.ducetech.app.model.Grouping;
import com.ducetech.app.service.GroupingService;
import com.ducetech.framework.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 统计报表
 */
@Controller
public class ReportController extends BaseController {
    @Autowired
    private GroupingService groupingService;

    @RequestMapping(value = "/hourReport", method = RequestMethod.GET)
    /**
     * 工时报表
     */
    public String hourReport(ModelMap model) {
        List<Grouping> groups = groupingService.queryGroupsByLength(6);
        model.put("groups", groups);
        return "/report/hourReport";
    }

    @RequestMapping(value = "/temporaryReport", method = RequestMethod.GET)
    /**
     * 临时报表
     */
    public String temporaryReport(ModelMap model) {
        List<Grouping> groups = groupingService.queryGroupsByLength(6);
        model.put("groups", groups);
        return "/report/temporaryReport";
    }
}
