package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by User on 22/03/2016.
 */
public class Files {


    private Context ctx;
    private String 			        Directory;
    private int 			        SizeBuffer;
    private File[]			        ListaArchivos;
    private java.io.FileFilter 	    OnlyFolders;
    private java.io.FilenameFilter	OnlyPictures;

    private FileInputStream fis;
    private FileReader file;

    public Files(Context ctx, String CurrentDirectory, int BufferKbytes){
        this.ctx 			= ctx;
        this.Directory 		= CurrentDirectory;
        this.SizeBuffer 	= BufferKbytes;
        this.OnlyFolders	= 	new FileFilter(){
            public boolean accept(File dir){
                return (dir.isDirectory());
            };
        };

        this.OnlyPictures	= 	new FilenameFilter(){
            public boolean accept(File dir, String name){
                return (name.endsWith(".jpg")||name.endsWith(".jpeg"));
            };
        };


        if(!ExistFolderOrFile(this.Directory, false)){
            MakeDirectory(this.Directory, false);
        }
    }


    /**
     * Metodo para crear una carpeta
     * @param _new_directory 					-> ruta de la nueva carpeta
     * @param _relativeCurrentDirectory true	-> si la carpeta es relativa al directorio del proyecto
     * 									false	-> si la carpeta es independiente al directorio del proyecto
     * @return 							true	-> si se creo correctamente la carpeta
     * 									false	-> si hubo algun error al crear la carpeta
     */
    public boolean MakeDirectory(String _new_directory, boolean _relativeCurrentDirectory){
        if(_relativeCurrentDirectory){
            _new_directory = this.Directory+File.separator+_new_directory;
        }
        File f = new File(_new_directory);
        if(f.mkdir()){
            Toast.makeText(this.ctx, "Directorio " + _new_directory + " correctamente.", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }
    }


    /**
     * Metodo para comprobar si existe una carpeta o archivo
     * @param _ruta String con la ruta completa de la carpeta que deseamos saber si existe
     * @return	retorna true si existe la carpeta false en caso contrario
     */
    public boolean ExistFolderOrFile(String _ruta, boolean _relativeCurrentDirectory){
        if(_relativeCurrentDirectory){
            _ruta = this.Directory+File.separator+_ruta;
        }
        File f = new File(_ruta);
        return f.exists();
    }

}
