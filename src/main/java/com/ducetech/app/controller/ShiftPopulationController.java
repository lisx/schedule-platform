package com.ducetech.app.controller;

import com.ducetech.app.service.ShiftPopulationService;
import com.ducetech.app.model.vo.ShiftPopulationVO;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 24小时人员表
 * Created by BLUE on 2017/5/22.
 */
@Controller
public class ShiftPopulationController extends BaseController {

    @Autowired
    private ShiftPopulationService shiftPopulationService;

    /**
     * 添加默认排班人数表
     * @param shiftPopulationVO
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addShiftPopulation", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addShiftPopulation(ShiftPopulationVO shiftPopulationVO) throws Exception {
        shiftPopulationVO.setStartAt(0);
        shiftPopulationVO.setEndAt(1440);
        shiftPopulationVO.setPopulationCount(0);
        Integer populationId = shiftPopulationService.insertShiftPopulation(shiftPopulationVO);

        //将id返回给页面
        return OperationResult.buildSuccessResult(String.valueOf(populationId), "success");
    }

    /**
     * 修改排版表
     * @param shiftPopulationVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateShiftPopulation", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult updateShiftPopulation(ShiftPopulationVO shiftPopulationVO, HttpServletRequest request) {
        shiftPopulationVO.setStartAt(DateUtil.timeToMinu(shiftPopulationVO.getStartAtStr()));

        String shiftEndAtStr = shiftPopulationVO.getEndAtStr();

        if("00:00".equals(shiftEndAtStr)) {
            shiftEndAtStr = "24:00";
        }

        shiftPopulationVO.setEndAt(DateUtil.timeToMinu(shiftEndAtStr));
        shiftPopulationService.updateShiftPopulation(shiftPopulationVO);
        return OperationResult.buildSuccessResult("修改成功", "success");
    }

    /**
     * 删除排班人数表
     * @param shiftPopulationVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteShiftPopulation", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult deleteShiftPopulation (ShiftPopulationVO shiftPopulationVO, HttpServletRequest request) {
        shiftPopulationService.deleteShiftPopulation(shiftPopulationVO);
        return OperationResult.buildSuccessResult("删除成功", "success");
    }

    /**
     * 获取某一岗位的某一班制的人数集合
     * @param shiftPopulationVO
     * @param request
     * @return
     */
    @RequestMapping(value = "/getShiftPopulation", method = RequestMethod.GET)
    @ResponseBody
    public List<ShiftPopulationVO> getShiftPopulation (ShiftPopulationVO shiftPopulationVO, HttpServletRequest request) {
        List<ShiftPopulationVO> ssvos =  shiftPopulationService.getShiftPopulation(shiftPopulationVO);
        return ssvos;
    }
}
