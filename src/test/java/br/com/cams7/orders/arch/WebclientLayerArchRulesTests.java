package br.com.cams7.orders.arch;

import static br.com.cams7.orders.arch.ArchUnitHelper.ALL_CLASSES_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_REQUEST_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_RESPONSE_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_REQUEST_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_REQUEST_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_RESPONSE_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_RESPONSE_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.getLayeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

@AnalyzeClasses(packages = ALL_CLASSES_PACKAGE, importOptions = DoNotIncludeTests.class)
class WebclientLayerArchRulesTests {

  //  @ArchTest
  //  void shouldBeSuffixedAsServiceWhenInWebClientPackage(JavaClasses allClasses) {
  //    classes()
  //        .that()
  //        .resideInAPackage(WEBCLIENT_PACKAGE)
  //        .should()
  //        .haveSimpleNameEndingWith(WEBCLIENT_SUFFIX)
  //        .orShould()
  //        .be(BaseWebclient.class)
  //        .check(allClasses);
  //  }
  //
  //  @ArchTest
  //  void shouldBeAnnotatedAsServiceWhenInWebClientPackage(JavaClasses allClasses) {
  //    classes()
  //        .that()
  //        .resideInAPackage(WEBCLIENT_PACKAGE)
  //        .should()
  //        .beAnnotatedWith(Service.class)
  //        .orShould()
  //        .be(BaseWebclient.class)
  //        .check(allClasses);
  //  }

  @ArchTest
  void shouldOnlyHaveSuffixAsRequestWhenInRequestPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(WEBCLIENT_REQUEST_SUFFIX)
        .should()
        .resideInAPackage(CONTROLLER_REQUEST_PACKAGE)
        .orShould()
        .resideInAPackage(WEBCLIENT_REQUEST_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsResponseWhenInResponsePackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(WEBCLIENT_RESPONSE_SUFFIX)
        .should()
        .resideInAPackage(CONTROLLER_RESPONSE_PACKAGE)
        .orShould()
        .resideInAPackage(WEBCLIENT_RESPONSE_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldNotAcessWebClientLayerWhenOtherLayer(JavaClasses allClasses) {
    getLayeredArchitecture()
        .whereLayer(WEBCLIENT_LAYER)
        .mayNotBeAccessedByAnyLayer()
        .check(allClasses);
  }
}
