package com.link.core;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.link.api.service.MenuServiceI;
import com.link.common.kit.TreeKit;
import com.link.common.util.Constant;
import com.link.common.util.ResultJson;
import com.link.core.base.BaseServiceImpl;
import com.link.model.Menu;
import com.link.model.RoleMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linkzz on 2017-05-26.
 */
public class MenuServiceImpl extends BaseServiceImpl implements MenuServiceI {

    @Override
    public List<Menu> sidebar(String userId, String pid) {
        String sql = "SELECT DISTINCT T.* FROM T_MENU T\n" +
                "  LEFT JOIN T_ROLE_MENU TRM ON T.ID = TRM.MENU_ID\n" +
                "  LEFT JOIN T_ROLE R ON TRM.ROLE_ID = R.ID\n" +
                "  LEFT JOIN T_USER_ROLE TUR ON R.ID = TUR.ROLE_ID\n" +
                "  LEFT JOIN T_USER U ON TUR.USER_ID = U.ID WHERE U.ID=?";
        List<Menu> menus = Menu.dao.find(sql,userId);
        return menus;
    }

    @Override
    public boolean save(Menu menu) {
        return false;
    }

    @Override
    public boolean delete(Menu menu) {
        return false;
    }

    @Override
    public List<Record> findMenu() {
        List<Record> listmodel = Db.find("select * from t_menu t");
        TreeKit listkit = new TreeKit(listmodel);
        List<Record> treeMenuList = listkit.startSorting();
        return treeMenuList;
    }

    @Override
    public List<String> findRoleMenus(String roleId) {
        List<RoleMenu> roleMenus = RoleMenu.dao.find("SELECT * FROM T_ROLE_MENU T WHERE T.ROLE_ID = ?",roleId);
        List<String> list = new ArrayList<String>();
        if (roleMenus != null && roleMenus.size() > 0){
            for (RoleMenu roleMenu : roleMenus){
                list.add(roleMenu.getMenuId());
            }
        }
        return list;
    }

    @Override
    public ResultJson saveRoleMenu(String roleId, List<String> linkMenuId) {
        ResultJson resultJson = new ResultJson();
        try {
            List<RoleMenu> roleMenus = RoleMenu.dao.find("select * from t_role_menu t where t.role_id = ?",roleId);
            if (roleMenus != null && roleMenus.size() > 0){
                for (RoleMenu rm : roleMenus){
                    rm.delete();
                }
            }
            if (linkMenuId != null && linkMenuId.size() > 0){
                for (String menuId : linkMenuId){
                    RoleMenu roleMenu = new RoleMenu();
                    roleMenu.setId(StrKit.getRandomUUID());
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    roleMenu.save();
                }
            }
            resultJson.setStatus(Constant.RESULT_SUCCESS);
            resultJson.setMsg("角色菜单分配成功！");
        } catch (Exception e) {
            resultJson.setStatus(Constant.RESULT_SQL_ERROR);
            resultJson.setMsg("分配角色是数据库发生异常！");
            e.printStackTrace();
        }
        return resultJson;
    }

}
