package org.vaadin.marcus.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.marcus.views.ChatView;
import org.vaadin.marcus.views.BookingView;
import org.vaadin.marcus.views.ClientManagementView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Funnair Customer Support");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        addToNavbar(toggle, title);

        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.add(new RouterLink("Chat", ChatView.class));
        menuLayout.add(new RouterLink("Bookings", BookingView.class));
        menuLayout.add(new RouterLink("Client Management", ClientManagementView.class));

        addToDrawer(menuLayout);
    }
}