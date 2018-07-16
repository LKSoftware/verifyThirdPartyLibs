package de.kolatanet.utils.gradle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.kolatanet.utils.basemodel.Library;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Function;

public class UpdateCheckerMaven implements Function<Library, Library> {

  private String MAVEN_BASE_URL = "http://search.maven.org/solrsearch/select?q=g:%22{groupId}%22+AND+a:%22{artifactId}%22&core=gav&rows=20&wt=json";

  private static final String URL_GROUP = "{groupId}";

  private static final String URL_ARTIFACT = "{artifactId}";

  private static Proxy proxy;

  public UpdateCheckerMaven() {
    //
  }

  public UpdateCheckerMaven(Proxy proxy) {
    this.proxy = proxy;
  }

  @Override
  public Library apply(Library library) {
    System.out.println("checking " + library.toString());
    String latest = getLatestVersion(library);
    System.out.println("checking " + library.toString() + "\n latest " + latest);
    if (!library.getVersion().equals(latest)) {
      library.addComment("Latest version " + latest + " is not used!");
    }
    return library;
  }

  public String getLatestVersion(Library lib) {
    try {
      return retrieveMavenReport(lib, MAVEN_BASE_URL.replace(URL_GROUP, lib.getGroupId())
          .replace(URL_ARTIFACT, lib.getArtifactId()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "failed";
  }

  private JsonElement requestJson(String url) throws IOException {
    URLConnection con;

    if (proxy != null) {
      con = new URL(url).openConnection(proxy);
    } else {
      con = new URL(url).openConnection();
    }

    try (InputStreamReader isr = new InputStreamReader(con.getInputStream())) {
      return new JsonParser().parse(isr);
    }
  }

  private String retrieveMavenReport(Library lib, String url) throws IOException {
    JsonObject root = requestJson(url).getAsJsonObject();
    JsonObject response = root.getAsJsonObject("response");
    JsonArray docs = response.getAsJsonArray("docs");
    JsonObject first = docs.get(0).getAsJsonObject();

    if (first.get("g").getAsString().equals(lib.getGroupId()) && first.get("a").getAsString()
        .equals(lib.getArtifactId())) {
      return first.get("v").getAsString();
    }
    return null;
  }
}
