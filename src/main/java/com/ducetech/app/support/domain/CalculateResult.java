package com.ducetech.app.support.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ducetech.app.model.PostSetting;
import com.ducetech.app.model.ShiftModel;
import com.ducetech.app.model.User;

public class CalculateResult {
	public ShiftModel model;
	public PostSetting post;
	public String startFrom;
	public String endAt;
	public int totalDays;
	public String modelId;
	public Map<Integer, List<Task>> taskMap;
	public List<User> userList;
	public String groupName;
}
