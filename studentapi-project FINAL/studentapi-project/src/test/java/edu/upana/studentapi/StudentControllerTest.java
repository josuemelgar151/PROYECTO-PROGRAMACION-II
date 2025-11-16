package edu.upana.studentapi;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class StudentControllerTest {

    private StudentService service;
    private StudentController controller;

    @Before
    public void before() throws Exception {
        service = new StudentService();
        service.clearAll();
        controller = new StudentController(service);
    }

    @Test
    public void testGetAllStudentsDevuelveLista() {
        // Arrange: agregamos dos estudiantes usando el servicio
        Student s1 = new Student();
        s1.setNombre("Ana");
        s1.setCorreo("ana@example.com");
        s1.setNumeroTelefono("12345678");
        s1.setIdioma("español");
        service.create(s1);

        Student s2 = new Student();
        s2.setNombre("Luis");
        s2.setCorreo("luis@example.com");
        s2.setNumeroTelefono("87654321");
        s2.setIdioma("inglés");
        service.create(s2);

        // Act
        List<Student> lista = controller.getAllStudents();

        // Assert
        assertEquals(2, lista.size());
    }

    @Test
    public void testGetStudentByIdDevuelveEstudianteCorrecto() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");

        Student creado = service.create(s);

        Student obtenido = controller.getStudentById(creado.getId());

        assertEquals("Ana", obtenido.getNombre());
        assertEquals("ana@example.com", obtenido.getCorreo());
    }

    @Test
    public void testCreateStudentCreaYDevuelveConId() {
        Student s = new Student();
        s.setNombre("Carlos");
        s.setCorreo("carlos@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("francés");

        Student creado = controller.createStudent(s);

        assertNotNull(creado.getId());
        assertEquals("Carlos", creado.getNombre());

        // Confirmar que quedó guardado en el servicio
        Student buscado = service.findById(creado.getId());
        assertEquals("carlos@example.com", buscado.getCorreo());
    }

    @Test
    public void testUpdateStudentReemplazaDatos() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");
        Student creado = service.create(s);

        Student nuevo = new Student();
        nuevo.setNombre("Ana Actualizada");
        nuevo.setCorreo("nuevo@example.com");
        nuevo.setNumeroTelefono("22223333");
        nuevo.setIdioma("inglés");

        Student actualizado = controller.updateStudent(creado.getId(), nuevo);

        assertEquals("Ana Actualizada", actualizado.getNombre());
        assertEquals("nuevo@example.com", actualizado.getCorreo());
        assertEquals("22223333", actualizado.getNumeroTelefono());
        assertEquals("inglés", actualizado.getIdioma());
    }

    @Test
    public void testPartialUpdateStudentModificaSoloAlgunosCampos() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");
        Student creado = service.create(s);

        controller.partialUpdateStudent(creado.getId(), Map.of(
                "correo", "nuevo@example.com"
        ));

        Student resultado = service.findById(creado.getId());
        assertEquals("nuevo@example.com", resultado.getCorreo());
        assertEquals("Ana", resultado.getNombre());
        assertEquals("12345678", resultado.getNumeroTelefono());
        assertEquals("español", resultado.getIdioma());
    }

    @Test
    public void testDeleteStudentEliminaDelServicio() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");
        Student creado = service.create(s);

        controller.deleteStudent(creado.getId());

        try {
            service.findById(creado.getId());
            fail("Debió lanzar excepción por estudiante eliminado");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void testToString() {
        // Como la clase tiene @ToString de Lombok, verificamos el formato básico
        String esperado = ("StudentController(service=" + service.toString() + ")").toLowerCase();
        String obtenido = controller.toString().toLowerCase();

        assertEquals(esperado, obtenido);
    }
}