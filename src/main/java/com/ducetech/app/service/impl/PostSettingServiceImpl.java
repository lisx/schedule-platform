package com.ducetech.app.service.impl;

import com.ducetech.app.dao.PostSettingDAO;
import com.ducetech.app.model.PostSetting;
import com.ducetech.app.service.PostSettingService;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostSettingServiceImpl implements PostSettingService {

    @Autowired
    private PostSettingDAO postSettingDAO;

    @Override
    public List<PostSetting> getAllPostSettings() {
        return this.getPostSettingByPager(
                new BaseQuery<PostSetting>(new PostSetting())).getResults();
    }

    @Override
    public List<PostSetting> getPostSettingByQuery() {
        return postSettingDAO.selectPostSetting();
    }

    @Override
    public PagerRS<PostSetting> getPostSettingByPager(BaseQuery<PostSetting> query) {
        if (query != null && query.getPage() > 0) {            //如果传入offset大于0,则启用分页查询，否则不启用
            PageHelper.startPage(query.getPage(), query.getRows(), true);
        }
        List<PostSetting> deptList = postSettingDAO.selectPostSetting();
        @SuppressWarnings({"unchecked", "rawtypes"})
        PageInfo page = new PageInfo(deptList);

       /* if(deptList != null) {
            for(int i = 0; i < deptList.size() ; i++) {
                PostSetting postSetting = deptList.get(i);
                Integer max = postSetting.getMaxWeeklyReason();
                Integer min = postSetting.getMinWeeklyReason();
               if(max != null) {
                   postSetting.setMaxWeeklyReason(max / 60);
               }

               if(min != null) {
                   postSetting.setMinWeeklyReason(min / 60);
               }
            }
        }*/
        PagerRS<PostSetting> pagerRS = new PagerRS<PostSetting>(deptList, page.getTotal(), page.getPages());
        return pagerRS;
    }

    @Override
    public void insertPostSetting(PostSetting dept) {
       /* dept.setMaxWeeklyReason(dept.getMaxWeeklyReason() * 60);
        dept.setMinWeeklyReason(dept.getMinWeeklyReason() * 60);*/
        postSettingDAO.insertPostSetting(dept);
    }

    /**
     * 获取新的节点编号
     *
     * @param parentCode
     * @return
     */
    @Override
    public String selectPostSettingByParentCode(String parentCode) {
        List<PostSetting> groups = querySubPostSettingsByCode(parentCode);
        System.out.println(groups.size() + "|||");
        String newCode = "001";
        if (!groups.isEmpty()) {
            PostSetting group = groups.get(groups.size() - 1);
            String groupCode = group.getPostCode();
            String subCode = groupCode.substring(groupCode.length() - 3, groupCode.length());
            int current = Integer.parseInt(subCode);
            current = current + 1;
            newCode = leftJoin(current, 3, "0");
        }

        return parentCode + newCode;
    }

    public static String leftJoin(Object obj, int scale, String val) {
        String str = String.valueOf(obj);
        int len = str.length();
        if (len < scale) {
            int diff = scale - len;
            String prefix = "";
            for (int i = 0; i < diff; i++) {
                prefix += val;
            }
            str = prefix + str;
        }
        return str;
    }

    public List<PostSetting> querySubPostSettingsByCode(String parentCode) {
        System.out.println(parentCode + "||" + (parentCode.length() + 3));
        return postSettingDAO.selectByParentCode(parentCode + "%", parentCode.length() + 3);
    }

    @Override
    public void updatePostSetting(PostSetting dept) {
      /*  dept.setMaxWeeklyReason(dept.getMaxWeeklyReason() * 60);
        dept.setMinWeeklyReason(dept.getMinWeeklyReason() * 60);*/
        postSettingDAO.updatePostSetting(dept);
    }

    @Override
    public void deletePostSetting(String postCode, int ifUse) {
        postSettingDAO.deletePostSetting(postCode, ifUse);
    }

    @Override
    public PostSetting selectPostSettingByPostCode(String postCode) {
        return postSettingDAO.selectPostSettingByPostCode(postCode);
    }

    @Override
    public PostSetting selectPostSettingByPostName(String postName) {
        return postSettingDAO.selectPostSettingByPostName(postName);
    }

    @Override
    public List<PostSetting> selectByParentCode(String parentCode, int groupCodeLength) {
        List<PostSetting> deptList = postSettingDAO.selectByParentCode(parentCode + "%", groupCodeLength);
        List<PostSetting> list = new ArrayList<PostSetting>();
        for (PostSetting dep : deptList) {
            list.add(dep);
        }
        return list;
    }

    public List<PostSetting> queryAll() {
        return postSettingDAO.selectPostSetting();
    }

    @Override
    public PostSetting getPostSettingByPostId(String postId) {
        PostSetting postSetting = postSettingDAO.selectPostSettingByPostId(postId);
        Integer max = postSetting.getMaxWeeklyReason();
        Integer min = postSetting.getMinWeeklyReason();
        if(max != null) {
            postSetting.setMaxWeeklyReason(max / 60);
        }

        if(min != null) {
            postSetting.setMinWeeklyReason(min / 60);
        }
        return postSetting;
    }

    @Override
    public PostSetting getStationWorkerPost() {
        List<PostSetting> allPostSettings = getAllPostSettings();
        for (PostSetting p : allPostSettings
                ) {
            if (p.getPostName().equals("站务员")) {

                return p;
            }
        }
        return null;
    }

    @Override
    public PostSetting getOriginPostSettingByPostId(String postId) {
        PostSetting postSetting = postSettingDAO.selectPostSettingByPostId(postId);
        return postSetting;
    }

    @Override
    public List<PostSetting> getBackupPostSettings() {
        return postSettingDAO.selectBackupPostSettings();
    }
}
