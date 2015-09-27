package net.bingyan.hustpass.module;

import android.content.Context;

import net.bingyan.hustpass.Module;
import net.bingyan.hustpass.ModuleDao;
import net.bingyan.hustpass.R;
import net.bingyan.hustpass.module.ann.AnnActivity;
import net.bingyan.hustpass.module.classroom.ClassroomActivity;
import net.bingyan.hustpass.module.compter.ComputerRoomActivity;
import net.bingyan.hustpass.module.elec.ElectricActivity;
import net.bingyan.hustpass.module.food.FoodActivity;
import net.bingyan.hustpass.module.iknow.IknowActivity;
import net.bingyan.hustpass.module.lecture.LectureActivity;
import net.bingyan.hustpass.module.lib.LibraryActivity;
import net.bingyan.hustpass.module.map.MapActivity;
import net.bingyan.hustpass.module.news.NewsActivity;
import net.bingyan.hustpass.module.score.ScoreActivity;
import net.bingyan.hustpass.module.wifi.WifiActivity;
import net.bingyan.hustpass.util.AppLog;

import java.util.List;

/**
 * Created by ant on 14-8-3.
 */
public class Modules {
    static AppLog mLog = new AppLog("hustpass-Modules");

    static int moduleNameRes[] = {R.string.module_classroom, R.string.module_score, R.string.module_elec,
            R.string.module_lib, R.string.module_food, R.string.module_iknow,
            R.string.module_news, R.string.module_computer, R.string.module_map,
            R.string.module_wifi, R.string.module_ann, R.string.module_lecture,
    };
    public static int moduleIconRes[] = { R.drawable.home_icon_classroom, R.drawable.home_icon_score,R.drawable.home_icon_elec,
            R.drawable.home_icon_lib,R.drawable.home_icon_food, R.drawable.home_icon_iknow,
            R.drawable.home_icon_news,  R.drawable.home_icon_computer, R.drawable.home_icon_map,
            R.drawable.home_icon_wifi,R.drawable.home_icon_ann, R.drawable.home_icon_lecture};
    static String moduleClassName[] = { ClassroomActivity.class.getName(), ScoreActivity.class.getName(),ElectricActivity.class.getName(),
            LibraryActivity.class.getName(),FoodActivity.class.getName(), IknowActivity.class.getName(),
            NewsActivity.class.getName(), ComputerRoomActivity.class.getName(),MapActivity.class.getName(),
            WifiActivity.class.getName(),AnnActivity.class.getName(),LectureActivity.class.getName()};

    public static List<Module> initModuleDao(Context context, ModuleDao moduleDao) {

        for (int i = 0; i < moduleNameRes.length; i++) {
            Module module = new Module();
            module.setIconid(i);
            module.setName(context.getString(moduleNameRes[i]));
            module.setFrequency(0);
            if (moduleClassName[i] == null) moduleClassName[i] = "";
            module.setClassname(moduleClassName[i]);
            moduleDao.insert(module);
        }
        return moduleDao.queryBuilder().
                orderDesc(ModuleDao.Properties.Frequency).
                list();
    }

    public static String getClassNameByImgRes(int i) {
        return moduleClassName[i];
    }
}
