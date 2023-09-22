package com.github.bibenga.palabras.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@DynamicInsert
@DynamicUpdate
public class Word implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_word_text_pair"))
    @NotNull
    private TextPair textPair;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_word_language"))
    @NotNull
    private Language language;

    @Column(name="\"text\"", nullable = false)
    @NotNull
    @NotBlank
    private String text;

    @Column(name = "created_ts", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    @CreationTimestamp
    private ZonedDateTime created;

    @Column(name = "modified_ts", nullable = false, columnDefinition = "timestamp with time zone")
    @UpdateTimestamp
    private ZonedDateTime modified;
}
