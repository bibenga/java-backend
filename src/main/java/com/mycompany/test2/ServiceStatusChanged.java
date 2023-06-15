package com.mycompany.test2;

import com.mycompany.test2.db.Service;
import com.mycompany.test2.db.Status;
import org.springframework.context.ApplicationEvent;

public class ServiceStatusChanged extends ApplicationEvent {
    private final Status previousStatus;

    public ServiceStatusChanged(Service service) {
        super(service);
        this.previousStatus = null;
    }

    public ServiceStatusChanged(Service service, Status previousStatus) {
        super(service);
        this.previousStatus = previousStatus;
    }

    public Service getService() {
        return (Service) getSource();
    }

    public Status getPreviousStatus() {
        return previousStatus;
    }
}
