package iuh.fit.se.services;

import iuh.fit.se.entities.Lecturer;

import java.util.List;

public interface LecturerService {
    List<Lecturer> findAll();
    List<Lecturer> findByCourseId(int id);
}

