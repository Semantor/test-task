package com.haulmont.testtask.view;

import com.haulmont.testtask.model.entity.Payment;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


public class PaymentGridLayout extends VerticalLayout implements CanBeShown {
    private final Grid<Payment> grid = new Grid<>();
    private final HorizontalLayout layoutWithTotalInfo = new HorizontalLayout();
    private final Div total = new Div();
    private final Div mainPart = new Div();
    private final Div percentPart = new Div();
    @Getter
    @Setter
    private transient List<Payment> payments;

    public PaymentGridLayout() {
        tuneGrid();
        layoutWithTotalInfo.add(total, mainPart, percentPart);
        add(grid, layoutWithTotalInfo);
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

        total.setText("total value: " + payments.stream().reduce(
                BigDecimal.ZERO,
                (bigDecimal, payment) -> payment.getAmount().add(bigDecimal),
                BigDecimal::add
        ) + " $");

        mainPart.setText("base value: " + payments.stream().reduce(
                BigDecimal.ZERO,
                (bigDecimal, payment) -> payment.getMainPart().add(bigDecimal),
                BigDecimal::add
        ) + " $");

        percentPart.setText("percent value: " + payments.stream().reduce(
                BigDecimal.ZERO,
                (bigDecimal, payment) -> payment.getPercentPart().add(bigDecimal),
                BigDecimal::add
        ) + " $");
    }
}
