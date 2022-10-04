package br.com.cams7.orders.arch;

import static br.com.cams7.orders.arch.ArchUnitHelper.ADAPTER_COMMON_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.ADAPTER_CONFIGURATION_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.ADAPTER_CONFIGURATION_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.ALL_CLASSES_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.CONTROLLER_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.CORE_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.CORE_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.DOMAIN_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.DOMAIN_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.DOMAIN_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.EXCEPTION_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_IN_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_IN_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_OUT_EXCEPTION_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_OUT_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_OUT_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.PORT_OUT_PROPERTIES_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.PROPERTIES_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.REPOSITORY_PORT_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.SERVICE_PORT_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.SPRINGFRAMEWORK_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.USECASE_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.USECASE_PORT_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.UTILS_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.UTILS_SUFFIX;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_LAYER;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_LAYER_PATH;
import static br.com.cams7.orders.arch.ArchUnitHelper.WEBCLIENT_PACKAGE;
import static br.com.cams7.orders.arch.ArchUnitHelper.getLayeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.constructors;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

@AnalyzeClasses(packages = ALL_CLASSES_PACKAGE, importOptions = DoNotIncludeTests.class)
class CoreLayerArchRulesTests {

  //  @ArchTest
  //  void shouldImplementUseCaseAndBePublicClassWhenInUseCasePackage(JavaClasses allClasses) {
  //    classes()
  //        .that()
  //        .resideInAPackage(USECASE_PACKAGE)
  //        .should()
  //        .implement(simpleNameEndingWith(USECASE_PORT_SUFFIX))
  //        .andShould()
  //        .bePublic()
  //        .check(allClasses);
  //
  //    classes()
  //        .that()
  //        .implement(simpleNameEndingWith(USECASE_PORT_SUFFIX))
  //        .should()
  //        .resideInAPackage(USECASE_PACKAGE)
  //        .check(allClasses);
  //  }

  //  @ArchTest
  //  void shouldBeFinalFieldsWhenInUseCaseClass(JavaClasses allClasses) {
  //    fields()
  //        .that()
  //        .areDeclaredInClassesThat()
  //        .resideInAPackage(USECASE_PACKAGE)
  //        .should()
  //        .beFinal()
  //        .check(allClasses);
  //  }

  @ArchTest
  void shouldHaveExecuteMethodWhenInUseCaseClass(JavaClasses allClasses) {
    methods()
        .that()
        .areDeclaredInClassesThat()
        .resideInAPackage(USECASE_PACKAGE)
        .and()
        .arePublic()
        .should()
        .haveName("execute")
        .check(allClasses);
  }

  @ArchTest
  void shouldHavePublicConstructorWhenInUseCaseClass(JavaClasses allClasses) {
    constructors()
        .that()
        .areDeclaredInClassesThat()
        .resideInAPackage(USECASE_PACKAGE)
        .should()
        .bePublic()
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsUseCaseWhenInPortInPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(PORT_IN_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(USECASE_PORT_SUFFIX)
        .andShould()
        .bePublic()
        .andShould()
        .beInterfaces()
        .check(allClasses);

    classes()
        .that()
        .haveSimpleNameEndingWith(USECASE_PORT_SUFFIX)
        .should()
        .resideInAPackage(PORT_IN_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldBeSuffixedAsGatewayAndBePublicInterfaceWhenInPortOutPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(PORT_OUT_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(REPOSITORY_PORT_SUFFIX)
        .orShould()
        .haveSimpleNameEndingWith(SERVICE_PORT_SUFFIX)
        .andShould()
        .bePublic()
        .andShould()
        .beInterfaces()
        .check(allClasses);

    classes()
        .that()
        .haveSimpleNameEndingWith(REPOSITORY_PORT_SUFFIX)
        .should()
        .resideInAPackage(PORT_OUT_PACKAGE)
        .check(allClasses);

    classes()
        .that()
        .haveSimpleNameEndingWith(SERVICE_PORT_SUFFIX)
        .should()
        .resideInAPackage(PORT_OUT_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void
      shouldAcessGatewayInterfaceOnlyWhenThroughUseCaseOrDataProviderOrWebClientOrConfigurationClasses(
          JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(PORT_OUT_PACKAGE)
        .should()
        .onlyBeAccessed()
        .byAnyPackage(
            USECASE_PACKAGE, REPOSITORY_PACKAGE, WEBCLIENT_PACKAGE, ADAPTER_CONFIGURATION_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsEntityWhenInDomainPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(DOMAIN_SUFFIX)
        .should()
        .resideInAPackage(DOMAIN_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsUtilsWhenInUtilsPackage(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(UTILS_PACKAGE)
        .should()
        .haveSimpleNameEndingWith(UTILS_SUFFIX)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsPropertiesWhenInPropertiesPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(PROPERTIES_SUFFIX)
        .should()
        .resideInAPackage(PORT_OUT_PROPERTIES_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyHaveSuffixAsExceptionWhenInExceptionPackage(JavaClasses allClasses) {
    classes()
        .that()
        .haveSimpleNameEndingWith(EXCEPTION_SUFFIX)
        .should()
        .resideInAPackage(PORT_OUT_EXCEPTION_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void shouldNotAccessInfraLayersWhenInCoreLayers(JavaClasses allClasses) {
    noClasses()
        .that()
        .resideInAPackage(CORE_LAYER_PATH)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(
            SPRINGFRAMEWORK_LAYER_PATH,
            ADAPTER_COMMON_LAYER_PATH,
            CONTROLLER_LAYER_PATH,
            REPOSITORY_LAYER_PATH,
            WEBCLIENT_LAYER_PATH)
        .check(allClasses);
  }

  @ArchTest
  void shouldAcessUseCaseClassOnlyWhenThroughConfigurationClasses(JavaClasses allClasses) {
    classes()
        .that()
        .resideInAPackage(USECASE_PACKAGE)
        .should()
        .onlyBeAccessed()
        .byAnyPackage(USECASE_PACKAGE, ADAPTER_CONFIGURATION_PACKAGE)
        .check(allClasses);
  }

  @ArchTest
  void
      shouldOnlyAcessPortLayerOrOtherLayersOutsideCoreLayerWhenThroughControllerOrRepositoryOrWebclientLayers(
          JavaClasses allClasses) {
    classes()
        .that()
        .resideInAnyPackage(CONTROLLER_LAYER_PATH, REPOSITORY_LAYER_PATH, WEBCLIENT_LAYER_PATH)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(PORT_LAYER_PATH, DOMAIN_LAYER_PATH)
        .orShould()
        .dependOnClassesThat()
        .resideOutsideOfPackage(CORE_LAYER_PATH)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyAcessPortInLayerWhenCoreOrControllerLayers(JavaClasses allClasses) {
    getLayeredArchitecture()
        .whereLayer(PORT_IN_LAYER)
        .mayOnlyBeAccessedByLayers(CORE_LAYER, CONTROLLER_LAYER, ADAPTER_CONFIGURATION_LAYER)
        .check(allClasses);
  }

  @ArchTest
  void shouldOnlyAcessPortOutLayerWhenCoreOrControllerOrRepositoryOrWebclientLayers(
      JavaClasses allClasses) {
    getLayeredArchitecture()
        .whereLayer(PORT_OUT_LAYER)
        .mayOnlyBeAccessedByLayers(
            CORE_LAYER,
            CONTROLLER_LAYER,
            REPOSITORY_LAYER,
            WEBCLIENT_LAYER,
            ADAPTER_CONFIGURATION_LAYER)
        .check(allClasses);
  }
}
