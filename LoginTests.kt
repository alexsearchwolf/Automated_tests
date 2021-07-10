


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginTests {

    @get:Rule
    var mainActivity: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    companion object {

        init {
            StrictMode.disableStrictMode()
        }

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        private fun getBiometricAuthentication(): BiometricAuthentication {
            return BriaGraph.biometricAuthentication
        }

        private val biometricAuth = getBiometricAuthentication().isBiometricAuthenticationPossible()

    }

    @Test
    fun test01_skipPermissions() {

        waitForViewWithText("Tweak")
        onView(allOf(withText("Tweak"), hasSibling(withText("Phone Permission")))).perform(click())
        uiDevice.findObject(UiSelector().text("ALLOW")).click()
        onView(allOf(withId(R.id.login_screen_build_version), withText("Version " + BuildConfig.VERSION_NAME))).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).perform(scrollTo()).check(matches(isDisplayed()))

    }

    @Test
    fun test02_loginScreenCheck() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(allOf(withId(R.id.login_screen_build_version), withText("Version " + BuildConfig.VERSION_NAME))).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).perform(scrollTo()).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_password_eye)).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_login)).perform(scrollTo()).check(matches(withText("LOG IN")))

        //onView(withId(R.id.login_screen_remember_login_check)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.login_screen_remember_login_check)).perform(scrollTo()).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_remember_login_label)).check(matches(withText("Remember Login")))

    }

    @Test
    fun test03_emptyStringUsernameFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).perform(scrollTo(), click(), clearText(), typeText("     "))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("Incorrect user name or password")

    }

    @Test
    fun test04_emptyUsernameFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed())).perform(click(), clearText())

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("Please enter your username")
    }

    @Test
    fun test05_specialCharactersUsernameFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed())).perform(click(), clearText(),
                typeText("c#o%&78@cpc.com"))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("Incorrect user name or password")
    }

    @Test
    fun test06_emptyPasswordFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed())).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText())

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(scrollTo(), click())

        waitForViewWithText("Please enter your password")
    }

    @Test
    fun test07_wrongUsernameFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed())).perform(click(), clearText(), typeText("w$loginUsername"))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("Incorrect user name or password")
    }

    @Test
    fun test08_noDataLogin() {

        Assume.assumeTrue(!biometricAuth)

        uiDevice.executeShellCommand("svc wifi disable")
        uiDevice.executeShellCommand("svc data disable")

        onView(withId(R.id.login_screen_username)).perform(scrollTo(), click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("No Internet connection found. Please check your Internet connection.")

        uiDevice.executeShellCommand("svc wifi enable")
        Thread.sleep(5000)
    }
    @Test
    fun test09_wrongPasswordFieldLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed())).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText("w$loginPassword"))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(click())

        waitForViewWithText("Incorrect user name or password")
    }

    @Test fun test10_airplaneModeLogin() {

        Assume.assumeTrue(!biometricAuth)
        uiDevice.openQuickSettings()
        uiDevice.findObject(UiSelector().descriptionContains("Airplane")).click()
        uiDevice.pressBack()
        uiDevice.pressBack()

        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(isDisplayed())).perform(click())

        waitForViewWithText("No Internet connection found. Please check your Internet connection.")

        uiDevice.openQuickSettings()
        uiDevice.findObject(UiSelector().descriptionContains("Airplane")).click()
        uiDevice.pressBack()
        uiDevice.pressBack()
    }

    @Test
    fun test11_enterCorrectUsernameAndPasswordAndLoginWithWifi() {

        uiDevice.executeShellCommand("svc wifi enable")
        uiDevice.executeShellCommand("svc data disable")

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_password_eye)).perform(click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_login)).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)

    }

    @Test
    fun test12_autoLogin() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithText("Phone Ready")
    }

    @Test
    fun test13_logOutAndCheckLoginScreenWifi() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithText("Phone Ready")

        uiDevice.executeShellCommand("svc wifi enable")
        uiDevice.executeShellCommand("svc data disable")

        waitForViewWithText("Phone Ready")

        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"), withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        onView(allOf(withId(R.id.login_screen_build_version), withText("Version " + BuildConfig.VERSION_NAME))).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).perform(scrollTo()).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_password_eye)).check(matches(isDisplayed())).perform(scrollTo())

    }

    @Test
    fun test14_logInWithRememberLoginAndCheckDataAfterLogOut() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_password_eye)).perform(scrollTo(), click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.login_screen_remember_login_check), withContentDescription("Remember:on"))).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)

        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"), withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        onView(withText(loginUsername)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password_eye)).perform(scrollTo(), click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

    }

    @Test
    fun test15_logInWithoutRememberLoginAndCheckDataAfterLogOut() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_password_eye)).perform(scrollTo(), click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

        onView(withContentDescription("Remember:on")).perform(scrollTo(), click())
        onView(withId(R.id.login_screen_login)).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)

        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"), withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withText(loginUsername)).check(doesNotExist())
        onView(withId(R.id.login_screen_password_eye)).perform(click())
        onView(withId(R.id.login_screen_password_eye)).check(matches(withContentDescription("Eye:on")))
        onView(withText(loginPassword)).check(doesNotExist())

    }

    @Test
    fun test16_enterCorrectUsernameAndPasswordAndLoginMobileData() {

        Assume.assumeTrue(!biometricAuth)

        uiDevice.executeShellCommand("svc wifi disable")
        uiDevice.executeShellCommand("svc data enable")

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_password_eye)).check(matches(isDisplayed())).perform(click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_login)).check(matches(isDisplayed())).perform(click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)
        waitForViewWithText("Phone Ready",30)

    }

    @Test
    fun test17_logOutAndCheckLoginScreenMobileData() {

        Assume.assumeTrue(!biometricAuth)

        waitForViewWithText("Phone Ready")

        uiDevice.executeShellCommand("svc wifi disable")
        uiDevice.executeShellCommand("svc data enable")

        waitForViewWithText("Phone Ready")
        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"), withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        onView(allOf(withId(R.id.login_screen_build_version), withText("Version " + BuildConfig.VERSION_NAME))).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).perform(scrollTo()).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_password_eye)).check(matches(isDisplayed()))

        uiDevice.executeShellCommand("svc wifi enable")
        uiDevice.executeShellCommand("svc data disable")

    }

    @Test
    fun test22_loginScreenCheck() {

        Assume.assumeTrue(biometricAuth)

        onView(allOf(withId(R.id.login_screen_build_version), withText("Version " + BuildConfig.VERSION_NAME)))
                .check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).perform(scrollTo()).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_password_eye)).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_login)).perform(scrollTo()).check(matches(withText("LOG IN")))

        onView(withId(R.id.login_screen_remember_login_check)).perform(scrollTo())
                .check(matches(withContentDescription("Remember:on")))
        onView(withId(R.id.login_screen_remember_login_label)).check(matches(withText("Remember Login")))
    }


    @Test
    fun test23_biometricAuthAfterNotSavedInfo() {

        Assume.assumeTrue(biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_password_eye)).perform(click())
        onView(withText(loginPassword)).check(matches(isDisplayed()))

        onView(withId(R.id.login_screen_remember_login_check)).perform(scrollTo(), click())
        onView(withId(R.id.login_screen_remember_login_check)).check(matches(withContentDescription("Remember:off")))

        onView(withId(R.id.login_screen_login)).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)

        // log out
        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"),
                withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        // check screen
        waitForViewWithId(R.id.login_screen_logo)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_username)).check(matches(isDisplayed()))
        onView(withText(loginUsername)).check(doesNotExist())
        onView(withId(R.id.login_screen_password)).check(matches(isDisplayed()))
        onView(withText(loginPassword)).check(doesNotExist())
        onView(withId(R.id.login_screen_remember_login_check)).check(matches(withContentDescription("Remember:off")))

    }

    @Test
    fun test24_biometricAuthAfterSavedInfo() {

        Assume.assumeTrue(biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_screen_login)).perform(scrollTo())
        onView(withId(R.id.login_screen_username)).perform(click(), clearText(), typeText(loginUsername))
        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))

        onView(withId(R.id.login_screen_remember_login_check)).perform(scrollTo(), click())
        onView(withId(R.id.login_screen_remember_login_check)).check(matches(withContentDescription("Remember:on")))
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)

        //logout
        Screen.onScreen<KNavigationScreen> { settingsButton.click() }
        waitForViewWithText("Settings [RESUMED]")
        onView(allOf(withContentDescription("More options"), withParent(hasSibling(withText("Settings [RESUMED]"))))).perform(click())
        onView(allOf(withId(R.id.title), withText("Log Out"))).perform(click())

        // check
        waitForViewWithId(R.id.login_screen_logo)
        Espresso.closeSoftKeyboard()

        onView(withText(loginUsername)).check(matches(isDisplayed()))
        onView(withId(R.id.login_screen_password)).check(matches(isDisplayed()))
        onView(withText(loginPassword)).check(doesNotExist())
        onView(withId(R.id.login_screen_remember_login_check)).check(matches(withContentDescription("Remember:on")))
        onView(withId(R.id.login_screen_login)).check(matches(withText("USE BIOMETRIC AUTH")))

    }

    @Test
    fun test25_biometricAuthAfterBg2fg() {

        Assume.assumeTrue(biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        uiDevice.pressHome()
        uiDevice.openNotification()

        uiDevice.findObject(UiSelector().text("Logged Out")).waitForExists(1000)
        uiDevice.findObject(UiSelector().text("Logged Out")).click()

        uiDevice.findObject(UiSelector().text("Login using your biometric credential")).exists()

    }

    @Test
    fun test26_biometricAuthSkipWithEnterPassword() {

        Assume.assumeTrue(biometricAuth)

        waitForViewWithId(R.id.login_screen_logo)
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_screen_login)).perform(scrollTo()).check(matches(withText("USE BIOMETRIC AUTH")))
        onView(withId(R.id.login_screen_login)).perform(click())

        //skip biometric
        uiDevice.findObject(UiSelector().text("Login using your biometric credential")).exists()
        uiDevice.findObject(UiSelector().text("USE PASSWORD")).click()

        onView(withId(R.id.login_screen_password)).perform(scrollTo(), click(), clearText(), typeText(loginPassword))
        onView(withId(R.id.login_screen_remember_login_check)).perform(scrollTo()).check(matches(withContentDescription("Remember:on")))
        onView(withId(R.id.login_screen_login)).check(matches(withText("LOG IN"))).perform(scrollTo(), click())

        waitForViewWithIdToDisappear(R.id.login_screen_progress_text, 20)

        waitForViewWithText("Phone Ready", 20)
    }

}