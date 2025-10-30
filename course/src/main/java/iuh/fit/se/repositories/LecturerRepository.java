package iuh.fit.se.repositories;

import iuh.fit.se.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    @Query("SELECT l FROM Lecturer l JOIN l.courses c WHERE c.id = :courseId")
    List<Lecturer> findByCourseId(@Param("courseId") int courseId);
}

