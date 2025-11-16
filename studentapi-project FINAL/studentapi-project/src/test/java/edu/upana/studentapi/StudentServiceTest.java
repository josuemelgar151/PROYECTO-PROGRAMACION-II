package edu.upana.studentapi;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static org.junit.Assert.*;


public class StudentServiceTest {

    private StudentService service;

    @Before
    public void before() throws Exception {
        service = new StudentService();
        service.clearAll();
    }

    @Test
    public void testCrearYBuscarEstudiantePorId() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");

        Student creado = service.create(s);

        assertNotNull(creado.getId());
        assertEquals("Ana", creado.getNombre());

        Student encontrado = service.findById(creado.getId());
        assertEquals("Ana", encontrado.getNombre());
        assertEquals("ana@example.com", encontrado.getCorreo());
        assertEquals("12345678", encontrado.getNumeroTelefono());
        assertEquals("español", encontrado.getIdioma());
    }

    @Test
    public void testFindByIdLanzaExcepcionCuandoNoExiste() {
        try {
            service.findById(500L);
            fail("Debió lanzar excepción por estudiante inexistente");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getReason().contains("Estudiante no encontrado"));
        }
    }

    @Test
    public void testNoPermiteCorreosDuplicadosEnCreate() {
        Student s1 = new Student();
        s1.setNombre("Ana");
        s1.setCorreo("ana@example.com");
        s1.setNumeroTelefono("12345678");
        s1.setIdioma("español");

        Student s2 = new Student();
        s2.setNombre("Luis");
        s2.setCorreo("ana@example.com"); // correo repetido
        s2.setNumeroTelefono("87654321");
        s2.setIdioma("inglés");

        service.create(s1);

        try {
            service.create(s2);
            fail("Debió lanzar ResponseStatusException por correo duplicado");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
            assertTrue(e.getReason().contains("El correo ya está registrado"));
        }
    }

    @Test
    public void testUpdateReemplazaDatosCorrectamente() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");

        Student creado = service.create(s);

        Student nuevo = new Student();
        nuevo.setNombre("Ana María");
        nuevo.setCorreo("nuevo@example.com");
        nuevo.setNumeroTelefono("22223333");
        nuevo.setIdioma("francés");

        Student actualizado = service.update(creado.getId(), nuevo);

        assertEquals(creado.getId(), actualizado.getId());
        assertEquals("Ana María", actualizado.getNombre());
        assertEquals("nuevo@example.com", actualizado.getCorreo());
        assertEquals("22223333", actualizado.getNumeroTelefono());
        assertEquals("francés", actualizado.getIdioma());
    }

    @Test
    public void testPartialUpdateModificaSoloCamposEnviados() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");

        Student creado = service.create(s);

        // Solo se envía el correo en el PATCH
        service.partialUpdate(creado.getId(), Map.of(
                "correo", "nuevo@example.com"
        ));

        Student resultado = service.findById(creado.getId());
        assertEquals("nuevo@example.com", resultado.getCorreo());
        // Lo demás no debe cambiar
        assertEquals("Ana", resultado.getNombre());
        assertEquals("12345678", resultado.getNumeroTelefono());
        assertEquals("español", resultado.getIdioma());
    }

    @Test
    public void testDeleteEliminaEstudiante() {
        Student s = new Student();
        s.setNombre("Ana");
        s.setCorreo("ana@example.com");
        s.setNumeroTelefono("12345678");
        s.setIdioma("español");

        Student creado = service.create(s);

        service.delete(creado.getId());

        try {
            service.findById(creado.getId());
            fail("Debió lanzar excepción porque el estudiante ya fue eliminado");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            assertTrue(e.getReason().contains("Estudiante no encontrado"));
        }
    }

    @Test
    public void testToString() {
        Student s = new Student();
        s.setId(10L);
        s.setNombre("Carlos");
        s.setCorreo("carlos@example.com");
        s.setNumeroTelefono("87654321");
        s.setIdioma("francés");

        // Lombok @ToString normalmente genera:
        // Student(id=10, nombre=Carlos, correo=carlos@example.com, numeroTelefono=87654321, idioma=francés)
        String esperado = ("Student(id=" + s.getId()
                + ", nombre=" + s.getNombre()
                + ", correo=" + s.getCorreo()
                + ", numeroTelefono=" + s.getNumeroTelefono()
                + ", idioma=" + s.getIdioma()
                + ")").toLowerCase();

        String obtenido = s.toString().toLowerCase();

        assertEquals(esperado, obtenido);
    }
}