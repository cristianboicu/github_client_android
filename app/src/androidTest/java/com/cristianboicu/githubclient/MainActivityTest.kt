package com.cristianboicu.githubclient

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.cristianboicu.githubclient.data.repository.IDefaultRepository
import com.cristianboicu.githubclient.ui.MainActivity
import com.cristianboicu.githubclient.util.DataBindingIdlingResource
import com.cristianboicu.githubclient.util.monitorActivity
import com.cristianboicu.githubclient.utils.EspressoIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: IDefaultRepository

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun reset() = runBlocking {
        repository.deleteAll()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun appFlow() = runBlocking {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        onView(withId(R.id.et_username)).perform(ViewActions.typeText("cristianboicu"))
        onView(withId(R.id.btn_navigate_to_repos)).perform(ViewActions.click())

        onView(withId(R.id.tv_username)).check(matches(withText(
            "Cristian")))

        onView(withId(R.id.rv_repositories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_repositories)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
            hasDescendant(withText("coding_exercise_weather")),
            ViewActions.click()))

        onView(withId(R.id.tv_repository_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_repository_title)).check(matches(withText("coding_exercise_weather")))

        onView(withId(R.id.tv_repository_content)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_repository_content)).check(matches(not(withText(""))))

        activityScenario.close()
    }

}