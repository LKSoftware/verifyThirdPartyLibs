package de.kolatanet.utils.maven;

import de.kolatanet.utils.basemodel.Library;
import java.io.IOException;
import java.net.Proxy;
import java.util.function.Function;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to call maven and look for license of a given dependency.
 *
 * @author Leon Kolata
 */
public class LicenseChecker implements Function<Library, Library> {

  private static final Logger LOG = LoggerFactory.getLogger(LicenseChecker.class);

  private static final String MAVEN_ARTIFACT_URL = "https://mvnrepository.com/artifact/";
  private final Proxy proxy;

  /**
   * Creates a new license checker with proxy.
   */
  public LicenseChecker(Proxy proxy) {
    this.proxy = proxy;
  }


  private String buildRequestURL(Library lib) {
    return MAVEN_ARTIFACT_URL + lib.getGroupId() + "/" + lib.getArtifactId()
        + "/" + lib.getVersion();
  }

  private String getLicense(Library lib) {
    if (!lib.getVersion().equals("undefined")) {
      try {
        Document page = Jsoup.connect(buildRequestURL(lib)).proxy(proxy).get();
        Elements sections = page.body().getElementsByClass("version-section")
            .select(":contains(Licenses)");
        Elements licenses = sections.select("table > tbody > tr > td");
        if (licenses.size() >= 1) {
          return licenses.get(0).text();
        }
      } catch (IOException e) {
        LOG.error("Problem while fetching from Maven, " + e.toString());
      }
    }
    return "NO LICENSE FOUND";
  }

  /**
   * Calls maven and asks for a licenses.
   */
  @Override
  public Library apply(Library library) {
    library.addLicense(getLicense(library));
    return library;
  }
}
