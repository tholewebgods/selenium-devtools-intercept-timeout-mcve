package mcve.selenium;

import static org.openqa.selenium.remote.http.Contents.utf8String;
import static org.openqa.selenium.remote.http.HttpMethod.POST;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DevtoolsInterceptTimeoutMcve {
	private static URL DRIVER_URL = null;

	static {
		try {
			DRIVER_URL = new URL("http://localhost:4444");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			tests();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	private static void tests() throws Exception {
		System.out.println("Info: run WebDriver with interceptor test");
		test();
		System.out.println("Info: sleeping 15s after test is done ...");
		// Give the "CDP Connection" thread time to run
		// into a TimeoutException.
		// This is a simpler proxy for a test runner that
		// continues running tests.
		// The time was determined empirically.
		Thread.sleep(15000);
		System.out.println("Info: exit.");
	}

	private static void test() {
		WebDriver driver = getDriver();

		try {
			Route route = Route.matching(req -> {
				return POST == req.getMethod() && req.getUri().contains("/some/path");
			})
				.to(() -> req -> new HttpResponse()
					.setStatus(200)
					.addHeader("Content-Type", "application/json")
					.addHeader("x-e2e-dev-tools-override", "mocked")
					.setContent(utf8String("{ \"response\": \"does not matter\" }")));

			NetworkInterceptor interceptor = new NetworkInterceptor(driver, route);

			System.out.println("Info: Youtube");

			driver.get("https://www.youtube.com/");

			By ytConsentDialogSel = By.cssSelector("ytd-consent-bump-v2-lightbox");

			System.out.println("Info: wait for consent dialog");

			new WebDriverWait(driver, Duration.ofSeconds(30)).until(ExpectedConditions.presenceOfElementLocated(ytConsentDialogSel));

			System.out.println("Info: dialog found");

			System.out.println("Info: close the interceptor");
			interceptor.close();
		} finally {
			System.out.println("Info: close the driver");
			driver.quit();
		}
	}

	private static WebDriver getDriver() {
		DesiredCapabilities dc = new DesiredCapabilities();

		ChromeOptions options = new ChromeOptions();
		dc.setBrowserName(options.getBrowserName());
		dc.setPlatform(Platform.fromString("LINUX"));

		System.out.println("Info: connect to grid " + DRIVER_URL);
		WebDriver driver = new RemoteWebDriver(DRIVER_URL, dc);

		// Enable BIDI
		WebDriver augmentedDriver = new Augmenter().augment(driver);

		return augmentedDriver;
	}
}
