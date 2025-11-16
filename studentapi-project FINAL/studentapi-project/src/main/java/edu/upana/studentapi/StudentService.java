package edu.upana.studentapi;

import lombok.ToString;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@ToString
public class StudentService {


    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public Student findById(Long id) {
        Student s = students.get(id);
        if (s == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado");
        }
        return s;
    }

    public Student create(Student student) {
        // Validar correo único
        boolean emailExists = students.values().stream()
                .anyMatch(s -> s.getCorreo().equalsIgnoreCase(student.getCorreo()));
        if (emailExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }

        long id = sequence.incrementAndGet();
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student partialUpdate(Long id, Map<String, Object> changes) {
        Student existing = findById(id);

        if (changes.containsKey("nombre")) {
            existing.setNombre((String) changes.get("nombre"));
        }
        if (changes.containsKey("correo")) {
            String nuevoCorreo = (String) changes.get("correo");
            boolean emailExists = students.values().stream()
                    .anyMatch(s -> !s.getId().equals(id)
                            && s.getCorreo().equalsIgnoreCase(nuevoCorreo));
            if (emailExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
            }
            existing.setCorreo(nuevoCorreo);
        }
        if (changes.containsKey("numeroTelefono")) {
            existing.setNumeroTelefono((String) changes.get("numeroTelefono"));
        }
        if (changes.containsKey("idioma")) {
            existing.setIdioma((String) changes.get("idioma"));
        }

        students.put(id, existing);
        return existing;
    }

    public void delete(Long id) {
        Student existing = students.remove(id);
        if (existing == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado");
        }
    }

    public Student update(Long id, Student updated) {
        Student existing = findById(id);

        // Si cambia el correo, validar que no exista en otro estudiante
        if (!existing.getCorreo().equalsIgnoreCase(updated.getCorreo())) {
            boolean emailExists = students.values().stream()
                    .anyMatch(s -> !s.getId().equals(id)
                            && s.getCorreo().equalsIgnoreCase(updated.getCorreo()));
            if (emailExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
            }
        }

        updated.setId(id);
        students.put(id, updated);
        return updated;
    }

    public void clearAll() {
        students.clear();
        sequence.set(0);
    }
}
