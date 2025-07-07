package TestNG;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

public class SauceDemoTests {

    // Global variables
    String baseURL = "https://www.saucedemo.com/v1/index.html"; // Maintained original URL
    WebDriver driver;
    WebDriverWait wait;
    String actualText;
    String expectedText;
    DecimalFormat df = new DecimalFormat("0.00");

    @BeforeTest
    public void BeforeTestMethod() {

        System.out.println("[SETUP] Initializing ChromeDriver and setting up test environment ⚙️");

        // Initialize ChromeDriver
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Set implicit wait to handle general element loading
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Explicit wait for specific conditions
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Create screenshots directory if it doesn't exist
        new File("screenshots").mkdirs();

        System.out.println("[SETUP] Test environment ready ⚙️");
    }

    // Test Case 1: Login and verify hamburger menu functionality
    @Test(priority = 1)
    public void loginAndVerifyMenu() throws IOException {
        System.out.println("\n===== TC 001: Login and Hamburger Menu Verification Start =====\n");

        // Navigate to login page
        driver.get(baseURL);
        System.out.println("[NAVIGATION] Launched application: " + baseURL + " 🧭");
        waitForPageLoad();

        // Login with standard user credentials
        System.out.println("\n--- [AUTH] Starting login process \uD83D\uDEC2 ---\n");
        userLogin("standard_user", "secret_sauce");

        // Verify successful navigation to products page
        System.out.println("\n--- [VERIFICATION] Checking products page title \uD83D\uDD0D ---\n");
        verifyPageTitle("Products", "Products");

        // Test hamburger menu functionality
        System.out.println("\n--- [MENU] Testing hamburger menu functionality \uD83D\uDCC2 ---\n");
        testHamburgerMenu();

        System.out.println("\n===== TC 001: Login and Hamburger Menu Verification End =====\n");
        System.out.println("=====================================================================");
    }

    // Test Case 2: Verify product cards content
    @Test(priority = 2)
    public void verifyProductCards() throws IOException {
        System.out.println("\n===== TC 002: Product Cards Verification Start =====\n");

        // Verify contents of first product card
        System.out.println("\n--- [PRODUCT] Verifying first product card \uD83D\uDCE6 ---\n");
        verifyProductCardContents(1);

        System.out.println("\n===== TC 002: Product Cards Verification End =====\n");
        System.out.println("=====================================================================");
    }

    // Test Case 3: Add product to cart and verify
    @Test(priority = 3)
    public void addToCartAndVerify() throws IOException {
        System.out.println("\n===== TC 003: Add to Cart Verification Start =====\n");

        // Add first product to cart and verify
        System.out.println("\n--- [CART] Adding first product to cart \uD83D\uDED2 ---\n");
        addProductToCart(1);
        System.out.println("\n--- [CART] Verifying cart badge count \uD83D\uDED2 ---\n");
        verifyCartBadge(1);

        // Navigate to cart page and verify contents
        System.out.println("\n--- [NAVIGATION] Going to cart page \uD83E\uDDED ---\n");
        goToCartPage();
        System.out.println("\n--- [VERIFICATION] Checking cart page title \uD83D\uDD0D ---\n");
        verifyPageTitle("Your Cart", "Your Cart");
        System.out.println("\n--- [CART] Verifying product in cart \uD83D\uDED2 ---\n");
        verifyProductInCart(1);

        // Continue shopping and add second product
        System.out.println("\n--- [NAVIGATION] Continuing shopping \uD83E\uDDED ---\n");
        continueShopping();
        System.out.println("\n--- [VERIFICATION] Checking products page title \uD83D\uDD0D ---\n");
        verifyPageTitle("Products", "Products");

        // Add second product and verify cart updates
        System.out.println("\n--- [CART] Adding second product to cart \uD83D\uDED2 ---\n");
        addProductToCart(2);
        System.out.println("\n--- [CART] Verifying cart badge count \uD83D\uDED2 ---\n");
        verifyCartBadge(2);

        System.out.println("\n===== TC 003: Add to Cart Verification End =====\n");
        System.out.println("=====================================================================");
    }

    // Test Case 4: Complete checkout process
    @Test(priority = 4)
    public void checkoutProcess() throws IOException {
        System.out.println("\n===== TC 004: Checkout Process Start =====\n");

        // Navigate to cart and initiate checkout
        System.out.println("\n--- [NAVIGATION] Going to cart page \uD83E\uDDED ---\n");
        goToCartPage();
        System.out.println("\n--- [CHECKOUT] Initiating checkout \uD83D\uDCB3 ---\n");
        checkout();

        // Fill out checkout information
        System.out.println("\n--- [VERIFICATION] Checking checkout information page title \uD83D\uDD0D ---\n");
        verifyPageTitle("Checkout: Your Information", "Checkout: Your Information");
        System.out.println("\n--- [FORM] Filling checkout information \uD83D\uDCDD ---\n");
        fillCheckoutInfo("Thila", "De", "80000");

        // Verify order summary and complete checkout
        System.out.println("\n--- [VERIFICATION] Checking checkout overview page title \uD83D\uDD0D ---\n");
        verifyPageTitle("Checkout: Overview", "Checkout: Overview");
        System.out.println("\n--- [ORDER] Verifying order summary \uD83D\uDCC3 ---\n");
        verifyOrderSummaryDetails();
        System.out.println("\n--- [CHECKOUT] Completing checkout \uD83D\uDCB3 ---\n");
        finishCheckout();

        // Verify successful order completion
        System.out.println("\n--- [VERIFICATION] Checking order completion \uD83D\uDD0D ---\n");
        verifyOrderCompletion();

        System.out.println("\n===== TC 004: Checkout Process End =====\n");
        System.out.println("=====================================================================");
    }

    @AfterTest
    public void AfterTestMethod() {
        // Clean up by closing the browser
        if (driver != null) {
            driver.quit();
        }
    }

    // ========== Supportive Methods ==========

    // Logs in with provided credentials
    private void userLogin(String username, String password) {
        System.out.println("[LOGIN] Attempting login with username: " + username + " 🔐");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        System.out.println("[LOGIN] Login button clicked 🔐");
        waitForPageLoad();
    }

    // Verifies page title matches expected value
    private void verifyPageTitle(String expectedTitle, String pageName) throws IOException {
        System.out.println("[VERIFICATION] Checking page title for: " + pageName + " \uD83D\uDD0D");
        System.out.println("[VERIFICATION] Expected title: " + expectedTitle + " \uD83D\uDD0D");

        try {
            By titleLocator;

            // Determine appropriate locator based on page type
            if (pageName.contains("Products")) {
                titleLocator = By.className("product_label");
            }
            else if (pageName.contains("Your Cart")) {
                titleLocator = By.xpath("//*[@id=\"contents_wrapper\"]/div[2]");
            }
            else if (pageName.contains("Checkout: Your Information")) {
                titleLocator = By.xpath("//*[@id=\"contents_wrapper\"]/div[2]");
            }
            else if (pageName.contains("Checkout: Overview")) {
                titleLocator = By.xpath("//*[@id=\"contents_wrapper\"]/div[2]");
            }
            else if (pageName.contains("Finish")) {
                titleLocator = By.xpath("//*[@id=\"contents_wrapper\"]/div[2]");
            }
            else{
                // Default locator if page type not recognized
                titleLocator = By.cssSelector(".title");
            }

            // Use more reliable CSS selector for the title element
            System.out.println("[VERIFICATION] Using locator: " + titleLocator + " \uD83D\uDD0D");
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(titleLocator));
            actualText = titleElement.getText().trim();
            System.out.println("[VERIFICATION] Actual title: " + actualText + " \uD83D\uDD0D");

            if (actualText.equals(expectedTitle)) {
                System.out.println("[SUCCESS] Title verification passed ✅");
            }else{
                System.out.println("[WARNING] Title verification failed ⚠\uFE0F");
                takeScreenshot(pageName.replace(":", "") + "_TitleMismatch");
            }
        } catch (TimeoutException e) {
            System.out.println("[ERROR] Page title element not found ❌");
            takeScreenshot(pageName.replace(":", "") + "_TitleNotFound");
        }
    }

    private void testHamburgerMenu() throws IOException {
        System.out.println("[MENU] Testing hamburger menu functionality \uD83D\uDCC2");
        try {
            System.out.println("[MENU] Opening menu \uD83D\uDCC2");
            // Use ID locator which is more reliable than XPath
            WebElement menuButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("react-burger-menu-btn")));
            menuButton.click();
            System.out.println("[MENU] Hamburger menu opened successfully \uD83D\uDCC2");

            // Wait for menu animation to complete
            Thread.sleep(300); // Brief pause for menu animation

            // Verify menu items using specific class names
            System.out.println("[MENU] Verifying menu items \uD83D\uDCC2");
            List<WebElement> menuItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.cssSelector(".bm-item.menu-item")));
            System.out.println("[MENU] Found " + menuItems.size() + " menu items \uD83D\uDCC2");

            if (menuItems.size() < 4) {
                // Expected 4 menu items
                System.out.println("[WARNING] Missing menu items (expected: 4) ⚠\uFE0F");
                takeScreenshot("Menu_ItemCountMismatch");
            }

            // Close menu
            System.out.println("[MENU] Closing menu \uD83D\uDCC2");
            WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("react-burger-cross-btn")));
            closeButton.click();
            System.out.println("[MENU] Menu closed successfully \uD83D\uDCC2");

            // Verify menu is closed
            System.out.println("[MENU] Verifying menu is closed \uD83D\uDCC2");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.className("bm-menu-wrap")));
            System.out.println("[SUCCESS] Menu test completed ✅");

        } catch (TimeoutException e) {
            System.out.println("[ERROR] Menu interaction timeout ❌");
            takeScreenshot("Menu_InteractionTimeout");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Verifies contents of a product card by index
    private void verifyProductCardContents(int index) throws IOException {
        System.out.println("[PRODUCT] Verifying product card # " + index + " \uD83D\uDCE6");
        try {
            List<WebElement> products = driver.findElements(By.cssSelector(".inventory_item"));
            System.out.println("[PRODUCT] Found " + products.size() + " products \uD83D\uDCE6");

            if (products.size() >= index) {
                WebElement product = products.get(index - 1);
                String productName = product.findElement(By.cssSelector(".inventory_item_name")).getText();

                System.out.println("[PRODUCT] Checking product: " + productName + " \uD83D\uDCE6");

                // Check all required elements are present
                boolean namePresent = product.findElement(By.cssSelector(".inventory_item_name")).isDisplayed();
                boolean descPresent = product.findElement(By.cssSelector(".inventory_item_desc")).isDisplayed();
                boolean pricePresent = product.findElement(By.cssSelector(".inventory_item_price")).isDisplayed();
                boolean buttonPresent = product.findElement(By.cssSelector(".btn_inventory")).isDisplayed();
                boolean imagePresent = product.findElement(By.cssSelector(".inventory_item_img")).isDisplayed();

                if (namePresent && descPresent && pricePresent && buttonPresent && imagePresent) {
                    System.out.println("[SUCCESS] All elements present in product card ✅");
                } else {
                    System.out.println("[WARNING] Missing elements in product card ⚠\uFE0F");
                    takeScreenshot("ProductCard_" + index + "_MissingElements");
                }

                // Verify price format
                String priceText = product.findElement(By.cssSelector(".inventory_item_price")).getText();
                System.out.println("[PRODUCT] Price verification: " + priceText + " \uD83D\uDCE6");
                if (!priceText.startsWith("$")) {
                    System.out.println("[WARNING] Price format issue ⚠\uFE0F");
                    takeScreenshot("ProductCard_" + index + "_PriceFormat");
                }else{
                    System.out.println("[SUCCESS] Valid price format: " + priceText + " ✅");
                }
            }else{
                System.out.println("[ERROR] Product index out of range ❌");
                takeScreenshot("ProductCountMismatch");
            }
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Product card elements not found ❌");
            takeScreenshot("ProductCard_" + index + "_ElementNotFound");
        }
    }

    // Adds product to cart by index
    private void addProductToCart(int index) throws IOException {
        System.out.println("[CART] Adding product #" + index + " to cart \uD83D\uDED2");
        try {
            List<WebElement> products = driver.findElements(By.cssSelector(".inventory_item"));
            System.out.println("[CART] Found " + products.size() + " products \uD83D\uDED2");

            if (products.size() >= index) {
                WebElement product = products.get(index - 1);
                String productName = product.findElement(By.cssSelector(".inventory_item_name")).getText();
                System.out.println("[CART] Adding product: " + productName + " \uD83D\uDED2");

                WebElement button = product.findElement(By.cssSelector(".btn_inventory"));
                String buttonText = button.getText();
                System.out.println("[CART] Initial button state: " + buttonText + " \uD83D\uDED2");

                button.click();
                System.out.println("[CART] Add to cart button clicked \uD83D\uDED2");

                String newButtonText = button.getText();
                System.out.println("[CART] Updated button state: " + newButtonText + " \uD83D\uDED2");

                // Verify button text changed to "REMOVE"
                if (newButtonText.equals("REMOVE")) {
                    System.out.println("[SUCCESS] Product added to cart ✅");
                } else {
                    System.out.println("[WARNING] Button state not updated ⚠\uFE0F");
                    takeScreenshot("AddToCart_" + index + "_ButtonState");
                }
            } else {
                System.out.println("[ERROR] Product index out of range ❌");
                takeScreenshot("ProductIndexError");
            }
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Could not find product or button ❌");
            takeScreenshot("AddToCart_" + index + "_ElementNotFound");
        }
    }

    // Verifies cart badge shows expected item count
    private void verifyCartBadge(int expectedCount) throws IOException {
        System.out.println("[CART] Verifying cart badge count \uD83D\uDED2");
        System.out.println("[CART] Expected count: " + expectedCount + " \uD83D\uDED2");

        try {
            WebElement cartBadge = driver.findElement(By.cssSelector(".shopping_cart_badge"));
            int actualCount = Integer.parseInt(cartBadge.getText());
            System.out.println("[CART] Actual count: " + actualCount + " \uD83D\uDED2");

            if (actualCount == expectedCount) {
                System.out.println("[SUCCESS] Cart count matches ✅");
            } else {
                System.out.println("[WARNING] Cart count mismatch ⚠\uFE0F");
                takeScreenshot("CartBadge_CountMismatch");
            }
        } catch (NoSuchElementException e) {
            if (expectedCount > 0) {
                System.out.println("[ERROR] Cart badge not found ❌");
                takeScreenshot("CartBadge_NotFound");
            } else {
                System.out.println("[SUCCESS] Cart is empty as expected ✅");
            }
        }
    }

    // Navigates to cart page
    private void goToCartPage() throws IOException {
        System.out.println("[NAVIGATION] Going to cart page 🧭");

        try {
            driver.findElement(By.cssSelector(".shopping_cart_link")).click();
            System.out.println("[NAVIGATION] Cart icon clicked 🧭");
            waitForPageLoad();
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Cart icon not found ❌");
            takeScreenshot("CartIcon_NotFound");
        }
    }

    // Verifies product in cart by index
    private void verifyProductInCart(int index) throws IOException {
        System.out.println("[CART] Verifying product #" + index + " in cart \uD83D\uDED2");

        try {
            List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item"));
            System.out.println("[CART] Found " + cartItems.size() + " items in cart \uD83D\uDED2");

            if (cartItems.size() >= index) {
                WebElement cartItem = cartItems.get(index - 1);
                String productName = cartItem.findElement(By.cssSelector(".inventory_item_name")).getText();
                System.out.println("[CART] Verifying product: " + productName + " \uD83D\uDED2");

                String priceText = cartItem.findElement(By.cssSelector(".inventory_item_price")).getText();
                System.out.println("[CART] Price verification: " + priceText + " \uD83D\uDED2");

                if (!priceText.startsWith("$")) {
                    System.out.println("[WARNING] Price format issue ⚠\uFE0F");
                    takeScreenshot("CartItem_" + index + "_PriceFormat");
                }
                String buttonText = cartItem.findElement(By.cssSelector(".btn_secondary.cart_button")).getText();
                System.out.println("[CART] Button state: " + buttonText + " \uD83D\uDED2");

                if (!buttonText.equals("REMOVE")) {
                    System.out.println("[WARNING] Button state issue");
                    takeScreenshot("CartItem_" + index + "_ButtonState");
                }
            } else {
                System.out.println("[ERROR] Product not in cart ❌");
                takeScreenshot("ProductNotInCart");
            }
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Cart elements not found ❌");
            takeScreenshot("CartItem_" + index + "_VerificationFailed");
        }
    }

    // Continues shopping from cart page
    private void continueShopping() throws IOException {
        System.out.println("[NAVIGATION] Continuing shopping 🧭");

        try {
            driver.findElement(By.className("btn_secondary")).click();
            System.out.println("[NAVIGATION] Continue shopping button clicked 🧭");
            waitForPageLoad();
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Continue shopping button not found ❌");
            takeScreenshot("ContinueShopping_ButtonNotFound");
        }
    }

    // Initiates checkout from cart page
    private void checkout() throws IOException {
        System.out.println("[CHECKOUT] Initiating checkout \uD83D\uDCB3");

        try {
            driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[2]/a[2]")).click();
            System.out.println("[CHECKOUT] Checkout button clicked \uD83D\uDCB3");
            waitForPageLoad();
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Checkout button not found ❌");
            takeScreenshot("Checkout_ButtonNotFound");
        }
    }

    // Fills out checkout information
    private void fillCheckoutInfo(String firstName, String lastName, String zipCode) throws IOException {
        System.out.println("[FORM] Filling checkout information \uD83D\uDCDD");
        System.out.println("[FORM] First name: " + firstName + " \uD83D\uDCDD");
        System.out.println("[FORM] Last name: " + lastName + " \uD83D\uDCDD");
        System.out.println("[FORM] ZIP code: " + zipCode + " \uD83D\uDCDD");

        try {
            WebElement firstNameField = driver.findElement(By.id("first-name"));
            firstNameField.clear();
            firstNameField.sendKeys(firstName);
            System.out.println("[FORM] First name entered \uD83D\uDCDD");

            WebElement lastNameField = driver.findElement(By.id("last-name"));
            lastNameField.clear();
            lastNameField.sendKeys(lastName);
            System.out.println("[FORM] Last name entered \uD83D\uDCDD");

            WebElement zipCodeField = driver.findElement(By.id("postal-code"));
            zipCodeField.clear();
            zipCodeField.sendKeys(zipCode);
            System.out.println("[FORM] ZIP code entered \uD83D\uDCDD");

            System.out.println("[FORM] Submitting information \uD83D\uDCDD");
            driver.findElement(By.xpath("//*[@id=\"checkout_info_container\"]/div/form/div[2]/input")).click();
            waitForPageLoad();
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Checkout form fields not found ❌");
            takeScreenshot("CheckoutInfo_FieldNotFound");
        }
    }

    // Verifies order summary details
    private void verifyOrderSummaryDetails() throws IOException {
        System.out.println("[ORDER] Verifying order summary \uD83D\uDCC3");

        try {
            String paymentInfo = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[2]")).getText();
            System.out.println("[ORDER] Payment info: " + paymentInfo + " \uD83D\uDCC3");

            if (!paymentInfo.contains("SauceCard #31337")) {
                System.out.println("[WARNING] Payment information issue ⚠\uFE0F");
                takeScreenshot("OrderSummary_PaymentInfo");
            }

            // Verify shipping information
            String shippingInfo = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[4]")).getText();
            System.out.println("[ORDER] Shipping info: " + shippingInfo + " \uD83D\uDCC3");

            if (!shippingInfo.contains("FREE PONY EXPRESS DELIVERY!")) {
                System.out.println("[WARNING] Shipping information issue ⚠\uFE0F");
                takeScreenshot("OrderSummary_ShippingInfo");
            }

            // Verify total calculation
            String itemTotalText = driver.findElement(By.className("summary_subtotal_label")).getText();
            double itemTotal = Double.parseDouble(itemTotalText.replace("Item total: $", ""));
            System.out.println("[ORDER] Item total: $" + df.format(itemTotal) + " \uD83D\uDCC3");

            String taxText = driver.findElement(By.className("summary_tax_label")).getText();
            double tax = Double.parseDouble(taxText.replace("Tax: $", ""));
            System.out.println("[ORDER] Tax: $" + df.format(tax) + " \uD83D\uDCC3");

            String totalText = driver.findElement(By.className("summary_total_label")).getText();
            double total = Double.parseDouble(totalText.replace("Total: $", ""));
            System.out.println("[ORDER] Total: $" + df.format(total) + " \uD83D\uDCC3");

            double calculatedTotal = itemTotal + tax;
            if (Math.abs(calculatedTotal - total) > 0.01) {
                System.out.println("[WARNING] Calculation error: $" + df.format(calculatedTotal) + " vs $" + df.format(total) + " ⚠\uFE0F");
                takeScreenshot("OrderSummary_CalculationError");
            } else {
                System.out.println("[SUCCESS] Order totals match ✅");
            }
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Order summary elements not found ❌");
            takeScreenshot("OrderSummary_VerificationFailed");
        }
    }

    // Completes checkout process
    private void finishCheckout() throws IOException {
        System.out.println("[CHECKOUT] Completing order \uD83D\uDCB3");

        try {
            driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]/a[2]")).click();
            System.out.println("[CHECKOUT] Finish button clicked \uD83D\uDCB3");
            waitForPageLoad();
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Finish button not found ❌");
            takeScreenshot("FinishCheckout_ButtonNotFound");
        }
    }

    // Verifies order completion message
    private void verifyOrderCompletion() throws IOException {
        System.out.println("[ORDER] Verifying completion \uD83D\uDCC3");

        try {
            String completionMessage = driver.findElement(By.className("complete-header")).getText();
            System.out.println("[ORDER] Completion message: " + completionMessage + " \uD83D\uDCC3");

            if (completionMessage.equals("THANK YOU FOR YOUR ORDER")) {
                System.out.println("[SUCCESS] Order completed successfully ✅");
            } else {
                System.out.println("[WARNING] Completion message mismatch ⚠\uFE0F");
                takeScreenshot("OrderCompletion_MessageMismatch");
            }
        } catch (NoSuchElementException e) {
            System.out.println("[ERROR] Completion elements not found ❌");
            takeScreenshot("OrderCompletion_VerificationFailed");
        }
    }

    // Waits for page to fully load
    private void waitForPageLoad() {
        System.out.println("[SYSTEM] Waiting for page to load 🖥️");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                    d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
            System.out.println("[SYSTEM] Page loaded successfully 🖥️");
        } catch (TimeoutException e) {
            System.out.println("[WARNING] Page load timeout ⚠\uFE0F");
        }
    }

    // Takes screenshot and saves to specified location
    private void takeScreenshot(String fileName) throws IOException {
        System.out.println("[SYSTEM] Capturing screenshot: " + fileName + " 🖥️");
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File("D:\\SQA SLIIT\\SauceDemoTests\\" + fileName + ".png"));
            System.out.println("[SYSTEM] Screenshot saved successfully 🖥️");
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save screenshot ❌");
        }
    }
}