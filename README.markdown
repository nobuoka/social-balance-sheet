社会的貸借対照表 (Social B/S)
==========

[![CircleCI](https://circleci.com/gh/nobuoka/social-balance-sheet.svg?style=svg)](https://circleci.com/gh/nobuoka/social-balance-sheet)
[![codecov](https://codecov.io/gh/nobuoka/social-balance-sheet/branch/master/graph/badge.svg)](https://codecov.io/gh/nobuoka/social-balance-sheet)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/44c5090c3dbf4bb1aa7375ba4477936b)](https://app.codacy.com/app/nobuoka/social-balance-sheet?utm_source=github.com&utm_medium=referral&utm_content=nobuoka/social-balance-sheet&utm_campaign=Badge_Grade_Dashboard)

## How to run on localhost

* Copy local.conf.template file to local.conf.
* Edit local.conf file for your environment.
* Run `Main#main` function with parameter `-config=local.conf`.
    * For example, execute command `./gradlew :app:run --args="-config=../local.conf"`

## Heroku

This application runs on Heroku.

* https://vcsbs.herokuapp.com/

[Procfile](./Procfile) file and Gradle `:app:stage` task are defined for Heroku.

* See : [Deploying Gradle Apps on Heroku | Heroku Dev Center](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku)

## Application configuration

### Environment variables

| Name | Example |  |
|---|---|---|
| SBS_CONTEXT_URL | https://vcsbs.herokuapp.com/ | |
| SBS_SESSION_ENCRYPTION_KEY | 0123456789ABCDEF | Length must be 16 |
| SBS_SESSION_SIGN_KEY | 01234567 | Length must be 8 |
| SBS_TWITTER_CLIENT_IDENTIFIER | YOUR_TWITTER_APP_IDENTIFIER | |
| SBS_TWITTER_CLIENT_SECRET | YOUR_TWITTER_APP_SHARED_SECRET | |
