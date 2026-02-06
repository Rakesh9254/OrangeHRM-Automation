
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class OrangeHRM1 {

	public String baseUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
	public WebDriver driver;

	@BeforeTest
	public void setup() {
		System.out.println("Before Test executed");
		// TODO Auto-generated method stub
		driver = new ChromeDriver();

		// maximise windows
		driver.manage().window().maximize();

		// open url
		driver.get(baseUrl);

		// timer i kept as 60 you can keep 40
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60)); // 60 seconds
	}

	@Test(priority = 1, enabled = true)
	public void doLoginWithInvalidCredential() throws InterruptedException {
		// find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		// find password and enter invalid password
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("1234");// wrong password

		// login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		String message_expected = "Invalid credentials";

		String message_actual = driver
				.findElement(By.xpath("//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")).getText();

		// Assert.assertTrue(message_actual.contains(message_expected));

		AssertJUnit.assertEquals(message_expected, message_actual);

		Thread.sleep(1500);
	}

	@Test(priority = 2, enabled = true)
	public void loginTestWithValidCredential() {
		// find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		// find password and enter password admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");

		// login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		// Verify if the login was successful by checking the page title
		String pageTitle = driver.getTitle();

		/*
		 * if (pageTitle.equals("OrangeHRM")) { System.out.println("Login successful!");
		 * } else { System.out.println("Login failed!"); }
		 */

		logOut();
		AssertJUnit.assertEquals("OrangeHRM", pageTitle);
	}

	@Test(priority = 3, enabled = true)
	public void addEmployee() throws InterruptedException, IOException {
		logIn();

		// Click on the PIM menu
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		// Click on Add Employee
		driver.findElement(By.xpath("//a[text()='Add Employee']")).click();

		// Enter first name
		driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys("Rakesh");

		// Enter last name
		driver.findElement(By.xpath("//input[@placeholder='Last Name']")).sendKeys("Yadav");

		////////////////////// Upload Image - For macOS //////////////////////

		// Click the image upload button (optional - depends on UI flow)
		driver.findElement(By.xpath("//button[@class='oxd-icon-button oxd-icon-button--solid-main employee-image-action']"))
		      .click();

		Thread.sleep(2000); // short wait to ensure input appears

		// Locate the hidden file input and upload image using sendKeys()
		WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));
		uploadInput.sendKeys("/Users/rakesh/Desktop/Jobs/Resume/Rakesh.jpeg");

		Thread.sleep(2000); // wait to ensure upload completes

		////////////////////////////////////////////////////////////////////////

		// Click the Save button
		driver.findElement(By.xpath("//button[normalize-space()='Save']")).click();

		// Verify if employee added successfully by checking "Personal Details" heading
		String confirmationMessage = driver.findElement(By.xpath("//h6[normalize-space()='Personal Details']")).getText();

		if (confirmationMessage.contains("Personal Details")) {
			System.out.println("Employee added successfully!");
		} else {
			System.out.println("Failed to add employee!");
		}

		logOut();
		AssertJUnit.assertEquals("Personal Details", confirmationMessage);
	}

	@Test(priority = 4, enabled = true)
	public void searchEmployeeNyName() throws InterruptedException {
	    logIn();

	    driver.findElement(By.xpath("//span[text()='PIM']")).click();
	    driver.findElement(By.xpath("//a[normalize-space()='Employee List']")).click();

	    // Enter employee name using specific locator
	    driver.findElement(By.xpath("//input[@placeholder='Employee Name']")).sendKeys("Rakesh Yadav");
	    driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

	    Thread.sleep(5000);
	    List<WebElement> elements = driver.findElements(By.xpath("//span[@class='oxd-text oxd-text--span']"));
	    String messageActual = elements.get(0).getText();
	    System.out.println(messageActual);

	    logOut();
	    AssertJUnit.assertTrue(messageActual.contains("Record Found"));
	}

	@Test(priority = 5, enabled = true)
	public void searchEmployeeById() throws InterruptedException {

		String empId = "0372";
		String message_actual = "";
		logIn();

		// find PIM Menu and click on PIM Menu
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		// Select Employee List
		driver.findElement(By.xpath("//a[normalize-space()='Employee List']")).click();

		// enter empoyee id
		driver.findElements(By.tagName("input")).get(2).sendKeys(empId);

		// Click the search button.
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

		Thread.sleep(2000);

		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("window.scrollBy(0," + 500 + ")");

		Thread.sleep(2000);

		List<WebElement> rows = driver.findElements(By.xpath("(//div[@role='row'])"));

		if (rows.size() > 1) {
			message_actual = driver.findElement(By.xpath("((//div[@role='row'])[2]/div[@role='cell'])[2]")).getText();

		}

		logOut();
		AssertJUnit.assertEquals(empId, message_actual);

	}

	@Test(priority = 6, enabled = true)
	public void fileUpload() throws IOException, InterruptedException {
	    logIn();

	    driver.findElement(By.xpath("//span[text()='PIM']")).click();
	    driver.findElement(By.xpath("//span[@class='oxd-topbar-body-nav-tab-item']")).click();
	    driver.findElement(By.partialLinkText("Data")).click();

	    // Use relative path to the file
	    String filePath = new File("src/test/resources/EmployeeData.csv").getAbsolutePath();
	    WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
	    fileInput.sendKeys(filePath);

	    Thread.sleep(3000);
	    driver.findElement(By.xpath("//button[@type='submit']")).click();

	    logOut();
	}

	public void logIn() {
		// find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		// find password and enter password admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");

		// login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

	}

	public void logOut() {
		driver.findElement(By.xpath("//p[@class='oxd-userdropdown-name']")).click();
		// driver.findElement(By.xpath("//a[normalize-space()='Logout']")).click();

		List<WebElement> elementlist = driver.findElements(By.xpath("//a[@class='oxd-userdropdown-link']"));

		/*
		 * for (int i=0; i<elementlist.size(); i++) { Thread.sleep(1000);
		 * System.out.println(i + ":" + elementlist.get(i).getText());
		 * 
		 * }
		 */

		elementlist.get(3).click();// click on logout

	}

	@Test(priority = 7, enabled = true)
	public void deleteEmployee() throws InterruptedException {
		logIn();

		// find PIM Menu and click on PIM Menu
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		// Select Employee List
		driver.findElement(By.xpath("//a[text()='Employee List']")).click();

		// enter employee name
		driver.findElements(By.tagName("input")).get(1).sendKeys("Odis");

		// driver.findElement(By.tagName("input")).get(1).sendKeys("Nesta");

		// Click the search button.
		driver.findElement(By.xpath("//button[normalize-space()='Search']")).click();

		Thread.sleep(3000);
		/////////////////// Delete/////////////////////////

		// click on delete icon of the record
		driver.findElement(By.xpath("//i[@class='oxd-icon bi-trash']")).click();

		// click on yes, delete messaage button
		driver.findElement(By.xpath(
				"//button[@class='oxd-button oxd-button--medium oxd-button--label-danger orangehrm-button-margin']"))
				.click();

		// check for message "No Record Found"
		String msg = driver.findElement(By.xpath("(//span[@class='oxd-text oxd-text--span'])[1]")).getText();

		AssertJUnit.assertEquals(msg, "No Records Found");

		Thread.sleep(5000);
		logOut();

	}

	@Test(priority = 8, enabled = true)
	public void ListEmployees() throws InterruptedException {
		logIn();
		// find PIM Menu and click on PIM Menu
		driver.findElement(By.xpath("//span[text()='PIM']")).click();

		// Select Employee List
		driver.findElement(By.xpath("//a[normalize-space()='Employee List']")).click();
		Thread.sleep(3000);

		// find total links
		List<WebElement> totalLinksElements = driver.findElements(By.xpath("//ul[@class='oxd-pagination__ul']/li"));

		int totalLinks = totalLinksElements.size();

		for (int i = 0; i < totalLinks; i++)// 0,1,2,3,
		{

			try {
				String currentLinkText = totalLinksElements.get(i).getText();

				int page = Integer.parseInt(currentLinkText);
				System.out.println("Page: " + page);

				totalLinksElements.get(i).click();

				Thread.sleep(2000);

				List<WebElement> emp_list = driver.findElements(By.xpath("//div[@class='oxd-table-card']/div /div[4]"));

				for (int j = 0; j < emp_list.size(); j++) {
					// print last name of each row
					String lastName = emp_list.get(j).getText();
					System.out.println(lastName);
				}
			} catch (Exception e) {
				System.out.println("Not a number.");
			}

		}

		Thread.sleep(5000);
		logOut();
	}

	@Test(priority = 9, enabled = false)
	public void applyLeave() throws InterruptedException {
		// find username and enter username "Admin"
		driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Admin");

		// find password and enter password admin123
		driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("admin123");

		// login button click
		driver.findElement(By.xpath("//button[@type='submit']")).submit();

		// click on leave menu
		driver.findElement(By.linkText("Leave")).click();

		// click on Apply menu
		driver.findElement(By.linkText("Apply")).click();

		// click on leave type drop down
		driver.findElement(By.xpath("//i[@class='oxd-icon bi-caret-down-fill oxd-select-text--arrow']")).click();

		// select CAN-FMLA option from leave type dropdown
		driver.findElement(By.xpath("//*[contains(text(),'CAN')]")).click();

		// enter from date
		driver.findElement(By.xpath("//div[@class='oxd-date-input']/input")).sendKeys("2024-08-04");

		// enter comment
		driver.findElement(By.tagName("textarea")).sendKeys("This is my personal leave");
		Thread.sleep(3000);

		// click on Apply button
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		Thread.sleep(5000);
		logOut();

	}

	@AfterMethod
	@AfterTest
	public void tearDown() throws InterruptedException {

		// logOut();

		Thread.sleep(5000);// wait for 5 secs before quit
		driver.close();
		driver.quit();

	}

	// https://github.com/PallaviTanpure/OrangeHRM_SeleniumPythonProject/blob/master/pageObjects/EmployeePage.py

}