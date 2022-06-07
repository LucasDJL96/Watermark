package watermark

/** Main function. Controls the flow of the program */
fun main() {
    try{
        val imageBlender = ImageBlender()
        imageBlender.init()
        imageBlender.createImage()
        imageBlender.saveImage()
    } catch (e: IllegalArgumentException) {
        println(e.message)
        return
    }

}

/**
 * Calculates the modulus of this Int between 0 and n
 *
 * @param n the number with respect to which the modulus is taken
 */
fun Int.mod(n: Int): Int =
    if (this >= 0) this % n
    else -this % n
