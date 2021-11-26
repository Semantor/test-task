package com.haulmont.testtask.view;

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
import java.util.UUID;

import static com.haulmont.testtask.view.Constant.creditLimitFormat;
import static com.haulmont.testtask.view.Constant.creditRateFormat;

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
        grid.addColumn(Credit::getCreditId).setHeader("UUID");

        Grid.Column<Credit> bank_uuid = grid.addColumn((ValueProvider<Credit, UUID>) credit -> credit.getBank().getBankId()).setHeader("Bank UUID").setWidth("40px");

        Grid.Column<Credit> creditLimit = grid.addColumn(new NumberRenderer<>(Credit::getCreditLimit, creditLimitFormat,
                Locale.US, "$ 0.00")).setHeader("Credit limit");

        Grid.Column<Credit> creditRate = grid.addColumn(new NumberRenderer<>(Credit::getCreditRate, creditRateFormat,
                Locale.US, "% 0.00")).setHeader("Credit Rate, %");
        grid.addComponentColumn(credit -> {
            Button delete = new Button();
            delete.setText(Constant.DELETE_TEXT_BUTTON);
            delete.addThemeVariants(Constant.DELETE_STYLE);
            delete.addClickListener(event -> fireEvent(new CreditGridLayout.DeleteEvent(this, credit)));
            return delete;
        });

        List<GridSortOrder<Credit>> bankSort = new GridSortOrderBuilder<Credit>()
                .thenDesc(bank_uuid).build();
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
}
