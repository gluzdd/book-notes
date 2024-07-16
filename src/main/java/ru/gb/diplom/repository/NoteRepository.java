package ru.gb.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.diplom.model.Note;
import ru.gb.diplom.model.User;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    List<Note> findByTag(String tag);
}
