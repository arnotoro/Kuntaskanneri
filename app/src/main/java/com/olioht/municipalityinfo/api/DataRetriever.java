package com.olioht.municipalityinfo.api;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.olioht.municipalityinfo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DataRetriever {
    private HashMap<String, String> municipalityCodes;

    public DataRetriever() {
        fetchMunicipalityCodes();
    }

    public MunicipalityData getMunicipalityData(Context context, String location) {
        PopulationData popData = fetchPopulationData(context, location);
        PoliticalData polData = fetchPoliticalData(context, location);

        return new MunicipalityData(location, municipalityCodes.get(location), popData, polData);
    }


    private PopulationData fetchPopulationData(Context context, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String municipalityCode = municipalityCodes.get(location);

        JsonNode jsonInputString = null;

        try {
            jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.populationquery));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(municipalityCode);

        JsonNode rawPopulationData = fetchDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px", jsonInputString, municipalityCode);
        if (rawPopulationData == null) {
            System.out.println("Data fetch failed.");
            return null;
        }

        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> populations = new ArrayList<>();

        for (JsonNode node : rawPopulationData.get("dimension").get("Vuosi").get("category").get("label")) {
            years.add(node.asText());
        }

        for (JsonNode node : rawPopulationData.get("value")) {
            populations.add(node.asText());
        }

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
        try {
            jsonInputStringEmploymentSuffiency = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentsuffiencyquery));
            ((ObjectNode) jsonInputStringEmploymentSuffiency.get("query").get(1).get("selection")).putArray("values").add(municipalityCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode rawEmploymentSuffiencyData = fetchDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px", jsonInputStringEmploymentSuffiency, municipalityCode);
        if (rawEmploymentSuffiencyData == null) {
            System.out.println("Data fetch failed.");
            return null;
        }

        try {
            jsonInputStringEmployment = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentquery));
            ((ObjectNode) jsonInputStringEmployment.get("query").get(0).get("selection")).putArray("values").add(municipalityCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode rawEmploymentData = fetchDataFromApi("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_115x.px", jsonInputStringEmployment, municipalityCode);
        if (rawEmploymentData == null) {
            System.out.println("Data fetch failed.");
            return null;
        }

//        Double employmentRate = rawEmploymentData.get("value").get(0).asDouble();
//        System.out.println("Employment data: " + rawEmploymentData.get("value").get(0) + " " + employmentRate);
//        System.out.println("Employment suffiency data: " + rawEmploymentSuffiencyData.get("value").asDouble());

        return new PoliticalData(rawEmploymentData.get("value").get(0).asDouble(), rawEmploymentSuffiencyData.get("value").get(0).asDouble());
    }


    private JsonNode fetchDataFromApi(String StringUrl, JsonNode query, String mCode) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = new URL(StringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

//            // JsonNode jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.query));
//            JsonNode jsonInputString = objectMapper.readTree(query);
//            ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(mCode);

            byte[] input = objectMapper.writeValueAsBytes(query);
            OutputStream os = connection.getOutputStream();
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            return objectMapper.readTree(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void fetchMunicipalityCodes() {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode areas = null;

        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

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

