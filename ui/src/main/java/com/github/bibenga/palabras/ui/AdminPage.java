package com.github.bibenga.palabras.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("admin")
public class AdminPage extends WebPage {
    public AdminPage(final PageParameters parameters) {
        super(parameters);
    }
}
