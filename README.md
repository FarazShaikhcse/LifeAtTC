# Truecaller Text Processor App

## Overview
This Android app fetches and processes the content of the following webpage:
[Life as an Android Engineer - Truecaller](https://www.truecaller.com/blog/life-at-truecaller/life-as-an-android-engineer).
The webpage content is treated as plain text and processed to perform three parallel tasks. The results of each task are displayed on the screen as soon as they are completed.

## Features
1. **Truecaller15thCharacterRequest**
    - Extracts the 15th character from the webpage content(ignores all white-space characters(space, tab, line break, etc.)).
    - Displays the character on the screen.

2. **TruecallerEvery15thCharacterRequest**
    - Extracts every 15th character from the webpage content (i.e., 15th, 30th, 45th, etc.).
    - Ignores all white-space characters(space, tab, line break, etc.)).
    - Displays the array of extracted characters.

3. **TruecallerWordCounterRequest**
    - Splits the webpage text by whitespace characters (space, tab, line break, etc.).
    - Counts occurrences of every unique word (case insensitive).
    - Displays the word count for each word.

## How It Works
- The app features a **Load Website Content** button.
- When pressed, it fetches the content of the specified webpage.
- After the response is received from the website, the three tasks mentioned above are called parallel
- The results of each task are displayed as soon as they complete.

## Technologies Used
- **Android SDK** (Java/Kotlin for Android development)
- **Multithreading (Coroutines/Executors)** for parallel processing
- **HTTP Library (OkHttp/Retrofit)** for fetching webpage content
- **Text Processing(Regex/String functions)** for character and word extraction

## Installation
1. Clone this repository(https://github.com/FarazShaikhcse/LifeAtTC).
2. Open the project in Android Studio.
3. Build and run the app on an Android device or emulator.

## Usage
1. Launch the app.
2. Click the "Load Content" button.
3. Wait for the three tasks to complete.
4. View the extracted character, character array, and word count results on the screen.
