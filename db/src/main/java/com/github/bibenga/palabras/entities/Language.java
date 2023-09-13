package com.github.bibenga.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "u_language_code", columnNames = { "code" }),
})
@DynamicInsert
@DynamicUpdate
@Cacheable
public class Language implements Serializable {
    @Id
    private Byte id;

    @Column(nullable = false)
    @NotNull
    private String code;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(name = "created_ts", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(name = "modified_ts", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime modified;
}
