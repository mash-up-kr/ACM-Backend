package mashup.backend.spring.acm.domain.util

object Convert {
    /**
     * 영문자로 변환
     */
    fun toEnglish(value: String): String {
        return frenchToEnglish(value)
    }

    /**
     * 프랑스 문자를 영문자로 변환
     * à â æ À Â Æ ç Ç é è ë ê É È Ë Ê î ï Î Ï ô œ Ô Œ ù û ü Ù Û Ü
     */
    private fun frenchToEnglish(name: String): String {
        val stringBuilder = StringBuilder()
        for (c in name) {
            var t: String
            when (c) {
                'à', 'â', 'ã', 'á' -> t = "a"
                'æ' -> t = "ae"
                'À', 'Â' -> t = "A"
                'Æ' -> t = "AE"
                'ç' -> t = "c"
                'Ç' -> t = "C"
                'é', 'è', 'ë', 'ê' -> t = "e"
                'É', 'È', 'Ë', 'Ê' -> t = "E"
                'î', 'ï' -> t = "i"
                'Î', 'Ï' -> t = "I"
                'ô', 'ó' -> t = "o"
                'œ' -> t = "oe"
                'Ô' -> t = "O"
                'Œ' -> t = "OE"
                'ù', 'û', 'ü', 'ú' -> t = "u"
                'Ù', 'Û', 'Ü' -> t = "U"
                'ķ' -> t = "k"
                else -> t = c.toString()
            }
            stringBuilder.append(t)
        }
        return stringBuilder.toString()
    }
}