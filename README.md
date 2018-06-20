<p align="center" width="400">
    <img src="https://raw.githubusercontent.com/ed-george/fukkit/master/images/logo.png">
        
</p>

--------------

[![GitHub release](https://img.shields.io/github/release/ed-george/fukkit.svg)](https://github.com/ed-george/fukkit/releases) [![Support fukkit](https://img.shields.io/badge/buy%20me%20a%20beer-via%20beerpay-f85d5d.svg)](https://beerpay.io/ed-george/fukkit) [![GitHub issues](https://img.shields.io/github/issues/ed-george/fukkit.svg)](https://github.com/ed-george/fukkit/issues) [![GitHub license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://raw.githubusercontent.com/ed-george/fukkit/master/LICENSE)

--------------

<p align="center" width="400">
    A Kotlin application to censor profanity within an audio file using ffmpeg
</p>


## Setup ⚠️

This project uses Google's [Cloud Speech-to-Text API](https://cloud.google.com/speech-to-text/docs/reference/libraries) and as such, will require authentication to carry out any audio analysis.

### Google Console Setup

Firstly, you will need to [create an application](https://console.developers.google.com/projectcreate) within Google's Cloud Console and [enable the Cloud Speech-to-Text API](https://console.developers.google.com/apis/api/speech.googleapis.com/overview).

Once completed, follow these steps in order to correctly generate the auth file required:

1. Go to the [Create service account](https://console.cloud.google.com/apis/credentials/serviceaccountkey) key page in the GCP Console.

2. From the Service account drop-down list, select New service account.

3. Enter a name into the Service account name field.

4. From the Role drop-down list, select **Project > Owner**.

5. Click Create. A JSON file that contains your key downloads to your computer.

6. Within a terminal, create an environment variable `GOOGLE_APPLICATION_CREDENTIALS` that points to the path of the downloaded file.

## Usage 🙊🗯

This project is still a work in progress, full usage details will be provided as soon as possible 

## Contributing 🛠

I welcome contributions and discussion for new features or bug fixes. It is recommended to file an issue first to prevent unnecessary efforts, but feel free to put in pull requests in the case of trivial changes. In any other case, please feel free to open discussion and I will get back to you when possible.

## Thanks 💕

Thanks to Savvas Constantinides for the project idea!



 
