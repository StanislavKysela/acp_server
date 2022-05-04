package com.example.demo.readClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ConnectionsReader {

    private String title;
    private UrlParametersEditor urlParametersEditor;
    private List<List<LinkedHashMap<String, String>>> allConnections;
    private String lastConnectionTime;
    private String error;

    public ConnectionsReader(String vehicle, String from, String to, String via, String time, String date, String direct, String maxTransports) {
        this.urlParametersEditor = new UrlParametersEditor();
        this.allConnections = new LinkedList<>();
        this.lastConnectionTime = "null";
        this.error = "";
        if (!vehicle.equals("null") && !from.equals("null") && !to.equals("null")) {
            String url = createUrl(vehicle, from, to, via, time, date, direct, maxTransports);
            System.out.println(url);
            WebDriver webDriver = new HtmlUnitDriver();
            webDriver.get(url);
            scrapResults(webDriver);
            if (!lastConnectionTime.equals("null")) {
                System.out.println(lastConnectionTime);
            }
        }
        this.title = "sss";
    }

    private void scrapResults(WebDriver webDriver) {

        try {
            WebElement connectionList = webDriver.findElement(By.className("connection-list"));
            List<WebElement> connectionBoxes = connectionList.findElements(By.className("box connection detail-box ca-collapsed"));
            //System.out.println(connectionBoxes);

            int idx = 0;
            for (WebElement connectionBox : connectionBoxes) {
                LinkedList<LinkedHashMap<String, String>> oneConnection = new LinkedList<>(); //list, kde je ulozene jedno spojenie - cize hash s hlavickou a vsetky hashe so subspojeniami
                LinkedHashMap<String, String> connectionHeader = new LinkedHashMap<>();  //hash s hlavickou spojenia

                WebElement boxHeader = connectionBox.findElement(By.cssSelector(".date-total"));
                boxHeader = boxHeader.findElement(By.tagName("label"));
                String startTime = boxHeader.findElement(By.tagName("h2")).getText();
                if (startTime.charAt(1) == ':') {
                    startTime = startTime.substring(0, 4);
                } else {
                    startTime = startTime.substring(0, 5);
                }
                connectionHeader.put("idCon", String.valueOf(idx + 1));
                connectionHeader.put("startTime", startTime);
                if (idx + 1 == connectionBoxes.size()) {
                    this.lastConnectionTime = startTime;
                }
                connectionHeader.put("startDate", boxHeader.findElement(By.className("date-after")).getText());
                List<WebElement> totalTimeAndLengthElements = boxHeader.findElements(By.tagName("strong"));
                connectionHeader.put("totalTime", totalTimeAndLengthElements.get(0).getText());
                connectionHeader.put("totalLength", totalTimeAndLengthElements.get(1).getText());
                //System.out.println(connectionHeader);

                oneConnection.add(connectionHeader); //pridanie hlavicky spojenia do listu, kde je ulozene jedno spojenie

                WebElement subConnectionsList = connectionBox.findElement(By.className("line-item"));
                List<WebElement> subConnections = subConnectionsList.findElements(By.className("outside-of-popup"));
                //System.out.println(subConnections);
                int id = 0;
                for (WebElement subConnection : subConnections) {
                    LinkedHashMap<String, String> subConnectionFinalData = new LinkedHashMap<>();
                    id++;
                    subConnectionFinalData.put("id", String.valueOf(id));
                    subConnectionFinalData.put("detailsLink", subConnection.findElement(By.tagName("a")).getAttribute("href"));

                    String walkTime = "";
                    try {
                        walkTime = subConnection.findElement(By.className("walk walk--detail")).getText();

                    } catch (Exception e) {
                        //e.printStackTrace();
                        //System.out.println("nenaslo cas na presun");
                    }
                    subConnectionFinalData.put("walkTime", walkTime);

                    subConnectionFinalData.put("vehicleName", subConnection.findElement(By.cssSelector(".line-title h3")).getText());
                    subConnectionFinalData.put("vehicleTitle", subConnection.findElement(By.cssSelector(".line-title h3")).getAttribute("title"));
                    subConnectionFinalData.put("vehicleOwner", subConnection.findElement(By.cssSelector(".owner")).getText());

                    WebElement vehicleStationsElement = subConnection.findElement(By.cssSelector(".reset.stations"));
                    WebElement startStation = vehicleStationsElement.findElement(By.cssSelector(".item:first-child"));
                    subConnectionFinalData.put("startTime", startStation.findElement(By.cssSelector(".time")).getText());
                    startStation = startStation.findElement(By.className("station"));
                    subConnectionFinalData.put("startStation", startStation.findElement(By.className("name")).getText());
                    String platform = "";
                    try {
                        if (startStation.findElement(By.tagName("span")).findElement(By.tagName("span")).getAttribute("title").equals("nástupište")) {
                            platform = startStation.findElement(By.tagName("span")).getText();
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    subConnectionFinalData.put("startStationPlatform", platform);

                    WebElement finishStation = vehicleStationsElement.findElement(By.cssSelector(".item.last"));
                    subConnectionFinalData.put("finishTime", finishStation.findElement(By.cssSelector(".time")).getText());
                    finishStation = finishStation.findElement(By.className("station"));
                    subConnectionFinalData.put("finishStation", finishStation.findElement(By.className("name")).getText());
                    platform = "";
                    try {
                        if (finishStation.findElement(By.tagName("span")).findElement(By.tagName("span")).getAttribute("title").equals("nástupište")) {
                            platform = finishStation.findElement(By.tagName("span")).getText();
                        }
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    subConnectionFinalData.put("finishStationPlatform", platform);

                    oneConnection.add(subConnectionFinalData); //pridanie dat z jedneho subspojenia do listu, kde je ulozene cele spojenie
                    //System.out.println(subConnectionFinalData);
                }
                //System.out.println(oneConnection);
                this.allConnections.add(oneConnection);
                System.out.println(oneConnection);
                idx++;
            }
        } catch (Exception e) {
            System.out.println("Searched page with connections results not available.");
            this.error = "No results error";
        }

    }

    private String createUrl(String vehicle, String from, String to, String via, String time, String date, String direct, String maxTransports) {
        String url = "https://cp.hnonline.sk/" + vehicle + "/spojenie/?af=true&submit=true";
        url = url + "&f=" + this.urlParametersEditor.editStationName(from) + "&t=" + this.urlParametersEditor.editStationName(to);
        if (!via.equals("null")) {
            url = url + "&v=" + this.urlParametersEditor.editStationName(via);
        }
        if (this.urlParametersEditor.isTimeCorrect(time)) {
            url = url + "&time=" + time;
        }
        if (this.urlParametersEditor.isDateCorrect(date)) {
            url = url + "&date=" + date;
        }
        if (direct.equals("true")) {
            url = url + "&direct=true";
        }
        if (this.urlParametersEditor.isMaxTransportsCorrect(maxTransports)) {
            url = url + "&mch=" + maxTransports;
        }
        return url;
    }

    public String getTitle() {
        return title;
    }

    public List<List<LinkedHashMap<String, String>>> getAllConnections() {
        return allConnections;
    }

    public String getError() {
        return error;
    }
}
