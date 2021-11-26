package com.haulmont.testtask.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@PageTitle("Client list")
@Route(value = "")
@PropertySource("application.yml")
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainScreen extends VerticalLayout {

    private final CreateClientForm createClientForm;
    private final CreateCreditOfferForm createCreditOfferForm;
    private final CreateCreditForm createCreditForm;
    private final CreditOfferGridLayout creditOfferGridLayout;
    private final ClientGridLayout clientGridLayout;
    private final CreditGridLayout creditGridLayout;
    private final BankGridLayout bankGridLayout;
    private final CreateBankForm createBankForm;
    private final PaymentGridLayout paymentGridLayout;

    private final Notification notification = new Notification();

    private final Dialog createClientFormDialog = new Dialog();
    private final Dialog createCreditFormDialog = new Dialog();
    private final Dialog createBankFormDialog = new Dialog();
    private final Dialog createCreditOfferFormDialog = new Dialog();
    private final Dialog creditGridLayoutDialog = new Dialog();
    private final Dialog bankGridLayoutDialog = new Dialog();
    private final Dialog paymentGridDialog = new Dialog();

    private final Button createNewClient = new Button("create new client");
    private final Button createNewBank = new Button("create new bank");
    private final Button createNewCredit = new Button("create new credit");
    private final Button showCreditList = new Button("show credit list");
    private final Button showBankList = new Button("show bank list");

    private final RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button();


    private final Button next = new Button("next");
    private final Button update = new Button("update");
    private final Button previous = new Button("previous");

    public MainScreen(CreateBankForm createBankForm,
                      CreateClientForm createClientForm,
                      CreateCreditOfferForm createCreditOfferForm,
                      CreateCreditForm createCreditForm,
                      ClientGridLayout clientGridLayout,
                      BankGridLayout bankGridLayout,
                      CreditGridLayout creditGridLayout,
                      PaymentGridLayout paymentGridLayout) {
        this.createBankForm = createBankForm;
        this.creditGridLayout = creditGridLayout;
        this.createClientForm = createClientForm;
        this.createCreditOfferForm = createCreditOfferForm;
        this.createCreditForm = createCreditForm;
        this.creditOfferGridLayout = clientGridLayout.getCreditOfferGridLayout();
        this.clientGridLayout = clientGridLayout;
        this.bankGridLayout = bankGridLayout;
        this.paymentGridLayout = paymentGridLayout;

        HorizontalLayout horizontalLayout = new HorizontalLayout(tuneTopLeftButtons(), tuneTopRightButtons());
        tuneDialog();
        setupEventListener();
        clientGridLayout.setHeight(Constant.CLIENT_GRID_LAYOUT_SIZE);

        add(new H3("SUPER COOL BANK SYSTEM™"), horizontalLayout, tuneClientBar(), this.clientGridLayout, tuneBotButtons());
    }


    private HorizontalLayout tuneTopLeftButtons() {
        createNewClient.addClickListener(event -> openCreateClientFormDialog());
        createNewBank.addClickListener(event -> openCreateBankFormDialog());
        createNewCredit.addClickListener(event -> openCreateCreditFormDialog());

        HorizontalLayout horizontalLayout = new HorizontalLayout(createNewClient, createNewBank, createNewCredit);
        horizontalLayout.setAlignItems(Alignment.START);
        return horizontalLayout;
    }

    private HorizontalLayout tuneTopRightButtons() {
        showBankList.addClickListener(event -> {
            bankGridLayout.show();
            bankGridLayoutDialog.open();
        });

        showCreditList.addClickListener(event -> {
            creditGridLayout.show();
            creditGridLayoutDialog.open();
        });

        return new HorizontalLayout(showBankList, showCreditList);
    }

    private HorizontalLayout tuneBotButtons() {
        previous.addClickListener(event -> clientGridLayout.previousPage());
        next.addClickListener(event -> clientGridLayout.nextPage());
        update.addClickListener(event -> clientGridLayout.update());

        previous.addClickShortcut(Key.ARROW_LEFT);
        next.addClickShortcut(Key.ARROW_RIGHT);
        return new HorizontalLayout(previous, update, next);
    }

    private HorizontalLayout tuneClientBar() {
        radioButtonGroup.setItems("lastName", "firstName", "patronymic", "phoneNumber", "email", "passport");
        radioButtonGroup.addValueChangeListener(event -> clientGridLayout.sort(event.getValue()));

        searchField.setPlaceholder("search");
        searchButton.setIcon(new Icon(VaadinIcon.SEARCH));

        searchField.setAutoselect(true);
        searchField.setAutofocus(true);
        searchField.addKeyDownListener(Key.ENTER, event -> searchButton.click());
        searchButton.addClickShortcut(Key.ENTER);
        searchButton.addClickListener(event -> {
            notification.setText("Search is not implemented yet");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.setDuration(2000);
            notification.open();
        });
        return new HorizontalLayout(radioButtonGroup, searchField, searchButton);
    }

    private void tuneDialog() {
        createClientFormDialog.add(createClientForm);
        createClientFormDialog.setCloseOnEsc(true);
        createClientFormDialog.setCloseOnOutsideClick(false);

        createBankFormDialog.add(createBankForm);
        createBankFormDialog.setCloseOnEsc(true);
        createBankFormDialog.setCloseOnOutsideClick(true);

        createCreditFormDialog.add(createCreditForm);
        createCreditFormDialog.setCloseOnEsc(true);
        createCreditFormDialog.setCloseOnOutsideClick(false);

        bankGridLayoutDialog.add(bankGridLayout);
        bankGridLayoutDialog.setCloseOnEsc(true);
        bankGridLayoutDialog.setCloseOnOutsideClick(true);
        bankGridLayoutDialog.setWidthFull();

        creditGridLayoutDialog.add(creditGridLayout);
        creditGridLayoutDialog.setCloseOnEsc(true);
        creditGridLayoutDialog.setCloseOnOutsideClick(true);
        creditGridLayoutDialog.setWidthFull();

        createCreditOfferFormDialog.add(createCreditOfferForm);
        createCreditOfferFormDialog.setCloseOnEsc(true);
        createCreditOfferFormDialog.setCloseOnOutsideClick(true);


        paymentGridDialog.add(paymentGridLayout);
        paymentGridDialog.setWidthFull();
        paymentGridDialog.setCloseOnEsc(true);
        paymentGridDialog.setCloseOnOutsideClick(true);
    }

    private void setupEventListener() {
        creditOfferGridLayout.addEventListener(CreditOfferGridLayout.CreateEvent.class, this::openCreateCreditOfferDialog);
        createBankForm.addEventListener(CreateBankForm.CloseEvent.class, this::closeCreateBankFormDialog);
        createClientForm.addEventListener(CreateClientForm.CloseEvent.class, this::closeCreateClientFormDialog);
        createCreditForm.addEventListener(CreateCreditForm.CloseEvent.class, this::closeCreateCreditFormDialog);
        createCreditOfferForm.addEventListener(CreateCreditOfferForm.CloseEvent.class, this::closeCreateCreditOfferDialog);
        bankGridLayout.addEventListener(BankGridLayout.CloseEvent.class, this::closeBankGridLayoutDialog);
        creditGridLayout.addEventListener(CreditGridLayout.CloseEvent.class, this::closeCreditGridLayout);
        creditOfferGridLayout.addEventListener(CreditOfferGridLayout.DetailsEvent.class, this::openCreditOfferPaymentDialog);
    }


    private void openCreditOfferPaymentDialog(CreditOfferGridLayout.DetailsEvent event) {
        paymentGridLayout.setPayments(event.getList());
        paymentGridLayout.show();
        paymentGridLayout.setVisible(true);
        paymentGridDialog.open();
    }

    private void closeCreditGridLayout(CreditGridLayout.CloseEvent event) {
        creditGridLayoutDialog.close();
    }

    private void closeBankGridLayoutDialog(BankGridLayout.CloseEvent event) {
        bankGridLayoutDialog.close();
    }

    private void openCreateClientFormDialog() {
        createClientFormDialog.open();
        createClientForm.show();
    }

    private void closeCreateClientFormDialog(CreateClientForm.CloseEvent event) {
        createClientFormDialog.close();
    }

    private void openCreateCreditFormDialog() {
        createCreditForm.show();
        createCreditFormDialog.open();
    }

    private void closeCreateCreditFormDialog(CreateCreditForm.CloseEvent event) {
        createCreditFormDialog.close();
    }

    private void openCreateBankFormDialog() {
        createBankFormDialog.open();
    }

    private void closeCreateBankFormDialog(CreateBankForm.CloseEvent event) {
        createBankFormDialog.close();
    }

    private void openCreateCreditOfferDialog(CreditOfferGridLayout.CreateEvent event) {
        createCreditOfferForm.setClient(event.getClient());
        createCreditOfferForm.show();
        createCreditOfferFormDialog.open();
    }

    private void closeCreateCreditOfferDialog(CreateCreditOfferForm.CloseEvent event) {
        createCreditOfferFormDialog.close();
    }


}