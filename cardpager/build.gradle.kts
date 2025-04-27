import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.vanniktech.maven.publish") version "0.31.0"
}

android {
    namespace = "com.ceos.cardpager"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.animation.core.android)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.foundation.android)
}

mavenPublishing {
    // Define coordinates for the published artifact
    coordinates(
        groupId = "com.ceosdevelopment",
        artifactId = "card-pager",
        version = "1.0.0"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Card Pager")
        description.set("A component for displaying cards in a carousel effect.")
        inceptionYear.set("2025")
        url.set("https://github.com/Ceos-development/CardPager")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("Ceos-development")
                name.set("Ceos development")
                email.set("jean.christophe.decary.ceos@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/Ceos-development/CardPager")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}