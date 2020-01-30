package com.domko.kanbanbackendapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrendSeries implements Series {
    private String name;
    private String type;
    private List<Float> data = new ArrayList<>();

    public TrendSeries(String name, String type) {
        this.name=name;
        this.type=type;
    }

    public void addAll(List<Float> values){
        data.addAll(values);
    }


}
