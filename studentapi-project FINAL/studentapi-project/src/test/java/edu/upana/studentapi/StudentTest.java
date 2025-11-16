package edu.upana.studentapi;

import org.junit.Before;
import org.junit.Test;

import jakarta.validation.*;
import java.util.Set;

import static org.junit.Assert.*;


public class StudentTest {

    private Student student;
    private Validator validator;

    @Before
    public void before() throws Exception {
        student = new Student();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testConstructorConParametrosAsignaValoresCorrectos() {
        Student s = new Student(1L, "Ana", "ana@example.com", "12345678", "español");

        assertEquals(Long.valueOf(1), s.getId());
        assertEquals("Ana", s.getNombre());
        assertEquals("ana@example.com", s.getCorreo());
        assertEquals("12345678", s.getNumeroTelefono());
        assertEquals("español", s.getIdioma());
    }

    @Test
    public void testSettersYGettersFuncionan() {
        student.setId(10L);
        student.setNombre("Luis");
        student.setCorreo("luis@gmail.com");
        student.setNumeroTelefono("87654321");
        student.setIdioma("inglés");

        assertEquals(Long.valueOf(10), student.getId());
        assertEquals("Luis", student.getNombre());
        assertEquals("luis@gmail.com", student.getCorreo());
        assertEquals("87654321", student.getNumeroTelefono());
        assertEquals("inglés", student.getIdioma());
    }

    @Test
    public void testValidacionNombreObligatorio() {
        student.setNombre(""); // ERROR
        student.setCorreo("email@test.com");
        student.setNumeroTelefono("12345678");
        student.setIdioma("español");

        Set<ConstraintViolation<Student>> errores = validator.validate(student);

        assertTrue(errores.stream()
                .anyMatch(e -> e.getMessage().contains("El nombre es obligatorio")));
    }

    @Test
    public void testValidacionCorreoFormatoInvalido() {
        student.setNombre("Ana");
        student.setCorreo("correo-invalido");
        student.setNumeroTelefono("12345678");
        student.setIdioma("español");

        Set<ConstraintViolation<Student>> errores = validator.validate(student);

        assertTrue(errores.stream()
                .anyMatch(e -> e.getMessage().contains("formato válido")));
    }

    @Test
    public void testValidacionTelefonoDebeSer8Digitos() {
        student.setNombre("Ana");
        student.setCorreo("ana@example.com");
        student.setNumeroTelefono("123"); // ERROR
        student.setIdioma("español");

        Set<ConstraintViolation<Student>> errores = validator.validate(student);

        assertTrue(errores.stream()
                .anyMatch(e -> e.getMessage().contains("exactamente 8 dígitos")));
    }

    @Test
    public void testValidacionIdiomaSoloPermitidos() {
        student.setNombre("Ana");
        student.setCorreo("ana@example.com");
        student.setNumeroTelefono("12345678");
        student.setIdioma("chino"); // ERROR

        Set<ConstraintViolation<Student>> errores = validator.validate(student);

        assertTrue(errores.stream()
                .anyMatch(e -> e.getMessage().contains("inglés")));
    }

    @Test
    public void testToString() {
        student.setId(1L);
        student.setNombre("Ana");
        student.setCorreo("ana@example.com");
        student.setNumeroTelefono("12345678");
        student.setIdioma("español");

        // Lombok produce: Student(id=1, nombre=..., correo=..., ...)
        String obtenido = student.toString().toLowerCase();

        assertTrue(obtenido.contains("student"));
        assertTrue(obtenido.contains("ana"));
        assertTrue(obtenido.contains("español"));
        assertTrue(obtenido.contains("12345678"));
    }
}