package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hu.deik.boozepal.common.vo.DrinkStatisticsVO;
import hu.deik.boozepal.service.statistics.DrinkStatisticsService;

/**
 * Ital statisztikákat megjelenítő felületet kiszolgáló bean.
 *
 */
@ManagedBean(name = "drinkStatistics")
@ViewScoped
public class DrinkStatisticsMBean extends BoozePalAbstractMBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DrinkStatisticsMBean.class);

    /**
     * Ital statisztikákat kezelő szolgáltatás.
     */
    @EJB
    private DrinkStatisticsService drinkStatisticsService;

    /**
     * A felületen megjelenő diagramm.
     */
    private PieChartModel drinkTypeModel;

    /**
     * Ital toplista.
     */
    private List<String> topList;

    /**
     * Osztályt inicializáló metódus.
     */
    @PostConstruct
    public void init() {
        logger.info("DrinkStatisticsMBean#init()");
        createDrinkTypeModel(drinkStatisticsService.getDrinkStatistics());
        createTopList(drinkStatisticsService.getDrinksTopList());
    }

    private void createTopList(List<DrinkStatisticsVO> drinkStatistics) {
        topList = new ArrayList<>();
        Integer rank = 1;
        for (DrinkStatisticsVO drinkStatisticsVO : drinkStatistics) {
            topList.add((rank++) + ". " + getKeyFromProperty(drinkStatisticsVO.getDrinkType().getValue()) + " ("
                    + drinkStatisticsVO.getTotal() + ")");
        }
    }

    private void createDrinkTypeModel(List<DrinkStatisticsVO> drinkStatistics) {
        drinkTypeModel = new PieChartModel();
        drinkStatistics.stream()
                .forEach(p -> drinkTypeModel.set(getKeyFromProperty(p.getDrinkType().getValue()), p.getTotal()));
        drinkTypeModel.setTitle(getKeyFromProperty("drinks"));
        drinkTypeModel.setLegendPosition("w");
    }

    public PieChartModel getDrinkTypeModel() {
        return drinkTypeModel;
    }

    public void setDrinkTypeModel(PieChartModel drinkTypeModel) {
        this.drinkTypeModel = drinkTypeModel;
    }

    public List<String> getTopList() {
        return topList;
    }

    public void setTopList(List<String> topList) {
        this.topList = topList;
    }

}
