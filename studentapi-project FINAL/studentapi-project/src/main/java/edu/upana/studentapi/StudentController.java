package edu.upana.studentapi;

import jakarta.validation.Valid;

import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
@ToString
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // 1. Ver todos los estudiantes
    @GetMapping
    public List<Student> getAllStudents() {
        return service.findAll();
    }

    // 2. Ver estudiante por ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return service.findById(id);
    }

    // 3. Agregar estudiante
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@Valid @RequestBody Student student) {
        return service.create(student);
    }

    // 4. Editar estudiante (completo)
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @Valid @RequestBody Student student) {
        return service.update(id, student);
    }

    // 5. Editar estudiante (parcial)
    @PatchMapping("/{id}")
    public Student partialUpdateStudent(@PathVariable Long id, @RequestBody Map<String, Object> changes) {
        return service.partialUpdate(id, changes);
    }

    // 6. Eliminar estudiante
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        service.delete(id);
    }

    // Manejo simple de errores de validación
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult result = ex.getBindingResult();
        result.getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );
        return errors;
    }
}
