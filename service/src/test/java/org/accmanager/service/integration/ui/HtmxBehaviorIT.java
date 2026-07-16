package org.accmanager.service.integration.ui;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.accmanager.service.integration.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(HtmxBehaviorIT.TestSecurity.class)
public class HtmxBehaviorIT extends BaseIT {

    @TestConfiguration
    static class TestSecurity {
        @Bean
        @Order(1)
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http.securityMatcher("/web/**", "/**.html")
                    .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                    .csrf(csrf -> csrf.disable())
                    .formLogin(form -> form.disable())
                    .httpBasic(httpBasic -> httpBasic.disable());
            return http.build();
        }
    }

    @Value("${local.server.port}")
    private int port;

    @org.springframework.beans.factory.annotation.Autowired
    private TestRestTemplate testRestTemplate;

    private String baseUrl;
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
    }

    @AfterEach
    void tearDown() {
        webClient.close();
    }

    private String getContent(WebResponse response) {
        return response.getContentAsString();
    }

    private String getContent(Object page) {
        if (page instanceof HtmlPage htmlPage) {
            return htmlPage.asXml();
        } else if (page instanceof com.gargoylesoftware.htmlunit.TextPage textPage) {
            return textPage.getContent();
        }
        return "";
    }

    private String postContent(String url, java.util.Map<String, String> params) {
        return postContent(url, params, java.util.Map.of());
    }

    private String postContent(String url, java.util.Map<String, String> params, java.util.Map<String, String> headers) {
        try {
            String body = params.entrySet().stream()
                    .map(e -> java.net.URLEncoder.encode(e.getKey(), java.nio.charset.StandardCharsets.UTF_8) + "=" + java.net.URLEncoder.encode(e.getValue(), java.nio.charset.StandardCharsets.UTF_8))
                    .collect(java.util.stream.Collectors.joining("&"));
            
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest.Builder requestBuilder = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(body));
            
            for (var entry : headers.entrySet()) {
                requestBuilder.header(entry.getKey(), entry.getValue());
            }
            
            java.net.http.HttpRequest request = requestBuilder.build();
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            return response.body() != null ? response.body() : "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ==================== CONFIGS PAGE - HTMX ATTRIBUTES ====================

    @Test
    void configsPage_deleteButton_hasHxDeleteAttribute() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement deleteBtn = page.getFirstByXPath("//button[contains(text(), 'Delete')]");
        assertNotNull(deleteBtn);
        assertEquals("/web/configs/delete", deleteBtn.getAttribute("hx-delete"));
    }

    @Test
    void configsPage_deleteButton_hasHxTargetClosestTr() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement deleteBtn = page.getFirstByXPath("//button[contains(text(), 'Delete')]");
        assertNotNull(deleteBtn);
        assertEquals("closest tr", deleteBtn.getAttribute("hx-target"));
    }

    @Test
    void configsPage_deleteButton_hasHxSwapOuterHTML() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement deleteBtn = page.getFirstByXPath("//button[contains(text(), 'Delete')]");
        assertNotNull(deleteBtn);
        String swap = deleteBtn.getAttribute("hx-swap");
        assertNotNull(swap);
        assertTrue(swap.contains("outerHTML"));
    }

    @Test
    void configsPage_deleteButton_hasHxConfirm() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement deleteBtn = page.getFirstByXPath("//button[contains(text(), 'Delete')]");
        assertNotNull(deleteBtn);
        assertEquals("Are you sure?", deleteBtn.getAttribute("hx-confirm"));
    }

    @Test
    void configsPage_addButton_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        assertEquals("/web/configs/create", addBtn.getAttribute("hx-post"));
    }

    @Test
    void configsPage_addButton_hasHxTargetTodoList() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        assertEquals("#todo-list", addBtn.getAttribute("hx-target"));
    }

    @Test
    void configsPage_addButton_hasHxSwapBeforeend() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        assertEquals("beforeend", addBtn.getAttribute("hx-swap"));
    }

    @Test
    void configsPage_addButton_hasHxIncludeNewTodo() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        assertEquals("#new-todo", addBtn.getAttribute("hx-include"));
    }

    @Test
    void configsPage_addButton_hasHxTriggerClick() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        assertEquals("click", addBtn.getAttribute("hx-trigger"));
    }

    // ==================== SERVERS PAGE - HTMX ATTRIBUTES ====================

    @Test
    void serversPage_formHasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("/web/servers/edit/1", form.getAttribute("hx-post"));
    }

    @Test
    void serversPage_formHasHxTargetSelf() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("this", form.getAttribute("hx-target"));
    }

    @Test
    void serversPage_formHasHxSwapOuterHTML() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("outerHTML", form.getAttribute("hx-swap"));
    }

    // ==================== SERVERS DEFAULT PAGE - HTMX ATTRIBUTES ====================

    @Test
    void serversDefaultPage_formHasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers/commit");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("/web/servers/edit/1", form.getAttribute("hx-post"));
    }

    @Test
    void serversDefaultPage_formHasHxTargetSelf() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers/commit");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("this", form.getAttribute("hx-target"));
    }

    @Test
    void serversDefaultPage_formHasHxSwapOuterHTML() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/servers/commit");
        HtmlForm form = page.getForms().get(0);
        assertNotNull(form);
        assertEquals("outerHTML", form.getAttribute("hx-swap"));
    }

    // ==================== PROFILE PAGE - HTMX ATTRIBUTES ====================

    @Test
    void profilePage_nicknameInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement nicknameInput = page.getHtmlElementById("nickname");
        assertNotNull(nicknameInput);
        assertEquals("nickname", nicknameInput.getAttribute("name"));
    }

    @Test
    void profilePage_userRoleInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement userRoleInput = page.getHtmlElementById("user-role");
        assertNotNull(userRoleInput);
        assertEquals("user-role", userRoleInput.getAttribute("name"));
        assertTrue(userRoleInput.hasAttribute("disabled"));
    }

    @Test
    void profilePage_emailInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement emailInput = page.getHtmlElementById("email");
        assertNotNull(emailInput);
        assertEquals("email", emailInput.getAttribute("name"));
        assertTrue(emailInput.hasAttribute("disabled"));
    }

    @Test
    void profilePage_submitButton_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement submitBtn = page.getFirstByXPath("//button[contains(text(), 'Save Changes')]");
        assertNotNull(submitBtn);
        assertEquals("Save Changes", submitBtn.getTextContent().trim());
    }

    // ==================== MANAGEMENT PAGE - HTMX ATTRIBUTES ====================

    @Test
    void managementPage_buttonInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("/web/management/button", buttonInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_buttonInput_hasHxTargetEvent() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("#event", buttonInput.getAttribute("hx-target"));
    }

    @Test
    void managementPage_buttonInput_hasHxTriggerClick() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("click", buttonInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_buttonInput_hasHxVals() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        String hxVals = buttonInput.getAttribute("hx-vals");
        assertNotNull(hxVals);
        assertTrue(hxVals.contains("demo-button"));
    }

    @Test
    void managementPage_checkbox_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement checkbox = page.getHtmlElementById("demo-checkbox");
        assertNotNull(checkbox);
        assertEquals("/web/management/checkbox", checkbox.getAttribute("hx-post"));
    }

    @Test
    void managementPage_checkbox_hasHxTriggerClick() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement checkbox = page.getHtmlElementById("demo-checkbox");
        assertNotNull(checkbox);
        assertEquals("click", checkbox.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_radioA_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement radioA = page.getHtmlElementById("demo-radio-a");
        assertNotNull(radioA);
        assertEquals("/web/management/radio", radioA.getAttribute("hx-post"));
    }

    @Test
    void managementPage_radioA_hasHxTriggerClick() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement radioA = page.getHtmlElementById("demo-radio-a");
        assertNotNull(radioA);
        assertEquals("click", radioA.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_radioB_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement radioB = page.getHtmlElementById("demo-radio-b");
        assertNotNull(radioB);
        assertEquals("/web/management/radio", radioB.getAttribute("hx-post"));
    }

    @Test
    void managementPage_slider_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement slider = page.getHtmlElementById("demo-slider");
        assertNotNull(slider);
        assertEquals("/web/management/slider", slider.getAttribute("hx-post"));
    }

    @Test
    void managementPage_slider_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement slider = page.getHtmlElementById("demo-slider");
        assertNotNull(slider);
        assertEquals("change", slider.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_selectSingle_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement select = page.getHtmlElementById("demo-single-select");
        assertNotNull(select);
        assertEquals("/web/management/select-single", select.getAttribute("hx-post"));
    }

    @Test
    void managementPage_selectSingle_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement select = page.getHtmlElementById("demo-single-select");
        assertNotNull(select);
        assertEquals("change", select.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_selectMultiple_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement select = page.getHtmlElementById("demo-select-multiple");
        assertNotNull(select);
        assertEquals("/web/management/select-multiple", select.getAttribute("hx-post"));
    }

    @Test
    void managementPage_selectMultiple_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement select = page.getHtmlElementById("demo-select-multiple");
        assertNotNull(select);
        assertEquals("change", select.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_dateInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement dateInput = page.getFirstByXPath("//input[@name='demo-date']");
        assertNotNull(dateInput);
        assertEquals("/web/management/date", dateInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_timeInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement timeInput = page.getFirstByXPath("//input[@name='demo-time']");
        assertNotNull(timeInput);
        assertEquals("/web/management/time", timeInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_datetimeInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement datetimeInput = page.getFirstByXPath("//input[@name='demo-date-time-local']");
        assertNotNull(datetimeInput);
        assertEquals("/web/management/datetime", datetimeInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_colorInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement colorInput = page.getHtmlElementById("demo-color");
        assertNotNull(colorInput);
        assertEquals("/web/management/color", colorInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_colorInput_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement colorInput = page.getHtmlElementById("demo-color");
        assertNotNull(colorInput);
        assertEquals("change", colorInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_fileInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement fileInput = page.getFirstByXPath("//input[@name='demo-file']");
        assertNotNull(fileInput);
        assertEquals("/web/management/file", fileInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_fileInput_hasHxEncodingMultipart() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement fileInput = page.getFirstByXPath("//input[@name='demo-file']");
        assertNotNull(fileInput);
        assertEquals("multipart/form-data", fileInput.getAttribute("hx-encoding"));
    }

    @Test
    void managementPage_fileInput_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement fileInput = page.getFirstByXPath("//input[@name='demo-file']");
        assertNotNull(fileInput);
        assertEquals("change", fileInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_numberInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement numberInput = page.getHtmlElementById("demo-number");
        assertNotNull(numberInput);
        assertEquals("/web/management/number", numberInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_numberInput_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement numberInput = page.getHtmlElementById("demo-number");
        assertNotNull(numberInput);
        assertEquals("change", numberInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_textInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement textInput = page.getHtmlElementById("demo-text-single");
        assertNotNull(textInput);
        assertEquals("/web/management/text", textInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_textInput_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement textInput = page.getHtmlElementById("demo-text-single");
        assertNotNull(textInput);
        assertEquals("change", textInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_textarea_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement textarea = page.getHtmlElementById("demo-text-multiple");
        assertNotNull(textarea);
        assertEquals("/web/management/text", textarea.getAttribute("hx-post"));
    }

    @Test
    void managementPage_textarea_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement textarea = page.getHtmlElementById("demo-text-multiple");
        assertNotNull(textarea);
        assertEquals("change", textarea.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_emailInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement emailInput = page.getHtmlElementById("demo-email");
        assertNotNull(emailInput);
        assertEquals("/web/management/text", emailInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_emailInput_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement emailInput = page.getHtmlElementById("demo-email");
        assertNotNull(emailInput);
        assertEquals("change", emailInput.getAttribute("hx-trigger"));
    }

    @Test
    void managementPage_searchInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement searchInput = page.getHtmlElementById("demo-search");
        assertNotNull(searchInput);
        assertEquals("/web/management/text", searchInput.getAttribute("hx-post"));
    }

    @Test
    void managementPage_searchInput_hasComplexTrigger() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement searchInput = page.getHtmlElementById("demo-search");
        assertNotNull(searchInput);
        String trigger = searchInput.getAttribute("hx-trigger");
        assertNotNull(trigger);
        assertTrue(trigger.contains("keyup"));
        assertTrue(trigger.contains("changed"));
        assertTrue(trigger.contains("delay:500ms"));
    }

    @Test
    void managementPage_resetButton_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement resetBtn = page.getFirstByXPath("//input[@type='reset']");
        assertNotNull(resetBtn);
        assertEquals("/web/management/reset", resetBtn.getAttribute("hx-post"));
    }

    @Test
    void managementPage_submitButton_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement submitBtn = page.getFirstByXPath("//input[@type='submit']");
        assertNotNull(submitBtn);
        assertEquals("/web/management/submit", submitBtn.getAttribute("hx-post"));
    }

    // ==================== TEMPLATES PAGE - HTMX ATTRIBUTES ====================

    @Test
    void templatesPage_buttonInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("/web/templates/button", buttonInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_buttonInput_hasHxTargetEvent() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("#event", buttonInput.getAttribute("hx-target"));
    }

    @Test
    void templatesPage_buttonInput_hasHxTriggerClick() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement buttonInput = page.getHtmlElementById("demo-button");
        assertNotNull(buttonInput);
        assertEquals("click", buttonInput.getAttribute("hx-trigger"));
    }

    @Test
    void templatesPage_checkbox_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement checkbox = page.getHtmlElementById("demo-checkbox");
        assertNotNull(checkbox);
        assertEquals("/web/templates/checkbox", checkbox.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_radioA_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement radioA = page.getHtmlElementById("demo-radio-a");
        assertNotNull(radioA);
        assertEquals("/web/templates/radio", radioA.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_slider_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement slider = page.getHtmlElementById("demo-slider");
        assertNotNull(slider);
        assertEquals("/web/templates/slider", slider.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_slider_hasHxTriggerChange() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement slider = page.getHtmlElementById("demo-slider");
        assertNotNull(slider);
        assertEquals("change", slider.getAttribute("hx-trigger"));
    }

    @Test
    void templatesPage_selectSingle_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement select = page.getHtmlElementById("demo-single-select");
        assertNotNull(select);
        assertEquals("/web/templates/select-single", select.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_selectMultiple_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement select = page.getHtmlElementById("demo-select-multiple");
        assertNotNull(select);
        assertEquals("/web/templates/select-multiple", select.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_dateInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement dateInput = page.getFirstByXPath("//input[@name='demo-date']");
        assertNotNull(dateInput);
        assertEquals("/web/templates/date", dateInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_timeInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement timeInput = page.getFirstByXPath("//input[@name='demo-time']");
        assertNotNull(timeInput);
        assertEquals("/web/templates/time", timeInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_datetimeInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement datetimeInput = page.getFirstByXPath("//input[@name='demo-date-time-local']");
        assertNotNull(datetimeInput);
        assertEquals("/web/templates/datetime", datetimeInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_colorInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement colorInput = page.getHtmlElementById("demo-color");
        assertNotNull(colorInput);
        assertEquals("/web/templates/color", colorInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_fileInput_hasHxEncodingMultipart() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement fileInput = page.getFirstByXPath("//input[@name='demo-file']");
        assertNotNull(fileInput);
        assertEquals("multipart/form-data", fileInput.getAttribute("hx-encoding"));
    }

    @Test
    void templatesPage_numberInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement numberInput = page.getHtmlElementById("demo-number");
        assertNotNull(numberInput);
        assertEquals("/web/templates/number", numberInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_textInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement textInput = page.getHtmlElementById("demo-text-single");
        assertNotNull(textInput);
        assertEquals("/web/templates/text", textInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_textarea_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement textarea = page.getHtmlElementById("demo-text-multiple");
        assertNotNull(textarea);
        assertEquals("/web/templates/text", textarea.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_emailInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement emailInput = page.getHtmlElementById("demo-email");
        assertNotNull(emailInput);
        assertEquals("/web/templates/text", emailInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_searchInput_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement searchInput = page.getHtmlElementById("demo-search");
        assertNotNull(searchInput);
        assertEquals("/web/templates/text", searchInput.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_searchInput_hasComplexTrigger() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement searchInput = page.getHtmlElementById("demo-search");
        assertNotNull(searchInput);
        String trigger = searchInput.getAttribute("hx-trigger");
        assertNotNull(trigger);
        assertTrue(trigger.contains("keyup"));
        assertTrue(trigger.contains("changed"));
        assertTrue(trigger.contains("delay:500ms"));
    }

    @Test
    void templatesPage_resetButton_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement resetBtn = page.getFirstByXPath("//input[@type='reset']");
        assertNotNull(resetBtn);
        assertEquals("/web/templates/reset", resetBtn.getAttribute("hx-post"));
    }

    @Test
    void templatesPage_submitButton_hasHxPost() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/templates");
        HtmlElement submitBtn = page.getFirstByXPath("//input[@type='submit']");
        assertNotNull(submitBtn);
        assertEquals("/web/templates/submit", submitBtn.getAttribute("hx-post"));
    }

    // ==================== HTMX ENDPOINT RESPONSES ====================

    @Test
    void managementButtonEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/button", java.util.Map.of("demo-button", "Test Button"));
        assertTrue(content.contains("Button"));
        assertTrue(content.contains("Test Button"));
        assertTrue(content.contains("clicked"));
    }

    @Test
    void managementCheckboxEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/checkbox", java.util.Map.of("checkbox", "demo-checkbox-1"));
        assertTrue(content.contains("Checkbox"));
        assertTrue(content.contains("demo-checkbox-1"));
    }

    @Test
    void managementRadioEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/radio", java.util.Map.of("demo-radio", "option-a"));
        assertTrue(content.contains("Radio"));
        assertTrue(content.contains("option-a"));
        assertTrue(content.contains("selected"));
    }

    @Test
    void managementSliderEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/slider", java.util.Map.of("demo-range", "50"));
        assertTrue(content.contains("Slider"));
        assertTrue(content.contains("50"));
    }

    @Test
    void managementSelectSingleEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/select-single", java.util.Map.of("demo-select-single", "option-1"));
        assertTrue(content.contains("Selected"));
        assertTrue(content.contains("option-1"));
    }

    @Test
    void managementSelectMultipleEndpoint_returnsPElement() throws Exception {
        java.util.LinkedHashMap<String, String> params = new java.util.LinkedHashMap<>();
        params.put("demo-select-multiple", "option-1");
        params.put("demo-select-multiple", "option-2");
        String content = postContent(baseUrl + "/web/management/select-multiple", params);
        assertTrue(content.contains("Selected"));
    }

    @Test
    void managementDateEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/date", java.util.Map.of("demo-date", "2024-06-15"));
        assertTrue(content.contains("Selected"));
        assertTrue(content.contains("2024-06-15"));
    }

    @Test
    void managementTimeEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/time", java.util.Map.of("demo-time", "14:30:00"));
        assertTrue(content.contains("Selected"));
        assertTrue(content.contains("14:30"));
    }

    @Test
    void managementDatetimeEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/datetime", java.util.Map.of("demo-date-time-local", "2024-06-15T14:30"));
        assertTrue(content.contains("Selected"));
    }

    @Test
    void managementColorEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/color", java.util.Map.of("demo-color", "#ff0000"));
        assertTrue(content.contains("Hex"));
        assertTrue(content.contains("#ff0000"));
        assertTrue(content.contains("RGB"));
    }

    @Test
    void managementNumberEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/number", java.util.Map.of("demo-number", "42"));
        assertTrue(content.contains("Number"));
        assertTrue(content.contains("42"));
    }

    @Test
    void managementTextEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/text", java.util.Map.of("demo-single-string", "hello world"), java.util.Map.of("HX-Trigger-Name", "demo-single-string"));
        assertTrue(content.contains("hello world"));
    }

    @Test
    void managementResetEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/reset", java.util.Map.of());
        assertTrue(content.contains("Form reset"));
    }

    @Test
    void managementSubmitEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/management/submit", java.util.Map.of("demo-single-string", "value1"));
        assertTrue(content.contains("Form submitted"));
    }

    // ==================== TEMPLATES ENDPOINT RESPONSES ====================

    @Test
    void templatesButtonEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/button", java.util.Map.of("demo-button", "Test Button"));
        assertTrue(content.contains("Button"));
        assertTrue(content.contains("Test Button"));
        assertTrue(content.contains("clicked"));
    }

    @Test
    void templatesCheckboxEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/checkbox", java.util.Map.of("checkbox", "demo-checkbox-1"));
        assertTrue(content.contains("Checkbox"));
    }

    @Test
    void templatesRadioEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/radio", java.util.Map.of("demo-radio", "option-b"));
        assertTrue(content.contains("Radio"));
        assertTrue(content.contains("option-b"));
        assertTrue(content.contains("selected"));
    }

    @Test
    void templatesSliderEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/slider", java.util.Map.of("demo-range", "75"));
        assertTrue(content.contains("Slider"));
        assertTrue(content.contains("75"));
    }

    @Test
    void templatesSelectSingleEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/select-single", java.util.Map.of("demo-select-single", "option-2"));
        assertTrue(content.contains("Selected"));
    }

    @Test
    void templatesSelectMultipleEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/select-multiple", java.util.Map.of("demo-select-multiple", "option-1"));
        assertTrue(content.contains("Selected"));
    }

    @Test
    void templatesDateEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/date", java.util.Map.of("demo-date", "2024-12-25"));
        assertTrue(content.contains("Selected"));
        assertTrue(content.contains("2024-12-25"));
    }

    @Test
    void templatesTimeEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/time", java.util.Map.of("demo-time", "09:00:00"));
        assertTrue(content.contains("Selected"));
    }

    @Test
    void templatesDatetimeEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/datetime", java.util.Map.of("demo-date-time-local", "2024-12-25T10:00"));
        assertTrue(content.contains("Selected"));
    }

    @Test
    void templatesColorEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/color", java.util.Map.of("demo-color", "#00ff00"));
        assertTrue(content.contains("Hex"));
        assertTrue(content.contains("RGB"));
    }

    @Test
    void templatesNumberEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/number", java.util.Map.of("demo-number", "100"));
        assertTrue(content.contains("Number"));
        assertTrue(content.contains("100"));
    }

    @Test
    void templatesTextEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/text", java.util.Map.of("demo-single-string", "test value"), java.util.Map.of("HX-Trigger-Name", "demo-single-string"));
        assertTrue(content.contains("test value"));
    }

    @Test
    void templatesResetEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/reset", java.util.Map.of());
        assertTrue(content.contains("Form reset"));
    }

    @Test
    void templatesSubmitEndpoint_returnsPElement() throws Exception {
        String content = postContent(baseUrl + "/web/templates/submit", java.util.Map.of("demo-single-string", "form-value"));
        assertTrue(content.contains("Form submitted"));
    }

    // ==================== CONFIGS ENDPOINT RESPONSES ====================

    @Test
    void configsCreateEndpoint_returnsFragment() throws Exception {
        String content = postContent(baseUrl + "/web/configs/create", java.util.Map.of("new-todo", "Test Task"));
        assertTrue(content.contains("Test Task"));
    }

    @Test
    void configsDeleteEndpoint_returnsEmpty() throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(baseUrl + "/web/configs/delete"))
                .DELETE()
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    // ==================== PROFILE ENDPOINT RESPONSES ====================

    @Test
    void profileDataEndpoint_returnsHtmlWithDate() throws Exception {
        Object response = webClient.getPage(baseUrl + "/web/profile/data");
        String content = getContent(response);
        assertTrue(content.contains("hi!"));
    }

    // ==================== SERVERS ENDPOINT RESPONSES ====================

    @Test
    void serversEditEndpoint_returnsFormView() throws Exception {
        String content = postContent(baseUrl + "/web/servers/edit/1", java.util.Map.of("firstName", "John", "lastName", "Doe", "email", "john@example.com"));
        assertTrue(content.contains("John"));
        assertTrue(content.contains("Doe"));
    }

    @Test
    void serversCommitEndpoint_returnsDefaultView() throws Exception {
        String content = postContent(baseUrl + "/web/servers/commit", java.util.Map.of("firstName", "Jane", "lastName", "Smith", "email", "jane@example.com"));
        assertTrue(content.contains("Jane"));
        assertTrue(content.contains("Smith"));
    }

    // ==================== HYPERSCRIPT ====================

    @Test
    void managementPage_burgerMenu_hasHyperscript() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement burgerBtn = page.getFirstByXPath("//button[contains(@class, 'p-2')]");
        assertNotNull(burgerBtn);
        String hyperscript = burgerBtn.getAttribute("_");
        assertNotNull(hyperscript);
        assertTrue(hyperscript.contains("click"));
        assertTrue(hyperscript.contains("toggle"));
        assertTrue(hyperscript.contains("navbar-default"));
    }

    @Test
    void configsPage_addButton_hasHyperscript() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement addBtn = page.getHtmlElementById("mon");
        assertNotNull(addBtn);
        String hyperscript = addBtn.getAttribute("_");
        assertNotNull(hyperscript);
        assertTrue(hyperscript.contains("htmx:afterRequest"));
    }

    @Test
    void managementPage_eventDiv_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/management");
        HtmlElement eventDiv = page.getHtmlElementById("event");
        assertNotNull(eventDiv);
    }

    @Test
    void managementPage_todoListDiv_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement todoList = page.getHtmlElementById("todo-list");
        assertNotNull(todoList);
    }

    @Test
    void managementPage_newTodoInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/configs");
        HtmlElement newTodo = page.getHtmlElementById("new-todo");
        assertNotNull(newTodo);
    }

    @Test
    void managementPage_nicknameInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement nicknameInput = page.getHtmlElementById("nickname");
        assertNotNull(nicknameInput);
    }

    @Test
    void managementPage_userRoleInput_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement userRoleInput = page.getHtmlElementById("user-role");
        assertNotNull(userRoleInput);
    }

    @Test
    void managementPage_submitButton_exists() throws Exception {
        HtmlPage page = webClient.getPage(baseUrl + "/web/profile/details");
        HtmlElement submitBtn = page.getFirstByXPath("//button[contains(text(), 'Save Changes')]");
        assertNotNull(submitBtn);
    }
}
