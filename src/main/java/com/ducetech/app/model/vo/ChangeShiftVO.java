package com.ducetech.app.model.vo;

import java.io.Serializable;
import java.util.Date;

public class ChangeShiftVO implements Serializable {
    //需要换班的人员id
    private String userId;
    //与其换班的人员id
    private String changeUserId;
    //从多少时间以及之后换班 格式 2017-08-19
    private String scheduleDate;
}
