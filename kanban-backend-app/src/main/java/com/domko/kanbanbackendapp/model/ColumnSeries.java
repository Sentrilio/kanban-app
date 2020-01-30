package com.domko.kanbanbackendapp.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class ColumnSeries implements Series {
    private String name;
    private String type;
    private List<Integer> data;

    public void set(int index, Integer value) {
        data.set(index, value);
    }

    public ColumnSeries(int capacity, String type) {
        this.type = type;
        this.data = IntStream.of(new int[capacity])
                .boxed()
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Series{" +
                "name='" + name + '\'' +
                ", values=" + data + "\n" +
                '}';
    }
}
