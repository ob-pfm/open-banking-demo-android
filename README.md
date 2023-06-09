# Open Banking PFM SDK Demo Android

![](https://img.shields.io/badge/minSDK-24+-blue.svg) ![Language](https://img.shields.io/badge/Language-Java-orange.svg) ![Language](https://img.shields.io/badge/Language-Kotlin-purple.svg)

This repository contains a subset of the Open Banking PFM SDK demo.

### Installation

1. Download the [latest PFM SDK release](https://github.com/ob-pfm/open-banking-demo-android/releases) zip file.
2. Unzip and take the `com` folder (containing the SDK's local maven dependencies) and put it in `~/.m2/repository/`.
3. Add `maven url` as repository in your root level settings.gradle file.

#### Using Settings Gradle

Add next configuration to project `settings.gradle`:

```gradle
    repositories {
        maven {
            url 'C:/Users/user_name/.m2/repository'
        }
        maven { url 'https://jitpack.io' }
    }
```

#### Using Project Build Gradle

Add next configuration to project `build.gradle`:

```gradle
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
android {
    ...
    android.buildFeatures.viewBinding = true
}

dependencies {
    //SDK's
    implementation 'com.openbanking.core.sdk:open-banking-core-sdk:1.0.0'
    implementation 'com.openbanking.pfm.sdk:open-banking-pfm-sdk:1.0.0'
}
```

#### Using Manifest

Add next permission into manifest.xml:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

The following property in Manifest is used only for vevelop purpose
```xml
    <application
        ...
        android:usesCleartextTraffic="true"
        ...
    </application>
```

#### Dependencies:

| Product               | README                                                                                               | Description                                                                                                         | Gradle dependency                                                               |
| --------------------- | ---------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| OpenBankingCore   | [README](https://github.com/ob-pfm/open-banking-demo-android/blob/master/documentation/README-OpenBankingCore.md)                    | Main SDK library, it is necessary for all SDKs.                                                                | `implementation 'com.openbanking.core.sdk:open-banking-core-sdk:1.0.0'` |
| OpenBankingPFMSDK     | [README](https://github.com/ob-pfm/open-banking-demo-android/blob/master/documentation/README-OpenBankingPFMSDK.md)     | Library for Open Banking PFM. | `implementation 'com.openbanking.pfm.sdk:open-banking-pfm-sdk:1.0.0'`                     |

### License
