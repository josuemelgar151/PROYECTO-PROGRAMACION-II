package edu.upana.studentapi;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.ToString;

@ToString
public class Student {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no debe exceder 255 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El número de teléfono debe tener exactamente 8 dígitos")
    private String numeroTelefono;

    @NotBlank(message = "El idioma es obligatorio")
    @Pattern(regexp = "inglés|español|francés", message = "El idioma debe ser 'inglés', 'español' o 'francés'")
    private String idioma;

    public Student() {}

    public Student(Long id, String nombre, String correo, String numeroTelefono, String idioma) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.numeroTelefono = numeroTelefono;
        this.idioma = idioma;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}
