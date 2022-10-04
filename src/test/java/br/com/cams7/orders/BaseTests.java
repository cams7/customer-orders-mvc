package br.com.cams7.orders;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.apache.commons.lang3.ClassUtils.getPackageName;

import br.com.cams7.orders.template.DomainTemplateLoader;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTests {
  protected static final String REQUEST_TRACE_ID = "123";
  protected static final String ERROR_MESSAGE = "Something wrong";

  @BeforeAll
  static void setup() {
    loadTemplates(getPackageName(DomainTemplateLoader.class));
  }
}
