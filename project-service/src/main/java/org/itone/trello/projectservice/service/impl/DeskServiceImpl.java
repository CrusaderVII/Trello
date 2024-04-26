package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.desk.NoSuchDeskException;
import org.itone.trello.projectservice.model.Desk;
import org.itone.trello.projectservice.repository.DeskRepository;
import org.itone.trello.projectservice.service.DeskService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeskServiceImpl implements DeskService {

    private final DeskRepository deskRepository;

    public DeskServiceImpl(DeskRepository deskRepository) {
        this.deskRepository = deskRepository;
    }

    @Override
    public Desk getDeskById(long id) {
        return deskRepository.findById(id)
                .orElseThrow(() -> new NoSuchDeskException("id "+id));
    }

    @Override
    public List<Desk> getAllDesks() {
        return deskRepository.findAll();
    }

    @Override
    public Desk saveDesk(Desk entity) {
        return deskRepository.save(entity);
    }

    @Override
    public void deleteDesk(long id) {
        deskRepository.deleteById(id);
    }
}
