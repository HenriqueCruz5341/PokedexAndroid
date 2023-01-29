package com.example.pokedex.utils

class Converter {
    companion object {
        fun idFromUrl(url: String): Int {
            val id = url.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().last()
            return Integer.parseInt(id)
        }

        fun beautifyName(name: String): String {
            return caseName(replaceDashWithSpace(name))
        }

        fun urlImageFromId(id: Int): String {
            return Constants.API.URL_IMAGES_POKEMON.replace("{{id}}", id.toString())
        }

        private fun caseName(name: String): String {
            return name.replaceFirstChar { it.uppercase() }
        }

        private fun replaceDashWithSpace(name: String): String {
            return name.replace("-", " ")
        }
    }
}