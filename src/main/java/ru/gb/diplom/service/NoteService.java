package ru.gb.diplom.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.diplom.model.Note;
import ru.gb.diplom.model.User;
import ru.gb.diplom.repository.NoteRepository;

import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public Note createNote(Note note) {
        note.setCreatedDate(LocalDate.now());
        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(()-> new RuntimeException("Note not found"));
    }
    @Transactional
    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Note> getNotesByUser(User user) {
        return noteRepository.findByUser(user);
    }

    @Transactional
    public Note updateNote(Long id,Note note) {
        Note noteByID = noteRepository.findById(note.getId()).orElseThrow(()-> new RuntimeException("Note not found"));
        noteByID.setTitle(note.getTitle());
        noteByID.setBody(note.getBody());
        noteByID.setTag(note.getTag());
        return noteRepository.save(noteByID);
    }
    //получение заметки по тегу
    public List<Note> getNotesByTag(String tag) {
        return noteRepository.findByTag(tag);
    }

}
