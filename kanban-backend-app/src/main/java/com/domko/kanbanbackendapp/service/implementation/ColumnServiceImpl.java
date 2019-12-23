package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.repository.ListRepository;
import com.domko.kanbanbackendapp.service.BoardListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColumnServiceImpl implements BoardListService {

    @Autowired
    private ListRepository listRepository;

    public BColumn save(BColumn BColumn) {
        return listRepository.save(BColumn);
    }

    public Optional<BColumn> findList(Long id) {
        return listRepository.findById(id);
    }
}
