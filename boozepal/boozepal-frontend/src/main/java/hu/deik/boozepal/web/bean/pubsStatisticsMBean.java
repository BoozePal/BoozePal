package hu.deik.boozepal.web.bean;

import hu.deik.boozepal.common.vo.PubCategoryVO;
import hu.deik.boozepal.service.statistics.PubCategoryStatisticsService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.chart.*;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Random;

/**
 * A kocsma statsztikákat kezelő szolgáltatás.
 */
@NoArgsConstructor
@Getter
@Setter
@ManagedBean(name = "pubsStatisticsMBean")
@ViewScoped
public class pubsStatisticsMBean extends BoozePalAbstractMBean {

    private TagCloudModel tagCloudModel;

    private BubbleChartModel bubbleModel;

    private BarChartModel animatedModel;

    @EJB
    private PubCategoryStatisticsService pubStatisticsService;

    @PostConstruct
    public void init() {
        List<PubCategoryVO> pubCategoryStatistics = pubStatisticsService.getAllPubsCategoryStatistics ();
        createCloudModels (pubCategoryStatistics);
//        createBubbleModels (pubCategoryStatistics);
        createBarChartModels (pubCategoryStatistics);
    }

    public void onSelect(SelectEvent event) {
        TagCloudItem item = (TagCloudItem) event.getObject ();
        FacesMessage msg = new FacesMessage (FacesMessage.SEVERITY_INFO, getKeyFromProperty ("pubLayoutPopUp"), item.getLabel ());
        FacesContext.getCurrentInstance ().addMessage (null, msg);
    }

    private void createCloudModels(List<PubCategoryVO> statistic) {
        tagCloudModel = new DefaultTagCloudModel ();
        for (PubCategoryVO pubCategoryVO : statistic) {
            DefaultTagCloudItem bubbleChartSeries =
                    new DefaultTagCloudItem (pubCategoryVO.getPubName (), pubCategoryVO.getTotalLike ());
            tagCloudModel.addTag (bubbleChartSeries);
        }
        if (tagCloudModel.getTags ().isEmpty ()) {
            tagCloudModel.addTag (new DefaultTagCloudItem (getKeyFromProperty ("pubStatisticLayoutPubNull"), 0));
        }
    }

//    private void createBubbleModels(List<PubCategoryVO> statistic) {
//        bubbleModel = new BubbleChartModel ();
//        initBubbleModelProperties (bubbleModel);
//        for (PubCategoryVO priceCategoryVO : statistic) {
//            int randX = new Random ().nextInt (49) + 1;
//            int randY = new Random ().nextInt (99) + 1;
//            BubbleChartSeries bubbleChartSeries = new BubbleChartSeries (priceCategoryVO.getPubName (), randX, randY, priceCategoryVO.getTotalLike ());
//            bubbleModel.add (bubbleChartSeries);
//        }
//        if (bubbleModel.getData ().isEmpty ()) {
//            bubbleModel.setTitle (getKeyFromProperty ("pubStatisticLayoutPubNull"));
//            bubbleModel.add (new BubbleChartSeries (getKeyFromProperty ("pubStatisticLayoutPubNull"), 50, 50, 0));
//        }
//    }

    private void createBarChartModels(List<PubCategoryVO> statistic) {
        animatedModel = new BarChartModel ();
        initAnimatedModelProperties (animatedModel);
        for (PubCategoryVO priceCategoryVO : statistic) {
            ChartSeries boys = new ChartSeries ();
            boys.setLabel (priceCategoryVO.getPubName ());
            boys.set (getKeyFromProperty ("pubLayoutPubName"), priceCategoryVO.getTotalLike ());
            animatedModel.addSeries (boys);
        }
        if (animatedModel.getSeries ().isEmpty ()) {
            animatedModel.setTitle (getKeyFromProperty ("pubStatisticLayoutPubNull"));
            ChartSeries emptyPub = new ChartSeries ();
            emptyPub.setLabel (getKeyFromProperty ("pubLayoutPubName"));
            emptyPub.set (getKeyFromProperty ("pubLayoutPubName"), 0);
            animatedModel.addSeries (emptyPub);
        }
    }

    private void initAnimatedModelProperties(BarChartModel animatedModel) {
        animatedModel.setTitle (getKeyFromProperty ("masterLayoutPubCategoryMenu"));
        animatedModel.setAnimate (true);
        animatedModel.setLegendPosition ("ne");
    }

//    private void initBubbleModelProperties(BubbleChartModel bubbleModel) {
//        bubbleModel.setTitle (getKeyFromProperty ("masterLayoutPubCategoryMenu"));
//        bubbleModel.setShadow (false);
//        bubbleModel.setBubbleGradients (true);
//        Axis yAxis = bubbleModel.getAxis (AxisType.Y);
//        yAxis.setMin (0);
//        yAxis.setMax (100);
//        Axis xAxis = bubbleModel.getAxis (AxisType.X);
//        xAxis.setMin (0);
//        xAxis.setMax (100);
//    }
}
