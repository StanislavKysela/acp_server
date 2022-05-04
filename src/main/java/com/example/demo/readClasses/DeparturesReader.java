package com.example.demo.readClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class DeparturesReader {

    private String title;
    private List<LinkedHashMap<String, String>> departuresList = new LinkedList<>();
    private UrlParametersEditor urlParametersEditor;
    private String error;

    public DeparturesReader(String vehicle, String from, String time, String date, String arrivals) {
//        System.out.println(from);
//        System.out.println(isTimeCorrect(time));
//        System.out.println(isDateCorrect(date));
        this.urlParametersEditor = new UrlParametersEditor();
        this.title = "";
        this.error = "";
        if (!from.equals("null")) {
            String url = createUrl(vehicle, from, time, date, arrivals);
            System.out.println(url);
            WebDriver webDriver = new HtmlUnitDriver();
            webDriver.get(url);
////            WebDriver webDriver = obtainResultsPage(from);
            scrapResults(webDriver);
        } else {
            this.title = "From not inserted";
        }

    }

    private String createUrl(String vehicle, String from, String time, String date, String arrivals) {
        String url = "https://cp.hnonline.sk/" + vehicle + "/odchody/";
        url = url + "?f=" + urlParametersEditor.editStationName(from);
        if (urlParametersEditor.isTimeCorrect(time)) {
            url = url + "&time=" + time;
        }
        if (urlParametersEditor.isDateCorrect(date)) {
            url = url + "&date=" + date;
        }
        if (!arrivals.equals("null")) {
            if (arrivals.equals("true")) {
                url = url + "&byarr=true";
            } else {
                url = url + "&byarr=false";
            }
        }
        url = url + "&submit=true";

        return url;
    }

    private void scrapResults(WebDriver webDriver) {
        //System.out.println(webDriver.getPageSource());
        try {
            this.title = webDriver.findElement(By.cssSelector(".depTitlePage")).getText();
            //System.out.println(title);

            List<WebElement> firstLinesInTable = webDriver.findElements(By.cssSelector(".dep-row-first"));
            List<WebElement> secondLinesInTable = webDriver.findElements(By.cssSelector(".dep-row-second"));
            System.out.println(firstLinesInTable);
            //System.out.println(secondLinesInTable);

            if (firstLinesInTable.size() == secondLinesInTable.size()) {
                for (int i = 0; i < firstLinesInTable.size(); i++) {
                    //System.out.println(e);
                    LinkedHashMap<String, String> departure = new LinkedHashMap<>();
                    //System.out.println(e.getAttribute("data-stationname"));
                    departure.put("direction", firstLinesInTable.get(i).getAttribute("data-stationname"));
                    departure.put("vehicle", firstLinesInTable.get(i).findElement(By.cssSelector(".departures-table__cell span.desc h3")).getText());
                    List<WebElement> firstLineElements = firstLinesInTable.get(i).findElements(By.className("departures-table__cell"));
                    departure.put("platform", firstLineElements.get(3).getText());
                    String timeDate = firstLineElements.get(2).getText();
                    String date = "";
                    String time = "";
                    if (timeDate.length() > 5) {
                        if (timeDate.charAt(4) == ' ') {
                            time = timeDate.substring(0, 4);
                            date = timeDate.substring(5);
                        } else {
                            time = timeDate.substring(0, 5);
                            date = timeDate.substring(6);
                        }
                    } else {
                        time = timeDate;
                    }
                    departure.put("time", time);
                    departure.put("date", date);

                    List<WebElement> secondLineElements = secondLinesInTable.get(i).findElements(By.className("departures-table__cell"));
                    //System.out.println(secondLineElements);
                    departure.put("via", secondLineElements.get(0).getText());
                    departure.put("operator", secondLineElements.get(1).getText());
                    departure.put("delay", secondLineElements.get(2).getText());
                    System.out.println(departure);
                    departuresList.add(departure);
                }
            }
        } catch (Exception e) {
            System.out.println("Searched page with departures results not available.");
            this.error = "No results error";
        }

    }

    public String getFrom() {
        return this.title;
    }

    public List<LinkedHashMap<String, String>> getDeparturesList() {
        return departuresList;
    }

    public String getError() {
        return error;
    }
}
