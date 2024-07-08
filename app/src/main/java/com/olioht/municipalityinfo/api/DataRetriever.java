package com.olioht.municipalityinfo.api;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.olioht.municipalityinfo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DataRetriever {
    private HashMap<String, String> municipalityCodes;

    public DataRetriever() {
        fetchMunicipalityCodes();
    }

    public ArrayList<PopulationData> getPopulationData(Context context, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String municipalityCode;
        municipalityCode = municipalityCodes.get(location);

        JsonNode jsonInputString = null;
        try {
            jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.populationquery));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(municipalityCode);

        JsonNode rawPopulationData = fetchData("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/synt/statfin_synt_pxt_12dy.px", jsonInputString, municipalityCode);
        assert rawPopulationData != null;

        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> populations = new ArrayList<>();

        for (JsonNode node : rawPopulationData.get("dimension").get("Vuosi").get("category").get("label")) {
            years.add(node.asText());
        }

        for (JsonNode node : rawPopulationData.get("value")) {
            populations.add(node.asText());
        }

        ArrayList<PopulationData> populationData = new ArrayList<>();

        for (int i = 0; i < years.size(); i++) {
            populationData.add(new PopulationData(Integer.parseInt(years.get(i)), Integer.parseInt(populations.get(i))));
        }

        return populationData;
    }

    public ArrayList<Double> getEmploymentData(Context context, String location) {
        ObjectMapper objectMapper = new ObjectMapper();
        String municipalityCode;
        municipalityCode = municipalityCodes.get(location);

        JsonNode jsonInputString = null;
        try {
            jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.employmentquery));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((ObjectNode) jsonInputString.get("query").get(1).get("selection")).putArray("values").add(municipalityCode);

        JsonNode rawPopulationData = fetchData("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/tyokay/statfin_tyokay_pxt_125s.px", jsonInputString, municipalityCode);
        assert rawPopulationData != null;

        ArrayList<Double> employmentSuffiency = new ArrayList<>();

        for (JsonNode node : rawPopulationData.get("value")) {
            employmentSuffiency.add(node.asDouble());
        }

        return employmentSuffiency;
    }



    public ArrayList<PoliticalData> getPoliticalData(Context context, String query) {
        return null;
    }


    private JsonNode fetchData(String StringUrl, JsonNode query, String mCode) {
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

