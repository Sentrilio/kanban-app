package com.domko.kanbanbackendapp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class TrendSeries implements Series {
    private String name;
    private String type;
    private List<Double> data = new ArrayList<>();

    public TrendSeries(String name, String type) {
        this.name=name;
        this.type=type;
    }

    public void addAll(List<Double> values){
        data.addAll(values);
    }


}
