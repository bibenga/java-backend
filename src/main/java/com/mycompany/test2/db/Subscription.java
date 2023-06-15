package com.mycompany.test2.db;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "set", toBuilder = true)
@Entity
@DynamicInsert
@DynamicUpdate
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subscription_id", columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    @NaturalId
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "status_ts", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    // @ColumnDefault(value="CURRENT_TIMESTAMP")
    // @Generated(GenerationTime.INSERT)
    private ZonedDateTime statusDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<SubscriptionStatus> subscriptionStatusList;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime createdDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime modifiedDate;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Service> serviceList;

    // @Version
    // private Long version;
    @Version
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime version;

    private String f00;
    private Double f01;
    private Boolean f02;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> f03;

    public void setStatus(Status status) {
        if (status != this.status) {
            this.status = status;
            this.statusDate = ZonedDateTime.now();
        }
    }
}
