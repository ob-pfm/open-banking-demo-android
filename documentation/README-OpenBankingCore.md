# Open Banking Core Android

![](https://img.shields.io/badge/minSDK-24+-blue.svg) ![Language](https://img.shields.io/badge/Language-Java-orange.svg) ![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)

This repository contains a subset of the Open Banking Core SDK.

### Installation

#### Using Project Build Gradle

Add next configuration to project `build.gradle`:

```build.gradle
buildscript {  
    ext.kotlin_version = "1.6.0"  
    ...  
    dependencies {  
         classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"  
         ...  
    }  
}  
```

#### Using Module Gradle

Add next configuration to module `build.gradle`:

```build.gradle
dependencies {   
    implementation 'com.openbanking.core.sdk:open-banking-core-sdk:1.0.0'
}  
```

#### Using Manifest

Add next permission into manifest.xml:

```xml
<uses-permission android:name="android.permission.INTERNET"/>      
```

#### Initialize Open Banking Core SDK

Java:

```java
    final OpenBankingCore openBankingCore = OpenBankingCore.Companion.getShared();
    openBankingCore.setApiKey("your_api_key");
    openBankingCore.configure();
```

Kotlin:

```kotlin
    val openBankingCore = shared
    openBankingCore.apiKey = "your_api_key"
    openBankingCore.configure()
```
