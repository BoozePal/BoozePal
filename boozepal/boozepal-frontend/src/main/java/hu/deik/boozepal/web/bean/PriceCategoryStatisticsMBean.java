package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import hu.deik.boozepal.common.vo.PriceCategoryVO;
import hu.deik.boozepal.service.statistics.PriceCategoryStatisticsService;

/**
 * Az ár kategóriák felületet kezelő szolgáltatás.
 * 
 */
@ManagedBean(name = "priceCategoryStatistics")
@ViewScoped
public class PriceCategoryStatisticsMBean extends BoozePalAbstractMBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Az ár-kategóriák statisztikáit biztosító szolgáltatás.
     */
    @EJB
    private PriceCategoryStatisticsService statService;

    /**
     * Felületen megjelenő statisztikai modell.
     */
    private BarChartModel categoryModel;

    @PostConstruct
    public void init() {
        List<PriceCategoryVO> priceCategoryStatistics = statService.getPriceCategoryStatistics();
        initBarModel(priceCategoryStatistics);
    }

    public BarChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(BarChartModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    private BarChartModel initBarModel(List<PriceCategoryVO> statistic) {
        categoryModel = new BarChartModel();
        for (PriceCategoryVO priceCategoryVO : statistic) {
            ChartSeries series = createSeriesFromVO(priceCategoryVO);
            categoryModel.addSeries(series);
        }
        initBarModelProperties(categoryModel);
        return categoryModel;
    }

    private void initBarModelProperties(BarChartModel model) {
        model.setTitle(getKeyFromProperty("priceCategoryStatistics"));
        model.setAnimate(true);
        model.setLegendPosition("ne");
        setAxis(model);
    }

    private void setAxis(BarChartModel model) {
        int getMaxRoundedToFive = roundUp(statService.getTheBiggestCategoryTotal());
        Axis yAxis = model.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(getMaxRoundedToFive);
    }

    private ChartSeries createSeriesFromVO(PriceCategoryVO priceCategoryVO) {
        ChartSeries series = new ChartSeries();
        series.setLabel(priceCategoryVO.getCategory().toString());
        series.set(getKeyFromProperty("price"), priceCategoryVO.getTotal());
        return series;
    }

    int roundUp(int num) {
        return (int) (Math.ceil(num / 5d) * 5);
    }

}
