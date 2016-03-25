package persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import prueba.rappiprueba.SplashActivity;
import util.Files;

/**
 * Created by User on 22/03/2016.
 */
public class DBAItunesApplication {
    private static String N_BD = null;
    private static final int VERSION_BD = 1;

    private BDHelper nHelper;
    private static Context nContexto;
    private static String   Directorio;
    private SQLiteDatabase nBD;

    private boolean ValorRetorno;
    private static Files fcnFiles;

    private static class BDHelper extends SQLiteOpenHelper {

        public BDHelper(Context context) {
            super(context, N_BD, null, VERSION_BD);
        }

        @Override
        public void onCreate(SQLiteDatabase db){

            //Tabla cpara los datos basicos de feed
            db.execSQL(	"CREATE TABLE data_feed(author_name     VARCHAR(255) NOT NULL PRIMARY KEY," +
                                                "update_last	TEXT NOT NULL," +
                                                "rights	        TEXT NOT NULL," +
                                                "title 		    TEXT NOT NULL)");

            //Tabla con los datos de las imagenes
            db.execSQL("CREATE TABLE    data_entry (name            VARCHAR(255) NOT NULL PRIMARY KEY," +
                                                  "image            TEXT NOT NULL," +
                                                  "summary          TEXT NOT NULL," +
                                                  "price_amount     TEXT NOT NULL," +
                                                  "price_currency   TEXT NOT NULL," +
                                                  "rights           TEXT NOT NULL," +
                                                  "title            TEXT NOT NULL," +
                                                  "link             TEXT NOT NULL," +
                                                  "category         TEXT NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion == 1) {

            }
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){

        }
    }

    private static DBAItunesApplication ourInstance = null;

    public static DBAItunesApplication getInstance() {
        if(ourInstance == null){
            ourInstance = new DBAItunesApplication();
        }

        return ourInstance;
    }

    public DBAItunesApplication (){
        this.nContexto = SplashActivity.context;
        this.Directorio = SplashActivity.path_files_app;
        DBAItunesApplication.N_BD = this.Directorio + File.separator + SplashActivity.name_database;
        fcnFiles = new Files(this.nContexto, this.Directorio, 10);
        if(!fcnFiles.ExistFolderOrFile(this.Directorio,false)){
            fcnFiles.MakeDirectory(this.Directorio,false);
        }
    }


    public int getVersionBD(){
        return VERSION_BD;
    }

    public String getNameBD(){
        return SplashActivity.name_database;
    }

    private DBAItunesApplication abrir(){
        nHelper = new BDHelper(nContexto);
        nBD = nHelper.getWritableDatabase();
        return this;
    }

    private void cerrar() {
        nHelper.close();
    }


    /**Funcion para realizar INSERT
     * @param _tabla 		-> tabla a la cual se va a realizar el INSERT
     * @param _informacion	-> informacion que se va a insertar
     * @return				-> true si se realizo el insert correctamente, false en otros casos
     */
    public boolean InsertRegistro(String _tabla, ContentValues _informacion){
        abrir();
        ValorRetorno = false;
        try{
            if(nBD.insert(_tabla,null, _informacion)>=0){
                ValorRetorno = true;
            }
        }catch(Exception e){
            e.toString();
        }
        cerrar();
        return ValorRetorno;
    }


    /**
     *
     * @param Tabla
     * @param Informacion
     * @param Condicion
     * @return
     */
    public boolean UpdateRegistro(String Tabla, ContentValues Informacion, String Condicion){
        ValorRetorno = false;
        abrir();
        try{
            if(nBD.update(Tabla, Informacion, Condicion, null)>=0){
                ValorRetorno = true;
            }
        }catch(Exception e){
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion para realizar un insert en caso de no existir el registro o update en caso de existir
     * @param _tabla		->tabla sobre la cual se va a operar
     * @param _informacion	->informacion que se va a insertar o actualizar
     * @param _condicion	->Condicion que debe cumplirse para realizar el update y/o insert
     * @return				->String con el mensaje de retorno, ya puede ser insert/update realizado o no realizado.
     */
    public String InsertOrUpdateRegistro(String _tabla, ContentValues _informacion, String _condicion){
        String _retorno = "Sin acciones";
        if(!this.ExistRegistros(_tabla, _condicion)){
            if(this.InsertRegistro(_tabla, _informacion)){
                _retorno = "Registro ingresado en "+_tabla;
            }else{
                _retorno = "Error al ingresar el registro en "+_tabla;
            }
        }else{
            if(this.UpdateRegistro(_tabla, _informacion, _condicion)){
                _retorno = "Registro actualizado en "+_tabla;
            }else{
                _retorno = "Error al actualizar el registro en "+_tabla;
            }
        }
        return _retorno;
    }


    /**Funcion para eliminar un registro de una tabla en particular
     * @param _tabla		->tabla sobre la cual se va a trabajar
     * @param _condicion	->condicion que debe cumplirse para ejecutar el delete respectivo
     * @return				->true si fue ejecutado el delete correctamente, false en caso contrario
     */
    public boolean DeleteRegistro(String _tabla, String _condicion){
        ValorRetorno = false;
        abrir();
        try{
            if(nBD.delete(_tabla, _condicion,null)>0){
                ValorRetorno = true;
            }
        }catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        cerrar();
        return ValorRetorno;
    }


    /**
     * @param _tabla    ->tabla sobre la cual se va a relizar la consulta
     * @param _campos   ->campos que se desean obtener datos
     * @return          ->ArrayList de content values con la informacion obtenida en la consulta
     */
    public ArrayList<ContentValues> SelectData(String _tabla, String _campos){
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _registro.put(_columnas[i], c.getString(i));
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }


    /**Funcion encargada de realizar una consulta y retornarla con un array list de content values, similar a un array de diccionarios
     * @param _tabla		->tabla sobre la cual va a correr la consulta
     * @param _campos		->campos que se van a consultar
     * @param _condicion	->condicion para filtrar la consulta
     * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
     */
    public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion){
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _registro.put(_columnas[i], c.getString(i));
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }


    /**
     * @param _tabla		->tabla sobre la cual va a correr la consulta
     * @param _campos		->campos que se van a consultar
     * @param _condicion	->condicion para filtrar la consulta
     * @param _orden        ->orden de como se muestran los resultados
     * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
     */
    public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion, String _orden){
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" ORDER BY "+_orden, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _registro.put(_columnas[i], c.getString(i));
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }



    /**
     * @param _tabla		->tabla sobre la cual va a correr la consulta
     * @param _campos		->campos que se van a consultar
     * @param _condicion	->condicion para filtrar la consulta
     * @param _orden        ->orden de como se muestran los resultados
     * @param _limit        ->limite de la consulta, cuantos registros se quieren tomar
     * @return				->ArrayList de ContentValues con la informacion resultado de la consulta
     */
    public ArrayList<ContentValues> SelectData(String _tabla, String _campos, String _condicion, String _orden, int _limit){
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" ORDER BY "+_orden+" LIMIT "+_limit, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _registro.put(_columnas[i], c.getString(i));
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }

    /**Funcion para realizar la consulta de un registro, retorna un contentvalues con la informacion consultada
     * @param _tabla		->tabla sobre la cual se va a ejecutar la consulta
     * @param _campos		->campos que se quieren consultar
     * @param _condicion	->condicion para ejecutar la consulta
     * @return				-->ContentValues con la informacion del registro producto de la consulta
     */
    public ContentValues SelectDataRegistro(String _tabla, String _campos, String _condicion){
        ContentValues _query = new ContentValues();
        _query.clear();
        this.abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT DISTINCT "+_campos+" FROM "+_tabla+" WHERE "+_condicion+" LIMIT 1", null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                //ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    _query.put(_columnas[i], c.getString(i));
                }
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }


    //Relizar la consulta teniendo en cuenta varios JOIN a la izquierda
    public ArrayList<ContentValues> SelectNJoinLeftData(String _tabla, String _campos, String _join_left[], String _on_left[], String _condicion){
        String Query = "";
        ArrayList<ContentValues> _query = new ArrayList<ContentValues>();
        _query.clear();
        this.abrir();
        try{
            Query = "SELECT DISTINCT "+ _campos + " FROM "+ _tabla;
            for(int i=0;i<_join_left.length;i++){
                Query += " LEFT JOIN " +_join_left[i] + " ON "+_on_left[i];
            }
            Query += " WHERE "+ _condicion;
            Cursor c = nBD.rawQuery(Query, null);
            String[] _columnas = c.getColumnNames();

            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                ContentValues _registro = new ContentValues();
                for(int i=0;i<_columnas.length;i++){
                    if(c.getString(i) == null){
                        _registro.put(_columnas[i], "");
                    }else{
                        _registro.put(_columnas[i], c.getString(i));
                    }
                }
                _query.add(_registro);
            }
        }
        catch(Exception e){
            Log.i("Error en SQLite", e.toString());
        }
        this.cerrar();
        return _query;
    }



    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un entero
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public int IntSelectShieldWhere(String _tabla, String _campo, String _condicion){
        int intRetorno = -1;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                intRetorno = c.getInt(0);
            }
        }
        catch(Exception e){
            intRetorno = -1;
        }
        cerrar();
        return intRetorno;
    }


    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un double
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public double DoubleSelectShieldWhere(String _tabla, String _campo, String _condicion){
        double intRetorno = -1;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                intRetorno = c.getDouble(0);
            }
        }
        catch(Exception e){
            intRetorno = -1;
        }
        cerrar();
        return intRetorno;
    }


    /**Funcion que consulta un campo de una tabla segun la condicion recibida y retorna el resultado como un String
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _campo		->Campo que se quiere consultar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public String StrSelectShieldWhere(String _tabla, String _campo, String _condicion){
        String strRetorno = null;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT " + _campo + " FROM " + _tabla + " WHERE " + _condicion, null);
            for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
                strRetorno = c.getString(0);
            }
        }
        catch(Exception e){
            e.toString();
            strRetorno = null;
        }
        cerrar();
        return strRetorno;
    }


    /**Funcion retorna la cantidad de registros de una tabla que cumplen la condicion recibida por parametro
     * @param _tabla		->Tabla sobre la cual se va a trabajar
     * @param _condicion	->Condicion para filtro de la consulta
     * @return
     */
    public int CountRegistrosWhere(String _tabla, String _condicion){
        int ValorRetorno = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT count(*) FROM "+ _tabla +" WHERE "+ _condicion, null);
            c.moveToFirst();
            ValorRetorno = c.getInt(0);
        }
        catch(Exception e){
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion que retorna true o falso segun existan o no registros que cumplan la condicion recibida por parametro
     * @param _tabla		->Tabla sobre la cual se va trabajar
     * @param _condicion	->Condicion de filtro
     * @return
     */
    public boolean ExistRegistros(String _tabla, String _condicion){
        ValorRetorno = false;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT count(*) as cantidad FROM " + _tabla +" WHERE " + _condicion , null);
            c.moveToFirst();
            if(c.getDouble(0)>0)
                ValorRetorno = true;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return ValorRetorno;
    }


    /**Funcion que retorna la cantidad de minutos transcurridos desde la fecha actual y la recibida por parametro
     * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
     * @return 			->String con el resultado en minutos
     */
    public String MinutesBetweenDateAndNow(String _oldDate){
        String _retorno = "";
        int _minutos = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT strftime('%s','now')-strftime('%s','"+_oldDate+"') as segundos", null);
            c.moveToFirst();
            _retorno = c.getString(0);
            _minutos = Integer.parseInt(_retorno)/60;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return String.valueOf(_minutos);
    }


    /**Funcion que retorna la cantidad de minutos transcurridos entre las fechas recibidas por parametro
     * @param _newDate	->fecha mas reciente contra la cual se quiere caldular la diferencia
     * @param _oldDate	->fecha anterior contra la cual se quiere calcular la diferencia en segundos
     * @return 			->String con el resultado en minutos
     */
    public String MinutesBetweenDates(String _newDate, String _oldDate){
        String _retorno = "";
        int _minutos = 0;
        abrir();
        try{
            Cursor c = nBD.rawQuery("SELECT strftime('%s','"+_newDate+"')-strftime('%s','"+_oldDate+"') as segundos", null);
            c.moveToFirst();
            _retorno = c.getString(0);
            _minutos = Integer.parseInt(_retorno)/60;
        }catch(Exception e){
            Log.v("Excepcion",e.toString());
        }
        cerrar();
        return String.valueOf(_minutos);
    }
}
