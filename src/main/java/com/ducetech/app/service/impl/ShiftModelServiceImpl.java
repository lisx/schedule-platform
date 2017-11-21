package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ShiftModelDAO;
import com.ducetech.app.dao.ShiftSettingDAO;
import com.ducetech.app.model.ShiftModel;
import com.ducetech.app.service.ShiftModelService;
import com.ducetech.app.service.ShiftModelService;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftModelServiceImpl implements ShiftModelService{

    @Autowired
    private ShiftModelDAO shiftModelDAO;
    @Autowired
    private ShiftSettingDAO shiftSettingDAO;

    @Override
    public List<ShiftModel> selectShiftModel(String postId) {
        return shiftModelDAO.selectShiftModel(postId);
    }

    @Override
    public PagerRS<ShiftModel> getShiftModelByPager(BaseQuery<ShiftModel> query) {
        if( query != null && query.getPage() > 0 ){			//如果传入offset大于0,则启用分页查询，否则不启用
            PageHelper.startPage(query.getPage(), query.getRows(), true);
        }
        List<ShiftModel> deptList=new ArrayList<>();
//        List<ShiftModel> deptList = shiftModelDAO.selectShiftModel(query.getT());
//        @SuppressWarnings({ "unchecked", "rawtypes" })
        PageInfo page = new PageInfo(deptList);
        PagerRS<ShiftModel> pagerRS = new PagerRS<ShiftModel>(deptList, page.getTotal(), page.getPages());
        return pagerRS;
    }

    @Override
    public void insertShiftModel(ShiftModel dept) {
        shiftModelDAO.insertShiftModel(dept);
    }
    /**
     * 获取新的节点编号
     * @param parentCode
     * @return
     */
    @Override
    public String selectShiftModelByParentCode(String parentCode){
        List<ShiftModel> models = querySubModelsByCode(parentCode);
        String newCode="001";
        if(!models.isEmpty()){
            ShiftModel model = models.get(models.size()-1);
            String modelCode = model.getModelCode();
            String subCode = modelCode.substring(modelCode.length()-3, modelCode.length());
            int current = Integer.parseInt(subCode);
            current = current+1;
            newCode = leftJoin(current, 3, "0");
        }

        return parentCode+newCode;
    }
    public static String leftJoin(Object obj,int scale,String val){
        String str = String.valueOf(obj);
        int len = str.length();
        if(len<scale){
            int diff = scale-len;
            String prefix="";
            for(int i=0;i<diff;i++){
                prefix+=val;
            }
            str = prefix+str;
        }
        return str;
    }
    public List<ShiftModel> querySubModelsByCode(String parentCode){
        return shiftModelDAO.selectByParentCode(parentCode+"%",parentCode.length()+3);
    }

    @Override
    public void deleteShiftModel(String modelCode) {
        shiftModelDAO.deleteShiftModel(modelCode,1);
        shiftSettingDAO.deleteShiftSettingByModelId(modelCode);
    }
    @Override
    public void updateShiftModel(ShiftModel dept){
        shiftModelDAO.updateShiftModel(dept);
    }
    @Override
    public ShiftModel selectShiftModelByModelCode(String modelCode) {
        return shiftModelDAO.selectShiftModelByModelCode(modelCode);
    }

    @Override
    public List<ShiftModel> selectByParentCode(String parentCode, int modelCodeLength) {
        return shiftModelDAO.selectByParentCode(parentCode+"%",modelCodeLength);
    }

    @Override
    public ShiftModel selectShiftModelByModelCodeId(String modelCode, String modelId) {
        return shiftModelDAO.selectShiftModelByModelCodeId(modelCode,modelId);
    }

    @Override
    public List<ShiftModel> selectShiftModelByPostGroup(String postId, String groupCode) {
        return shiftModelDAO.selectShiftModelByPostGroup(postId,groupCode);
    }

    @Override
    public List<ShiftModel> getAllModels() {
        return shiftModelDAO.selectAllModels();
    }
}
