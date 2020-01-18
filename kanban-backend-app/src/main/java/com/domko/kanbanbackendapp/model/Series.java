package com.domko.kanbanbackendapp.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Series {
    private String name;
    private List<Integer> data;

    public void add(int index, Integer value) {
//        values.add(index, value);
        data.set(index, value);
    }

    public Series(int capacity) {
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
