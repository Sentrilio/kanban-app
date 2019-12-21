package com.domko.kanbanbackendapp.service.implementation;

import com.domko.kanbanbackendapp.model.BList;
import com.domko.kanbanbackendapp.repository.ListRepository;
import com.domko.kanbanbackendapp.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository listRepository;

    public BList save(BList bList) {
        return listRepository.save(bList);
    }

    public Optional<BList> findList(Long id) {
        return listRepository.findById(id);
    }
}
