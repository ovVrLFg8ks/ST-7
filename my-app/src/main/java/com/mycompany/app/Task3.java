package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.io.FileWriter;
import java.util.logging.Level;

public class Task3 {
    public static void getSthIdk() {
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        WebDriver driver = new ChromeDriver();

        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";

            driver.get(url);
            WebElement elem = driver.findElement(By.tagName("pre"));
            String json_str  = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(json_str);

            if (!obj.containsKey("hourly")) {
                throw new Exception("?");
            }

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temps = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            String output = " № | Время | Температура | Осадки\n";
            output +=       "---|-------|-------------|-------\n";

            for (int i = 0; i < times.size(); i++) {
                String time = ((String) times.get(i)).substring(11);
                String temp = temps.get(i).toString();
                String rain = rains.get(i).toString();

                output += String.format("%s| %s | %s°C    | %s мм\n",
                        String.format("%3s", i+1), time,
                        String.format("%6s", temp), rain);
            }

            System.out.println(output);

            try (FileWriter writer = new FileWriter("../result/forecast.txt")) {
                writer.write(output);
            }
            try (FileWriter writer = new FileWriter("../result/forecast.md")) {
                writer.write(output);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            driver.quit();
        }
    }
}