package iuh.fit.se.services.impl;

import iuh.fit.se.entities.Lecturer;
import iuh.fit.se.repositories.LecturerRepository;
import iuh.fit.se.services.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository repo;

    @Autowired
    public LecturerServiceImpl(LecturerRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Lecturer> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Lecturer> findByCourseId(int id) {
        return repo.findByCourseId(id);
    }
}

