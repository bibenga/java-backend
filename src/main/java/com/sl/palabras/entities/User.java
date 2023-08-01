package com.sl.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Table(name = "user0", uniqueConstraints = {
        @UniqueConstraint(name = "u_application_username", columnNames = { "username" }),
})
@DynamicInsert
@DynamicUpdate
@Cacheable
public class User implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String username;

    @Column
    private String password;

    @Column(name = "is_enabled_flg")
    @ColumnDefault("true")
    private boolean enabled;

    @Column(name = "created_ts", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(name = "modified_ts", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime modified;
}
