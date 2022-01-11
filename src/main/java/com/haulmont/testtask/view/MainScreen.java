package com.haulmont.testtask.view;

import com.haulmont.testtask.Setting;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private final DeleteForm deleteForm;
    private final ClientEditorForm clientEditorForm;
    private final CreditEditorForm creditEditorForm;

    private final Dialog createClientFormDialog = new Dialog();
    private final Dialog createCreditFormDialog = new Dialog();
    private final Dialog createBankFormDialog = new Dialog();
    private final Dialog createCreditOfferFormDialog = new Dialog();
    private final Dialog creditGridLayoutDialog = new Dialog();
    private final Dialog bankGridLayoutDialog = new Dialog();
    private final Dialog paymentGridDialog = new Dialog();
    private final Dialog deleteFormDialog = new Dialog();
    private final Dialog clientEditorFormDialog = new Dialog();
    private final Dialog creditEditorFormDialog = new Dialog();

    private final Button createNewClient = new Button(Setting.CREATE_NEW_BUTTON_TEXT);
    private final Button createNewBank = new Button(Setting.CREATE_NEW_BANK_BUTTON_TEXT);
    private final Button createNewCredit = new Button(Setting.CREATE_NEW_CREDIT_BUTTON_TEXT);
    private final Button showCreditList = new Button(Setting.SHOW_CREDIT_LIST_BUTTON_TEXT);
    private final Button showBankList = new Button(Setting.SHOW_BANK_LIST_BUTTON_TEXT);

    private final RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
    private final TextField searchField = new TextField();
    private final Button searchButton = new Button();


    private final Button next = new Button(Setting.NEXT_BUTTON_TEXT);
    private final Button update = new Button(Setting.UPDATE_BUTTON_TEXT);
    private final Button previous = new Button(Setting.PREVIOUS_BUTTON_TEXT);

    public MainScreen(CreateBankForm createBankForm,
                      CreateClientForm createClientForm,
                      CreateCreditOfferForm createCreditOfferForm,
                      CreateCreditForm createCreditForm,
                      ClientGridLayout clientGridLayout,
                      BankGridLayout bankGridLayout,
                      CreditGridLayout creditGridLayout,
                      PaymentGridLayout paymentGridLayout,
                      DeleteForm deleteForm,
                      ClientEditorForm clientEditorForm,
                      CreditEditorForm creditEditorForm) {
        this.creditEditorForm = creditEditorForm;
        this.deleteForm = deleteForm;
        this.clientEditorForm = clientEditorForm;
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
        clientGridLayout.setHeight(Setting.CLIENT_GRID_LAYOUT_SIZE);

        add(new H3(Setting.WEB_SERVER_NAME), horizontalLayout, tuneClientBar(), this.clientGridLayout, tuneBotButtons());
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
        radioButtonGroup.setItems(Setting.CLIENT_LAST_NAME_FOR_SORTING,
                Setting.CLIENT_FIRST_NAME_FOR_SORTING,
                Setting.CLIENT_PATRONYMIC_FOR_SORTING,
                Setting.CLIENT_PHONE_NUMBER_FOR_SORTING,
                Setting.CLIENT_EMAIL_FOR_SORTING,
                Setting.CLIENT_PASSPORT_FOR_SORTING);

        radioButtonGroup.addValueChangeListener(event -> clientGridLayout.sort(event.getValue()));

        searchField.setPlaceholder(Setting.SEARCH_FIELD_PLACEHOLDER);
        searchButton.setIcon(new Icon(VaadinIcon.SEARCH));

        searchField.setAutoselect(Setting.IS_SEARCH_FIELD_AUTOSELECTED);
        searchField.setAutofocus(Setting.IS_SEARCH_FIELD_AUTOFOCUSED);
        searchField.addKeyDownListener(Key.ENTER, event -> searchButton.click());
        searchButton.addClickShortcut(Key.ENTER);
        searchButton.addClickListener(event -> clientGridLayout.search(searchField.getValue()));
        return new HorizontalLayout(radioButtonGroup, searchField, searchButton);
    }

    private void tuneDialog() {
        createClientFormDialog.add(createClientForm);
        createClientFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        createClientFormDialog.setCloseOnOutsideClick(Setting.IS_CREATE_CLIENT_FORM_DIALOG_CLOSE_ON_OUTSIDE_CLICK);

        createBankFormDialog.add(createBankForm);
        createBankFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        createBankFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);

        createCreditFormDialog.add(createCreditForm);
        createCreditFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        createCreditFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);

        bankGridLayoutDialog.add(bankGridLayout);
        bankGridLayoutDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        bankGridLayoutDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);
        bankGridLayoutDialog.setWidthFull();

        creditGridLayoutDialog.add(creditGridLayout);
        creditGridLayoutDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        creditGridLayoutDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);
        creditGridLayoutDialog.setWidthFull();

        createCreditOfferFormDialog.add(createCreditOfferForm);
        createCreditOfferFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        createCreditOfferFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);


        paymentGridDialog.add(paymentGridLayout);
        paymentGridDialog.setWidthFull();
        paymentGridDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        paymentGridDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);

        deleteFormDialog.add(deleteForm);
        deleteFormDialog.setWidth(Setting.DELETE_FORM_DIALOG_WIDTH);
        deleteFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        deleteFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);

        clientEditorFormDialog.add(clientEditorForm);
        clientEditorFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        clientEditorFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);

        creditEditorFormDialog.add(creditEditorForm);
        creditEditorFormDialog.setCloseOnEsc(Setting.IS_DIALOG_CLOSE_ON_ESC_BY_DEFAULT);
        creditEditorFormDialog.setCloseOnOutsideClick(Setting.IS_DIALOG_CLOSE_ON_OUTSIDE_CLICK_BY_DEFAULT);
    }

    private void setupEventListener() {
        clientGridLayout.addEventListener(ClientGridLayout.EditEvent.class, this::openClientEditorFormDialog);
        clientGridLayout.addEventListener(ClientGridLayout.DeleteEvent.class, this::openClientDeleteDialog);
        clientGridLayout.addEventListener(CreditOfferGridLayout.CreateEvent.class, this::openCreateCreditOfferDialog);

        bankGridLayout.addEventListener(BankGridLayout.CloseEvent.class, this::closeBankGridLayoutDialog);
        bankGridLayout.addEventListener(BankGridLayout.DeleteEvent.class, this::openBankDeleteDialog);

        creditGridLayout.addEventListener(CreditGridLayout.EditEvent.class, this::openCreditEditFormDialog);
        creditGridLayout.addEventListener(CreditGridLayout.CloseEvent.class, this::closeCreditGridLayoutDialog);
        creditGridLayout.addEventListener(CreditGridLayout.DeleteEvent.class, this::openCreditDeleteDialog);

        creditOfferGridLayout.addEventListener(CreditOfferGridLayout.DeleteEvent.class, this::openCreditOfferDeleteDialog);
        creditOfferGridLayout.addEventListener(CreditOfferGridLayout.DetailsEvent.class, this::openCreditOfferPaymentDialog);

        createBankForm.addEventListener(CreateBankForm.CloseEvent.class, this::closeCreateBankFormDialog);
        createClientForm.addEventListener(CreateClientForm.CloseEvent.class, this::closeCreateClientFormDialog);
        createCreditForm.addEventListener(CreateCreditForm.CloseEvent.class, this::closeCreateCreditFormDialog);
        createCreditOfferForm.addEventListener(CreateCreditOfferForm.CloseEvent.class, this::closeCreateCreditOfferDialog);

        deleteForm.addEventListener(DeleteForm.CloseEvent.class, this::closeDeleteFormDialog);
        deleteForm.addEventListener(DeleteForm.UpdateEvent.class, this::updateClientGridLayout);

        clientEditorForm.addEventListener(ClientEditorForm.CloseEvent.class, this::closeClientEditorFormDialog);
        creditEditorForm.addEventListener(CreditEditorForm.CloseEvent.class, this::closeCreditEditorFormDialog);
    }

    private void openCreditEditFormDialog(CreditGridLayout.EditEvent event) {
        creditEditorForm.setUpdatedCredit(event.getCredit());
        creditEditorForm.show();
        creditEditorFormDialog.open();
    }

    private void updateClientGridLayout(DeleteForm.UpdateEvent t) {
        clientGridLayout.update();
    }

    private void closeCreditEditorFormDialog(CreditEditorForm.CloseEvent event) {
        creditEditorFormDialog.close();
    }

    private void closeClientEditorFormDialog(ClientEditorForm.CloseEvent event) {
        clientEditorFormDialog.close();
    }

    private void openClientEditorFormDialog(ClientGridLayout.EditEvent event) {
        clientEditorForm.setUpdatingClient(event.getClient());
        clientEditorForm.show();
        clientEditorFormDialog.open();
    }

    private void openBankDeleteDialog(BankGridLayout.DeleteEvent event) {
        deleteForm.setBank(event.getBank());
        deleteForm.show();
        deleteFormDialog.open();
    }

    private void openCreditDeleteDialog(CreditGridLayout.DeleteEvent event) {
        deleteForm.setCredit(event.getCredit());
        deleteForm.show();
        deleteFormDialog.open();
    }

    private void openClientDeleteDialog(ClientGridLayout.DeleteEvent event) {
        deleteForm.setClient(event.getClient());
        deleteForm.show();
        deleteFormDialog.open();
    }

    private void openCreditOfferDeleteDialog(CreditOfferGridLayout.DeleteEvent event) {
        deleteForm.setCreditOffer(event.getCreditOffer());
        deleteForm.show();
        deleteFormDialog.open();
    }

    private void closeDeleteFormDialog(DeleteForm.CloseEvent t) {
        deleteFormDialog.close();
    }


    private void openCreditOfferPaymentDialog(CreditOfferGridLayout.DetailsEvent event) {
        paymentGridLayout.setPayments(event.getList());
        paymentGridLayout.show();
        paymentGridLayout.setVisible(true);
        paymentGridDialog.open();
    }

    private void closeCreditGridLayoutDialog(CreditGridLayout.CloseEvent event) {
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
        createBankForm.show();
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
