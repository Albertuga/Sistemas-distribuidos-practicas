package com.sistemasdistr.basico.model;
/**
 * Creo la entidad Categoria con la finalidad de que contenga 
 * un listado de los tipos de juegos que tendrá la pagina ya creada anteriormente.
 * 
 * */

import jakarta.persistence.*;
import java.util.List; //en enunciados se nos pide almacenar en listads

@Entity
@Table(name="categorias")
public class Categoria {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // Relación: Una categoría puede tener muchos videojuegos
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Videojuego> videojuegos;

    // Constructores vacíos y con parámetros
    public Categoria() {}

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Videojuego> getVideojuegos() {
		return videojuegos;
	}

	public void setVideojuegos(List<Videojuego> videojuegos) {
		this.videojuegos = videojuegos;
	}
	

}
