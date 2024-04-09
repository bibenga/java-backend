package com.github.bibenga.palabras.ui;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import de.agilecoders.wicket.core.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.IeEdgeMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.html.MobileViewportMetaTag;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.NavbarComponents;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6CssReference;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesome6IconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.OpenWebIconsCssReference;

@WicketHomePage
@MountPath("home")
public class HomePage extends WebPage {
    public HomePage(final PageParameters parameters) {
        super(parameters);

        add(new HtmlTag("html", WebSession.get().getLocale()));
        MobileViewportMetaTag mvt = new MobileViewportMetaTag("viewport");
        mvt.setWidth("device-width");
        mvt.setInitialScale("1");
        add(mvt);
        add(new IeEdgeMetaTag("ie-edge"));
        add(new MetaTag("description", "description", "Demo"));
        add(new MetaTag("author", Model.of("author"), Model.of("Olala <olala-989987593@gmail.com>")));

        add(new Label("title", getString("title")));

        add(newNavbar("navbar"));
        // add(newNavigation("navigation"));
        // add(new Footer("footer"));

        // add(new Code("code-internal"));

        // add(new HeaderResponseContainer("footer-container", "footer-container"));
    }

    private Component newNavbar(String componentId) {
        var nav = new Navbar(componentId);
        // nav.setBrandName(Model.of(getString("title")));
        nav.setBrandName(() -> getString("title"));
        nav.addComponents(NavbarComponents.transform(
                Navbar.ComponentPosition.LEFT,
                newNavbarButton(HomePage.class, "Home", FontAwesome6IconType.house_s),
                newNavbarButton(AdminPage.class, "Admin", OpenWebIconType.ostatus_large)));

        return nav;
    }

    private <P extends Page> Component newNavbarButton(final Class<P> pageClass, final String label) {
        return newNavbarButton(pageClass, label, null);
    }

    private <P extends Page> Component newNavbarButton(final Class<P> pageClass, final String label,
            final IconType iconType) {
        var btn = new NavbarButton<Void>(pageClass, () -> label);
        if (iconType != null) {
            btn.setIconType(iconType);
        }
        return btn;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(OpenWebIconsCssReference.instance()));
        response.render(CssHeaderItem.forReference(FontAwesome6CssReference.instance()));
    }

    @Override
    protected void setHeaders(WebResponse response) {
        super.setHeaders(response);
        if (getApplication().usesDevelopmentConfig()) {
            response.disableCaching();
        }
    }
}
