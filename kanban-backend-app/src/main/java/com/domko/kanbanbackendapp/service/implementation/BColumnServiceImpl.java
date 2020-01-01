package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BColumn;
import com.domko.kanbanbackendapp.model.Task;
import com.domko.kanbanbackendapp.repository.BColumnRepository;
import com.domko.kanbanbackendapp.repository.TaskRepository;
import com.domko.kanbanbackendapp.service.BColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BColumnServiceImpl implements BColumnService {

    @Autowired
    private BColumnRepository bColumnRepository;

    public BColumn save(BColumn BColumn) {
        return bColumnRepository.save(BColumn);
    }

    public Optional<BColumn> findBColumn(Long id) {
        return bColumnRepository.findById(id);
    }



}
