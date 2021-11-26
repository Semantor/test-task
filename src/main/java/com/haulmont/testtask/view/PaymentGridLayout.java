package com.haulmont.testtask.view;

import com.haulmont.testtask.model.entity.Payment;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


public class PaymentGridLayout extends VerticalLayout implements CanBeShown {
    private final Grid<Payment> grid = new Grid<>();
    private final Div sum = new Div();
    @Getter
    @Setter
    private List<Payment> payments;

    public PaymentGridLayout() {
        tuneGrid();

        add(grid, sum);
    }

    private void tuneGrid() {
        grid.addColumn(Payment::getDate).setHeader("date");
        grid.addColumn(Payment::getAmount).setHeader("amount");
        grid.addColumn(Payment::getMainPart).setHeader("main part");
        grid.addColumn(Payment::getPercentPart).setHeader("percent part");
    }

    /**
     * MUST BE USE {@link #setPayments(List)} FIRST
     */
    @Override
    public void show() {
        grid.setItems(payments);
        sum.setText("total value: " + payments.stream().reduce(
                BigDecimal.ZERO,
                (b, p) -> p.getAmount().add(b),
                BigDecimal::add
        ) + " $");
    }
}
