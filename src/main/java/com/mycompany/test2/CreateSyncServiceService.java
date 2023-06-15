package com.mycompany.test2;

import com.mycompany.test2.db.Service;
import com.mycompany.test2.db.ServiceRepository;
import com.mycompany.test2.db.Status;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Slf4j
public class CreateSyncServiceService {

    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private TransactionTemplate transactionTemplate;

    // @Scheduled(fixedDelay = 5000)
    public void findAndProcess() {
        while (findAndProcessOne()) ;
    }

    private boolean findAndProcessOne() {
        var service = findNext();
        if (service == null) {
            return false;
        }

        for (var i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            log.info("tik: {}", service);
        }

        setStatus(service, Status.ACTIVE, true);
        return true;
    }

    public Service findNext() {
        return transactionTemplate.execute(trnStatus -> {
            var servicePage = serviceRepository.findAllBySubscriptionIsNullAndStatus(
                    Status.READY, PageRequest.of(0, 1));
            if (servicePage.isEmpty()) {
                log.info("no sync service found for create");
                return null;
            }
            var service = servicePage.get(0);
            log.info("found: {}", service);
            service = setStatus(service, Status.DEPLOYING, false);
            return service;
        });
    }

    public Service setStatus(Service service, Status status, boolean refresh) {
        return transactionTemplate.execute(trnStatus -> {
            var service0 = service;
            if (refresh) {
                service0 = serviceRepository.getReferenceById(service.getId());
            }
            var previousStatus = service0.getStatus();
            service0.setStatus(status);
            serviceRepository.save(service0);
            log.info("updated: {}", service0);
            applicationEventPublisher.publishEvent(new ServiceStatusChanged(service0, previousStatus));
            return service0;
        });
    }

}
