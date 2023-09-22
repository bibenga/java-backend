package com.github.bibenga.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
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
// @EntityListeners(AuditingEntityListener.class)
public class Language implements Serializable {
    @Id
    @NotNull
    private Byte id;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String code;

    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "created_ts", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    @CreationTimestamp
    // @CreatedDate
    private ZonedDateTime created;

    @Column(name = "modified_ts", nullable = false, columnDefinition = "timestamp with time zone")
    @UpdateTimestamp
    // @Version
    // @LastModifiedDate
    private ZonedDateTime modified;
}
