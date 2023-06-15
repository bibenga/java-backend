package com.mycompany.test2;

import com.mycompany.test2.db.Application;
import java.util.Collection;

public interface ApplicationService {
    public long count();

    public Collection<Application> findAll();

    public Application save(Application application);

    public Application findOne(final String dictApplicationPlatrorm, final String name);
}
