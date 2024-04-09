package com.github.bibenga.palabras.ui;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.stereotype.Component;

import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;

@Component
public class BootstrapInitConfiguration implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        var settings = new BootstrapSettings();
        Bootstrap.install(webApplication, settings);
        webApplication.getCspSettings().blocking().disabled();
    }

}
