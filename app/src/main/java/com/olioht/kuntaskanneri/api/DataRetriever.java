package com.olioht.kuntaskanneri.api;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.olioht.kuntaskanneri.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataRetriever {
    private HashMap<String, String> municipalityCodes;

    public DataRetriever() {
        // Fetch municipality codes for the population data
        fetchMunicipalityCodes();
    }

    public MunicipalityData getMunicipalityData(Context context, String location) {
        // Fetch all of data for the given location
        PopulationData popData = fetchPopulationData(context, location);
        PoliticalData polData = fetchPoliticalData(context, location);
        TrafficData trafficData = fetchTrafficData(context, location);
        WeatherData weatherData = fetchWeatherData(context, location);

        if (popData == null || polData == null) {
            // Check if data fetch failed for the location, popData and polData should never be null
            Log.d("DataRetriever", "Data fetch failed. Data might not be available for " + location);
            return null;
        }
        return new MunicipalityData(location, municipalityCodes.get(location), popData, polData, trafficData, weatherData);
    }

    private WeatherData fetchWeatherData(Context context, String location) {
        // OpenWeatherMap API key
        String API_KEY = "7fff64e1f441738c518add11ec237b8c";

        // Fetch location coordinates for weather data
        JsonNode locationGeoCode = fetchJsonDataFromApi("https://api.openweathermap.org/geo/1.0/direct?q=" + location +"&limit=5&appid=" +API_KEY, null);

        if (locationGeoCode != null) {
            for (JsonNode node : locationGeoCode) {
                // Check that the location is in Finland
                if (node.get("country").asText().toUpperCase().contains("FI")) {
                    String lat = node.get("lat").asText();
                    String lon = node.get("lon").asText();

                    // Fetch weather data for the location
                    JsonNode weatherData = fetchJsonDataFromApi("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY, null);

                    if (weatherData != null) {
                        // Get all of the weather data elements from the JSON response
                        int temperature = weatherData.get("main").get("temp").asInt();
                        int temperatureFeelsLike = weatherData.get("main").get("feels_like").asInt();
                        int humidity = weatherData.get("main").get("humidity").asInt();
                        int windSpeed = weatherData.get("wind").get("speed").asInt();
                        int pressure = weatherData.get("main").get("pressure").asInt();
                        String weather = weatherData.get("weather").get(0).get("description").asText();
                        String weatherIconID = weatherData.get("weather").get(0).get("icon").asText();

                        // Rain amount is not always available for every location
                        if (weatherData.get("rain") != null) {
                            double rain = weatherData.get("rain").get("1h").asDouble();
                            return new WeatherData(temperature, temperatureFeelsLike, humidity, windSpeed, pressure, rain, weather, weatherIconID);
                        }

                        return new WeatherData(temperature, temperatureFeelsLike, humidity, windSpeed, pressure, 0.0, weather, weatherIconID);
                    }
                }
            }
        }
        return null;
    }


    private TrafficData fetchTrafficData(Context context, String location) {
        // Fetch the weather cam location id for the location, if there are multiple weather cams for the location, the first found in the api is used
        JsonNode weatherCamLocationID = fetchWeatherCamLocationID(location);

        ArrayList<byte[]> images = new ArrayList<>();
        ArrayList<String> roadNames = new ArrayList<>();

        if (weatherCamLocationID == null) {
            // no weather cam data available for location
            return null;
        } else {
            Log.d("DataRetriever:fetchTrafficData", "weatherCamLocationID:" + weatherCamLocationID);

            // Fetch weathercam specific data for location
            JsonNode weatherCamData = fetchJsonDataFromApi("https://tie.digitraffic.fi/api/weathercam/v1/stations/" + weatherCamLocationID.asText(), null);
            Log.d("DataRetriever:fetchTrafficData", "weatherCamData: " + weatherCamData);

            // Get the specific road location name of the weather cam
            String camLocationName = weatherCamData.get("properties").get("names").get("fi").asText();

            // Get the weather cam image url and cam id
            JsonNode weatherCamProperties = weatherCamData.get("properties").get("presets");

            for (JsonNode node : weatherCamProperties) {
                String camID = node.get("id").asText();
                String camName = node.get("presentationName").asText();
                String imageURL = node.get("imageUrl").asText();

                // One location can have multiple cameras, add the names of the cameras to the list
                roadNames.add(camName);

                // Fetch the image data from the image API
                try {
                    URL URL = new URL(imageURL);
                    HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "image/jpeg");

                    BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }

                    bis.close();
                    baos.close();

                    // Add the images to an array
                    images.add(baos.toByteArray());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return new TrafficData(location, images, roadNames, camLocationName);
        }
    }

    private PopulationData fetchPopulationData(Context context, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String municipalityCode = municipalityCodes.get(location);

        JsonNode jsonInputString = null;

        // Read the population query from the resources to use for the API call
        try {
            jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.populationquery));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Add the municipality code to the query
        ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(municipalityCode);

        // Fetch the population data from the API
        JsonNode rawPopulationData = fetchJsonDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px", jsonInputString);
        if (rawPopulationData == null) {
            return null;
        }

        // Parse the population data from the JSON response
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> populations = new ArrayList<>();

        for (JsonNode node : rawPopulationData.get("dimension").get("Vuosi").get("category").get("label")) {
            years.add(node.asText());
        }

        for (JsonNode node : rawPopulationData.get("value")) {
            populations.add(node.asText());
        }

        // Combine the years and populations to a LinkedHashMap to return one object to use in the app UI
        LinkedHashMap<Integer, Integer> populationData = new LinkedHashMap<Integer, Integer>();

        for (int i = 0; i < years.size(); i++) {
            populationData.put(Integer.parseInt(years.get(i)), Integer.parseInt(populations.get(i)));
        }

        return new PopulationData(populationData);
    }

    private PoliticalData fetchPoliticalData(Context context, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String municipalityCode;
        municipalityCode = municipalityCodes.get(location);

        JsonNode jsonInputStringEmploymentSuffiency = null;
        JsonNode jsonInputStringEmployment = null;

        // Query for API call to get employment suffiency data and fetch the data
        try {
            jsonInputStringEmploymentSuffiency = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentsuffiencyquery));
            ((ObjectNode) jsonInputStringEmploymentSuffiency.get("query").get(1).get("selection")).putArray("values").add(municipalityCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode rawEmploymentSuffiencyData = fetchJsonDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px", jsonInputStringEmploymentSuffiency);
        if (rawEmploymentSuffiencyData == null) {
            return null;
        }

        // Query for API call to get employment data and fetch the data
        try {
            jsonInputStringEmployment = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentquery));
            ((ObjectNode) jsonInputStringEmployment.get("query").get(0).get("selection")).putArray("values").add(municipalityCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode rawEmploymentData = fetchJsonDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px", jsonInputStringEmployment);
        if (rawEmploymentData == null) {
            System.out.println("Data fetch failed.");
            return null;
        }

        return new PoliticalData(rawEmploymentData.get("value").get(0).asDouble(), rawEmploymentSuffiencyData.get("value").get(0).asDouble());
    }

    private JsonNode fetchWeatherCamLocationID(String location) {
        JsonNode weatherCamLocations = fetchJsonDataFromApi("https://tie.digitraffic.fi/api/weathercam/v1/stations", null);

        // find the weather cam id for the location
        if (weatherCamLocations == null) {
            // no weather cam data available
            return null;
        } else {
            for (JsonNode node : weatherCamLocations.get("features")) {
                // Find the location id for the location
                if (node.get("properties").get("name").asText().toUpperCase().contains(location.toUpperCase())) {
                    return node.get("properties").get("id");
                }
            }
        }
        return null;
    }


    private JsonNode fetchJsonDataFromApi(String stringUrl, JsonNode query) {
        ObjectMapper objectMapper = new ObjectMapper();

        // For GET requests with no query
        if (query == null) {
            try {
                URL url = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }

                br.close();
                return objectMapper.readTree(response.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // For POST requests with a query
            try {
                URL url = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                byte[] input = objectMapper.writeValueAsBytes(query);
                OutputStream os = connection.getOutputStream();
                os.write(input, 0, input.length);

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                br.close();
                os.close();

                return objectMapper.readTree(response.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void fetchMunicipalityCodes() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode areas = null;

        // Fetch the municipality codes from tilastokeskus API
        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        // Parse the municipality codes from the JSON response, names and codes

        for (JsonNode node : areas.get("variables").get(1).get("valueTexts")) {
            keys.add(node.asText());
        }

        for (JsonNode node : areas.get("variables").get(1).get("values")) {
            values.add(node.asText());
        }

        municipalityCodes = new HashMap<>();

        for (int i = 0; i < keys.size(); i++) {
            municipalityCodes.put(keys.get(i), values.get(i));
        }
    }
}