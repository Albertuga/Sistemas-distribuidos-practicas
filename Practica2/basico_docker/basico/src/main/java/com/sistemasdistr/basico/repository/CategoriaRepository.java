package com.sistemasdistr.basico.repository;

import com.sistemasdistr.basico.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Para que Java pueda hacer el CRUD (Crear, Leer, Actualizar, Borrar) 
 * en la base de datos sin que tengamos que escribir sentencias SQL a mano, 
 * necesitamos sus repositorios.
 * Por ello creamos los repos categirua y videjuegos con Jpa
 * 
 * */

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
}
