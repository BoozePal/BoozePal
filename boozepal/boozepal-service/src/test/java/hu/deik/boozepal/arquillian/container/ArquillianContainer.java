package hu.deik.boozepal.arquillian.container;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.common.exceptions.RegistrationException;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.UserService;
import hu.deik.boozepal.service.impl.UserServiceImpl;

/**
 * Arquillian tesztek bázis osztálya, a konténert hozza létre majd amit
 * használni tudunk.
 * 
 * @author Nandi
 *
 */
public class ArquillianContainer {
    private static final String SPRING_VERSION = "4.3.2.RELEASE";
    private static final String BOOZEPAL_CORE_API = "boozepal-core-api";
    private static final String CORE_API = "hu.deik:boozepal-core-api:1.0";
    private static final String SRPING_BEANS = "org.springframework:spring-beans:" + SPRING_VERSION;
    private static final String SRPING_WEB = "org.springframework:spring-web:" + SPRING_VERSION;
    private static final String SPRING_CONTEXT = "org.springframework:spring-context:" + SPRING_VERSION;

    @Deployment
    public static Archive<WebArchive> createDeployment() {
        File[] springContext = Maven.resolver().resolve(SPRING_CONTEXT).withTransitivity().asFile();
        File[] springWeb = Maven.resolver().resolve(SRPING_WEB).withTransitivity().asFile();
        File[] springBeans = Maven.resolver().resolve(SRPING_BEANS).withTransitivity().asFile();
        JavaArchive[] coreApi = Maven.resolver().resolve(CORE_API).withTransitivity().as(JavaArchive.class);
        replacePersistenceXMLFromArchive(coreApi, BOOZEPAL_CORE_API);
        Archive<WebArchive> webArchive = ShrinkWrap.create(WebArchive.class, "boozepal-test.war")
                .addPackage("hu.deik.boozepal.*").addPackage(User.class.getPackage())
                .addPackage(UserService.class.getPackage()).addPackage(UserServiceImpl.class.getPackage())
                .addPackage(UserRepository.class.getPackage())
                .addClasses(ArquillianContainer.class, RegistrationException.class).addAsResource("beanRefContext.xml")
                .addAsResource("spring-core-test.xml").addAsResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(springContext).addAsLibraries(springWeb).addAsLibraries(springBeans)
                .addAsLibraries(coreApi);
        return webArchive;
    }

    private static void replacePersistenceXMLFromArchive(JavaArchive[] archive, String api) {
        for (JavaArchive jar : archive) {
            if (jar.getName().contains(api)) {
                jar.delete("/META-INF/persistence.xml");
                jar.add(new ClassLoaderAsset("test-persistence.xml"), "/META-INF/persistence.xml");
                break;
            }
        }
    }

}
