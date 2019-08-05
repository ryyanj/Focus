package com.ryyanj.focus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanFile {

    Boolean available;
    Short duration;
    List<String> urlblacklist;
    List<String> urlwhitelist;
    List<String> appblacklist;
    List<String> appwhitelist;
    Long endtime;

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public Long getEndtime() {
        return endtime;
    }

    public void setEndtime(Long endtime) {
        this.endtime = endtime;
    }

    public List<String> getUrlblacklist() {
        return urlblacklist;
    }

    public void setUrlblacklist(List<String> urlblacklist) {
        this.urlblacklist = urlblacklist;
    }

    public List<String> getUrlwhitelist() {
        return urlwhitelist;
    }

    public void setUrlwhitelist(List<String> urlwhitelist) {
        this.urlwhitelist = urlwhitelist;
    }

    public List<String> getAppblacklist() {
        return appblacklist;
    }

    public void setAppblacklist(List<String> appblacklist) {
        this.appblacklist = appblacklist;
    }

    public List<String> getAppwhitelist() {
        return appwhitelist;
    }

    public void setAppwhitelist(List<String> appwhitelist) {
        this.appwhitelist = appwhitelist;
    }
}
