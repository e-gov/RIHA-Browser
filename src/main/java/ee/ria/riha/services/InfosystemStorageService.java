package ee.ria.riha.services;

import ee.ria.riha.models.Infosystem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class InfosystemStorageService {

  @Value("${infosystems.url}")
  private String infosystemsUrl;





  public Infosystem find(String shortName, String ownerCode) {
    JSONArray infosystems = new JSONArray(infosystemsUrl);
    int index = findIndex(shortName, ownerCode, infosystems);
    return index < 0 ? null : new Infosystem(infosystems.getJSONObject(index));
  }

  private int findIndex(String shortName, String ownerCode, JSONArray infosystems) {
    for (int i = 0; i < infosystems.length(); i++) {
      JSONObject infosystem = infosystems.getJSONObject(i);
      if (infosystem.getString("shortname").equals(shortName) && infosystem.getString("code").equals(ownerCode)) {
        return i;
      }
    }
    return -1;
  }


  }
