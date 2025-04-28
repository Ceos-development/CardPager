# CardPager

[![Maven Central](https://img.shields.io/maven-central/v/com.ceosdevelopment/card-pager)](https://search.maven.org/search?q=g:com.ceosdevelopment%20AND%20a:card-pager)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)
![CardPager Demo](docs/images/cardpager-demo.gif)

**CardPager** is a Kotlin library for Android that emulates a carousel-like UI, similar to the card display found in Google Wallet. It's built with Jetpack Compose, providing a modern and efficient way to implement this engaging card-swiping experience in your Android applications.

## Table of Contents

-   [Description](#description)
-   [Features](#features)
-   [Getting Started](#getting-started)
    -   [Installation](#installation)
    -   [Setup](#setup)
-   [Sample Application](#sample-application)
-   [Contributing](#contributing)
-   [License](#license)
-   [Contact](#contact)

## Description

CardPager provides a customizable and easy-to-use component for displaying a series of cards in a horizontal, carousel-like layout. It mimics the smooth animations and card-swiping behavior seen in Google Wallet, making it ideal for applications that need to present information in a visually appealing and interactive way.

## Features

-   **Carousel-like UI:** Emulates the card-swiping behavior of Google Wallet.
-   **Smooth Animations:** Provides fluid and engaging animations for card transitions.
-   **Jetpack Compose:** Built with modern declarative UI framework for Android.
-   **Customizable:** Offers flexibility in styling and behavior to fit your app's design.
-   **Easy Integration:** Simple to add to your existing Compose-based projects.

## Getting Started

### Installation
Add the codes below to your **root** `build.gradle` file (not your module-level build.gradle file):
```gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Next, add the dependency below to your **module**'s `build.gradle` file:

```gradle
dependencies {
    implementation("com.ceosdevelopment:card-pager:1.0.0")
}
```

## Setup

As simple has this:
```kotlin
 CardPager(
    modifier = Modifier.weight(1F),
    state = rememberCardPagerState(11)
 ) { index ->
        CardContent(index)
}
```
`rememberCardPagerState` has 3 parameters
```kotlin
@Composable
fun rememberCardPagerState(
    count: Int,
    carouselSize: Int = 4,
    onCardIndexChange: (cardIndex: Int) -> Unit = {},
): CardPagerState
```
* **count** Represent the number of card total
* **carouselSize** Represent the number of card visible, usually 4, 1 to the left, 1 center, 1 to the right and 1 always to the back to give the illusion of a carousel.
* **onCardIndexChange** will be trigger when the card in the center of the screen is change


## Contributing

Contributions are welcome and encouraged! We aim to make CardPager a stable and reliable library for the Android community. Here are some guidelines for contributing:

1.  **Bug Reports:** If you encounter a bug, please open an issue on GitHub with a clear description of the problem, steps to reproduce it, and any relevant information about your environment.
2.  **Feature Requests:** If you have an idea for a new feature, please open an issue to discuss it before starting to work on it.
3.  **Pull Requests:**
    *   Fork the repository and create a new branch for your changes.
    *   Ensure your code follows the existing style and conventions.
    *   Write clear and concise commit messages.
    *   Test your changes thoroughly.
    *   Submit a pull request with a detailed description of your changes.
4. **Stability:** We are looking for help to make this library more stable, if you have any idea or want to help, please open an issue or a pull request.

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

If you have any questions, suggestions, or feedback, feel free to open an issue on GitHub.


