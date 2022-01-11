package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.haulmont.testtask.backend.CreditProvider;
import com.haulmont.testtask.model.entity.Credit;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventBus;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.GridSortOrderBuilder;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.function.ValueProvider;
import lombok.Getter;

import java.util.List;
import java.util.Locale;

import static com.haulmont.testtask.Setting.*;

public class CreditGridLayout extends VerticalLayout implements CanBeShown, CanBeClosed, HasEvent {


    private final Grid<Credit> grid = new Grid<>();
    private final CreditProvider creditProvider;
    private final Button close = new Button();


    public CreditGridLayout(CreditProvider creditProvider) {
        this.creditProvider = creditProvider;
        grid.setItems(creditProvider.getAllCredit());
        tuneGrid();
        add(grid, createButtons());
    }

    private HorizontalLayout createButtons() {
        tuneCloseButton();
        return new HorizontalLayout(close);
    }

    @Override
    public Button getCloseButton() {
        return close;
    }

    @Override
    public void close() {
        fireEvent(new CloseEvent(this));
    }

    private void tuneGrid() {
        Grid.Column<Credit> bankName = grid.addColumn((ValueProvider<Credit, String>) credit -> credit.getBank().getName()).setHeader("Bank Name").setWidth("40px");

        Grid.Column<Credit> creditLimit = grid.addColumn(
                new NumberRenderer<>(Credit::getCreditLimit, CREDIT_LIMIT_FORMAT,
                        Locale.US, Setting.CREDIT_LIMIT_DEFAULT_VALUE)).setHeader(Setting.CREDIT_LIMIT_LABEL);

        Grid.Column<Credit> creditRate = grid.addColumn(
                new NumberRenderer<>(Credit::getCreditRate, CREDIT_RATE_FORMAT,
                Locale.US, Setting.CREDIT_RATE_DEFAULT_VALUE)).setHeader(CREDIT_RATE_LABEL);
        grid.addComponentColumn(credit -> {
            Button delete = new Button();
            delete.setText(Setting.DELETE_BUTTON_TEXT);
            delete.addThemeVariants(Setting.DELETE_STYLE);
            delete.addClickListener(event -> {
                fireEvent(new CreditGridLayout.CloseEvent(this));
                fireEvent(new CreditGridLayout.DeleteEvent(this, credit));
            });
            return delete;
        });
        grid.addComponentColumn(credit -> {
            Button edit = new Button();
            edit.setText(Setting.EDIT_BUTTON_TEXT);
            edit.addThemeVariants(Setting.DELETE_STYLE);
            edit.addClickListener(event -> {
                fireEvent(new CreditGridLayout.CloseEvent(this));
                fireEvent(new CreditGridLayout.EditEvent(this, credit));
            });
            return edit;
        });

        List<GridSortOrder<Credit>> bankSort = new GridSortOrderBuilder<Credit>()
                .thenDesc(bankName).build();
        List<GridSortOrder<Credit>> rateSort = new GridSortOrderBuilder<Credit>().thenDesc(creditRate).build();
        List<GridSortOrder<Credit>> limitSort = new GridSortOrderBuilder<Credit>().thenDesc(creditLimit).build();
        grid.sort(bankSort);
        grid.sort(limitSort);
        grid.sort(rateSort);
    }

    @Override
    public void show() {
        grid.setItems(creditProvider.getAllCredit());
    }

    public void update() {
        grid.setItems(creditProvider.getAllCredit());
    }

    @Override
    public ComponentEventBus getEventBusFromLayout() {
        return getEventBus();
    }

    public static class CloseEvent extends ComponentEvent<CreditGridLayout> {
        public CloseEvent(CreditGridLayout source) {
            super(source, false);
        }
    }

    public static class DeleteEvent extends ComponentEvent<CreditGridLayout> {
        @Getter
        private final Credit credit;

        public DeleteEvent(CreditGridLayout source, Credit credit) {
            super(source, false);
            this.credit = credit;
        }
    }

    public static class EditEvent extends ComponentEvent<CreditGridLayout> {
        @Getter
        private final Credit credit;

        public EditEvent(CreditGridLayout source, Credit credit) {
            super(source, false);
            this.credit = credit;
        }
    }
}
