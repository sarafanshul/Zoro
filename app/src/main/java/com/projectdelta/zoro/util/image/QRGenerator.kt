/*
 * Copyright (c) 2022. Anshul Saraf
 */

package com.projectdelta.zoro.util.image

import android.graphics.Bitmap
import com.github.sumimakito.awesomeqr.option.RenderOption
import com.github.sumimakito.awesomeqr.option.background.BlendBackground
import com.github.sumimakito.awesomeqr.option.color.Color
import com.github.sumimakito.awesomeqr.option.logo.Logo
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRGenerator {

    private const val IMAGE_SIZE = 700
    private const val BORDER_WIDTH = 20
    private const val DEFAULT_DARK_COLOR = 0xFF007F6B.toInt() // cyan
    private const val DEFAULT_EMPTY_COLOR = 0xFFFFFFFF.toInt() // white

    /**
     * Experimental
     */
    fun generateRenderOptions(
        content: String,
        backgroundImage: Bitmap,
        logoImage: Bitmap? = null
    ): RenderOption {

        val color = Color()
        color.light = DEFAULT_EMPTY_COLOR // for blank spaces
        color.dark = DEFAULT_DARK_COLOR // for non-blank spaces
        color.background = DEFAULT_EMPTY_COLOR
//        color.auto = true

        val background = BlendBackground()
        background.bitmap = backgroundImage
        background.alpha = 0.65f
        background.borderRadius = 10 // radius for blending corners

        val renderOption = RenderOption()
        renderOption.content = content
        renderOption.size = IMAGE_SIZE
        renderOption.ecl = ErrorCorrectionLevel.H
        renderOption.borderWidth = BORDER_WIDTH
        renderOption.roundedPatterns = true
        renderOption.clearBorder = true
        renderOption.color = color
        renderOption.background = background

        if (logoImage != null) {
            val logo = Logo()
            logo.bitmap = logoImage
            logo.borderRadius = 10 // radius for logo's corners
            logo.borderWidth = 10 // width of the border to be added around the logo
            logo.scale = 0.3f // scale for the logo in the QR code
//            logo.clippingRect = RectF(0f, 0f, 200f, 200f) // crop the logo image before applying it to the QR code

            renderOption.logo = logo
        }
        return renderOption
    }
}
