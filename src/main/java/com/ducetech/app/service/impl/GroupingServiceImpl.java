package com.ducetech.app.service.impl;

import com.ducetech.app.dao.GroupingDAO;
import com.ducetech.app.model.Grouping;
import com.ducetech.app.service.GroupingService;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Service
public class GroupingServiceImpl implements GroupingService{

    @Autowired
    private GroupingDAO groupingDAO;

    @Override
    public List<Grouping> getAllGroupings() {
        return this.getGroupingByPager(new BaseQuery<Grouping>(new Grouping())).getResults();
    }

    @Override
    public List<Grouping> getGroupingByQuery(Grouping dept) {
        return groupingDAO.selectGrouping(dept);
    }

    @Override
    public PagerRS<Grouping> getGroupingByPager(BaseQuery<Grouping> query) {
        if( query != null && query.getPage() > 0 ){			//如果传入offset大于0,则启用分页查询，否则不启用
            PageHelper.startPage(query.getPage(), query.getRows(), true);
        }
        List<Grouping> deptList = groupingDAO.selectGrouping(query.getT());
        @SuppressWarnings({ "unchecked", "rawtypes" })
        PageInfo page = new PageInfo(deptList);
        PagerRS<Grouping> pagerRS = new PagerRS<Grouping>(deptList, page.getTotal(), page.getPages());
        return pagerRS;
    }

    @Override
    public void insertGrouping(Grouping dept) {
        groupingDAO.insertGrouping(dept);
    }
    /**
     * 获取新的节点编号
     * @param parentCode
     * @return
     */
    @Override
    public String selectGroupingByParentCode(String parentCode){
        List<Grouping> groups = querySubGroupsByCode(parentCode);
        String newCode="001";
        if(!groups.isEmpty()){
            Grouping group = groups.get(groups.size()-1);
            String groupCode = group.getGroupCode();
            String subCode = groupCode.substring(groupCode.length()-3, groupCode.length());
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
    public List<Grouping> querySubGroupsByCode(String parentCode){
        System.out.println(parentCode+"||"+(parentCode.length()+3));
        return groupingDAO.selectByParentCode(parentCode+"%",parentCode.length()+3);
    }
    @Override
    public void updateGrouping(Grouping dept) {
        groupingDAO.updateGrouping(dept);
    }

    @Override
    public void deleteGrouping(String groupCode) {
        groupingDAO.deleteGrouping(groupCode+"%");
    }

    @Override
    public Grouping selectGroupingByGroupCode(String groupCode) {
        return groupingDAO.selectGroupingByGroupCode(groupCode);
    }
    @Override
    public Grouping selectGroupingByGroupId(String groupId){
        return groupingDAO.selectGroupingByGroupId(groupId);
    }
    @Override
    public List<Grouping> selectByParentCode(String parentCode, int groupCodeLength) {
        List<Grouping> deptList = groupingDAO.selectByParentCode(parentCode+"%",groupCodeLength);
        List<Grouping> list = new ArrayList<Grouping>();
        for(Grouping dep:deptList){
            dep.setStations(groupingDAO.selectByParentCode(dep.getGroupCode()+"%",dep.getGroupCode().length()+3));
            list.add(dep);
        }
        return list;
    }
    @Override
    public List<Grouping> queryGroupsByLength(int len) {
        return groupingDAO.queryGroupsByLength(len);
    }
    @Override
    public Grouping selectGroupingByGroupName(String groupName) {
        return groupingDAO.selectGroupingByGroupName(groupName);
    }
}
