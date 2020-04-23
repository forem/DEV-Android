# DEV Android 💝
<a href="https://codeclimate.com/github/thepracticaldev/DEV-Android/maintainability"><img src="https://api.codeclimate.com/v1/badges/ad31b8a267a37475e14c/maintainability" /></a>
<a href="https://codeclimate.com/github/thepracticaldev/DEV-Android/test_coverage"><img src="https://api.codeclimate.com/v1/badges/ad31b8a267a37475e14c/test_coverage" /></a>

This is the official repository for the [dev.to](https://dev.to/)'s Android app.

<a href='https://play.google.com/store/apps/details?id=to.dev.dev_android&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img width=150px alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>


## Design ethos

DEV Android is an [WebView](https://developer.android.com/guide/webapps/webview) based application. This application is inspired by [Basecamp's approach](https://m.signalvnoise.com/basecamp-3-for-ios-hybrid-architecture-afc071589c25). We will grow to include more native code over time.

By leveraging webviews as much as possible, we can smoothly sync up with our web dev work. And where it makes sense, we can re-implement certain things fully native, or build entirely native features. Life's a journey, not a destination.

## Contributions

We expect contributors to abide by our underlying [code of conduct](./CODE_OF_CONDUCT.md). All conversations and discussions on GitHub (issues, pull requests) and across dev.to must be respectful and harassment-free.

### System Requirements

You will need to have Android Studio 3.5 or up installed.

### Usage

```bash
$ ./gradlew tasks --group=custom

------------------------------------------------------------
Tasks runnable from root project
------------------------------------------------------------

Custom tasks
------------
androidTest - Run android instrumentation tests
hello - Hello World task - useful to solve build problems
install - Build and install the app
test - Run the unit tests

To see all tasks and more detail, run gradlew tasks --all

To see more detail about a task, run gradlew help --task <task>

```

### Push Notifications

For Push Notification delivery we use [Pusher Beams](https://pusher.com/beams). In order to get the app running locally you'll need a `google-services.json` configuration file from Firebase, otherwise you'll encounter the following error: `File google-services.json is missing. The Google Services Plugin cannot function without it.`

You can [sign up or sign in on Firebase](https://firebase.google.com/) account for free in order to get the app working locally. Steps 1-4 under **Firebase for Android Push Notifications** in our [official docs](https://docs.dev.to/backend/pusher/#mobile-push-notifications) show how to set this up in more detail. Drop the resulting `google-services.json` file in the `app` folder and you'll be good to go.

### How to contribute

1.  Fork the project & clone locally.
1.  Create a branch, naming it either a feature or bug: `git checkout -b feature/that-new-feature` or `bug/fixing-that-bug`
1.  Code and commit your changes. Bonus points if you write a [good commit message](https://chris.beams.io/posts/git-commit/): `git commit -m 'Add some feature'`
1.  Push to the branch: `git push origin feature/that-new-feature`
1.  Create a pull request for your branch 🎉

## License

This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Please see the [LICENSE](./LICENSE) file in our repository for the full text.

Like many open source projects, we require that contributors provide us with a Contributor License Agreement (CLA). By submitting code to the DEV project, you are granting us a right to use that code under the terms of the CLA.

Our version of the CLA was adapted from the Microsoft Contributor License Agreement, which they generously made available to the public domain under Creative Commons CC0 1.0 Universal.

Any questions, please refer to our [license FAQ](https://docs.dev.to/licensing/) doc or email yo@dev.to

<br/>

<p align="center">
  <img
    alt="sloan"
    width=250px
    src="https://thepracticaldev.s3.amazonaws.com/uploads/user/profile_image/31047/af153cd6-9994-4a68-83f4-8ddf3e13f0bf.jpg"
  />
  <br/>
  <strong>Happy Coding</strong> ❤️
</p>
