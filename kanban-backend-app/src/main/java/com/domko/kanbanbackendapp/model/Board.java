package com.domko.kanbanbackendapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @JsonManagedReference
    @OrderColumn(name = "position")
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<BColumn> columns;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    private List<BoardStatistics> statistics;


    public int getNumberOfTasks() {
//        int sum=0;
//        for (BColumn col : getColumns()) {
//            System.out.println(col.getTasks());
//            sum+=col.getTasks().size();
//        }
//        return sum;
        return getColumns()
                .stream()
                .mapToInt(o -> o.getTasks().size())
                .sum();
    }
}
