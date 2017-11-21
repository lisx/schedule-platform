package com.ducetech.app.support.domain;

import com.ducetech.app.model.ShiftSetting;

import java.util.*;

public class Task implements Cloneable {
    public int day;
    public ShiftSetting shift;
    public Integer userId;
    public int priBefore;
    public int priAfter;
    public Task relevance;
    public Task parent;
    public Queue<PersonalDuty> userAvailable = new PriorityQueue<>();

    public String toString() {
        return "day:" + day + ",pri:" + priBefore + ", shift:" + shift.getShiftId() + ", user:" + userId;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}