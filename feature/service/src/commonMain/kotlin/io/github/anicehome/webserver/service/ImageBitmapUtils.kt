package io.github.anicehome.webserver.service

/*
 * Copyright 2020 The Android Open Source Project
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


import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Hashtable
import kotlin.collections.set

fun createImageBitmap(url: String, size: Int): ImageBitmap {
    val hints = Hashtable<EncodeHintType, String>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
    hints[EncodeHintType.ERROR_CORRECTION] = "H"
    hints[EncodeHintType.MARGIN] = "2"
    val bitMatrix = QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, size, size, hints)
    val imageBitmap = ImageBitmap(size, size)
    val canvas = Canvas(imageBitmap)
    val paint = Paint()

    val w = bitMatrix.width
    val h = bitMatrix.height
    for (y in 0 until h) {
        for (x in 0 until w) {
            val left = x.toFloat()
            val top = y.toFloat()
            if (bitMatrix.get(x, y)) {
                paint.color = Color.Black
                canvas.drawRect(Rect(left, top, left + 1, top + 1), paint)
            } else {
                paint.color = Color.White
                canvas.drawRect(Rect(left, top, left + 1, top + 1), paint)
            }
        }
    }
    return imageBitmap
}