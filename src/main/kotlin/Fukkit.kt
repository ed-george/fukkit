
import com.google.cloud.speech.v1p1beta1.RecognitionAudio
import com.google.cloud.speech.v1p1beta1.RecognitionConfig
import com.google.cloud.speech.v1p1beta1.SpeechClient
import com.google.cloud.speech.v1p1beta1.WordInfo
import com.google.protobuf.ByteString
import java.io.File

/*
 * (C) Copyright 2018 Breedr Apps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class Fukkit {
    companion object {

        private var fileName = "default.flac"

        @JvmStatic fun main(args: Array<String>) {

            // Filename currently from bundled resources
            // TODO: Use relative filepath
            fileName = if (args.isNotEmpty()) args[0] else fileName

            // Instantiates a client

            SpeechClient.create().use { speechClient ->

                // The path to the audio file to transcribe

                // Reads the audio file into memory
                val classloader = Fukkit::class.java.classLoader
                val fileBytes = classloader.getResource(fileName).readBytes()
                val audioBytes = ByteString.copyFrom(fileBytes)

                // Builds the sync recognize request
                val config = RecognitionConfig.newBuilder()
                        .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                        .setLanguageCode("en-US")
                        .setEnableWordTimeOffsets(true)
                        .build()

                val audio = RecognitionAudio.newBuilder()
                        .setContent(audioBytes)
                        .build()

                // Performs speech recognition on the audio file
                val response = speechClient.recognize(config, audio)

                response.resultsList.forEach {
                    // There can be several alternative transcripts for a given chunk of speech. Just use the
                    // first (most likely) one here.
                    val alternative = it.alternativesList[0]
                    when (alternative.wordsCount) {
                        0 -> println("No words found")
                        else -> parseAllWords(alternative.wordsList)
                    }
                    println("Transcript: ${alternative.transcript}")
                }
            }
        }

        private fun parseAllWords(wordsList: List<WordInfo>) {
            if(wordsList.isEmpty()) return
            val profanityList = mutableListOf<WordInfo>()
            wordsList.forEach {
                val isProfanity = ProfanityBlacklist.validate(it.word)
                if (isProfanity) profanityList.add(it)
            }
            parseProfanityList(profanityList)
        }

        private fun parseProfanityList(profanityList: List<WordInfo>) {
            // TODO: Move this
            val fileUri = Fukkit::class.java.classLoader.getResource(fileName).toURI()
            val file = File(fileUri)
            profanityList.forEach {
                generateProfanityMask(it, file)
            }
        }
    }
}