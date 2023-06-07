# PFM SDK Backend Android

![](https://img.shields.io/badge/minSDK-24+-blue.svg) ![Language](https://img.shields.io/badge/Language-Java-orange.svg) ![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)

This repository contains a subset of the Open Banking PFM Android SDK source. It currently includes the following Backend libraries.

| Table of contents                                   |
|:--------------------------------------------------- |
| [Installation](#installation)                       |
| [Users Client](#users-client)                       |
| [Banks Client](#bank-aggregation-module)            |
| [Categories Client](#categories-client)             |
| [Accounts Client](#accounts-client)                 |
| [Transactions Client](#transactions-client)         |
| [Budgets Client](#budgets-client)                   |
| [Bank Aggregation Client](#bank-aggregation-client) |
| [Consents Client](#consents-client)                 |
| [Credits Client](#credits-client)                   |
| [Insights Client](#insights-client)                 |
| [Error Codes](#error-codes)                         |

## Installation

#### Using Gradle

Add next configuration to project `build.gradle` for supporting kotlin language:

```gradle
buildscript {
    ext.kotlin_version = "1.5.30"
    ...
    dependencies {
         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
         ...
    }
}

allprojects {
    repositories {
         ...
         maven { url 'https://jitpack.io' }
    }
}
```

#### Using Module Gradle

Add next configuration to module `build.gradle`:

```build.gradle
dependencies {   
    implementation 'com.openbanking.pfm.sdk:open-banking-pfm-sdk:1.0.0'
}  
```

#### Using Manifest

Add next permission into manifest.xml:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

#### build gradle

Edit your app `build.gradle` and specify the dependencies:

```gradle
implementation 'to-update'
```

### Pre-requirements

Add `OpenBankingCore` initialization code to your application. Please check the `OpenBankingCore` [README]([https://)) file before continue to get the entire configuration.

### Configure OpenBankingPFMSDK

Java:

```java
OpenBankingPFMAPI.Companion.getShared().init();
```

Kotlin:

```kotlin
OpenBankingPFMAPI.shared.init()
```

## Users client

#### Get user

This client contains the available actions related to users.

Java:

```java
final Integer userId = 123456;

new OpenBankingPFMAPI().usersClient().get(
    userId,
    new GetUserListener() {
        @Override
        public void success(OBUser user) {
            // Use the server reponse 
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
           Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
val userId = 123456

OpenBankingPFMAPI.shared.usersClient().get(
    userId,
    object : GetUserListener {
        override fun success(user: OBUser) {
            // Use server response
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBUser(
    @Json(name = "id") val id : Int,
    @Json(name = "cpf") val cpf : String,
    @Json(name = "dateCreated") val dateCreated : Long,
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


## Bank Client

#### Get a list of available banks

This client contains the available actions related to banks..

Java:

```java
    final Integer userId = 123456;

    new OpenBankingPFMAPI().banksClient().getAvailables(
        userId,
        new GetAvailableBanksListener() {

        @Override
        public void success(@NonNull OBBanksAvailableResponse response) {
            // Use the server reponse 
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
           Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val userId = 123456
    OpenBankingPFMAPI.shared.banksClient().getAvailables(userId, object: GetAvailableBanksListener {
        override fun success(response: OBBanksAvailableResponse) {
            // Use the server reponse 
        }

        override fun error(errors: List<OBError>) {
            // Catch error list in case that receive error
        }

        override fun severError(serverError: Throwable) {
            // Show server error message
        }
    })
```

#### Output

```kotlin
data class OBBanksAvailableResponse(
    @Json(name = "banks" ) val banks : List<OBBank>
)

data class OBBank(
    @Json(name = "bankId") val bankId: String,
    @Json(name = "financialEntityId") val financialEntityId: Int?,
    @Json(name = "name") val name: String?,
    @Json(name = "imagePath") val imagePath: String?,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.


#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


## Categories Client

#### Get Categories list

Fetches a list of categories, sorted by ID in descending order. The API is able to fetch up to 100 categories. If a cursor is specified, the list starts with the item that has that ID. If a user ID is specified, both system and user categories are fetched. If a user ID is not specified, only system categories are fetched.

Java:

```java
    final Integer userId = 123456;
    final Integer cursor = null;

    new OpenBankingPFMAPI().categoriesClient().getList(userId, cursor, new GetCategoriesListener() {
        @Override
        public void success(OBCategoriesResponse response) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val userId = 123456
    val cursor = null

    OpenBankingPFMAPI.shared.categoriesClient().getList(
        userId,
        cursor,
        object : GetCategoriesListener {
            override fun success(response: OBCategoriesResponse) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBCategoriesResponse(
    @Json(name = "data") var data: List<OBCategory>?,
    @Json(name = "nextCursor") var nextCursor: Int?
)

data class OBCategory(
    @Json(name = "id") val id : Long,
    @Json(name = "name") val name : String,
    @Json(name = "imagePath") val imagePath : String?,
    @Json(name = "color") val color : String?,
    @Json(name = "parentCategoryId") val parentCategoryId : Long?,
    @Json(name = "userId") val userId : Int?,
    @Json(name = "dateCreated") val dateCreated : Long?,
    @Json(name = "lastUpdated") val lastUpdated : Long?,
    @Json(name = "isUserCategory") val isUserCategory : Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Create Category

Creates a category. If a user ID is not specified, the category is considered as a system category. If a parent category ID is specified, the category is considered a subcategory.

Java:

```java
    final Integer userId = 123456;
    final String name = "Category name";
    final String color = "FFFFFF";
    final Long parentId = 1L;

    final OBCreateCategoryRequest request = new OBCreateCategoryRequest(
            userId,
            name,
            color,
            parentId
    );

    new OpenBankingPFMAPI().categoriesClient().create(request, new CreateCategoryListener() {
        @Override
        public void success(OBCategory category) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val request = OBCreateCategoryRequest(
        userId = 123456,
        name = "Category name",
        color = "FFFFFF",
        parentCategoryId = 1L
    )

    OpenBankingPFMAPI.shared.categoriesClient().create(
        request,
        object : CreateCategoryListener {
            override fun success(category: OBCategory) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBCategory(
    @Json(name = "id") val id : Long,
    @Json(name = "name") val name : String,
    @Json(name = "imagePath") val imagePath : String?,
    @Json(name = "color") val color : String?,
    @Json(name = "parentCategoryId") val parentCategoryId : Long?,
    @Json(name = "userId") val userId : Int?,
    @Json(name = "dateCreated") val dateCreated : Long?,
    @Json(name = "lastUpdated") val lastUpdated : Long?,
    @Json(name = "isUserCategory") val isUserCategory : Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Fetch Category

Given a valid category ID, fetches the information of a category.

Java:

```java
    final Long categoryId = 123456;

    new OpenBankingPFMAPI().categoriesClient().get(categoryId, new GetCategoryListener() {
        @Override
        public void success(OBCategory category) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val categoryId = 123456L

    OpenBankingPFMAPI.shared.categoriesClient().get(
        categoryId,
        object : GetCategoryListener {
            override fun success(category: OBCategory) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBCategory(
    @Json(name = "id") val id : Long,
    @Json(name = "name") val name : String,
    @Json(name = "imagePath") val imagePath : String?,
    @Json(name = "color") val color : String?,
    @Json(name = "parentCategoryId") val parentCategoryId : Long?,
    @Json(name = "userId") val userId : Int?,
    @Json(name = "isUserCategory") val isUserCategory : Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Update Category

Updates a category.

Java:

```java
    final String name = "Category name";
    final String color = "FFFFFF";
    final Integer parentId = 1;
    final Long categoryId = 123456L

    final OBUpdateCategoryRequest request = new OBUpdateCategoryRequest(
            name,
            color,
            parentId
    );

    new OpenBankingPFMAPI().categoriesClient().edit(categoryId, request, new UpdateCategoryListener() {
        @Override
        public void success(OBCategory category) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val categoryId = 123456L
    val requestUpdate = OBUpdateCategoryRequest(
        name = "Category name",
        color = "FFFFFF",
        parentCategoryId = 1
    )

    OpenBankingPFMAPI.shared.categoriesClient().edit(
        categoryId,
        requestUpdate,
        object : UpdateCategoryListener {
            override fun success(category: OBCategory) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBCategory(
    @Json(name = "id") val id : Long,
    @Json(name = "name") val name : String,
    @Json(name = "imagePath") val imagePath : String?,
    @Json(name = "color") val color : String?,
    @Json(name = "parentCategoryId") val parentCategoryId : Long?,
    @Json(name = "userId") val userId : Int?,
    @Json(name = "dateCreated") val dateCreated : Long?,
    @Json(name = "lastUpdated") val lastUpdated : Long?,
    @Json(name = "isUserCategory") val isUserCategory : Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Delete Category

Deletes a category and all its information. All transactions and budgets related remain active.

Java:

```java
    final Long categoryId = 123456

    new OpenBankingPFMAPI().categoriesClient().delete(categoryId, new DeleteCategoryListener() {
        @Override
        public void success() {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val categoryId = 123456L

    OpenBankingPFMAPI.shared.categoriesClient().delete(
        categoryId,
        object : DeleteCategoryListener {
            override fun success() {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

The listener triggered the success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



## Accounts Client

#### Get Accounts list

Fetches a list of accounts per user, sorted by ID in descending order. The API is able to fetch up to 100 accounts. If a cursor is specified, the list starts with the item that has that ID.

Java:

```java
    final Integer userId = 123456;
    final Boolean isBankAggregation = true;
    final Integer cursor = 0;

    new OpenBankingPFMAPI().accountsClient().getList(
            userId,
            isBankAggregation,
            cursor,
            new GetAccountsListener() {
            @Override
            public void success(@NonNull OBAccountsResponse response) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin:

```kotlin
val userId = 123456
val isBankAgreggation = true
val cursor = 0

OpenBankingPFMAPI.shared.accountsClient().getList(
    userId,
    isBankAgreggation,
    cursor,
    object: GetAccountsListener {
        override fun success(response: OBAccountsResponse) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBAccountsResponse(
    @Json(name = "data") var data: List<OBAccount>?,
    @Json(name = "nextCursor") var nextCursor: Int?
)

data class OBAccount(
    @Json(name = "id") var id: Int?,
    @Json(name = "providerId") var providerId: String?,
    @Json(name = "financialEntityId") var financialEntityId: Int?,
    @Json(name = "nature") var nature: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "number") var number: String?,
    @Json(name = "balance") var balance: Double?,
    @Json(name = "chargeable") var chargeable: Boolean?,
    @Json(name = "dateCreated") var dateCreated: Long?,
    @Json(name = "lastUpdated") var lastUpdated: Long?,
    @Json(name = "isBankAggregation") var isBankAggregation: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Create an Account

Create an account. A previously created user and a Financial Entity is required.

Java:

```java
    final Integer userId = 123456;
    final Integer financialEntityId = 123456;
    final String nature = "Debit";
    final String name = "Premium Account";
    final String number = "XXXX-1234";
    final Double balance = 1000.00;
    final Boolean chargeable = true;

    final OBCreateAccountRequest accountRequest = new OBCreateAccountRequest(
            userId,
            financialEntityId,
            nature,
            name,
            number,
            balance,
            chargeable
    );

    new OpenBankingPFMAPI().accountsClient().create(
        accountRequest, 
        new CreateAccountListener() {
        @Override
        public void success(OBAccount account) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
val accountRequest = OBCreateAccountRequest(
    userId = 123456,
    financialEntityId = 123456,
    nature = "Debit",
    name = "Premium account",
    number = "XXXX-1234",
    balance = 1000.00,
    chargeable = true
)

OpenBankingPFMAPI.shared.accountsClient().create(
    accountRequest,
    object: CreateAccountListener {
        override fun success(account: OBAccount) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBAccount(
    @Json(name = "id") var id: Int?,
    @Json(name = "providerId") var providerId: String?,
    @Json(name = "financialEntityId") var financialEntityId: Int?,
    @Json(name = "nature") var nature: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "number") var number: String?,
    @Json(name = "balance") var balance: Double?,
    @Json(name = "chargeable") var chargeable: Boolean?,
    @Json(name = "dateCreated") var dateCreated: Long?,
    @Json(name = "lastUpdated") var lastUpdated: Long?,
    @Json(name = "isBankAggregation") var isBankAggregation: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Fetch Account

Given a valid account ID, fetches the information of an account.

Java:

```java
    final Integer accountId = 123456;

    new OpenBankingPFMAPI().accountsClient().get(accountId, new GetAccountListener() {
            @Override
            public void success(OBAccount account) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin:

```kotlin
val accountId = 123456

OpenBankingPFMAPI.shared.accountsClient().get(
    accountId,
    object: GetAccountListener {
        override fun success(account: OBAccount) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBAccount(
    @Json(name = "id") var id: Int?,
    @Json(name = "providerId") var providerId: String?,
    @Json(name = "financialEntityId") var financialEntityId: Int?,
    @Json(name = "nature") var nature: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "number") var number: String?,
    @Json(name = "balance") var balance: Double?,
    @Json(name = "chargeable") var chargeable: Boolean?,
    @Json(name = "dateCreated") var dateCreated: Long?,
    @Json(name = "lastUpdated") var lastUpdated: Long?,
    @Json(name = "isBankAggregation") var isBankAggregation: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Update Account

Updates an account.

Java:

```java
    final Integer accountId = 123456;
    final String nature = "Debit";
    final String name = "Premium Account";
    final String number = "XXXX-1234";
    final Double balance = 1000.00;
    final Boolean chargeable = true;

    final OBUpdateAccountRequest accountRequest = new OBUpdateAccountRequest(
        nature,
        name,
        number,
        balance,
        chargeable
    );

    new OpenBankingPFMAPI().accountsClient().update(accountId, accountRequest, new UpdateAccountListener() {
        @Override
        public void success(OBAccount fcAccount) {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
val accountId = 123456
val accountUpdateRequest = OBUpdateAccountRequest(
    nature = "Debit",
    name = "Premium account",
    number = "XXXX-1234",
    balance = 1000.00,
    chargeable = true
)

OpenBankingPFMAPI.shared.accountsClient().update(
    accountId,
    accountUpdateRequest,
    object: UpdateAccountListener {
        override fun success(account: OBAccount) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBAccount(
    @Json(name = "id") var id: Int?,
    @Json(name = "providerId") var providerId: String?,
    @Json(name = "financialEntityId") var financialEntityId: Int?,
    @Json(name = "nature") var nature: String?,
    @Json(name = "name") var name: String?,
    @Json(name = "number") var number: String?,
    @Json(name = "balance") var balance: Double?,
    @Json(name = "chargeable") var chargeable: Boolean?,
    @Json(name = "dateCreated") var dateCreated: Long?,
    @Json(name = "lastUpdated") var lastUpdated: Long?,
    @Json(name = "isBankAggregation") var isBankAggregation: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Delete Account

Deletes an account and all its information, including transactions.

Java:

```java
    final Integer accountId = 123456;

    new OpenBankingPFMAPI().accountsClient().delete(id, new DeleteAccountListener() {
        @Override
        public void success() {
            // Use the server reponse
        }

        @Override
        public void error(List<OBError> errors) {
            if(!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
val accountId = 123456

OpenBankingPFMAPI.shared.accountsClient().delete(
    accountId,
    object: DeleteAccountListener {
        override fun success() {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

The listener triggered a success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



## Transactions Client

#### Get Transactions list

Fetches a list of transactions per account, sorted by ID in descending order. The API is able to fetch up to 100 transactions. If a cursor is specified, the list starts with the item that has that ID.

Java:

```java
    final Integer accountId = 123456;

    new OpenBankingPFMAPI().transactionsClient().getList(accountId, new HashMap<>(), new GetTransactionsListener() {
        @Override
        public void success(@NonNull OBTransactionsResponse response) {
            // Use the server response
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val accountId = 123456

    OpenBankingPFMAPI.shared.transactionsClient().getList(
        accountId,
        HashMap(),
        object : GetTransactionsListener {
            override fun success(response: OBTransactionsResponse) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBTransactionsResponse(
    @Json(name = "data") var data: List<OBTransaction>?,
    @Json(name = "nextCursor") var nextCursor: Int?
)

data class OBTransaction(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: Long,
    @Json(name = "charge") val charge: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "dateCreated") val dateCreated: Long,
    @Json(name = "lastUpdated") val lastUpdated: Long,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Create Transactions

Creates a transaction. A previously created account is required.

Java:

```java
    final Integer accountId = 123456;
    final Long date = 1685205848L;
    final Boolean charge = false;
    final String description = "Description";
    final Double amount = 1000.00;
    final Integer categoryId = 1;

    final OBCreateTransactionRequest request = new OBCreateTransactionRequest(
            accountId,
            date,
            charge,
            description,
            amount,
            categoryId
    );

    new OpenBankingPFMAPI().transactionsClient().create(request, new CreateTransactionListener() {
        @Override
        public void success(OBTransaction transaction) {
            // Use the server response
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val transactionRequest = OBCreateTransactionRequest(
        accountId = 123456,
        date = 1685205848L,
        charge = false,
        description = "Description",
        amount = 1000.00,
        categoryId = 1
    )

    OpenBankingPFMAPI.shared.transactionsClient().create(
        transactionRequest,
        object : CreateTransactionListener {
            override fun success(transaction: OBTransaction) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBTransaction(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: Long,
    @Json(name = "charge") val charge: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "dateCreated") val dateCreated: Long,
    @Json(name = "lastUpdated") val lastUpdated: Long,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Fetch Transactions

Given a valid transaction ID, fetches the information of a transaction.

Java:

```java
    final Integer transactionId = 123456;

    new OpenBankingPFMAPI().transactionsClient().get(transactionId, new GetTransactionListener() {
        @Override
        public void success(OBTransaction transaction) {
            // Use the server response
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val transactionId = 123456

    OpenBankingPFMAPI.shared.transactionsClient().get(
        transactionId,
        object : GetTransactionListener {
            override fun success(transaction: OBTransaction) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBTransaction(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: Long,
    @Json(name = "charge") val charge: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "dateCreated") val dateCreated: Long,
    @Json(name = "lastUpdated") val lastUpdated: Long,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Update Transactions

Updates a transaction.

Java:

```java
    final Long date = 1685205848L;
    final Boolean charge = false;
    final String description = "Description";
    final Double amount = 1000.00;
    final Integer categoryId = 1;
    final Integer transactionId = 123456;

    final OBUpdateTransactionRequest request = new OBUpdateTransactionRequest(
            date,
            charge,
            description,
            amount,
            categoryId
    );

    new OpenBankingPFMAPI().transactionsClient().edit(transactionId, request, new UpdateTransactionListener() {
        @Override
        public void success(OBTransaction transaction) {
            // Use the server response
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val transactionUpdateRequest = OBUpdateTransactionRequest(
        date = 1685205848L,
        charge = false,
        description = "Description",
        amount = 1000.00,
        categoryId = 1
    )
    val transactionId = 123456

    OpenBankingPFMAPI.shared.transactionsClient().edit(
        transactionId,
        transactionUpdateRequest,
        object : UpdateTransactionListener {
            override fun success(transaction: OBTransaction) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBTransaction(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: Long,
    @Json(name = "charge") val charge: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "dateCreated") val dateCreated: Long,
    @Json(name = "lastUpdated") val lastUpdated: Long,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Delete Transactions

Deletes a transaction and all its information.

Java:

```java
    final Integer transactionId = 123456;

    new OpenBankingPFMAPI().transactionsClient().delete(transactionId, new DeleteTransactionListener() {
        @Override
        public void success() {
            // Use the server response
        }

        @Override
        public void error(List<OBError> errors) {
            if (!errors.isEmpty()) {
                final OBError error = errors.get(0);
                Log.e("ERROR", error.getDetail());
            }
        }

        @Override
        public void severError(Throwable serverError) {
            Log.e("SERVER ERROR", serverError.getMessage());
        }
    });
```

Kotlin:

```kotlin
    val transactionId = 123456

    OpenBankingPFMAPI.shared.transactionsClient().delete(
        transactionId,
        object : DeleteTransactionListener {
            override fun success() {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

The listener triggered a success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



## Budgets Client

#### Get Budgets list

Fetches a list of budgets per user, sorted by ID in descending order. The API is able to fetch up to 100 budgets. If a cursor is specified, the list starts with the item that has that ID.

Java:

```java
    final Integer userId = 123456;
    final Integer cursor = null;

    new OpenBankingPFMAPI().budgetsClient().getList(
        userId,
        cursor,
        new GetBudgetsListener() {

            @Override
            public void success(OBBudgetsResponse response) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

kotlin:

```kotlin
    OpenBankingPFMAPI.shared.budgetsClient().getList(
    userId,
    cursor,
    object: GetBudgetsListener {
        override fun success(response: OBBudgetsResponse) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBBudgetsResponse(
    @Json(name = "data") val budgets : List<OBBudget>,
    @Json(name = "nextCursor") val nextCursor : Int?
)

data class OBBudget(
    @Json(name = "id") val id : Int,
    @Json(name = "categoryId") val categoryId : Long,
    @Json(name = "name") val name : String,
    @Json(name = "amount") val amount : Double,
    @Json(name = "warningPercentage") val warningPercentage : Double,
    @Json(name = "spent") val spent : Double,
    @Json(name = "leftToSpend") val leftToSpend : Double,
    @Json(name = "status") val status : String,
    @Json(name = "dateCreated") val dateCreated : Long,
    @Json(name = "lastUpdated") val lastUpdated : Long
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Create Budget

Creates a budget. A previously created user and a category is required. There can be only one budget per category.

Java:

```java
    final Integer budgetId = 123456;
    final Integer categoryId = 123456;
    final String name = "Budget Name";
    final Double amount = 10000.00;
    final Double warningPercentage = 0.7;

    OBCreateBudgetRequest budgetRequest = new OBCreateBudgetRequest(
        budgetId,
        categoryId,
        name,
        amount,
        warningPercentage
    );

    new OpenBankingPFMAPI().budgetsClient().create(
        budgetRequest,
        new CreateBudgetListener() {

            @Override
            public void success(OBBudget budget) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
    });
```

Kotlin:

```kotlin
val budgetRequest = OBCreateBudgetRequest(
    userId = 123456,
    categoryId = 123456,
    name = "Budget Name",
    amount = 1000.00,
    warningPercentage = 0.7
)

OpenBankingPFMAPI.shared.budgetsClient().create(
    budgetRequest,
    object: CreateBudgetListener {
        override fun success(budget: OBBudget) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBBudget(
    @Json(name = "id") val id : Int,
    @Json(name = "categoryId") val categoryId : Long,
    @Json(name = "name") val name : String,
    @Json(name = "amount") val amount : Double,
    @Json(name = "warningPercentage") val warningPercentage : Double,
    @Json(name = "spent") val spent : Double,
    @Json(name = "leftToSpend") val leftToSpend : Double,
    @Json(name = "status") val status : String,
    @Json(name = "dateCreated") val dateCreated : Long,
    @Json(name = "lastUpdated") val lastUpdated : Long
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Fetch a Budget

Given a valid budget ID, fetches the information of a budget.

Java:

```java
    final Integer budgetId = 123456;

    new OpenBankingPFMAPI().budgetsClient().get(
        budgetId,
        new GetBudgetListener() {

            @Override
            public void success(OBBudget budget) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin:

```kotlin
val budgetId = 123456

OpenBankingPFMAPI.shared.budgetsClient().get(
    budgetId,
    object: GetBudgetListener {
        override fun success(budget: OBBudget) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBBudget(
    @Json(name = "id") val id : Int,
    @Json(name = "categoryId") val categoryId : Long,
    @Json(name = "name") val name : String,
    @Json(name = "amount") val amount : Double,
    @Json(name = "warningPercentage") val warningPercentage : Double,
    @Json(name = "spent") val spent : Double,
    @Json(name = "leftToSpend") val leftToSpend : Double,
    @Json(name = "status") val status : String,
    @Json(name = "dateCreated") val dateCreated : Long,
    @Json(name = "lastUpdated") val lastUpdated : Long
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Update a Budget

Given a valid budget ID, fetches the information of a budget.Update a budget. A previously created user and a category is required. There can be only one budget per category.

Java:

```java
    final Integer budgetId = 123456;
    final Integer categoryId = 123456;
    final String name = "Budget Name";
    final Double amount = 10000.00;
    final Double warningPercentage = 0.7;

    OBUpdateBudgetRequest budgetRequest = new OBUpdateBudgetRequest(
        categoryId,
        name,
        warningPercentage,
        amount
    );

    new OpenBankingPFMAPI().budgetsClient().edit(
        budgetId,
        budgetRequest,
        new UpdateBudgetListener() {

            @Override
            public void success(OBBudget budget) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
    });
```

Kotlin:

```kotlin
val budgetId = 123456
val budgetToUpdate = OBUpdateBudgetRequest(
    categoryId = 123456,
    name = "Budget Name",
    warningPercentage = 0.7,
    amount = 1000.00
)

OpenBankingPFMAPI.shared.budgetsClient().edit(
    budgetId,
    budgetToUpdate,
    object: UpdateBudgetListener {
        override fun success(budget: OBBudget) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBBudget(
    @Json(name = "id") val id : Int,
    @Json(name = "categoryId") val categoryId : Long,
    @Json(name = "name") val name : String,
    @Json(name = "amount") val amount : Double,
    @Json(name = "warningPercentage") val warningPercentage : Double,
    @Json(name = "spent") val spent : Double,
    @Json(name = "leftToSpend") val leftToSpend : Double,
    @Json(name = "status") val status : String,
    @Json(name = "dateCreated") val dateCreated : Long,
    @Json(name = "lastUpdated") val lastUpdated : Long
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



#### Delete Budget

Deletes a budget and all its information.

Java:

```java
    final Integer budgetId = 123456;

    new OpenBankingPFMAPI().budgetsClient().delete(
        budgetId,
        new DeleteBudgetListener() {

            @Override
            public void success() {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin:

```kotlin
val budgetId = 123456
OpenBankingPFMAPI.shared.budgetsClient().delete(
    budgetId,
    object: DeleteBudgetListener {
        override fun success() {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

The listener triggered a success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



## Bank Aggregation Consent

#### Request Consent

Given a bank ID and a user ID, create consent and retrieve the url for ask it.

Java:

```java
    final Integer userId = 123456;
    ArrayList<Integer> time = new ArrayList<>(Arrays.asList(3, 6, 12));

    new OpenBankingPFMAPI().banksClient().createConsent(
        currentBank.getBankId(),
        userId,
        time.get(0),
        new CreateConsentListener() {

            @Override
            public void success(@NonNull OBCreateConsentResponse consent) {
                // Use the server reponse 
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    );
```

Kotlin:

```kotlin
    val userId = 123456
    val time = arrayListOf(3, 6, 12)

    OpenBankingPFMAPI.shared.banksClient().createConsent(
    currentBank.bankId,
    userId,
    time.get(0),
    object : CreateConsentListener {
        override fun success(consent: OBCreateConsentResponse) {
            // Use the server reponse 
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBCreateConsentResponse(
    @Json(name = "bankId") val bankId: String,
    @Json(name = "userId") val userId: Int,
    @Json(name = "url") val url: String
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Bank agreggation status

Obtain bank aggregation status. using the previuos 'OBBanksAvailableResponse' response:

Java:

```java
    final Integer userId = 123456;

    new OpenBankingPFMAPI().banksClient().getAggregationStatus(
        bankReponse.getBankId(),
        userId,
        new GetAggregationStatusListener() {
            @Override
            public void success(@NonNull OBAggregationStatusResponse response) {
                // Use the server reponse 
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    );
```

Kotlin:

```kotlin
    val userId = 123456
    OpenBankingPFMAPI.shared.banksClient().getAggregationStatus(
        currentBank.bankId,
        userId,
        object: GetAggregationStatusListener {
            override fun success(response: OBAggregationStatusResponse) {
                // Use the server reponse 
            }

            override fun error(errors: List<OBError>) {
                // Catch error list in case that receive error
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    )
```

#### Output

```kotlin
data class OBAggregationStatusResponse(
    @Json(name = "bankId") val bankId: String,
    @Json(name = "userId") val userId: Int,
    @Json(name = "status") val status: String
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>

#### Resource list

Let's continue checking the consent resources list, using the previuos 'OBBanksAvailableResponse' response:

Java:

```java
    final Integer userId = 123456;

    new OpenBankingPFMAPI().banksClient().getResources(
        currentBank.getBankId(),
        userId,
        new GetResourcesListener() {
            @Override
            public void success(@NonNull OBResourcesResponse response) {
                // Use the server reponse 
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    );
```

kotlin:

```kotlin
    val userId = 123456
    OpenBankingPFMAPI.shared.banksClient().getResources(
        currentBank.bankId,
        userId,
        object : GetResourcesListener{
            override fun success(response: OBResourcesResponse) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBResourcesResponse(
    @Json(name = "bankId") val bankId: String,
    @Json(name = "userId") val userId: Int,
    @Json(name = "resources") val resources: List<String>
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>

#### Synchronize consent

Consult the resources granted to the user for the given bank and start in the background the aggregation of the resources that the user is authorised to user.

Java:

```java
    final Integer userId = 123456;

    new OpenBankingPFMAPI().banksClient().synchronize(
        currentBank.getBankId(),
        userId,
        new SynchronizeListener() {
            @Override
            public void success() {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    );
```

Kotlin:

```kotlin
    val userId = 123456

    OpenBankingPFMAPI.shared.banksClient().synchronize(
    currentBank.bankId,
    userId,
    object : SynchronizeListener{
        override fun success() {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

The listener triggered a success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


## Consents Client

#### Get Consents list

Fetches a list of consents per user.

Java:

```java
    final Integer userId = 123456;
    final String type = null; // Type: "Received" "Transmitted" or null.
    final String status = null; // Status: "Active" "Pending" "Expired" "Cancelled" or null.

    new OpenBankingPFMAPI().consentsClient().getList(
        userId,
        type,
        status,
        new GetConsentsListener() {

            @Override
            public void success(@NonNull OBConsentsResponse response) {
                // Use the server reponse
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin

```kotlin
val userId = 123456
val type = null // Type: "Received" "Transmitted" or null.
val status = null // Status: "Active" "Pending" "Expired" "Cancelled" or null.

OpenBankingPFMAPI.shared.consentsClient().getList(
    userId,
    type,
    status,
    object: GetConsentsListener {
        override fun success(response: OBConsentsResponse) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBConsentsResponse(
    @Json(name = "consents") var consents: List<OBConsent>
)

data class OBConsent(
    @Json(name = "consentId") var consentId: String,
    @Json(name = "expirationDate") var expirationDate: String?,
    @Json(name = "bank") var bank: OBBank,
    @Json(name = "status") var status: String,
    @Json(name = "originBankName") val originBankName: String?,
    @Json(name = "customerIdentification") val customerIdentification: String?,
    @Json(name = "cpf") val cpf: String?,
    @Json(name = "purpose") val purpose: String?,
    @Json(name = "deadline") val deadline: Int?,
    @Json(name = "expirationDay") val expirationDay: String?,
    @Json(name = "isSynchronized") val isSynchronized: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>

#### Fetch Consent

Given a valid consent ID, fetches the information of a consent.

Java:

```java
    final String consentId = "123456";

    new OpenBankingPFMAPI().consentsClient().get(
        consentId,
        new GetConsentListener() {
            @Override
            public void success(@NonNull OBConsent consent) {
                // Use the server reponse 
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin

```kotlin
val consenId = "123456"

OpenBankingPFMAPI.shared.consentsClient().get(
    consenId,
    object: GetConsentListener {
        override fun success(consent: OBConsent) {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
)
```

#### Output

```kotlin
data class OBConsent(
    @Json(name = "consentId") var consentId: String,
    @Json(name = "expirationDate") var expirationDate: String?,
    @Json(name = "bank") var bank: OBBank,
    @Json(name = "status") var status: String,
    @Json(name = "originBankName") val originBankName: String?,
    @Json(name = "customerIdentification") val customerIdentification: String?,
    @Json(name = "cpf") val cpf: String?,
    @Json(name = "purpose") val purpose: String?,
    @Json(name = "deadline") val deadline: Int?,
    @Json(name = "expirationDay") val expirationDay: String?,
    @Json(name = "isSynchronized") val isSynchronized: Boolean?
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 401

Invalid authorization.

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>

#### Cancel Consent

A consent is canceled given the identifier.

Java:

```java
    final String consentId = "123456";

    new OpenBankingPFMAPI().consentsClient().delete(
        consentId,
        new DeleteConsentListener() {
            @Override
            public void success() {
                // Use the server reponse 
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                    Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin

```kotlin
val consenId = "123456"

OpenBankingPFMAPI.shared.consentsClient().delete(
    consenId,
    object: DeleteConsentListener {
        override fun success() {
            // Use the server reponse
        }

        override fun error(errors: List<OBError>) {
            if (errors.isNotEmpty()) {
                val (_, _, detail) = errors[0]
                Log.e("ERROR", detail)
            }
        }

        override fun severError(serverError: Throwable) {
            Log.e("SERVER ERROR", serverError.message!!)
        }
    }
) 
```

#### Output

The listener triggered a success response

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>




## Credits Client

#### Get Credits list

Fetches a list of accounts per user, sorted by ID in descending order. The API is able to fetch up to 100 accounts. If a cursor is specified, the list starts with the item that has that ID.

Java:

```java
    final Integer userId = 123456
    final Integer cursor = null

    new OpenBankingPFMAPI().creditsClient().getList(userId, cursor,
        new GetCreditsListener() {
            @Override
            public void success(@NonNull OBCreditsResponse response) {
                // Use the server response
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        }
    );
```

Kotlin:

```kotlin
    val userId = 123456
    val cursor = 1

    OpenBankingPFMAPI.shared.creditsClient().getList(
        userId,
        cursor,
        object : GetCreditsListener {
            override fun success(response: OBCreditsResponse) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBCreditsResponse(
    @Json(name = "data") val credits: List<OBCredit>,
    @Json(name = "nextCursor") val nextCursor: Int?,
    @Json(name = "totalBalance") val totalBalance: OBTotalBalance?
)

data class OBCredit(
    @Json(name = "id") val id: Int,
    @Json(name = "providerId") val providerId: String,
    @Json(name = "financialEntityId") val financialEntityId: Int,
    @Json(name = "nature") val nature: String,
    @Json(name = "name") val name: String,
    @Json(name = "number") val number: String,
    @Json(name = "chargeable") val chargeable: Boolean,
    @Json(name = "dateCreated") val dateCreated: String,
    @Json(name = "lastUpdated") val lastUpdated: String,
    @Json(name = "lineName") val lineName: String,
    @Json(name = "availableAmount") val availableAmount: Double,
    @Json(name = "limitAmount") val limitAmount: Double,
    @Json(name = "usedAmount") val usedAmount: Double,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>



## Insights Client

#### Get Analysis

Given a valid user ID, fetches an analysis of the financial information of a user. It contains expenses, incomes and balances.

Java:

```java
    final Integer userId = 123456; 
    final Integer accountId = null;
    final Long dateFrom = null;
    final Long dateTo = null;

    new OpenBankingPFMAPI().insightsClient().getAnalysis(
        userId,
        accountId,
        dateFrom,
        dateTo,
        new GetAnalysisListener() {
            @Override
            public void success(List<OBAnalysisByMonth> response) {
                // Use the server response
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
    });
```

Kotlin:

```kotlin
    val userId = 123456
    val acountId = null
    val dateFrom = null
    val dateTo = null

    OpenBankingPFMAPI.shared.insightsClient().getAnalysis(
        userId,
        accountId,
        dateFrom,
        dateTo,
        object : GetAnalysisListener {
            override fun success(analysis: List<OBAnalysisByMonth>) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBAnalysisByMonth(
    @Json(name = "date") var date: Long,
    @Json(name = "categories") var categories: List<OBAnalysisByCategory>
)

data class OBAnalysisByCategory(
    @Json(name = "categoryId") val categoryId: Int,
    @Json(name = "amount") val amount: Double,
    @Json(name = "subcategories") val subcategories: List<OBAnalysisBySubcategory>
)

data class OBAnalysisBySubcategory(
    @Json(name = "categoryId") val categoryId : Int,
    @Json(name = "average") val average : Double,
    @Json(name = "quantity") val quantity : Int,
    @Json(name = "amount") val amount : Double,
    @Json(name = "transactions") val transactions : List<OBAnalysisByTransaction>
)

data class OBAnalysisByTransaction(
    @Json(name = "description") val description: String,
    @Json(name = "average") val average: Double,
    @Json(name = "quantity") val quantity: Int,
    @Json(name = "amount") val amount: Double,
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>


#### Get Resume

Given a valid user ID, fetches a resume of the financial information of a user. It contains expenses, incomes and balances.

Java:

```java
    final Integer userId = 123456; 
    final Integer accountId = null;
    final Long dateFrom = null;
    final Long dateTo = null;

    new OpenBankingPFMAPI().insightsClient().getResume(
        userId,
        accountId,
        dateFrom,
        dateTo,
        new GetSummaryListener() {

            @Override
            public void success(OBSummaryResponse summary) {
                // User the server response
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Log.e("ERROR", error.getDetail());
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("SERVER ERROR", serverError.getMessage());
            }
        });
```

Kotlin:

```kotlin
    val userId = 123456
    val acountId = null
    val dateFrom = null
    val dateTo = null

    OpenBankingPFMAPI.shared.insightsClient().getResume(
        userId,
        accountId,
        dateFrom,
        dateTo,
        object : GetSummaryListener {
            override fun success(summary: OBSummaryResponse) {
                // Use the server reponse
            }

            override fun error(errors: List<OBError>) {
                if (errors.isNotEmpty()) {
                    val (_, _, detail) = errors[0]
                    Log.e("ERROR", detail)
                }
            }

            override fun severError(serverError: Throwable) {
                Log.e("SERVER ERROR", serverError.message!!)
            }
        }
    )
```

#### Output

```kotlin
data class OBSummaryResponse(
    @Json(name = "incomes") val incomes : List<OBResumeByMonth>,
    @Json(name = "expenses") val expenses : List<OBResumeByMonth>,
    @Json(name = "balances") val balances : List<OBResumeBalance>
)

data class OBResumeByMonth(
    @Json(name = "date") val date : Long,
    @Json(name = "amount") val amount : Double,
    @Json(name = "categories") val categories : List<OBResumeByCategory>
)

data class OBResumeByCategory(
    @Json(name = "categoryId") val categoryId : Int,
    @Json(name = "amount") val amount : Double,
    @Json(name = "subcategories") val subcategories : List<OBResumeBySubcategory>
)

data class OBResumeBySubcategory(
    @Json(name = "categoryId") val categoryId : Int,
    @Json(name = "amount") val amount : Double,
    @Json(name = "transactionsByDate") val transactionsByDate : List<OBMovementsByDate>
)

data class OBMovementsByDate(
    @Json(name = "date") val date : Long,
    @Json(name = "transactions") val transactions : List<OBTransaction>
)

data class OBResumeBalance(
    @Json(name = "date") val date : Long,
    @Json(name = "incomes") val incomes : Double,
    @Json(name = "expenses") val expenses : Double
)

data class OBTransaction(
    @Json(name = "id") val id: Int,
    @Json(name = "date") val date: Long,
    @Json(name = "charge") val charge: Boolean,
    @Json(name = "description") val description: String,
    @Json(name = "amount") val amount: Double,
    @Json(name = "categoryId") val categoryId: Int?,
    @Json(name = "dateCreated") val dateCreated: Long,
    @Json(name = "lastUpdated") val lastUpdated: Long,
    @Json(name = "isBankAggregation") val isBankAggregation: Boolean
)
```

<details>
  <summary><h2>Error Codes</h2></summary>

#### Error 400

Something in your request was wrong.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 404

The requested param was not found.

```kotlin
data class OBError(
    @Json(name = "title") var title: String,
    @Json(name = "code") var code: String,
    @Json(name = "detail") var detail: String
)
```

#### Error 500

Something in your request was wrong.

```kotlin
val serverError: Throwable
serverError.message
```
</details>

