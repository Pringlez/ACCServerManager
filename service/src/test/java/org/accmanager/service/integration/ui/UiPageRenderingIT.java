package org.accmanager.service.integration.ui;

import org.accmanager.service.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.servlet.RequestDispatcher;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UiPageRenderingIT extends BaseIT {

    // ==================== PUBLIC PAGES ====================

    @Test
    void homePage_returnsOk() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void homePage_containsTitle() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<title>Home</title>")));
    }

    @Test
    void homePage_containsManagedServersCard() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Managed Servers")));
    }

    @Test
    void homePage_containsFeatureCards() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Quick Setup")));
    }

    @Test
    void homePage_containsGettingStartedSection() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Getting Started")));
    }

    @Test
    void homePage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void homePage_containsNowAttribute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("now", org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void homePage_containsFooterContent() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sign Up")));
    }

    // ==================== PING ====================

    @Test
    void ping_returnsOk() throws Exception {
        mockMvc.perform(get("/web/ping"))
                .andExpect(status().isOk());
    }

    @Test
    void ping_containsText() throws Exception {
        mockMvc.perform(get("/web/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("OK")));
    }

    @Test
    void ping_withDebug_returnsDebugInfo() throws Exception {
        mockMvc.perform(get("/web/ping").param("debug", "test-value"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("test-value")));
    }

    // ==================== SIGN IN ====================

    @Test
    void signInPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk());
    }

    @Test
    void signInPage_containsForm() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sign")));
    }

    @Test
    void signInPage_containsUsernameInput() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("username")));
    }

    @Test
    void signInPage_containsPasswordInput() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("password")));
    }

    @Test
    void signInPage_containsForgotPasswordLink() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Forgotten Password")));
    }

    @Test
    void signInPage_containsSignUpLink() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/web/sign-up")));
    }

    @Test
    void signInPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void signInPage_withError_containsErrorMessage() throws Exception {
        mockMvc.perform(get("/web/sign-in").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Invalid login, check your credentials!"));
    }

    // ==================== SIGN UP ====================

    @Test
    void signUpPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk());
    }

    @Test
    void signUpPage_containsForm() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sign")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("account")));
    }

    @Test
    void signUpPage_containsUsernameInput() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("username")));
    }

    @Test
    void signUpPage_containsPasswordInputs() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"password\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"password-confirm\"")));
    }

    @Test
    void signUpPage_containsLogo() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ACC Manager")));
    }

    @Test
    void signUpPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    // ==================== FORGOT PASSWORD ====================

    @Test
    void forgotPasswordPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/forgot-password"))
                .andExpect(status().isOk());
    }

    @Test
    void forgotPasswordPage_containsForm() throws Exception {
        mockMvc.perform(get("/web/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Forgot Password")));
    }

    @Test
    void forgotPasswordPage_containsEmailInput() throws Exception {
        mockMvc.perform(get("/web/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("email")));
    }

    @Test
    void forgotPasswordPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    // ==================== PASSWORD RESET ====================

    @Test
    void passwordResetPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/password-reset").param("token", "test-token-123"))
                .andExpect(status().isOk());
    }

    @Test
    void passwordResetPage_containsForm() throws Exception {
        mockMvc.perform(get("/web/password-reset").param("token", "test-token-123"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reset Password")));
    }

    @Test
    void passwordResetPage_containsTokenInput() throws Exception {
        mockMvc.perform(get("/web/password-reset").param("token", "test-token-123"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("test-token-123")));
    }

    @Test
    void passwordResetPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/password-reset").param("token", "test-token-123"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    // ==================== CONTACT ADMIN ====================

    @Test
    void contactAdminPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/contact-admin"))
                .andExpect(status().isOk());
    }

    @Test
    void contactAdminPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/contact-admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    // ==================== SERVERS (authenticated) ====================

    @Test
    void serversPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void serversPage_containsForm() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Click To Edit")));
    }

    @Test
    void serversPage_containsContactFields() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("First Name")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Last Name")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Email")));
    }

    @Test
    void serversPage_containsDemoContactData() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bob")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Smith")));
    }

    @Test
    void serversPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void serversPage_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/servers"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== MANAGEMENT (authenticated) ====================

    @Test
    void managementPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/management").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void managementPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/management").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void managementPage_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/management"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== TEMPLATES (authenticated) ====================

    @Test
    void templatesPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/templates").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void templatesPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/templates").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void templatesPage_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/templates"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== CONFIGS (authenticated) ====================

    @Test
    void configsPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void configsPage_containsTodoList() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("To Do")));
    }

    @Test
    void configsPage_containsAddButton() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add")));
    }

    @Test
    void configsPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void configsPage_containsDemoItem() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Get Stuff Done")));
    }

    @Test
    void configsPage_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/configs"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== PROFILE (authenticated) ====================

    @Test
    void profileDetailsPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void profileDetailsPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void profileDetailsPage_containsNicknameAttribute() throws Exception {
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("nickname", "Backend Populated"));
    }

    @Test
    void profileDetailsPage_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/profile/details"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void profileData_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/profile/data").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Test
    void profileData_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/web/profile/data"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== ERROR PAGES ====================

    @Test
    void error404Page_returnsOk() throws Exception {
        mockMvc.perform(get("/web/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/errors/error-404"));
    }

    @Test
    void error404Page_containsNotFoundContent() throws Exception {
        mockMvc.perform(get("/web/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("404")));
    }

    @Test
    void error500Page_returnsOk() throws Exception {
        mockMvc.perform(get("/web/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/errors/error-500"));
    }

    @Test
    void error500Page_containsErrorContent() throws Exception {
        mockMvc.perform(get("/web/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("500")));
    }

    // ==================== LOGOUT ====================

    @Test
    void logoutPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/logout"))
                .andExpect(status().isNoContent());
    }

    // ==================== SIGN IN FORM SUBMISSION ====================

    @Test
    void doSignIn_withError_redirectsToSignInWithErrorMessage() throws Exception {
        mockMvc.perform(post("/web/do-sign-in").param("error", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/sign-in?error=true"));
    }

    // ==================== SERVERS FORM ====================

    @Test
    void serversEditForm_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(post("/web/servers/edit/1")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/general/servers-form"));
    }

    @Test
    void serversEditForm_containsSubmittedData() throws Exception {
        mockMvc.perform(post("/web/servers/edit/1")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("John")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Doe")));
    }

    @Test
    void serversEditForm_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/web/servers/edit/1"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== SERVERS COMMIT ====================

    @Test
    void serversCommit_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(post("/web/servers/commit")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq"))
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/general/servers-default"));
    }

    @Test
    void serversCommit_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(post("/web/servers/commit"))
                .andExpect(status().isUnauthorized());
    }

    // ==================== LAYOUT CONTENT ====================

    @Test
    void homePage_containsNavigation() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Home")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Servers")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Management")));
    }

    @Test
    void homePage_containsFooterTimestamp() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rendered at")));
    }

    @Test
    void signInPage_containsNavigation() throws Exception {
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("ACC Manager")));
    }

    // ==================== HTMX / WEBJARS ====================

    @Test
    void homePage_containsHtmxScript() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("htmx.min.js")));
    }

    @Test
    void homePage_containsHyperscriptScript() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("_hyperscript.js")));
    }

    @Test
    void homePage_containsTailwindCss() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("style.build.tailwind.css")));
    }

    // ==================== DARK MODE ====================

    @Test
    void allPublicPages_haveDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/sign-in"))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/sign-up"))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/forgot-password"))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/password-reset").param("token", "test"))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/contact-admin"))
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void allAuthenticatedPages_haveDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/management").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/templates").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(model().attribute("isDarkMode", true));
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(model().attribute("isDarkMode", true));
    }
}
