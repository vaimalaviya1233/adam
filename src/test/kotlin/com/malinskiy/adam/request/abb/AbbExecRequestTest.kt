/*
 * Copyright (C) 2021 Anton Malinskiy
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

package com.malinskiy.adam.request.abb

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.malinskiy.adam.Const
import org.junit.Test

class AbbExecRequestTest {
    /**
     * It's a bit tricky to check for '\0'
     */
    @Test
    fun testSerialize() {
        val array = AbbExecRequest(listOf("cmd", "package", "install")).serialize()

        assertThat(String(array, 0, 16, Const.DEFAULT_TRANSPORT_ENCODING)).isEqualTo("001Cabb_exec:cmd")
        assertThat(array[16].toChar()).isEqualTo(AbbExecRequest.DELIMITER)
        assertThat(String(array, 17, 7, Const.DEFAULT_TRANSPORT_ENCODING)).isEqualTo("package")
        assertThat(array[24].toChar()).isEqualTo(AbbExecRequest.DELIMITER)
        assertThat(String(array, 25, 7, Const.DEFAULT_TRANSPORT_ENCODING)).isEqualTo("install")
    }
}
