package com.projectx.module.user.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String userName;
    private String fullName;
    @Lob
    private String address1;
    @Lob
    private String address2;
    private String cityName;
    private String state;
    private String country;
    private String pinCode;
    private String countryCode;
    private String mobile1;
    private String mobile2;
    @Lob
    private String identityCardUrl;
    @Lob
    private String photoUrl;
    @Column(unique = true)
    private String emailId;
    private String password;
}
