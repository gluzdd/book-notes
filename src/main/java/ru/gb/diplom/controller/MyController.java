package ru.gb.diplom.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.diplom.model.Note;
import ru.gb.diplom.model.User;
import ru.gb.diplom.service.NoteService;
import ru.gb.diplom.service.UserService;

import java.util.List;
@Slf4j
@AllArgsConstructor
@Controller
public class MyController {

    private NoteService noteService;
    private UserService userService;
    /**
     * Главное меню.
     * @return перенаправление на главную страницу .
     */
    @GetMapping("/home")
    String home() {
        return "home";
    }
    @GetMapping("/createNote")
    public String createNoteForm(Note note) {
        return "createNote";
    }

    /**
     * Создание заметки.
     * @param note идентификатор товара.
     * @return перенаправление на страницу создания заметки.
     */

    @PostMapping("/createNote")
    public String createNote(Model model, Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        user.addNoteToUser(note);
        noteService.createNote(note);
        List<Note> noteList = noteService.getNotesByUser(user);
        model.addAttribute("notes", noteList);
        return "storyNote";
    }
    /**
     * Получение списка заметок пользователя.
     * @param model модель для передачи данных в представление.
     * @return перенаправление на страницу со списком заметок.
     */
    @GetMapping("/storyNote")
    public String noteStory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        List<Note> noteList = noteService.getNotesByUser(user);
        model.addAttribute("notes", noteList);
        return "storyNote";
    }

    @GetMapping("note-delete/{id}")
    public String deleteNote(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        User user = note.getUser();
        user.getNotes().remove(note);
        userService.updateUsers(user.getId(), user);
        noteService.deleteNote(id);
        return "redirect:/storyNote";
    }
    @GetMapping("/storyNoteUpdate/{id}")
    public String updateNote(@PathVariable("id") Long id, Model model) {
        Note note = noteService.getNoteById(id);
        model.addAttribute("note", note);
        return "storyNoteUpdate";
    }
    @PostMapping("/storyNoteUpdate/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute Note note) {
        noteService.getNoteById(note.getId());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByEmail(authentication.getName());
        noteService.updateNote(id, note);
        return "redirect:/storyNote";
    }
    @GetMapping("/notes")
    public String getNotesByTag(@RequestParam(value = "tag", required = false) String tag, Model model) {
        List<Note> notes;
        if (tag != null && !tag.isEmpty()) {
            notes = noteService.getNotesByTag(tag);
        } else {
            notes = noteService.getAllNotes(); // Предполагается, что такой метод существует для получения всех заметок
        }
        model.addAttribute("notes", notes);
        return "notes";
    }

}
