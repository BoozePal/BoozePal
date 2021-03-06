package hu.deik.boozepal.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.google.gson.Gson;

import hu.deik.boozepal.common.vo.MapUserVO;
import hu.deik.boozepal.service.UserService;

@ManagedBean(name = "mapBean")
@ViewScoped
public class MapMBean extends BoozePalAbstractMBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @EJB
    private UserService userService;

    private List<MapUserVO> onlineUsers;
    private Gson gson;

    @PostConstruct
    public void init() {
        gson = new Gson();
        onlineUsers = userService.getOnlineUsersToMap();
    }

    public String getOnlineUsers() {
        return gson.toJson(onlineUsers);
    }

    public void setOnlineUsers(List<MapUserVO> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

}
