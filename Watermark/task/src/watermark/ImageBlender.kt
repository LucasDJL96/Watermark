package watermark

import java.awt.Color
import java.awt.Transparency
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/** Class representing an Image Blender for adding watermarks to images */
class ImageBlender {

    /** Initializes all the fields of this */
    fun init() {
        initImage()
        initWatermark()
        checkCompatible()
        initAnswer1()
        initAnswer2()
        if (answer2 == "YES") initTransparencyColor()
        initWeight()
        initPositionMethod()
        if (positionMethod == PositionMethod.SINGLE) initPosition()
        initRangeX()
        initRangeY()
        initOutputFile()
        initOutputImage()
    }

    /**
     * BufferedImage representing the original image.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the image is not valid
     */
    private val image: BufferedImage by lazy {
        println("Input the image filename:")
        val filename = readln()
        val file = File(filename)
        checkImageIsValid(file, "image")
    }

    /** Initializes image */
    private fun initImage() {
        image
    }

    /**
     * BufferedImage representing the watermark image.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the image is not valid
     */
    private val watermark: BufferedImage by lazy {
        println("Input the watermark image filename:")
        val watermarkName = readln()
        val waterMarkFile = File(watermarkName)
        checkImageIsValid(waterMarkFile, "watermark")
    }

    /** Initializes watermark */
    private fun initWatermark() {
        watermark
    }

    /**
     * String representing the answer to using watermark's alpha channel.
     * Initialized through a dialog
     */
    private val answer1: String by lazy {
        if (watermark.transparency == Transparency.TRANSLUCENT) {
            println("Do you want to use the watermark's Alpha channel?")
            readln().uppercase()
        } else {
            "NO"
        }
    }

    /** Initializes answer1 */
    private fun initAnswer1() {
        answer1
    }

    /**
     * String representing the answer to setting a transparency color.
     * Initialized through a dialog
     */
    private val answer2: String by lazy {
        if (watermark.transparency != Transparency.TRANSLUCENT) {
            println("Do you want to set a transparency color?")
            readln().uppercase()
        } else {
            "NO"
        }
    }

    /** Initializes answer2 */
    private fun initAnswer2() {
        answer2
    }

    /**
     * Color representing a transparency color for the watermark.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the input isn't according to the
     * specified format or is outside the range 0-255
     */
    private val transparencyColor: Color by lazy {
        val errorMSG = "The transparency color input is invalid."
        println("Input a transparency color ([Red] [Green] [Blue]):")
        try {
            val colors = readln().split(" ").map(String::toInt)
            require(colors.size == 3
                && colors[0] in 0..255
                && colors[1] in 0..255
                && colors[2] in 0..255)
            Color(colors[0], colors[1], colors[2])
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(errorMSG)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException(errorMSG)
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException(errorMSG)
        }
    }

    /** Initializes transparencyColor */
    private fun initTransparencyColor() {
        transparencyColor
    }

    /**
     * Int representing the weight of each image in a color blend.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the input is not a number in the
     * specified range
     */
    private val weight: Int by lazy {
        println("Input the watermark transparency percentage (Integer 0-100):")
        val input = readln()
        try {
            val weight = input.toInt()
            require(weight in 0..100)
            weight
        } catch(e: NumberFormatException) {
            throw IllegalArgumentException("The transparency percentage isn't an integer number.")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("The transparency percentage is out of range.")
        }
    }

    /** Initializes weight */
    private fun initWeight() {
        weight
    }

    /**
     * PositionMethod representing the position method for the watermark.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the position method is not of the
     * accepted values
     */
    private val positionMethod: PositionMethod by lazy {
        try {
            println("Choose the position method (single, grid):")
            PositionMethod.valueOf(readln().uppercase())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("The position method input is invalid.")
        }
    }

    /** Initializes positionMethod */
    private fun initPositionMethod() {
        positionMethod
    }

    /**
     * Position representing the starting position for the watermark in the
     * single position method case.
     * Initialized through a dialog
     *
     * @throws IllegalArgumentException if the position is not of the correct format
     * or if its values are out of range
     */
    private val position: Position by lazy {
        try {
            val diffX = image.width - watermark.width
            val diffY = image.height - watermark.height
            println("Input the watermark position ([x 0-$diffX] [y 0-$diffY]):")
            val (x, y) = readln().split(" ").map(String::toInt)
            require(x in 0..diffX && y in 0..diffY)
            Position(x, y)
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("The position input is invalid.")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("The position input is out of range.")
        }
    }

    /** Initializes position */
    private fun initPosition() {
        position
    }

    /** RangeInt representing the x positions where the watermark can have effect */
    private val rangeX by lazy {
        if (positionMethod == PositionMethod.SINGLE) {
            position.x until position.x + watermark.width
        } else {
            0 until image.width
        }
    }

    /** Initializes rangeX */
    private fun initRangeX() {
        rangeX
    }

    /** RangeInt representing the y positions where the watermark can have effect */
    private val rangeY by lazy {
        if (positionMethod == PositionMethod.SINGLE) {
            position.y until position.y + watermark.height
        } else {
            0 until image.height
        }
    }

    /** Initializes rangeY */
    private fun initRangeY() {
        rangeY
    }

    /**
     * File representing the file for the resulting image.
     * Initialized through a dialog
     */
    private val outputFile: File by lazy {
        println("Input the output image filename (jpg or png extension):")
        val outputName = readln()
        if (!outputName.endsWith(".jpg") && !outputName.endsWith(".png")) {
            println("The output file extension isn't \"jpg\" or \"png\".")
        }
        File(outputName)
    }

    /** Initializes outputFile */
    private fun initOutputFile() {
        outputFile
    }

    /** BufferedImage representing the resulting image */
    private val outputImage: BufferedImage by lazy {
        BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
    }

    /** Initializes the outputImage */
    private fun initOutputImage() {
        outputImage
    }

    /**
     * Checks if the image specified is valid for blending
     *
     * @param file the file with the image
     * @param name the name to use on the standard output in case of non-validity
     *
     * @return BufferedImage representing the image that was checked
     *
     * @throws IllegalArgumentException if the file doesn't exist, if the number of
     * color components is not 3 or if the image isn't 24 or 32-bit
     */
    private fun checkImageIsValid(file: File, name: String): BufferedImage {
        if (!file.exists()) {
            throw IllegalArgumentException("The file ${file.path} doesn't exist.")
        }
        val image = ImageIO.read(file)
        if (image.colorModel.numColorComponents != 3) {
            throw IllegalArgumentException("The number of $name color components isn't 3.")
        }
        if (image.colorModel.pixelSize != 24 && image.colorModel.pixelSize != 32) {
            throw IllegalArgumentException("The $name isn't 24 or 32-bit.")
        }
        return image
    }

    /** Checks whether the image and the watermark are compatible */
    private fun checkCompatible() {
        if (image.width < watermark.width || image.height < watermark.height) {
            throw IllegalArgumentException("The watermark's dimensions are larger.Ble")
        }
    }

    /** Creates an image by watermarking the original */
    fun createImage() {
        checkCompatible()
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val imageColor = Color(image.getRGB(x, y))
                val watermarkColor = getWatermarkColor(x, y)
                val color = getNewColor(x, y, imageColor, watermarkColor)
                outputImage.setRGB(x, y, color.rgb)
            }
        }
    }

    private fun getWatermarkColor(x: Int, y: Int): Color {
        return if (positionMethod == PositionMethod.SINGLE) {
            Color(
                watermark.getRGB(
                    (x - position.x).mod(watermark.width),
                    (y - position.y).mod(watermark.height)
                ), answer1 == "YES"
            )
        } else {
            Color(
                watermark.getRGB(
                    x % watermark.width,
                    y % watermark.height
                ), answer1 == "YES"
            )
        }
    }

    private fun getNewColor(x: Int, y: Int, imageColor: Color, watermarkColor: Color): Color {
        return if (answer1 == "YES" && watermarkColor.alpha == 0) imageColor
        else if (answer2 == "YES" && watermarkColor == transparencyColor) imageColor
        else if (x !in rangeX || y !in rangeY) imageColor
        else blend(imageColor, watermarkColor, weight)
    }

    /** Saves the image created by createImage() to the output file */
    fun saveImage() {
        ImageIO.write(outputImage, outputFile.extension, outputFile)
        println("The watermarked image ${outputFile.path} has been created.")
    }

    /**
     * Blends two colors by linear combination according to a weight
     *
     * @param a the first color
     * @param b the second color
     * @param weight the weight
     *
     * @return Color: blend of the colors passed
     */
    private fun blend(a: Color, b: Color, weight: Int): Color {
        return Color(
            linear(a.red, b.red, weight),
            linear(a.green, b.green, weight),
            linear(a.blue, b.blue, weight),
        )
    }

    /**
     * Returns a linear combination of the parameters according to a weight
     *
     * @param a the first parameter. Equal to the result if w = 0
     * @param b the second parameter. Equal to the result if w = 1
     * @param w the weight of the combination
     */
    private fun linear(a: Int, b: Int, w: Int) = ((100 - w) * a + w * b) / 100

    /** Enum class representing the position methods for the watermark */
    private enum class PositionMethod {
        SINGLE, GRID
    }

    /**
     * Data class representing a position in an image
     *
     * @param x the horizontal position
     * @param y the vertical position
     */
    private data class Position(val x: Int, val y: Int)

}
