package com.sl.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// __tablename__ = "slfrase_studystate"
// # "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT
// # "is_passed_flg" bool NOT NULL
// # "passed_ts" datetime NULL
// # "created_ts" datetime NOT NULL
// # "modified_ts" datetime NOT NULL
// # "text_pair_id" bigint NOT NULL REFERENCES "slfrase_textpair" ("id") DEFERRABLE INITIALLY DEFERRED
// # "is_skipped_flg" bool NOT NULL
// # "answer" text NOT NULL
// # "possible_answers" text NOT NULL
// # "question" text NOT NULL

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Cacheable
public class StudyState implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_text_pair"))
    @NotNull
    private TextPair textPair;

    @Column(nullable = false)
    private boolean is_passed_flg;

    @Column(nullable = false)
    private boolean is_skipped_flg;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime modified;
}
