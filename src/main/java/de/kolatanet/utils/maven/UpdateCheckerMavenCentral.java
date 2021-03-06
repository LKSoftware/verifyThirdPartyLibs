package de.kolatanet.utils.maven;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to call Maven and ask for latest versions when a dependency is given.
 *
 * @author Leon Kolata
 */
public class UpdateCheckerMavenCentral implements Function<Library, Library> {

  private static final Logger LOG = LoggerFactory.getLogger(UpdateCheckerMavenCentral.class);

  private static final String MAVEN_BASE_URL = "http://search.maven.org/solrsearch/select?q=g:%22{groupId}%22+AND+a:%22{artifactId}%22&core=gav&rows=20&wt=json";

  private static final String URL_GROUP = "{groupId}";

  private static final String URL_ARTIFACT = "{artifactId}";

  private final Proxy proxy;

  /**
   * Creates UpdateChecker with Proxy
   */
  public UpdateCheckerMavenCentral(Proxy proxy) {
    this.proxy = proxy;
  }

  /**
   * Checks whether a library is in the latest version
   */
  @Override
  public Library apply(Library library) {
    String latest = getLatestVersion(library);
    if (!library.getVersion().equals(latest)) {
      library.addComment("Latest version " + latest + " is not used!");
    }
    return library;
  }

  /**
   * Calls maven central and gets a json report for the dependency.
   */
  private String getLatestVersion(Library lib) {
    try {
      return retrieveMavenReport(lib, MAVEN_BASE_URL.replace(URL_GROUP, lib.getGroupId())
          .replace(URL_ARTIFACT, lib.getArtifactId()));
    } catch (IOException e) {
      LOG.error("Problem calling maven central ", e);
    }
    return "failed";
  }

  /**
   * Opens a connection to maven and parses the response into json.
   */
  private JsonElement requestJson(String url) throws IOException {
    URL mavenUrl = new URL(url);

    URLConnection con = mavenUrl.openConnection(proxy);

    try (InputStreamReader isr = new InputStreamReader(con.getInputStream())) {
      return new JsonParser().parse(isr);
    } catch (Exception e) {
      LOG.trace("Failed while parsing maven central json: " + e);
    }
    return null;
  }

  /**
   * Requests the json from maven and extract the version.
   */
  private String retrieveMavenReport(Library lib, String url) throws IOException {
    JsonElement rootElement = requestJson(url);
    if (rootElement != null) {
      JsonObject root = rootElement.getAsJsonObject();
      JsonObject response = root.getAsJsonObject("response");
      JsonArray docs = response.getAsJsonArray("docs");
      if (docs.size() >= 1) {
        JsonObject first = docs.get(0).getAsJsonObject();

        if (first.get("g").getAsString().equals(lib.getGroupId()) && first.get("a").getAsString()
            .equals(lib.getArtifactId())) {
          return first.get("v").getAsString();
        }
      } else {
        LOG.warn("Could not find updates for: " + lib.asDependency());
      }
    }
    return lib.getVersion();
  }
}
