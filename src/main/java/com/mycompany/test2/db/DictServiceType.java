package com.mycompany.test2.db;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Cacheable
@EntityListeners(SlugListener.class)
public class DictServiceType implements Serializable {
    @Id
    @Access(AccessType.PROPERTY)
    private Short id;

    @Column(nullable = false)
    @NaturalId
    @NotNull
    private String slug;

    @Column(nullable = false, unique = true)
    @NotNull
    private String name;

    @Column
    private String displayName;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Version
    @NotNull
    private ZonedDateTime version;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deleted;

    @Setter(value = AccessLevel.PRIVATE)
    @Formula("deleted is null")
    private boolean softDeleted;

    @Setter(value = AccessLevel.PRIVATE)
    @Formula("EXISTS(select * from service as s where s.input_topic=name)")
    private boolean anyServiceExist;
}
