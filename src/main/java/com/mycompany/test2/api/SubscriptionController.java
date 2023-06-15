package com.mycompany.test2.api;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.test2.SubscriptionService;
import com.mycompany.test2.SubscriptionStatusChanged;
import com.mycompany.test2.db.Service;
import com.mycompany.test2.db.ServiceRepository;
import com.mycompany.test2.db.Status;
import com.mycompany.test2.db.Subscription;
import com.mycompany.test2.db.SubscriptionRepository;
import com.mycompany.test2.db.SubscriptionStatus;
import com.mycompany.test2.db.SubscriptionStatusRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Slf4j
@Validated
// @OpenAPIDefinition(security = {
// @SecurityRequirement(name = "token"),
// @SecurityRequirement(name = "basic")
// })
@SecurityRequirements(value = {
        // @SecurityRequirement(name = "token"),
        @SecurityRequirement(name = "basic")
})
public class SubscriptionController {
    private final Collection<Status> ACTIVE_LIST = Collections.unmodifiableCollection(
            Arrays.asList(Status.READY, Status.DEPLOYING, Status.ACTIVE));

    private final Collection<Status> CAN_DELETE_LIST = Collections.unmodifiableCollection(
            Arrays.asList(Status.READY, Status.DEPLOYING, Status.ACTIVE, Status.READY_TO_DELETE));

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionStatusRepository subscriptionStatusRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping(path = "/count", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Secured("ROLE_ADMIN")
    @ResponseBody
    public @PositiveOrZero long count() {
        var count = subscriptionService.count();
        return count;
    }

    @GetMapping(produces = "application/json")
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    // ResponseEntity<List<SubscriptionModel>>
    public @ResponseBody List<SubscriptionModel> get(
            @RequestParam(required = false) List<UUID> id,
            @RequestParam(required = false, defaultValue = "true") Boolean isActive,
            // Pageable pageRequest
            @RequestParam(value = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) @Min(10) @Max(100) Integer pageSize,
            @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction order,
            @RequestParam(value = "sort", defaultValue = "id", required = false) Set<@Pattern(regexp = "id|name") String> sort) {

        var pageRequest = PageRequest.of(page, pageSize, Sort.by(order, sort.toArray(new String[0])));
        log.info("pageRequest: {}", pageRequest);

        try (var subscriptions = subscriptionRepository.finaAllActive()) {
            subscriptions.forEach(subscription -> log.info("subscription: {}", subscription));
        }

        var s1 = new SubscriptionModel(
                UUID.randomUUID(),
                "s1",
                Arrays.asList("user1"),
                Arrays.asList(new SubscriptionTopicModel("topic1")));
        var s2 = SubscriptionModel.builder()
                .setId(UUID.randomUUID())
                .setAlias("s2")
                .setTargetUserList(Arrays.asList("user2"))
                .setTopicList(Arrays.asList(
                        SubscriptionTopicModel.builder().setName("topic2").build()))
                .build();
        var subscriptionsModel = Arrays.asList(s1, s2);
        log.info("loaded: {}", subscriptionsModel);

        // return ResponseEntity.ok(subscriptionsModel);
        return subscriptionsModel;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<SubscriptionModel> create(@RequestBody @Valid SubscriptionModel subscriptionModel) {
        log.info("create: {}, {}", subscriptionModel.getId(), subscriptionModel);

        Subscription subscription = subscriptionRepository.getReferenceById(subscriptionModel.getId());
        try {
            subscription.getAlias(); // только при вызове метода реально выдасться EntityNotFoundException
            log.info("loaded by '{}': {}", subscription.getId(), subscription);
        } catch (EntityNotFoundException e) {
            subscription = null;
            log.info("loaded by '{}': null", subscriptionModel.getId());
        }
        if (subscription != null) {
            throw new SubscriptionAlreadyExists(subscriptionModel.getId());
        }

        subscription = new Subscription();
        subscription.setId(subscriptionModel.getId());
        subscription.setAlias(subscriptionModel.getAlias());
        subscription.setStatus(Status.READY);
        subscription.setStatusDate(ZonedDateTime.now());
        // subscription.setCreatedDate(ZonedDateTime.now());
        // subscription.setModifiedDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);
        log.info("create subscription: {}", subscription);

        subscriptionModel.getTopicList().sort(Comparator.comparing(SubscriptionTopicModel::getName));

        for (var topicModel : subscriptionModel.getTopicList()) {
            var syncService = serviceRepository.getOneByInputTopicAndStatusIn(topicModel.getName(), ACTIVE_LIST);
            if (syncService == null) {
                syncService = new Service();
                syncService.setInputTopic(topicModel.getName());
                syncService.setOutputTopic(String.format("BigKafka.%s", topicModel.getName()));
                syncService.setStatus(Status.READY);
                syncService.setStatusDate(ZonedDateTime.now());
                serviceRepository.save(syncService);
                log.info("create sync service: {}", syncService);
            } else {
                log.info("use existed sync service: {}", syncService);
            }

            var streamService = new Service();
            streamService.setSubscription(subscription);
            streamService.setInputTopic(syncService.getOutputTopic());
            streamService.setOutputTopic(
                    String.format("BigKafka.%s.%s", subscription.getAlias(), topicModel.getName()));
            streamService.setStatus(Status.READY);
            streamService.setStatusDate(ZonedDateTime.now());
            serviceRepository.save(streamService);
            log.info("create stream service: {}", streamService);
        }

        subscription.setStatus(Status.DEPLOYING);
        subscription.setStatusDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);
        var subscriptionStatus = new SubscriptionStatus();
        subscriptionStatus.setSubscription(subscription);
        subscriptionStatus.setStatus(subscription.getStatus());
        subscriptionStatus.setStatusDate(subscriptionStatus.getStatusDate());
        subscriptionStatusRepository.save(subscriptionStatus);
        log.info("subscription created: {}", subscription);

        applicationEventPublisher.publishEvent(new SubscriptionStatusChanged(subscription));

        return ResponseEntity.ok(subscriptionModel);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Subscription get(@PathVariable UUID id) {
        var subscription = subscriptionService.getById(id);
        return subscription;
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void delete(@PathVariable UUID id) {
        log.info("delete: {}", id);

        var subscription = subscriptionRepository.getReferenceById(id);
        if (subscription == null) {
            throw new SubscriptionNotFound(id);
        }
        try {
            subscription.getAlias(); // только при вызове метода реально выдасться EntityNotFoundException
        } catch (EntityNotFoundException e) {
            throw new SubscriptionNotFound(id);
        }
        if (!CAN_DELETE_LIST.contains(subscription.getStatus())) {
            throw new SubscriptionNotFound(id);
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("loaded: {}", subscription);

        subscription.setStatus(Status.DELETING);
        subscription.setStatusDate(ZonedDateTime.now());
        subscriptionRepository.save(subscription);
        log.info("updated: {}", subscription);

        applicationEventPublisher.publishEvent(new SubscriptionStatusChanged(subscription));
    }
}
