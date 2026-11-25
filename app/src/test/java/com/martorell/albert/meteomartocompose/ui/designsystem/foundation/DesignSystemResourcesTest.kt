package com.martorell.albert.meteomartocompose.ui.designsystem.foundation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class DesignSystemResourcesTest {

    @Test
    fun `bundled webp drawables and roboto flex font exist with valid binary headers and zero screen mockups`() {
        val resDir = resolveResDir()
        assertTrue("Expected res directory at ${resDir.absolutePath}", resDir.isDirectory)

        val drawableDir = File(resDir, "drawable")
        assertTrue("Expected drawable directory at ${drawableDir.absolutePath}", drawableDir.isDirectory)

        val expectedWebpFiles = listOf(
            "ic_logo.webp",
            "ic_warning.webp",
            "ic_error_forecast.webp",
        )
        expectedWebpFiles.forEach { fileName ->
            val asset = File(drawableDir, fileName)
            assertTrue("Missing WebP drawable: ${asset.absolutePath}", asset.isFile)
            val header = asset.inputStream().use { stream ->
                val buffer = ByteArray(WEBP_HEADER_SIZE)
                val bytesRead = stream.read(buffer)
                assertEquals("Unexpected truncated WebP header in $fileName", WEBP_HEADER_SIZE, bytesRead)
                buffer
            }
            val riffSignature = String(header, RIFF_OFFSET, SIGNATURE_LENGTH, Charsets.US_ASCII)
            val webpSignature = String(header, WEBP_OFFSET, SIGNATURE_LENGTH, Charsets.US_ASCII)
            assertEquals("Invalid RIFF header in $fileName", RIFF_MAGIC, riffSignature)
            assertEquals("Invalid WEBP signature in $fileName", WEBP_MAGIC, webpSignature)
        }

        val robotoFlexFont = File(resDir, "font/roboto_flex.ttf")
        assertTrue("Missing Roboto Flex font at ${robotoFlexFont.absolutePath}", robotoFlexFont.isFile)
        assertTrue("Roboto Flex font file must not be empty", robotoFlexFont.length() > 0L)

        val forbiddenMockups = drawableDir.listFiles().orEmpty().filter { file ->
            val name = file.name.lowercase()
            name == "screen.png" || (name.startsWith("meteomarto_") && name.endsWith(".png"))
        }
        assertTrue(
            "Screen mockup PNGs must not exist in res/drawable/: ${forbiddenMockups.map { it.name }}",
            forbiddenMockups.isEmpty(),
        )
    }

    private fun resolveResDir(): File {
        val moduleRelative = File("src/main/res")
        return if (moduleRelative.isDirectory) moduleRelative else File("app/src/main/res")
    }

    companion object {
        private const val WEBP_HEADER_SIZE = 12
        private const val RIFF_OFFSET = 0
        private const val WEBP_OFFSET = 8
        private const val SIGNATURE_LENGTH = 4
        private const val RIFF_MAGIC = "RIFF"
        private const val WEBP_MAGIC = "WEBP"
    }
}
