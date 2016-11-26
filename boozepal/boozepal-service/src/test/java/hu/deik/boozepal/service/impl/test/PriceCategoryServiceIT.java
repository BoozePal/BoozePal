package hu.deik.boozepal.service.impl.test;

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import hu.deik.boozepal.arquillian.container.ArquillianContainer;
import hu.deik.boozepal.common.vo.PriceCategoryVO;
import hu.deik.boozepal.common.vo.PriceCategroy;
import hu.deik.boozepal.core.repo.UserRepository;
import hu.deik.boozepal.service.statistics.PriceCategoryStatisticsService;
import hu.deik.boozepal.service.statistics.impl.PriceCategoryStatisticsServiceImpl;

@RunWith(Arquillian.class)
public class PriceCategoryServiceIT extends ArquillianContainer {

    private static Class<?>[] testClasses = { UserRepository.class, PriceCategoryVO.class, PriceCategroy.class,
            PriceCategoryStatisticsServiceImpl.class, PriceCategoryStatisticsService.class };

    @Deployment
    public static Archive<WebArchive> createDeployment() {
        Archive<WebArchive> deployment = ArquillianContainer.createDeployment();
        ((ClassContainer<WebArchive>) deployment).addClasses(testClasses);
        return deployment;
    }

    @EJB
    private PriceCategoryStatisticsService service;

    @Test
    public void testgetPriceCategoryStatistics() {
        List<PriceCategoryVO> priceCategoryStatistics = service.getPriceCategoryStatistics();
        PriceCategoryVO low = PriceCategoryVO.builder().category(PriceCategroy.LOWEST).total(1).build();
        PriceCategoryVO lowmed = PriceCategoryVO.builder().category(PriceCategroy.LOWMEDIUM).total(0).build();
        PriceCategoryVO medium = PriceCategoryVO.builder().category(PriceCategroy.MEDIUM).total(1).build();
        PriceCategoryVO medhigh = PriceCategoryVO.builder().category(PriceCategroy.MEDHIGH).total(0).build();
        PriceCategoryVO highest = PriceCategoryVO.builder().category(PriceCategroy.HIGHER).total(0).build();

        List<PriceCategoryVO> list = Arrays.asList(low, lowmed, medium, medhigh, highest);
        Assert.assertThat(list, Matchers.containsInAnyOrder(priceCategoryStatistics.toArray()));
    }

    @Test
    public void testGetTheBiggestCategoryTotal() {
        Integer theBiggestCategoryTotal = service.getTheBiggestCategoryTotal();
        Assert.assertEquals(Integer.valueOf(1), theBiggestCategoryTotal);
    }

}
