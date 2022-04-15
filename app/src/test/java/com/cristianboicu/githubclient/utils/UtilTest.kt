package com.cristianboicu.githubclient.utils

import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Test

class UtilTest {

    @Test
    fun formatDate_GithubFormatDate_resultFormattedDate() {
        val inputDate = "2022-02-13T21:24:30Z"
        val result = formatDate(inputDate)
        MatcherAssert.assertThat(result, Matchers.`is`("13-02-2022 21:24"))
    }

    @Test
    fun formatDate_FormattedDate_resultUnformatted() {
        val inputDate = "13-02-2022 21:24"
        val result = formatDate(inputDate)
        MatcherAssert.assertThat(result, Matchers.`is`("13-02-2022 21:24"))
    }

    @Test
    fun formatDate_EmptyDate_resultUnformatted() {
        val inputDate = ""
        val result = formatDate(inputDate)
        MatcherAssert.assertThat(result, Matchers.`is`(""))
    }

    @Test
    fun convertBooleanToPrivateOrPublic_true_returnPrivate() {
        val input = true
        val result = convertBooleanToPrivateOrPublic(input)
        MatcherAssert.assertThat(result, Matchers.`is`("private"))
    }

    @Test
    fun convertBooleanToPrivateOrPublic_false_returnPublic() {
        val input = false
        val result = convertBooleanToPrivateOrPublic(input)
        MatcherAssert.assertThat(result, Matchers.`is`("public"))
    }
}