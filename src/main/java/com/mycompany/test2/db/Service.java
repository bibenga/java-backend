package com.mycompany.test2.db;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@Table(indexes = {
        @Index(name = "service_input_topic_idx", columnList = "inputTopic", unique = true),
        @Index(name = "service_output_topic_idx", columnList = "outputTopic"),
})
public class Service implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Subscription subscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private DictServiceType dictServiceType;

    @Column(nullable = false)
    private String inputTopic;

    @Column(nullable = false)
    private String outputTopic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private ZonedDateTime statusDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ServiceStatus> serviceStatusList;

    // @Version
    // private Long version;

    @Version
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime version;
}
