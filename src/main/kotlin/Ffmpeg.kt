import com.google.cloud.speech.v1p1beta1.WordInfo
import java.io.File
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.concurrent.TimeUnit

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


fun generateProfanityMask(wordInfo: WordInfo, audioFile: File) {
    println("${wordInfo.word} duration: ${wordInfo.duration}")
    val beepFile = generateBeepFile(wordInfo)
    addBeep(wordInfo, audioFile, beepFile)
}

fun generateBeepFile(wordInfo: WordInfo): File {
    val seconds = wordInfo.duration / 1000000000.0
    val filename = "${wordInfo.word}_${wordInfo.startNanos}"
    val file = Files.createTempFile(filename, ".flac")
    // Previously used:
    // ffmpeg -y -f lavfi -i \"sine=frequency=1000:sample_rate=48000:duration=$seconds\" -c:a pcm_s16le ${file.toAbsolutePath()}
    // to generate WAV file
    val createCommand = "ffmpeg -y -f lavfi -i \"sine=frequency=1000:sample_rate=48000:duration=$seconds\" -af aformat=s16:44100 ${file.toAbsolutePath()}"

    println(createCommand)

    runProcess(createCommand)

    return file.toFile()

}

fun runProcess(command: String): Int {
    val process = ProcessBuilder(
            "/bin/sh",
            "-c",
            command
    ).start()

    val out = PipeThread(process.inputStream, System.out)
    val err = PipeThread(process.errorStream, System.err)
    out.start()
    err.start()

    process.waitFor()
    return process.exitValue()
}

fun addBeep(wordInfo: WordInfo, audioFile: File, beepFile: File) {

    val startTimeSecond = wordInfo.startNanos / 1000000000.0
    val endTimeSecond = startTimeSecond + (wordInfo.duration / 1000000000.0)
    val output = Files.createTempFile("output", ".flac")
    val overwriteCommand = """ffmpeg -y -i ${audioFile.absolutePath} -i ${beepFile.absolutePath} \
        | -filter_complex "[0]atrim=0:$startTimeSecond[Apre];[0]atrim=$endTimeSecond,asetpts=PTS-STARTPTS[Apost];\
        | [Apre][1][Apost]concat=n=3:v=0:a=1" ${output.toAbsolutePath()}""".trimMargin()

    println(overwriteCommand)

    val result = runProcess(overwriteCommand)
    when(result) {
        0 -> replaceOriginalAudioFile(audioFile.toPath(), output)
        else -> throw RuntimeException("Failed to update audio")
    }

}

fun replaceOriginalAudioFile(audioFile: Path, replacement: Path) {
    Files.move(replacement, audioFile, StandardCopyOption.REPLACE_EXISTING)
}

val WordInfo.startNanos : Long
    get() = startTime.nanos + TimeUnit.SECONDS.toNanos(startTime.seconds)

val  WordInfo.endNanos : Long
    get() = endTime.nanos + TimeUnit.SECONDS.toNanos(endTime.seconds)

val WordInfo.duration: Long
    get() {
        return endNanos - startNanos
    }