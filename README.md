# Super Cool Bank System Test Task

## What is Super Cool Bank System

**Super Cool Bank System** is standard one page dynamic bank app with not trivial delete system.

## Table of Contents

- [What is Super Cool Bank System](#what-is-super-cool-bank-system)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Build and Run](#build-and-run)
    - [Stack of technology](#stack-of-technology)
- [Opportunities](#opportunities)
- [Schema](#schema)
- [Structure of site](#structure-of-site)
- [Formulas](#formulas)
    - [Monthly Payment](#monthly-payment)
    - [Loan interest](#loan-interest)
- [Core Possibilities](#core-possibilities)

## Getting Started

### Prerequisites

* [Java Development Kit (JDK) 11](https://libericajdk.ru/pages/downloads/#/java-11-lts)
* [Maven 3](https://maven.apache.org/download.cgi)
* [NPM](https://www.npmjs.com/)

### Build and Run

1. Run in the command line in source folder:
    ```
    mvn -e -pl app spring-boot:run -am
    ```

2. Open `http://localhost:8080` in a web browser.

### Stack of Technology

1. Spring Boot
2. SpringData
3. Vaadin
4. Flyway
5. [HSQLDB](http://hsqldb.org/doc/2.0/guide/running-chapt.html#rgc_inprocess)
6. Lombok
7. jakarta.validation/hibernate validation

## Opportunities

### Functionality

1. Добавление, редактирование и удаление сущностей. [For more information](#core-possibilities) 
1. Процесс оформления кредита на клиента с созданием графика платежей и расчетом необходимых сумм:
   Автоматический расчет итоговой суммы процентов по кредиту; Автоматический расчет суммы ежемесячного платежа с учетом
   процентной ставки.

### UI

1. Экраны для добавления/редактирования/удаления сущностей;
    * List of all clients with opportunity to add new or edit/remove existent
2. Экран для формирования кредитного предложения и просмотра графика платежей, итоговой суммы по кредиту. Интерфейс
   пользователя должен быть простым, логичным и удобным.

Код доступа к данным должен быть изолирован в классах DAO;

Каждая таблица должна иметь первичный ключ типа UUID;

## Schema

### Client

* Client_id UUID
* Firstname/Lastname/Patronymic
* Phone number
* Email
* Passport
* isRemoved

### Bank

* Bank_id UUID
* Bank name
* Credit list
* Client list

### Кредит

* Credit_id UUID
* Bank_id UUID
* Credit limit
* Credit Rate
* isUnused

### Credit offer

* Credit_offer_id UUID
* Client_id UUID
* credit_id UUID
* Credit amount
* month count
* isCanceled
* list Payment
    * Payment_id UUID
    * Date
    * amount
    * main part
    * percent part

## Structure of site

- Main Screen
    - ClientGrid
    - DeleteForm for client
    - CreditOfferGrid
        - PaymentGrid
    - CreateCreditOfferForm
        - PaymentGrid
    - CreateClientForm
    - CreateBankForm
    - CreateCreditForm
    - BankGrid
        - DeleteForm for bank
    - CreditGrid
        - DeleteForm for credit
        - CreditEditorForm

## Formulas

### Monthly Payment

```MP = P * k^(LD) * (k - 1) / (k^(LD) - 1)```

где `k = (1 + yearRate) ^ (1/12)`

Monthly Payment(MP) - размер ежемесячного платежа;

Loan Duration (LD) - длительность кредита в месяцах;

yearRate - годовая процентная ставка;

P - сумма кредита;

### Loan interest

```
{(1 - yearRate/100)^(period/365[6]) - 1} * R
```

R - остаток по основной части кредита на день платежа;

period - длительность периода в днях от предыдущего платежа до исчесляемого;

## Core Possibilities

#### Creating

* Client by clicking "Create new client" button on main page
* Bank by clicking "Create new bank" button on main page
* Credit by clicking "Create new credit" button on main page
* Credit offer with calculating payment list in credit offer grid in client details. This credit offer automatically
  connect to corresponding client.

#### Reading

* Bank list with connecting client by clicking lmb on "show bank list button" on main page
* Client list sorting by any field and limit by page size on main
* Credit offer connecting to target client clicking on client lmb
* View payment list connecting to target credit offer by clicking details in credit offer grid column
* Bank list in create credit form
* credit list in create credit offer form
* calculating payment grid in create credit offer form

#### Editing

* Client's firstname, lastname, patronymic field in client edit form by clicking edit in client grid on main page
* Credit by creating new credit with target params. Old credit mark with UNUSED. Can be edited in credit grid from main
  page.
* Credit offer are immutable.
* Payment are immutable
* Bank can update own credit and client field.

#### Deleting

* Client by mark with REMOVED. Can delete when have no current credit offers only. Can be deleted from main page in
  client grid by clicking delete.
* Bank with cascade delete credit and credit offers. Bank went bankrupt. Client are indebtedness free. Can be deleted
  from bank grid.
* Credit by mark UNUSED. No options to create new credit offer with this credit. Can be deleted from credit grid.
* Credit offers by mark CANCELED. Connecting payments are deleting from rdb. Can be deleted from credit offer grid
  connecting to client. Couldn't cancel credit offer which already start and don't end.
* Payment can be deleted by canceled connecting credit offer.

REMOVED, UNUSED and CANCELED are hidden from general lists. Some unique fields like email or phone cant be used ever.
