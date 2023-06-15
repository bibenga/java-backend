package com.mycompany.test2.db;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Formula;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "u_dict_application_platform", columnNames = { "name" }),
})
@DynamicInsert
@DynamicUpdate
@Cacheable
// @Log4j2
public class DictApplicationPlatform implements Serializable {
    @Id
    @Access(AccessType.PROPERTY)
    private Short id;

    @Column(nullable = false)
    // @NaturalId(mutable = true)
    // @NotNull
    private String slug;

    // @Column(nullable = false, unique = true)
    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String displayName;

    @CreationTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    // @NotNull
    private ZonedDateTime created;

    @UpdateTimestamp
    @Setter(value = AccessLevel.PRIVATE)
    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    // @NotNull
    private ZonedDateTime modified;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime deleted;

    @Setter(value = AccessLevel.PRIVATE)
    @Version
    private Long version;

    @Setter(value = AccessLevel.PRIVATE)
    @Formula("deleted is not null")
    private boolean softDeleted;

    @PrePersist
    private void prePersist() {
        // setCreated(ZonedDateTime.now());
        // created = ZonedDateTime.now();
        // setModified(ZonedDateTime.now());
        // modified = ZonedDateTime.now();
        // setSlug(SlugListener.normalize(getName()));
        slug = SlugListener.normalize(getName());
    }

    @PreUpdate
    private void preUpdate() {
        // setModified(ZonedDateTime.now());
        // modified = ZonedDateTime.now();
        // setSlug(SlugListener.normalize(getName()));
        slug = SlugListener.normalize(getName());
    }

    public void softDelete() {
        if (deleted == null) {
            deleted = ZonedDateTime.now();
        }
    }
}
