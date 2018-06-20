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

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class PipeThread(private var inputStream: InputStream, private var outputStream: OutputStream) : Thread() {

    override fun run() {
        val buffer = ByteArray(1024)
        try {
            var len = inputStream.read(buffer)
            while (len >= 0) {
                outputStream.write(buffer, 0, len)
                len = inputStream.read(buffer)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}