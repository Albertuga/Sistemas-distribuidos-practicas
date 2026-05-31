package com.sistemasdistr.basico.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "username", length = 50)
    private String username;
    @Column(name = "emailQQQ", length = 50)
    private String emailuser;
    @Column(name = "nombre_usuario", length = 30)
    private String nombreUsuario;
    @Column(name = "password", length = 250)
    private String password;
    @Column(name = "estado")
    private boolean baneado = false;

    @Lob
    private byte[] publickey;

    @Column(name = "fechaUltimoAcceso")
    private LocalDateTime fechaUltimoAcceso;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role userRole;

	public boolean isBaneado() {
		return baneado;
	}

	public void setBaneado(boolean baneado) {
		this.baneado = baneado;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmailuser() {
		return emailuser;
	}

	public void setEmailuser(String emailuser) {
		this.emailuser = emailuser;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getPublickey() {
		return publickey;
	}

	public void setPublickey(byte[] publickey) {
		this.publickey = publickey;
	}

	public LocalDateTime getFechaUltimoAcceso() {
		return fechaUltimoAcceso;
	}

	public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

    

}
