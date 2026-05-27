package com.sistemasdistr.basico.repository;

import com.sistemasdistr.basico.model.Videojuego; //importante importo mi videojuego
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Interface para poder hacer operaciones CRUD referntes a videjuegos
 * 
 * */
public interface VideJuegosRepository extends JpaRepository<Videojuego, Long>{

}
