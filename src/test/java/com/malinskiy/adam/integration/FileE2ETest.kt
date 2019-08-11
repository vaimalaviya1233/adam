/*
 * Copyright (C) 2019 Anton Malinskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.malinskiy.adam.integration

import com.malinskiy.adam.request.sync.PushFileRequest
import com.malinskiy.adam.request.sync.ShellCommandRequest
import com.malinskiy.adam.rule.AdbDeviceRule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldEqual
import org.junit.Rule
import org.junit.Test
import java.io.File
import kotlin.math.roundToInt

class FileE2ETest {
    @get:Rule
    @JvmField
    val adbRule = AdbDeviceRule()

    @Test
    fun testApkPushing() {
        val testFile = File(javaClass.getResource("/app-debug.apk").toURI())
        val fileName = testFile.name
        runBlocking {
            val channel =
                adbRule.adb.execute(PushFileRequest(testFile, "/data/local/tmp/$fileName"), GlobalScope, serial = adbRule.deviceSerial)

            var percentage = 0
            while (!channel.isClosedForReceive) {
                val percentageDouble = channel.receiveOrNull() ?: break

                val newPercentage = (percentageDouble * 100).roundToInt()
                if (newPercentage != percentage) {
                    print('.')
                    percentage = newPercentage
                }
            }
            println()

            val size = adbRule.adb.execute(ShellCommandRequest("stat -c \"%s\" /data/local/tmp/app-debug.apk"), adbRule.deviceSerial)
            size.toLong() shouldEqual testFile.length()
        }
    }
}