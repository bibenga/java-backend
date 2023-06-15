package com.mycompany.test2.db;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "u_application", columnNames = { "dict_application_platrorm_id", "name" }),
        @UniqueConstraint(name = "u_application_slug", columnNames = { "slug" }),
})
@DynamicInsert
@DynamicUpdate
@Cacheable
public class Application implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_dict_application_platrorm"))
    @NotNull
    private DictApplicationPlatform dictApplicationPlatrorm;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime modified;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deleted;

    // @Version
    // @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    // private ZonedDateTime version;

    @Setter(value = AccessLevel.PRIVATE)
    @Formula("deleted is not null")
    private boolean softDeleted;

    @PrePersist
    @PreUpdate
    private void preSave() {
        var slug = String.format("%s-%s",
                dictApplicationPlatrorm.getSlug(),
                SlugListener.normalize(getName()));
        if (this.slug != slug) {
            // setSlug(slug);
            this.slug = slug;
        }
    }
}
