package com.example.notificationreader.utils

object NamedRegex {

    private fun getOrderedGroupNames(pattern: String): List<String> {
        return Regex("""\(\?<([^>]+)>""").findAll(pattern).map {
            it.destructured.component1()
        }.toList()
    }

    fun extractNamedGroups(regex: Regex, string: String): Map<String, String>? {
        return regex.find(string)?.destructured?.toList()?.let {
            getOrderedGroupNames(regex.pattern).zip(it).toMap()
        }
    }

}