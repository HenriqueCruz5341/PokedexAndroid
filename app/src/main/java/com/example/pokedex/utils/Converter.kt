package com.example.pokedex.utils

/**
 * Converter class used to convert some data in the app.
 *
 * This class is used to convert some data in the app with no need to create a new instance of the class.
 * This class is used to avoid the repetition of code.
 */
class Converter {
    companion object {

        /**
         * This function is used to get the id from the url
         *
         * @param url The url of the pokemon
         * @return The id of the pokemon
         */
        fun idFromUrl(url: String): Int {
            val id = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().last()
            return Integer.parseInt(id)
        }

        /**
         * This function is used to beautify the name of the pokemon
         *
         * @param name Some resource name
         * @return The beautified resource name
         */
        fun beautifyName(name: String): String {
            return caseName(replaceDashWithSpace(name))
        }

        /**
         * This function is used to get the url of the image of the pokemon from the id
         *
         * @param id The pokemon id
         * @return The pokemon url image
         */
        fun urlImageFromId(id: Int): String {
            return Constants.API.URL_IMAGES_POKEMON.replace("{{id}}", id.toString())
        }

        /**
         * This function is used to uppercase the first letter of the name
         *
         * @param name Some resource name
         * @return The resource name with the first letter uppercase
         */
        private fun caseName(name: String): String {
            return name.replaceFirstChar { it.uppercase() }
        }

        /**
         * This function is used to replace the dash with a space
         *
         * @param name Some name with a dash
         * @return The name with the dash replaced with a space
         */
        private fun replaceDashWithSpace(name: String): String {
            return name.replace("-", " ")
        }
    }
}