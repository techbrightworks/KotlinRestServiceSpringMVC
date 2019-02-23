package org.srinivas.siteworks.exception


class CalculateException : Exception {

    /**
     * Instantiates a new calculate exception.
     *
     * @param message the message
     * @param t the t
     */
    constructor(message: String, t: Throwable) : super(message, t) {}

    /**
     * Instantiates a new calculate exception.
     *
     * @param message the message
     */
    constructor(message: String) : super(message) {}

    companion object {

        private val serialVersionUID = 1L
    }

}
