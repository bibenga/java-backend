package com.github.bibenga.palabras.ui;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.stereotype.Component;

import com.giffing.wicket.spring.boot.context.extensions.WicketApplicationInitConfiguration;

import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeSettings;

@Component
public class BootstrapInitConfiguration implements WicketApplicationInitConfiguration {

    @Override
    public void init(WebApplication webApplication) {
        Bootstrap.install(webApplication);
        // FontAwesomeSettings.get(webApplication).setCssResourceReference(FontAwesome6CssReference.instance());
        webApplication.getCspSettings().blocking().disabled();
    }

}
