package com.domko.kanbanbackendapp.model;

import lombok.Data;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SeriesSet {
    private List<Series> seriesList = new ArrayList<>();
    private List<String> dates;

    public void add(Series series){
        seriesList.add(series);
    }

    @Override
    public String toString() {
        return "SeriesSet{" +
                "seriesList=\n" + seriesList +"\n"+
                ", dates=" + dates +
                '}';
    }

}
