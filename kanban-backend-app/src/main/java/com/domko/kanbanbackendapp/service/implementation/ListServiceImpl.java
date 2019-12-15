package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.List;
import com.domko.kanbanbackendapp.repository.ListRepository;
import com.domko.kanbanbackendapp.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository listRepository;

    public List save(List list) {
        return listRepository.save(list);
    }

    public Optional<List> findList(Long id) {
        return listRepository.findById(id);
    }
}
