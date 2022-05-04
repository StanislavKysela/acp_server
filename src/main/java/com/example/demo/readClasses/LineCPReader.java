package com.example.demo.readClasses;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.LinkedList;
import java.util.List;

public class LineCPReader {

    private UrlParametersEditor urlParametersEditor;
    private String title;
    private String date;
    private List<List<String>> lineStations;
    private List<String> departuresFromStation;
    private List<String> notes;
    private String error;

    public LineCPReader(String city, String line, String from, String to, String date) {
        this.urlParametersEditor = new UrlParametersEditor();
        this.date = "";
        this.lineStations = new LinkedList<>();
        this.departuresFromStation = new LinkedList<>();
        this.notes = new LinkedList<>();
        this.error = "";

        if ((!city.equals("null")) && (!line.equals("null"))) {
            String url = createUrl(city, line, from, to, date);
            WebDriver webDriver = new HtmlUnitDriver();
            webDriver.get(url);
            scrapResults(webDriver);
        } else {
            this.title = "line or from not inserted";
        }
    }

    private void scrapResults(WebDriver webDriver) {
        try {
            this.title = webDriver.findElement(By.cssSelector(".departures__title")).getText();  //ziskanie nazvu

            WebElement stationTable = webDriver.findElement(By.cssSelector(".zjr-table > tbody"));  //spracovanie lavej tabulky so zastavkami
            List<WebElement> linesOfStationsTable = stationTable.findElements(By.tagName("tr"));  //riadky z lavej tabulky
            for (WebElement element : linesOfStationsTable) {
                List<String> oneLineOfStationsOfLine = new LinkedList<>();
                oneLineOfStationsOfLine.add(element.findElement(By.className("zjr-table__time right valign-top bold")).getText());
                String stationName = (element.findElement(By.className("zjr-table__station_name")).getText());
                if (stationName.charAt(0) == ';') {  //odstranenie "; " zo zaciatku nazvu zastavok, na ktorych sa aktualne nenachadzame
                    try {
                        stationName = stationName.substring(2);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace(); //TODO: mozno este odstran
                        stationName = "";
                    }
                }
                oneLineOfStationsOfLine.add(stationName);
                try {                                                 //skuska ci je tam aj tarifa
                    oneLineOfStationsOfLine.add(element.findElement(By.className("tarif")).getText());
                } catch (NoSuchElementException e) {
                    //e.printStackTrace();
                    //System.out.println("nenaslo tarifu");
                }
                lineStations.add(oneLineOfStationsOfLine);
            }
            System.out.println(lineStations);


            WebElement tableContainerOfDepartureTimes = webDriver.findElement(By.cssSelector(".zjr-tables-wrap"));  //spracovanie pravej tabulky s casmi odchodov
            System.out.println(tableContainerOfDepartureTimes);
            List<WebElement> tablesOfDepartureTimes = tableContainerOfDepartureTimes.findElements(By.cssSelector(".zjr-table"));   //list tabuliek (moze ich byt viac v zobrazeni wholeweek)
            for (WebElement element : tablesOfDepartureTimes) {  //TODO: opravit aby nebralo wholeweek - cize het for asi
                String oneLineDeparture = "";
                //LinkedHashMap<String, String> lineDeparturesTable = new LinkedHashMap<>();
                WebElement tableHeader = element.findElement(By.tagName("thead"));
                date = tableHeader.findElement(By.cssSelector(".zjr-table tr th:last-child")).getText();  //nazov tabulky - rozsah dni

                WebElement tableBody = element.findElement(By.tagName("tbody"));
                List<WebElement> tableOfDepartureTimesRows = tableBody.findElements(By.tagName("tr"));
                for (WebElement departureTimesRow : tableOfDepartureTimesRows) {
                    oneLineDeparture = departureTimesRow.findElement(By.cssSelector(".zjr-table tr td:first-child")).getText() + ": " + departureTimesRow.findElement(By.cssSelector(".zjr-table tr td:last-child")).getText();
                    //lineDeparturesTable.put(departureTimesRow.findElement(By.cssSelector(".zjr-table tr td:first-child")).getText(),   //hodina odchodu
                    //                        departureTimesRow.findElement(By.cssSelector(".zjr-table tr td:last-child")).getText());   //minuty odchodu
                    this.departuresFromStation.add(oneLineDeparture);
                }
            }
            System.out.println(this.departuresFromStation);

            try {                                                           //spracovanie poznamok - ak su
                WebElement notesMainElement = webDriver.findElement(By.cssSelector(".messages > li ul"));
                List<WebElement> notesElements = notesMainElement.findElements(By.tagName("li"));
                for (WebElement noteElement : notesElements) {
                    this.notes.add(noteElement.getText());
                }
                System.out.println(this.notes);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("there are no notes");
            }
        } catch (Exception e) {
            System.out.println("Searched page with lines results not available.");
            this.error = "No results error";
        }
    }

    private String createUrl(String city, String line, String from, String to, String date) {
        String url = "https://cp.hnonline.sk/";
        url = url + city + "/zcp/?l=" + line;
        if (!from.equals("null")) {
            url = url + "&f=" + urlParametersEditor.editStationName(from);
        }
        if (!to.equals("null")) {
            url = url + "&t=" + urlParametersEditor.editStationName(to);
        }
        if (urlParametersEditor.isDateCorrect(date)) {
            url = url + "&date=" + date;
        }
        url = url + "&submit=true";
        System.out.println(url);
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public List<List<String>> getLineStations() {
        return lineStations;
    }

    public List<String> getDeparturesFromStation() {
        return departuresFromStation;
    }

    public List<String> getNotes() {
        return notes;
    }

    public String getError() {
        return error;
    }
}
