package com.ducetech.app.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.*;
import com.ducetech.app.model.vo.ChangeShiftVO;
import com.ducetech.app.model.vo.UserScheduleInfo;
import com.ducetech.app.service.*;
import com.ducetech.app.support.service.ScheduleCalculator;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.util.PictureUtil;
import com.ducetech.framework.util.PoiUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: MainPageController
 * @Description: 系统主页Controller
 * @date 2016年9月18日 下午5:38:26
 */
@Controller
public class ScheduleInfoController extends BaseController {
    @Autowired
    private GroupingService groupingService;
    @Autowired
    private ShiftModelService shiftModelService;
    @Autowired
    private ShiftSettingService shiftService;
    @Autowired
    private UserService userService;
    @Autowired
    private ScheduleInfoService scheduleInfoService;
    @Autowired
    private ScheduleLogService scheduleLogService;
    @Autowired
    private PostSettingService postSettingService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WorkflowContentService workflowContentService;

    private Logger logger = LoggerFactory.getLogger(ScheduleInfoController.class);

    /**
     * 排班表格
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/scheduleForm", method = RequestMethod.GET)
    public String scheduleForm(ModelMap model) {
        List<Grouping> groups = groupingService.queryGroupsByLength(6);
        model.put("groups", groups);
        return "/scheduleInfo/scheduleForm";
    }

    /**
     * 排班站区表格
     *
     * @return
     */
    @RequestMapping(value = "/scheduleAreaForm", method = RequestMethod.GET)
    public String scheduleAreaForm() {
        return "/scheduleInfo/scheduleAreaForm";
    }

    /**
     * 排班按人员分组
     *
     * @param collection
     * @param propertyName
     * @param clazz
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> Map<K, List<T>> groupByProperty(Collection<T> collection, String propertyName,
                                                         Class<K> clazz) {
        Map<K, List<T>> map = new HashMap<K, List<T>>();
        for (T item : collection) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(item);
            @SuppressWarnings("unchecked")
            K key = (K) beanWrapper.getPropertyValue(propertyName);
            List<T> list = map.get(key);
            if (null == list) {
                list = new ArrayList<T>();
                if (key != null)
                    map.put(key, list);
            }
            list.add(item);
//            Collections.sort(list, new Comparator() {
//                @Override
//                public int compare(Object o1, Object o2) {
//                    ScheduleInfo stu1 = (ScheduleInfo) o1;
//                    ScheduleInfo stu2 = (ScheduleInfo) o2;
//                    return stu1.getCreatedAt().compareTo(stu2.getCreatedAt());
//                }
//            });
        }
        return map;
    }

    /**
     * 新增排班按钮
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/automaticScheduling", method = RequestMethod.GET)
    public String automaticScheduling(ModelMap model,HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        Grouping grouping = groupingService.selectGroupingByGroupCode(loginUser.getStationArea());
        List<Grouping> groupingList = groupingService.selectByParentCode(grouping.getGroupCode(), 9);
        model.put("groupingList", groupingList);
        model.put("stationArea", grouping);
        return "/scheduleInfo/automaticScheduling";
    }

    /**
     * 站区中>站点按钮
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/loadGroup", method = RequestMethod.GET)
    public
    @ResponseBody
    Object loadGroupInfo(String groupId) {
        long start = System.currentTimeMillis();
        try {
            Grouping group = groupingService.selectGroupingByGroupId(groupId);
            String groupCode = group.getGroupCode();
            List<PostSetting> posts = postSettingService.getAllPostSettings();


            JSONObject result = new JSONObject();
            JSONArray shiftList = new JSONArray();
            result.put("models", shiftList);

            Map<String, Integer> map = new HashMap<>();
            List<User> userList = userService.selectUserByStation(group.getGroupCode());
            for (User user : userList) {
                map.put(user.getUserJob(), map.containsKey(user.getUserJob()) ? (map.get(user.getUserJob()) + 1) : 1);
            }
            for(PostSetting p:posts){
                if (map.containsKey(p.getPostCode())){
                    p.setUserCount(map.get(p.getPostCode()));
                }else {
                    p.setUserCount(0);
                }
                List<ShiftModel> list = shiftModelService.selectShiftModelByPostGroup(p.getPostId(), groupCode);
                if (list != null && list.size() > 0) {
                    for(ShiftModel m:list){
                        JSONObject o=new JSONObject();
                        o.put("modelId",m.getModelId());
                        o.put("modelName",m.getModelName());
                        shiftList.add(o);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            logger.error("error:", e);
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        return "/error";
    }

    /**
     * 导出
     *
     * @param startAt
     * @param endAt
     * @param monthDate
     * @param queryData
     * @param response
     * @throws ParseException
     */
    @RequestMapping(value = "/printSchedule", method = RequestMethod.GET)
    public void printSchedule(String startAt, String endAt, String monthDate,
                              String queryData, String scheduleType, String stationArea, String station, HttpServletResponse response) throws ParseException {
        String userName = null;
        QueryDate queryDate = new QueryDate(startAt, endAt, monthDate).invoke();
        startAt = queryDate.getStartAt();
        endAt = queryDate.getEndAt();
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(startAt, endAt, queryData, queryData, queryData, station, stationArea);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(queryData + "表格");

        HSSFRow row;
        HSSFCellStyle style = PoiUtil.getDefaultHssfCellStyle(wb);
        Map<String, List<ScheduleInfo>> map = null;
        Map<String, List<ScheduleInfo>> temp = null;
        if (scheduleType.equals("0")) {
            temp = groupByProperty(sis, "userId", String.class);
        } else {
            temp = groupByProperty(sis, "groupName", String.class);
        }
        List<ScheduleInfo> sort=scheduleInfoService.selectScheduleInfoSort(startAt, endAt, queryData, queryData, queryData, station, stationArea);
        for(ScheduleInfo info:sort){
            List<ScheduleInfo> infoTemp=temp.get(info.getUserId());
            map.put(info.getUserId(),infoTemp);
        }
        HSSFCell cell;
        HSSFCellStyle greenStyle = wb.createCellStyle();
        greenStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        greenStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        greenStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        HSSFCellStyle yellowStyle = wb.createCellStyle();
        yellowStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        yellowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        yellowStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
        HSSFCellStyle redStyle = wb.createCellStyle();
        redStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        redStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        redStyle.setFillForegroundColor(HSSFColor.RED.index);
        int rownum;
        String postName = null;
        List<ShiftSetting> shiftList = null;
        if (null != map && map.size() > 0) {
            String user = null;
            for (String key : map.keySet()) {
                user = key;
                break;
            }
            for (rownum = 0; rownum < 2; rownum++) {
                row = sheet.createRow(rownum);
                List<ScheduleInfo> list = map.get(user);
                int size = list.size();
                if (size > 0)
                    shiftList = shiftService.selectShiftSettingByModelId(list.get(0).getModelId());
                sheet.setColumnWidth(size + 5 + shiftList.size(), 25 * 256);//设置第一列的宽度是31个字符宽度

                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 0, size + 2, size + 5 + shiftList.size()), sheet, wb);
                RegionUtil.setBorderTop(1, new CellRangeAddress(0, 0, size + 2, size + 5 + shiftList.size()), sheet, wb);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, size + 2, size + 5 + shiftList.size()));
                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 1, 1, 1), sheet, wb);
                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 1, 0, 0), sheet, wb);
                RegionUtil.setBorderLeft(1, new CellRangeAddress(0, 1, 1, 1), sheet, wb);
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

                if (rownum == 0) {
                    cell = row.createCell(0);
                    cell.setCellValue("姓名");
                    cell.setCellStyle(style);
                    cell = row.createCell(1);
                    cell.setCellValue("岗位");
                    cell.setCellStyle(style);
                    for (int i = 0; i < size; i++) {
                        cell = row.createCell(i + 2);
                        cell.setCellValue(DateUtil.formatDate(list.get(i).getScheduleDate(), DateUtil.XIZHIMENDATE));
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(size + 2);
                    cell.setCellValue("共计" + size + "天");
                    cell.setCellStyle(style);
                } else if (rownum == 1) {
                    for (int i = 0; i < size; i++) {
                        cell = row.createCell(i + 2);
                        cell.setCellValue(list.get(i).getScheduleWeek());
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(size + 2);
                    cell.setCellValue("计划工时");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 3);
                    cell.setCellValue("实际工时");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 4);
                    cell.setCellValue("结余");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 5 + shiftList.size());
                    cell.setCellValue("员工工时确认签字");
                    cell.setCellStyle(style);
                }
            }
            rownum = 1;
            for (String userId : map.keySet()) {
                List<ScheduleInfo> list = map.get(userId);
                int planHours = scheduleInfoService.selectScheduleInfoSum(startAt, endAt, userId);
                int actualHours = 0;
                for (ScheduleInfo info : list) {
                    actualHours += scheduleLogService.selectScheduleLogSum(info.getScheduleInfoId());
                }
                int size = 0;
                if (null != list && list.size() > 0) {
                    postName = list.get(0).getPostName();
                    userName = list.get(0).getUserName();
                    size = list.size();
                }
                Map<String, Integer> shift = new HashMap<>();
                for (int i = 0; i < shiftList.size(); i++) {
                    int count = scheduleInfoService.selectScheduleInfoGroup(startAt, endAt, userId, shiftList.get(i).getShiftName());
                    shift.put(shiftList.get(i).getShiftName(), count);
                    System.out.println(userName + shiftList.get(i).getShiftName() + "+" + count);
                }

                if (postName.equals("站务员") && scheduleType.equals("0")) {
                    rownum = PoiUtil.getRownum(userName, sheet, style, greenStyle, yellowStyle, redStyle, rownum, postName, list, planHours, actualHours, size, shift, shiftList);
                } else if (!postName.equals("站务员") && scheduleType.equals("1")) {
                    rownum = PoiUtil.getRownum(userName, sheet, style, greenStyle, yellowStyle, redStyle, rownum, postName, list, planHours, actualHours, size, shift, shiftList);
                }

            }
        }
        responsePrint(response, wb);
    }


    /**
     * 向页面输出poi
     *
     * @param response
     * @param wb
     */
    private void responsePrint(HttpServletResponse response, HSSFWorkbook wb) {
        OutputStream os = null;
        try {
            String fileName = "排班列表" + DateUtil.formatDate(new Date(), DateUtil.XIZHIMENDATE) + ".xls";
            os = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            wb.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 西直门站务员导出
     *
     * @param startAt
     * @param endAt
     * @param monthDate
     * @param queryData
     * @param response
     * @throws ParseException
     */
    @RequestMapping(value = "/printXiZhiMenSchedule", method = RequestMethod.GET)
    public void printXiZhiMenSchedule(String startAt, String endAt, String monthDate,
                                      String queryData, String scheduleType, String stationArea, String station, HttpServletResponse response) throws ParseException {
        String userName = null;
        QueryDate queryDate = new QueryDate(startAt, endAt, monthDate).invoke();
        startAt = queryDate.getStartAt();
        endAt = queryDate.getEndAt();
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(startAt, endAt, queryData, queryData, queryData, station, stationArea);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("西直门站务员");

        HSSFRow row;
        HSSFCellStyle style = PoiUtil.getDefaultHssfCellStyle(wb);
        Map<String, List<ScheduleInfo>> map = new LinkedHashMap();
        Map<String, List<ScheduleInfo>> temp = null;
        if (scheduleType.equals("0")) {
            temp = groupByProperty(sis, "userId", String.class);
        } else {
            temp = groupByProperty(sis, "groupName", String.class);
        }
        List<ScheduleInfo> sort=scheduleInfoService.selectScheduleInfoSort(startAt, endAt, queryData, queryData, queryData, station, stationArea);
        for(ScheduleInfo info:sort){
            List<ScheduleInfo> infoTemp=temp.get(info.getUserId());
            map.put(info.getUserId(),infoTemp);
        }
        HSSFCell cell;
        int rownum;
        String postName = null;
        List<ShiftSetting> shiftList = null;
        if (null != map && map.size() > 0) {
            String user = null;
            for (String key : map.keySet()) {
                user = key;
                break;
            }
            for (rownum = 0; rownum < 2; rownum++) {
                row = sheet.createRow(rownum);
                List<ScheduleInfo> list = map.get(user);
                int size = list.size();
                if (size > 0)
                    shiftList = shiftService.selectShiftSettingByModelId(list.get(0).getModelId());
                sheet.setColumnWidth(size + 4, 25 * 256);//设置第一列的宽度是31个字符宽度
                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 0, size + 1, size + 4), sheet, wb);
                RegionUtil.setBorderTop(1, new CellRangeAddress(0, 0, size + 1, size + 4), sheet, wb);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, size + 1, size + 4));
                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 1, 1, 1), sheet, wb);
                RegionUtil.setBorderRight(1, new CellRangeAddress(0, 1, 0, 0), sheet, wb);
                RegionUtil.setBorderLeft(1, new CellRangeAddress(0, 1, 1, 1), sheet, wb);
                //sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
                sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

                if (rownum == 0) {
//                    cell = row.createCell(0);
//                    cell.setCellValue("班制");
//                    cell.setCellStyle(style);
//                    cell = row.createCell(1);
//                    cell.setCellValue("工作时间");
//                    cell.setCellStyle(style);
                    cell = row.createCell(0);
                    cell.setCellValue("日期");
                    cell.setCellStyle(style);
                    for (int i = 0; i < size; i++) {
                        cell = row.createCell(i + 1);
                        cell.setCellValue(DateUtil.formatDate(list.get(i).getScheduleDate(), DateUtil.XIZHIMENDATE));
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(size + 1);
                    cell.setCellValue("共计" + size + "天");
                    cell.setCellStyle(style);
                } else if (rownum == 1) {
//                    cell = row.createCell(1);
//                    cell.setCellValue(startAt + "---" + endAt);
//                    cell.setCellStyle(style);
                    cell = row.createCell(0);
                    cell.setCellValue("姓名");
                    cell.setCellStyle(style);
                    for (int i = 0; i < size; i++) {
                        cell = row.createCell(i + 1);
                        cell.setCellValue(list.get(i).getScheduleWeek());
                        cell.setCellStyle(style);
                    }
                    cell = row.createCell(size + 1);
                    cell.setCellValue("计划工时");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 2);
                    cell.setCellValue("实际工时");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 3);
                    cell.setCellValue("结余");
                    cell.setCellStyle(style);
                    cell = row.createCell(size + 4);
                    cell.setCellValue("员工工时确认签字");
                    cell.setCellStyle(style);
                }
            }
            rownum = 1;


            int num = 0;
            for (String userId : map.keySet()) {
                List<ScheduleInfo> list = map.get(userId);
                int planHours = scheduleInfoService.selectScheduleInfoSum(startAt, endAt, userId);
                int actualHours = 0;
                for (ScheduleInfo info : list) {
                    actualHours += scheduleLogService.selectScheduleLogSum(info.getScheduleInfoId());
                }
                int size = 0;
                if (null != list && list.size() > 0) {
                    postName = list.get(0).getPostName();
                    userName = list.get(0).getUserName();
                    size = list.size();
                }
                Map<String, Integer> shift = new HashMap<>();
                for (int i = 0; i < shiftList.size(); i++) {
                    int count = scheduleInfoService.selectScheduleInfoGroup(startAt, endAt, userId, shiftList.get(i).getShiftName());
                    shift.put(shiftList.get(i).getShiftName(), count);
                    System.out.println(userName + shiftList.get(i).getShiftName() + "+" + count);
                }

                int flag = shiftList.size() + 1;
                flag = num % flag;


                if (postName.equals("站务员") && scheduleType.equals("0")) {
                    rownum = PoiUtil.getXiZhiMenRownum(wb, userName, sheet, style, rownum, postName, list, planHours, actualHours, size, shift, shiftList, flag);
                } else if (!postName.equals("站务员") && scheduleType.equals("1")) {
                    rownum = PoiUtil.getXiZhiMenRownum(wb, userName, sheet, style, rownum, postName, list, planHours, actualHours, size, shift, shiftList, flag);
                }
                num++;
            }
        }

        responsePrint(response, wb);
    }


    /**
     * 西直门站务员单个导出
     *
     * @param startAt
     * @param endAt
     * @param monthDate
     * @param queryData
     * @param response
     * @throws ParseException
     */
    @RequestMapping(value = "/printSingleXiZhiMenSchedule", method = RequestMethod.GET)
    public void printSingleXiZhiMenSchedule(String startAt, String endAt, String monthDate,
                                            String queryData, String stationArea, String station,String modelId, HttpServletResponse response, HttpServletRequest request) {
        QueryDate queryDate = new QueryDate(startAt, endAt, monthDate).invoke();
        startAt = queryDate.getStartAt();
        endAt = queryDate.getEndAt();
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(startAt, endAt, queryData, queryData, queryData, station, stationArea);

        Map<String, List<ScheduleInfo>> map = new HashMap<>();

        for (ScheduleInfo info :
                sis) {
            String key=info.getUserId()+"-"+info.getUserName();
            if (!map.containsKey(key)){
                map.put(key,new ArrayList<ScheduleInfo>());
            }
            map.get(key).add(info);
        }

        Map<String,ShiftSetting> shiftMap = new HashMap<>();

        List<ShiftSetting> shiftList = shiftService.selectShiftSettingByStation(station);
        for (ShiftSetting shift :
                shiftList) {
            shiftMap.put(shift.getShiftId(),shift);
        }

        Map<String,List<Workflow>> workflowMap=new HashMap<>();
        Map<Integer,List<WorkflowContent>> contentMap=new HashMap<>();

        for (ShiftSetting ss : shiftMap.values()) {


            Workflow workflow = new Workflow();
            workflow.setShiftId(Integer.parseInt(ss.getShiftId()));
            List<Workflow> workflowList = workflowService.selectWorkflows(workflow);
            workflowMap.put(ss.getShiftId(),workflowList);

            for (Workflow wf : workflowList) {
                WorkflowContent workflowContent = new WorkflowContent();
                workflowContent.setWorkflowId(wf.getWorkflowId());
                List<WorkflowContent> workflowContentList = workflowContentService.selectWorkflowContents(workflowContent);
                contentMap.put(wf.getWorkflowId(),workflowContentList);
            }
        }

        //将压缩包的名字默认为日期时间串
        String fieldName = "排班表.zip";

        try {
            //重置response头信息
            response.reset();
            //自己写状态码
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/octet-stream ");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fieldName, "UTF-8"));
            //生成输出流
            PictureUtil.createUserSchedulePicture(map,shiftMap,workflowMap,contentMap,response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 换班
     * 班次变更
     * @param request
     * @return
     */
    @RequestMapping(value = "/changeShift", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult changeShift(ChangeShiftVO changeShiftVO, HttpServletRequest request) {
        scheduleInfoService.updateScheduleInfoChangeShift(changeShiftVO);
        return OperationResult.buildSuccessResult("班次变更成功", "success");
    }


    @RequestMapping(value = "/getScheduleLogByUserId", method = RequestMethod.GET)
    @ResponseBody
    public List<ScheduleLog> getScheduleLogByUserId(String startAt, String endAt, String userId) {
        List<ScheduleLog> logs = scheduleLogService.selectScheduleLogByUserId(startAt, endAt, userId);
        System.out.println(logs.size() + "|||||||||||||||||||||||||||||||||||||||||||||||");
        return logs;
    }

    /**
     * 排班表格数据
     *
     * @param startAt
     * @param endAt
     * @param monthDate
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/scheduleFormData", method = RequestMethod.GET)
    @ResponseBody
    public List<ScheduleInfoData> scheduleFormData(String startAt, String endAt, String monthDate,
                                                   String queryData,String postName, String scheduleType, String stationArea, String station, HttpServletRequest request) throws ParseException {

        QueryDate queryDate = new QueryDate(startAt, endAt, monthDate).invoke();
        startAt = queryDate.getStartAt();
        endAt = queryDate.getEndAt();
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(startAt, endAt, postName, queryData, postName, station, stationArea);
        List<ScheduleInfoData> dataList = new ArrayList<>();
        Map<String, List<ScheduleInfo>> map = groupByProperty(sis, "userId", String.class);
        groupData(startAt, endAt, scheduleType, dataList, map);
        Collections.sort(dataList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                ScheduleInfoData stu1 = (ScheduleInfoData) o1;
                ScheduleInfoData stu2 = (ScheduleInfoData) o2;
                return stu1.scheduleId.compareTo(stu2.scheduleId);
            }
        });
        return dataList;
    }

    /**
     * 数据分组
     *
     * @param startAt
     * @param endAt
     * @param scheduleType
     * @param dataList
     * @param map
     */
    private void groupData(String startAt, String endAt, String scheduleType, List<ScheduleInfoData> dataList, Map<String, List<ScheduleInfo>> map) {
        ScheduleInfoData sd;
        List<Grouping> ps=groupingService.getAllGroupings();
        for (String user : map.keySet()) {
            sd = new ScheduleInfoData();
            for (Grouping p : ps) {
                sd.stations.put(p.getGroupCode(), p.getGroupName());
            }
            List<ScheduleInfo> list = map.get(user);
            sd.userId = user;
            getInfoData(sd, list);
            sd.planHours = scheduleInfoService.selectScheduleInfoSum(startAt, endAt, user) / 60;
            int sum = 0;
            for (ScheduleInfo info : list) {
                sum += scheduleLogService.selectScheduleLogSum(info.getScheduleInfoId());
                sd.scheduleId=info.getScheduleInfoId();
            }
            sd.actualHours = sum / 60;
            dataList.add(sd);
        }
    }

    /**
     * 初始化数据
     *
     * @param sd
     * @param list
     */
    private void getInfoData(ScheduleInfoData sd, List<ScheduleInfo> list) {
        if (list != null && list.size() > 0) {
            sd.userCode = list.get(0).getUserCode();
            sd.postName = list.get(0).getPostName();
            sd.userName = list.get(0).getUserName();
            sd.stationArea = list.get(0).getStationArea();
            sd.station = list.get(0).getStation();
            sd.groupName = list.get(0).getGroupName();
            sd.scheduleInfoList = list;
        } else {
            sd.postName = "";
        }
    }

    /**
     * 获取综控员排班数据
     *
     * @param startAt
     * @param endAt
     * @param monthDate
     * @param queryData
     * @param scheduleType
     * @param stationArea
     * @param station
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/scheduleAreaFormData", method = RequestMethod.GET)
    @ResponseBody
    public List<ScheduleInfoData> scheduleAreaFormData(String startAt, String endAt, String monthDate,
                                                       String queryData, String scheduleType, String stationArea, String station, HttpServletRequest request) throws ParseException {
        QueryDate queryDate = new QueryDate(startAt, endAt, monthDate).invoke();
        startAt = queryDate.getStartAt();
        endAt = queryDate.getEndAt();
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(startAt, endAt, queryData, queryData, queryData, station, stationArea);
        List<ScheduleInfoData> dataList = new ArrayList<>();
        Map<String, List<ScheduleInfo>> map = groupByProperty(sis, "groupName", String.class);
        groupData(startAt, endAt, scheduleType, dataList, map);
        Collections.sort(dataList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                ScheduleInfoData stu1 = (ScheduleInfoData) o1;
                ScheduleInfoData stu2 = (ScheduleInfoData) o2;
                return stu1.groupName.compareTo(stu2.groupName);
            }
        });
        return dataList;
    }

    @RequestMapping(value = "/getShiftSettingByInfo", method = RequestMethod.GET)
    @ResponseBody
    public List<ShiftSetting> getShiftSettingByInfo(String scheduleInfoId, HttpServletRequest request) throws ParseException {
        ScheduleInfo si = scheduleInfoService.selectScheduleInfoById(scheduleInfoId);
        List<ShiftSetting> shiftList = shiftService.selectShiftSettingByModelId(si.getModelId());
        return shiftList;
    }

    /**
     * 综控员排班表格
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/autoAreaScheduling", method = RequestMethod.GET)
    public String autoAreaScheduling(ModelMap model) {
        return "/scheduleInfo/autoAreaScheduling";
    }

    /**
     * 综控员排班数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/autoZky", method = RequestMethod.GET)

    @ResponseBody
    public List<ScheduleInfoData> autoZky(Integer year, HttpServletRequest request) {
        List<ScheduleInfoData> dataList = null;
        try {
            Calendar calendar = Calendar.getInstance();
            if (null == year || year == 0) {
                year = calendar.getWeekYear();
            }
            //List<ScheduleInfo> sis=scheduleInfoService.selectScheduleInfo(year+"-01-01",year+"-12-31",null,null,null,null,null);
            scheduleInfoService.deleteAreaScheduleInfo(year + "-01-01", year + "-12-31", null);
            final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            List<String> list = Arrays.asList("A", "B", "C", "D");
            ScheduleInfo info;
            for (int month = 0; month < 12; month++) {
                calendar.set(year, month, 1);
                int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                System.out.println("Max Day: " + maxDay);
                int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
                System.out.println("Min Day: " + minDay);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
                for (int i = minDay; i <= maxDay; i++) {
                    calendar.set(year, month, i);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (dayOfWeek < 0) dayOfWeek = 0;
                    System.out.println("第" + calendar.get(Calendar.DAY_OF_YEAR) + "天");
                    int day = calendar.get(Calendar.DAY_OF_YEAR);
                    if (day % 4 == 1) {
                        for (String str : list) {
                            info = new ScheduleInfo();
                            info.setScheduleDate(sdf.parse(year + "-" + (month + 1) + "-" + i));
                            info.setScheduleWeek(dayNames[dayOfWeek]);
                            info.setTotalAt(10);
                            info.setPostName("综控员");
                            if (str.equals("A")) {
                                info.setGroupName(str);
                                info.setShiftName("白");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("B")) {
                                info.setGroupName(str);
                                info.setShiftName("夜");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("C")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("D")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            }
                        }
                    } else if (day % 4 == 2) {
                        for (String str : list) {
                            info = new ScheduleInfo();
                            info.setScheduleDate(sdf.parse(year + "-" + (month + 1) + "-" + i));
                            info.setScheduleWeek(dayNames[dayOfWeek]);
                            info.setPostName("综控员");
                            if (str.equals("A")) {
                                info.setGroupName(str);
                                info.setShiftName("夜");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("B")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("C")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("D")) {
                                info.setGroupName(str);
                                info.setShiftName("白");
                                scheduleInfoService.insertSchedule(info);
                            }
                        }
                    } else if (day % 4 == 3) {
                        for (String str : list) {
                            info = new ScheduleInfo();
                            info.setScheduleDate(sdf.parse(year + "-" + (month + 1) + "-" + i));
                            info.setScheduleWeek(dayNames[dayOfWeek]);
                            info.setPostName("综控员");
                            if (str.equals("A")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("B")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("C")) {
                                info.setGroupName(str);
                                info.setShiftName("白");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("D")) {
                                info.setGroupName(str);
                                info.setShiftName("夜");
                                scheduleInfoService.insertSchedule(info);
                            }
                        }
                    } else if (day % 4 == 0) {
                        for (String str : list) {
                            info = new ScheduleInfo();
                            info.setScheduleDate(sdf.parse(year + "-" + (month + 1) + "-" + i));
                            info.setScheduleWeek(dayNames[dayOfWeek]);
                            info.setPostName("综控员");
                            if (str.equals("A")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("B")) {
                                info.setGroupName(str);
                                info.setShiftName("白");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("C")) {
                                info.setGroupName(str);
                                info.setShiftName("夜");
                                scheduleInfoService.insertSchedule(info);
                            } else if (str.equals("D")) {
                                info.setGroupName(str);
                                info.setShiftName("休");
                                scheduleInfoService.insertSchedule(info);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error:", e);
        }
        List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfo(year + "-01-01", year + "-12-31", null, null, null, null, null);
        dataList = new ArrayList<>();
        ScheduleInfoData sd;
        Map<String, List<ScheduleInfo>> map = groupByProperty(sis, "groupName", String.class);
        for (String user : map.keySet()) {
            sd = new ScheduleInfoData();
            List<ScheduleInfo> list = map.get(user);
            sd.userId = user;
            getInfoData(sd, list);
            sd.planHours = scheduleInfoService.selectScheduleInfoSum(year + "-01-01", year + "-12-31", user) / 60;
            int sum = 0;
            for (ScheduleInfo info : list) {
                sum += scheduleLogService.selectScheduleLogSum(info.getScheduleInfoId());
            }
            sd.actualHours = sum / 60;
            dataList.add(sd);

        }
        Collections.sort(dataList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                ScheduleInfoData stu1 = (ScheduleInfoData) o1;
                ScheduleInfoData stu2 = (ScheduleInfoData) o2;
                return stu1.groupName.compareTo(stu2.groupName);
            }
        });
        return dataList;
    }


    @Autowired
    private ScheduleInfoTemplateService templateService;

    /**
     * 建立模版
     * @param zwyId 班制
     * @param stationId
     * @param request
     * @return
     */
    @RequestMapping(value = "/autoScheduling", method = RequestMethod.GET)
    public
    @ResponseBody
    Object autoScheduling(String zwyId, String stationId, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            logger.debug("params: {},{} ", zwyId, stationId);
            Grouping grouping = groupingService.selectGroupingByGroupId(stationId);
            List<User> userList = userService.getUsersByStationArea(grouping.getGroupCode().substring(0,6));
            ShiftModel model = shiftModelService.selectShiftModelByModelCodeId(null, zwyId);
            PostSetting post = postSettingService.getOriginPostSettingByPostId(model.getPostId());
            List<User> userIds = new ArrayList<>();
            for (User u : userList) {
                if (u.getUserJob().equals(post.getPostCode())) {
                    userIds.add(u);
                }
            }
            List<ShiftSetting> shiftList = shiftService.selectShiftSettingByModelId(zwyId);
            Map<String, List<String>> workFlowMap = shiftService.getWorkFlowMap(shiftList);
            List<ScheduleInfoTemplate> data = ScheduleCalculator.calculate(shiftList, model);

            User loginUser = getLoginUser(request);
            int weeks = templateService.insertScheduleInfoTemplateList(model.getModelId(), data, loginUser);

            Map<String,ShiftSetting> shiftMap=new HashMap<>();

            for (ShiftSetting s :
                    shiftList) {
                shiftMap.put(s.getShiftId(),s);
            }

            for (ScheduleInfoTemplate t:data
                    ) {
                weeks=Math.max(weeks,t.getWeekNumber());
            }

            result.put("data", JSONObject.toJSON(data));
            result.put("users", userIds);
            result.put("weeks", weeks);
            result.put("codes", workFlowMap);
            result.put("shifts", shiftMap);
            result.put("shiftIds",new ArrayList<>(shiftMap.keySet()));
        } catch (Exception e) {
            logger.error("error:", e);
            result.put("error",e.getMessage());
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        return result;
    }

    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
    public
    @ResponseBody
    Object loadTemplate(String modelId, String stationId) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            logger.debug("params: {} ", modelId);
            Grouping grouping = groupingService.selectGroupingByGroupId(stationId);
            List<ShiftSetting> shiftList = shiftService.selectShiftSettingByModelId(modelId);
            List<User> userList = userService.selectUserByStation(grouping.getGroupCode());
            ShiftModel model = shiftModelService.selectShiftModelByModelCodeId(null, modelId);
            PostSetting post = postSettingService.getOriginPostSettingByPostId(model.getPostId());
            List<User> userIds = new ArrayList<>();
            for (User u : userList) {
                if (u.getUserJob().equals(post.getPostCode())) {
                    userIds.add(u);
                }
            }
            List<ScheduleInfoTemplate> data = templateService.selectScheduleInfoTemplateByModelId(modelId);
            Map<String, List<String>> workFlowMap = shiftService.getWorkFlowMap(shiftList);
            List<ScheduleInfoUser> scheduleInfoUsers = infoUserService.selectByModelId(modelId);
            Map<String,ShiftSetting> shiftMap=new HashMap<>();
            for (ShiftSetting s :
                    shiftList) {
                shiftMap.put(s.getShiftId(),s);
            }
            int weeks=0;
            for (ScheduleInfoTemplate t:data
                    ) {
                weeks=Math.max(weeks,t.getWeekNumber());
            }
            result.put("data", JSON.toJSON(data));
            result.put("users", userIds);
            result.put("codes", workFlowMap);
            result.put("weeks", weeks);
            result.put("shifts", shiftMap);
            result.put("shiftIds",new ArrayList<>(shiftMap.keySet()));
            result.put("owners", scheduleInfoUsers);
        } catch (Exception e) {
            logger.error("error:", e);
            result.put("error",e.getMessage());
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        return result;
    }

    @RequestMapping(value = "/removeWeekTemplate", method = RequestMethod.GET)
    public
    @ResponseBody
    Object removeWeekTemplate(String modelId, int weekNumber) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            logger.debug("params: {} , {}", modelId,weekNumber);
            templateService.removeByModelAndWeek(modelId,weekNumber);
            result.put("result", "success");
        } catch (Exception e) {
            logger.error("error:", e);
            result.put("error",e.getMessage());
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        return result;
    }



    @Autowired
    private ScheduleInfoUserService infoUserService;

    @RequestMapping(value = "/changeOwner", method = RequestMethod.GET)
    public
    @ResponseBody
    Object changeOwner(String modelId, String userId, String userName,int weekNumber) {
        long start = System.currentTimeMillis();
        try {
            ScheduleInfoUser scheduleInfoUser = infoUserService.selectByModelAndWeek(modelId, weekNumber);
            if (scheduleInfoUser!=null){
                infoUserService.deleteById(scheduleInfoUser.getId());
            }
            scheduleInfoUser=infoUserService.selectByModelAndUser(modelId,userId);
            if(scheduleInfoUser!=null){
                infoUserService.deleteById(scheduleInfoUser.getId());
            }
            scheduleInfoUser=new ScheduleInfoUser();
            scheduleInfoUser.setUserName(userName);
            scheduleInfoUser.setUserId(userId);
            scheduleInfoUser.setModelId(modelId);
            scheduleInfoUser.setWeekNumber(weekNumber);
            infoUserService.insert(scheduleInfoUser);
        } catch (Exception e) {
            logger.error("error:", e);
            return "error";
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        JSONObject result = new JSONObject();
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/changeData", method = RequestMethod.GET)
    public
    @ResponseBody
    Object changeData(String modelId, String id1, String id2, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        try {
            scheduleInfoService.changeData(modelId, id1, id2);
        } catch (Exception e) {
            logger.error("error:", e);
            return "error";
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        JSONObject result = new JSONObject();
        result.put("result", "success");
        return result;
    }

    @RequestMapping(value = "/changeScheduleInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Object changeScheduleInfo(String id1, String id2, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        try {
            User loginUser = getLoginUser(request);
            scheduleInfoService.changeScheduleInfo(id1, id2, loginUser);
        } catch (Exception e) {
            logger.error("error:", e);
            return "error";
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        JSONObject result = new JSONObject();
        result.put("result", "success");
        return result;
    }

    /**
     * 保存排班
     * @param modelId
     * @param stationId
     * @param userIds
     * @param startAt
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveScheduling", method = RequestMethod.POST)
    public
    @ResponseBody
    Object shiftApproval(String modelId,String weeks, String stationId, String userIds, String startAt, HttpServletRequest request) {
        long start = System.currentTimeMillis();
        String [][]week = new String[0][];
        JSONObject object=JSON.parseObject(weeks);
        System.out.print(weeks.toString());
        try {
            User loginUser = getLoginUser(request);
            Grouping grouping = groupingService.selectGroupingByGroupId(stationId);
            ShiftModel shiftModel = shiftModelService.selectShiftModelByModelCodeId(null, modelId);
            PostSetting post = postSettingService.getPostSettingByPostId(shiftModel.getPostId());
            scheduleInfoService.saveSchedule(modelId,object, grouping.getGroupName(),post.getPostName(), userIds, startAt, loginUser);
        } catch (Exception e) {
            logger.error("error:", e);
            return "error";
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        JSONObject result = new JSONObject();
        result.put("result", "success");
        result.put("data", "success");
        return result;
    }

    @RequestMapping(value = "/getUserScheduleInfoById", method = RequestMethod.GET)
    @ResponseBody
    public UserScheduleInfo getUserScheduleInfoById(UserScheduleInfo userScheduleInfo) {
        String[] date=userScheduleInfo.getStartAt().split("-");
        UserScheduleInfo result = new UserScheduleInfo();
        String userId = userScheduleInfo.getUserId();
        String year = date[0];
        String month = date[1];
        String startAt = year + "-01-01";
        String endAt = year + "-12-31";

        //年度
        List<ScheduleInfo> scheduleInfoList = scheduleInfoService.selectScheduleInfoByUser(startAt, endAt, userId);
        int planYearHour = 0;

        for (ScheduleInfo si : scheduleInfoList) {
            Integer totalAt = si.getTotalAt();
            if (totalAt != null) {
                planYearHour += totalAt;
            }
        }

        int actualYearHour = 0;

        List<ScheduleLog> scheduleLogList = scheduleLogService.selectScheduleLogByUserId(startAt, endAt, userId);

        for (ScheduleLog sl : scheduleLogList) {
            Integer timeAt = sl.getTimeAt();
            actualYearHour += timeAt;
        }

        actualYearHour = planYearHour - actualYearHour;

        result.setPlanYearHour(DateUtil.minuToHour(planYearHour));
        result.setActualYearHour(DateUtil.minuToHour(actualYearHour));

        //月度
        startAt = DateUtil.getFirstDayOfMonth(Integer.parseInt(year), Integer.parseInt(month));
        endAt = DateUtil.getLastDayOfMonth(Integer.parseInt(year), Integer.parseInt(month));


        scheduleInfoList = scheduleInfoService.selectScheduleInfoByUser(startAt, endAt, userId);
        scheduleLogList = scheduleLogService.selectScheduleLogByUserId(startAt, endAt, userId);

        int planMonthHour = 0;

        for (ScheduleInfo si : scheduleInfoList) {
            Integer totalAt = si.getTotalAt();
            if (totalAt != null) {
                planMonthHour += totalAt;
            }
        }

        int actualMonthHour = 0;


        for (ScheduleLog sl : scheduleLogList) {
            Integer timeAt = sl.getTimeAt();
            if (timeAt != null) {
                actualMonthHour += timeAt;
            }
        }
        actualMonthHour = planMonthHour - actualMonthHour;

        result.setPlanMonthHour(DateUtil.minuToHour(planMonthHour));
        result.setActualMonthHour(DateUtil.minuToHour(actualMonthHour));

        result.setStartAt(userScheduleInfo.getStartAt());
        return result;
    }


    private class QueryDate {
        private String startAt;
        private String endAt;
        private String monthDate;

        public QueryDate(String startAt, String endAt, String monthDate) {
            this.startAt = startAt;
            this.endAt = endAt;
            this.monthDate = monthDate;
        }

        public String getStartAt() {
            return startAt;
        }

        public String getEndAt() {
            return endAt;
        }

        public QueryDate invoke() {
            if (startAt == null || startAt.equals("")) {
                DateFormat format = new SimpleDateFormat("yyyy-M-dd");
                Calendar calendar = new GregorianCalendar();
                Date date = new Date();
                if (null == monthDate || monthDate.equals("")) {
                    defaultDate(format, calendar, date);
                } else {
                    // 获取当前月第一天：
                    Calendar c = Calendar.getInstance();
                    c.setTime(DateUtil.parseDate(monthDate + "-01"));
                    c.add(Calendar.MONTH, 0);
                    c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
                    startAt = format.format(c.getTime());
                    // 获取当前月最后一天
                    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endAt = format.format(c.getTime());
                }
            }
            return this;
        }

        private void defaultDate(DateFormat format, Calendar calendar, Date date) {
            startAt = format.format(date);
            calendar.setTime(date);
            calendar.add(calendar.DATE, 6);// 把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
            endAt = DateUtil.formatDate(date, "yyyy-M-dd");
        }
    }
}
