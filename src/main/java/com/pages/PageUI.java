package com.pages;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.testframework.Log;
import com.testframework.Utils;

public class PageUI extends Utils{

		static HashMap<String,String> testData;
		
		@FindBy(xpath = "//input[@type='text']")
		WebElement locationTextArea;
		
		@FindBy(css = ".results-container .search-bar-result")
		List<WebElement> searchList;
		
		@FindBy(css = ".cur-con-weather-card__cta")
		WebElement linkMoreDetails;
		
		@FindBy(css = ".current-weather-details .right div div")
		List<WebElement> weatherDetails;
		
		
		public PageUI(HashMap<String,String> testDatas) {
			testData=testDatas;
			PageFactory.initElements(driver, this);
		}
		
		
		public HashMap<String,String> getWeatherDetailsForCity(String city) {
			HashMap<String,String> resultFromUI = new HashMap<String,String>();
			selectCity(city);
			//For now I have added few fields...
			for(int index=0;index<weatherDetails.size();index+=2) {
				resultFromUI.put(weatherDetails.get(index).getText().trim(),weatherDetails.get(index+1).getText().trim());
				System.out.println(weatherDetails.get(index).getText().trim());
				System.out.println(weatherDetails.get(index+1).getText().trim());
			}
			
			return resultFromUI;
			}
			
			public void selectCity(String city) {
				locationTextArea.sendKeys(city);
				WebDriverWait wait = new WebDriverWait(driver,20);
				wait.until(ExpectedConditions.visibilityOfAllElements(searchList));
				for(WebElement element : searchList) {
					if(element.getText().contains(city+",")) {
						System.out.println("Selecting : " +city);
						element.click();
						break;
					}
				}
				linkMoreDetails.click();
		}
		
		
		
}
