package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;

@ManagedBean(name = "drinkStatistics")
@ViewScoped
public class DrinkStatisticsMBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EJB
    private DrinkStatisticsService drinkStatisticsService;

    @ManagedProperty("#{out}")
    private ResourceBundle bundle;

    private PieChartModel drinkTypeModel;

    @PostConstruct
    public void init() {
        drinkTypeModel = new PieChartModel();
        List<DrinkStatisticsVO> drinkStatistics = drinkStatisticsService.getDrinkStatistics();
        drinkStatistics.stream().forEach(p -> drinkTypeModel.set(p.getDrinkType().getDisplayName(), p.getTotal()));
        drinkTypeModel.setTitle(bundle.getString("drinks"));
        drinkTypeModel.setLegendPosition("w");
    }

    public PieChartModel getDrinkTypeModel() {
        return drinkTypeModel;
    }

    public void setDrinkTypeModel(PieChartModel drinkTypeModel) {
        this.drinkTypeModel = drinkTypeModel;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

}
