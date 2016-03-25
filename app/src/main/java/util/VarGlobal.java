package util;

import android.os.Environment;

import java.io.File;

/**
 * Created by User on 22/03/2016.
 */
public interface VarGlobal {

    public static String name_database      = "Feeds";
    public static String path_files_app     =  Environment.getExternalStorageDirectory() + File.separator + "Feeds";

}
