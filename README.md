# Panatchai Test @ Crypto.com
Panatchai Interview Test at Crypto.com
Please check out **develop** branch.

## Architecture
[![N|Solid](https://developer.android.com/topic/libraries/architecture/images/paging3-library-architecture.svg)](Paging3)

## Basic commands
To run tests and generate a JaCoco report
```sh
./gradlew jacocoCoverageVerification
```

To only generate a JaCoco report
```sh
./gradlew generateJacocoTestReport 
```

To run Lint check e.g. Ktlint
```sh
./gradlew lintKotlin
```

## Features
- Lint checks on pre-commit using Git Hook

✨**Note** ✨
- You may increase the [HARDCODE_LIMIT](https://github.com/OrcaV/PanatchaiCryptoTest/blob/develop/app/src/main/java/com/v/panatchai/cryptocurrency/data/mediators/CoinServiceMediator.kt#L99) to allow more currencies, please read the comment.
- **Domain layer** is optional, however, included just for the sake of showing that it can be done.
- Instrument tests are ignored, otherwise, it would be too much work.
