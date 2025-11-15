package com.buapp.user_service2.model;

import com.buapp.user_service2.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * @author DELL
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "le nom ne peut pas etre vide")
    private String nom;

    @NotBlank(message = "email ne peut pas etre vide")
    @Email(message = "la format de l'email est invalide")
    private String email;

    @NotBlank(message = "le mot de passe ne peut pas ere vide")
    @Size(min = 8, max = 64)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
