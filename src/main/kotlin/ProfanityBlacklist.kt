import java.util.regex.Pattern

// Super simplistic and inefficient profanity filter - ideally replaced by a service
object ProfanityBlacklist {

    private val profanityList = listOf(
            "fuck",
            "shit",
            "cunt",
            "dick",
            "pussy",
            "bastard",
            "bitch",
            "wanker"
    )

    fun validate(string: String): Boolean {
        profanityList.forEach {
            val pattern = Pattern.compile(it, Pattern.CASE_INSENSITIVE)
            if(pattern.matcher(string).find()){
                return true
            }
        }
        return false
    }

}