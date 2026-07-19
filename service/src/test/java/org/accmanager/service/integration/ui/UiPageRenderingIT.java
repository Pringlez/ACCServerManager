package org.accmanager.service.integration.ui;

import org.accmanager.service.integration.BaseIT;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.accmanager.service.repository.InstancesRepository;
import org.accmanager.service.repository.ConfigRepository;
import org.accmanager.service.repository.SettingsRepository;
import org.accmanager.service.repository.AssistRulesRepository;
import org.accmanager.service.entity.InstancesEntity;
import org.accmanager.service.entity.SettingsEntity;
import org.accmanager.service.entity.ConfigEntity;
import org.accmanager.service.entity.AssistRulesEntity;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.servlet.RequestDispatcher;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UiPageRenderingIT extends BaseIT {

    @Autowired
    private InstancesRepository instancesRepository;

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private AssistRulesRepository assistRulesRepository;

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
    void homePage_containsLocalServerMonitor() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Local Server Monitor")));
    }

    @Test
    void homePage_containsInstancesTable() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Local Server Name")));
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

    // ==================== FAQ ====================

    @Test
    void faqPage_returnsOk() throws Exception {
        mockMvc.perform(get("/web/faq"))
                .andExpect(status().isOk());
    }

    @Test
    void faqPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/faq"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void faqPage_containsFaqSections() throws Exception {
        mockMvc.perform(get("/web/faq"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Docker Setup")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Database")));
    }

    // ==================== SERVERS (authenticated) ====================

    @Test
    void serversPage_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk());
    }

    @Test
    void serversPage_containsInstanceTitle() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Server Instances")));
    }

    @Test
    void serversPage_containsTableHeaders() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Server Name")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Status")));
    }

    @Test
    void serversPage_containsInstanceData() throws Exception {
        mockMvc.perform(get("/web/servers").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Instance")));
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
    void configsPage_containsPresetsTitle() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Event Presets")));
    }

    @Test
    void configsPage_containsDeployButton() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Deploy Setup")));
    }

    @Test
    void configsPage_containsDarkModeAttribute() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isDarkMode", true));
    }

    @Test
    void configsPage_containsTrackTemp() throws Exception {
        mockMvc.perform(get("/web/configs").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Track Temp")));
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
    void profileDetailsPage_containsUsersAttribute() throws Exception {
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("users", org.hamcrest.Matchers.notNullValue()));
    }

    @Test
    void profileDetailsPage_whenStandardUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/web/profile/details").with(httpBasic("user-2", "wKQWuDzpCQ8cxeeDzktK")))
                .andExpect(status().isForbidden());
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

    // ==================== SERVERS POWER CONTROLS ====================

    @Test
    void serversStart_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(post("/web/servers/test-id/start")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/general/servers :: server-row"));
    }

    @Test
    void serversStop_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(post("/web/servers/test-id/stop")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/general/servers :: server-row"));
    }

    @Test
    void serversRestart_returnsOk_whenAuthenticated() throws Exception {
        mockMvc.perform(post("/web/servers/test-id/restart")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(view().name("pages/general/servers :: server-row"));
    }

    // ==================== LAYOUT CONTENT ====================

    @Test
    void homePage_containsNavigation() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Home")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Servers")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Event Presets")));
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
        mockMvc.perform(get("/web/faq"))
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

    // ==================== SERVER CONFIG EDITING ====================

    @Test
    void editServerPage_returnsOk_whenAuthenticated() throws Exception {
        Mockito.when(serverControl.inspectInstance("test-instance")).thenReturn("{\"containerStatus\" : \"stopped\"}");

        mockMvc.perform(get("/web/servers/test-instance/edit").with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRunning", false))
                .andExpect(model().attribute("instance", org.hamcrest.Matchers.notNullValue()))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Edit Server Configuration")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Instance")));
    }

    @Test
    void editServer_savesConfig_whenStopped() throws Exception {
        Mockito.when(serverControl.inspectInstance("test-instance")).thenReturn("{\"containerStatus\" : \"stopped\"}");

        mockMvc.perform(post("/web/servers/test-instance/edit")
                        .param("serverName", "My Awesome Server")
                        .param("adminPassword", "admin123")
                        .param("password", "pass123")
                        .param("spectatorPassword", "spec123")
                        .param("maxCarSlots", "40")
                        .param("tcpPort", "9232")
                        .param("udpPort", "9231")
                        .param("maxConnections", "100")
                        .param("stabilityControlLevelMax", "25")
                        .param("disableAutosteer", "1")
                        .param("disableAutoLights", "0")
                        .param("disableAutoWiper", "0")
                        .param("disableAutoEngineStart", "0")
                        .param("disableAutoPitLimiter", "0")
                        .param("disableAutoGear", "0")
                        .param("disableAutoClutch", "0")
                        .param("disableIdealLine", "0")
                        .param("track", "spa")
                        .param("preRaceWaitingTimeSeconds", "120")
                        .param("sessionOverTimeSeconds", "180")
                        .param("ambientTemp", "30")
                        .param("cloudLevel", "0.1")
                        .param("rain", "0")
                        .param("weatherRandomness", "1")
                        .param("postQualySeconds", "10")
                        .param("postRaceSeconds", "15")
                        .param("persistToDb", "false")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));

        Mockito.verify(instanceDaoService, Mockito.times(1)).writeInstanceConfiguration(Mockito.any());
    }

    @Test
    void editServer_doesNotSave_whenRunning() throws Exception {
        Mockito.when(serverControl.inspectInstance("test-instance")).thenReturn("{\"containerStatus\" : \"running\"}");

        mockMvc.perform(post("/web/servers/test-instance/edit")
                        .param("serverName", "My Awesome Server")
                        .param("adminPassword", "admin123")
                        .param("password", "pass123")
                        .param("spectatorPassword", "spec123")
                        .param("maxCarSlots", "40")
                        .param("tcpPort", "9232")
                        .param("udpPort", "9231")
                        .param("maxConnections", "100")
                        .param("stabilityControlLevelMax", "25")
                        .param("disableAutosteer", "1")
                        .param("disableAutoLights", "0")
                        .param("disableAutoWiper", "0")
                        .param("disableAutoEngineStart", "0")
                        .param("disableAutoPitLimiter", "0")
                        .param("disableAutoGear", "0")
                        .param("disableAutoClutch", "0")
                        .param("disableIdealLine", "0")
                        .param("track", "spa")
                        .param("preRaceWaitingTimeSeconds", "120")
                        .param("sessionOverTimeSeconds", "180")
                        .param("ambientTemp", "30")
                        .param("cloudLevel", "0.1")
                        .param("rain", "0")
                        .param("weatherRandomness", "1")
                        .param("postQualySeconds", "10")
                        .param("postRaceSeconds", "15")
                        .param("persistToDb", "false")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isRunning", true))
                .andExpect(model().attribute("error", org.hamcrest.Matchers.containsString("Cannot modify configuration while server is running")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cannot modify configuration while server is running")));

        Mockito.verify(instanceDaoService, Mockito.never()).writeInstanceConfiguration(Mockito.any());
    }

    @Test
    void editServer_savesToDb_whenPersistToDbIsTrue() throws Exception {
        Mockito.when(serverControl.inspectInstance("test-instance")).thenReturn("{\"containerStatus\" : \"stopped\"}");

        mockMvc.perform(post("/web/servers/test-instance/edit")
                        .param("serverName", "Database Persisted Server")
                        .param("adminPassword", "secureAdmin123")
                        .param("password", "securePass123")
                        .param("spectatorPassword", "secureSpec123")
                        .param("maxCarSlots", "35")
                        .param("tcpPort", "9332")
                        .param("udpPort", "9331")
                        .param("maxConnections", "95")
                        .param("stabilityControlLevelMax", "15")
                        .param("disableAutosteer", "1")
                        .param("disableAutoLights", "0")
                        .param("disableAutoWiper", "0")
                        .param("disableAutoEngineStart", "1")
                        .param("disableAutoPitLimiter", "0")
                        .param("disableAutoGear", "0")
                        .param("disableAutoClutch", "0")
                        .param("disableIdealLine", "1")
                        .param("track", "spa")
                        .param("preRaceWaitingTimeSeconds", "100")
                        .param("sessionOverTimeSeconds", "150")
                        .param("ambientTemp", "25")
                        .param("cloudLevel", "0.3")
                        .param("rain", "20")
                        .param("weatherRandomness", "3")
                        .param("postQualySeconds", "12")
                        .param("postRaceSeconds", "18")
                        .param("persistToDb", "true")
                        .with(httpBasic("user-1", "vxUdzhqrwt8eqQS7yszq")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/servers"));

        Mockito.verify(instanceDaoService, Mockito.times(1)).writeInstanceConfiguration(Mockito.any());

        java.util.Optional<InstancesEntity> instanceEntityOpt = instancesRepository.findById("test-instance");
        org.junit.jupiter.api.Assertions.assertTrue(instanceEntityOpt.isPresent());
        InstancesEntity instanceEntity = instanceEntityOpt.get();
        org.junit.jupiter.api.Assertions.assertEquals("Database Persisted Server", instanceEntity.getInstanceName());

        org.junit.jupiter.api.Assertions.assertNotNull(instanceEntity.getSettingsId());
        java.util.Optional<SettingsEntity> settingsEntityOpt = settingsRepository.findSettingsEntityBySettingsId(instanceEntity.getSettingsId());
        org.junit.jupiter.api.Assertions.assertTrue(settingsEntityOpt.isPresent());
        SettingsEntity settingsEntity = settingsEntityOpt.get();
        org.junit.jupiter.api.Assertions.assertEquals("Database Persisted Server", settingsEntity.getServerInstanceName());
        org.junit.jupiter.api.Assertions.assertEquals("secureAdmin123", settingsEntity.getAdminPassword());
        org.junit.jupiter.api.Assertions.assertEquals(35, settingsEntity.getMaxCarSlots());

        org.junit.jupiter.api.Assertions.assertNotNull(instanceEntity.getConfigId());
        java.util.Optional<ConfigEntity> configEntityOpt = configRepository.findConfigEntityByConfigId(instanceEntity.getConfigId());
        org.junit.jupiter.api.Assertions.assertTrue(configEntityOpt.isPresent());
        ConfigEntity configEntity = configEntityOpt.get();
        org.junit.jupiter.api.Assertions.assertEquals(9332, configEntity.getTcpPort());
        org.junit.jupiter.api.Assertions.assertEquals(9331, configEntity.getUdpPort());
        org.junit.jupiter.api.Assertions.assertEquals(95, configEntity.getMaxConnections());

        org.junit.jupiter.api.Assertions.assertNotNull(instanceEntity.getAssistRulesId());
        java.util.Optional<AssistRulesEntity> assistRulesEntityOpt = assistRulesRepository.findAssistsEntityByAssistsId(instanceEntity.getAssistRulesId());
        org.junit.jupiter.api.Assertions.assertTrue(assistRulesEntityOpt.isPresent());
        AssistRulesEntity assistRulesEntity = assistRulesEntityOpt.get();
        org.junit.jupiter.api.Assertions.assertEquals(15, assistRulesEntity.getStabilityControlLevelMax());
        org.junit.jupiter.api.Assertions.assertEquals(1, assistRulesEntity.getDisableAutoSteer());
        org.junit.jupiter.api.Assertions.assertEquals(1, assistRulesEntity.getDisableAutoEngineStart());
        org.junit.jupiter.api.Assertions.assertEquals(1, assistRulesEntity.getDisableIdealLine());
    }
}
