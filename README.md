[![Build Status](https://travis-ci.org/sebaslogen/Blendletje.svg?branch=master)](https://travis-ci.org/sebaslogen/Blendletje)

# Blendletje
Sample Android project of a [Blendle](https://blendle.com) client.

The client will load automatically the most popular articles from Blendle API, which can be found here: https://static.blendle.nl/api.json


Technical features
============
The code demostrates how to use together multiple design patterns, libraries, automated tests on the Java virtual machine and on the UI using Espresso

Libraries
-------
- RxJava
- Retrofit, OkHttp and MockWebServer
- Gson
- Halarious
- AutoValue
- Dagger
- Picasso
- Mockito
- Espresso
- Timber

Patterns
-------
- Model view presenter
- Dependency inversion and dependency injection
- Continuous integration
- Page Object Pattern


Running the project and the tests
=============
Open the project in Android Studio and select the gradle task '**installDebug**' or simply press the Run button.

To run all the tests from the command line execute ```./gradlew check connectedCheck``` from the project's folder.

_Note: Make sure to connect a phone to the computer or start an emulator before running the tests._

Continuous integration environment
============
Builds are triggered and build automatically on every commit to git repository.
Build history and reports can be accessed here: https://travis-ci.org/sebaslogen/Blendletje/builds

License
-------
This content is released under the MIT License: http://opensource.org/licenses/MIT
