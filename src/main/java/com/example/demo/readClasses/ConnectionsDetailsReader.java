package com.example.demo.readClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ConnectionsDetailsReader {

    private String connectionName;
    private String connectionType;
    private List<LinkedHashMap<String, String>> connectionInfo;
    private String error;

    public ConnectionsDetailsReader(String url) {
        this.connectionName = "";
        this.connectionType = "";
        this.connectionInfo = new LinkedList<>();
        this.error = "";
        if (!url.equals("null")) {
            System.out.println(url);
            WebDriver webDriver = new HtmlUnitDriver();
            webDriver.get(url);
            scrapResults(webDriver);
        }
    }

    private void scrapResults(WebDriver webDriver) {
        try {
            this.connectionName = webDriver.findElement(By.cssSelector(".line-top h1")).getText();   //precitanie mena vozidla
            this.connectionType = webDriver.findElement(By.cssSelector(".line-top h1")).getAttribute("title");  //precitanie typu vozidla
            WebElement stationsListElement = webDriver.findElement(By.cssSelector(".train-detail-center"));
            stationsListElement = stationsListElement.findElement(By.className("reset line-itinerary"));
            List<WebElement> stationsListItems = stationsListElement.findElements(By.cssSelector(".item"));  //zoznam so vsetkymi zastavkami kde ide spojenie
            System.out.println(stationsListItems);
            for (WebElement stationsListItem : stationsListItems) {
                LinkedHashMap<String, String> stationInfo = new LinkedHashMap<>();     //hash pre ulozenie infa o jednej zo zastavok spojenia
                stationInfo.put("arrival", stationsListItem.findElement(By.className("arrival")).getText());   //zistenie casu prichodu na zastavku
                stationInfo.put("departure", stationsListItem.findElement(By.className("departure")).getText());  //zistenie casu odchodu zo zastavky
                stationInfo.put("stationName", stationsListItem.findElement(By.className("name")).getText());  //zistenie nazvu zastavky
                stationInfo.put("notes", stationsListItem.findElement(By.className("fixed-codes")).getText());  //zistenie poznamok
                stationInfo.put("distance", stationsListItem.findElement(By.className("distance")).getText());  //zistenie vzdialenosti, ktoru vozidlo preslo
                this.connectionInfo.add(stationInfo);
            }
        } catch (Exception e) {
            System.out.println("Searched page with connections details results not available.");
            this.error = "No results error";
        }
    }

    public String getConnectionName() {
        return connectionName;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public List<LinkedHashMap<String, String>> getConnectionInfo() {
        return connectionInfo;
    }

    public String getError() {
        return error;
    }
}
