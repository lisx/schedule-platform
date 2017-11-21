package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人员分组部门字典
 * Created by lisx on 2017/4/27.
 */
@Data
public class Grouping extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String groupId;
    // 字典code
    private String groupCode;

    // 字典名称
    private String groupName;

    // 字典排序
    private int groupOrder;

    // 是否删除
    private int isDeleted;

    // 创建人
    private String creatorId;

    // 更新人
    private String updatorId;

    // 创建时间
    private String createdAt;

    // 更新时间
    private String updatedAt;

    private List<Grouping> stations;
}
