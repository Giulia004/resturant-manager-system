package com.resturant.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity /*JPA dice che questa classe sarà una tabella nel database */
@Data /*Genera automaticamente i metodi getter, setter, toString, equals e hashCode */
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") /*Nome della tabella all'interno del database */

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Enumerated(EnumType.STRING) /*Salva il ruolo come una stringa */
    @Column(nullable=false)
    private Role role;

}
