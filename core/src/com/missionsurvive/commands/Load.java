package com.missionsurvive.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.missionsurvive.map.MapEditor;

/**
 * Created by kuzmin on 23.04.18.
 */
public class Load implements Command{

    private MapEditor mapEditor;

    public Load(MapEditor mapEditor){
        this.mapEditor = mapEditor;
    }

    @Override
    public void execute(String fileName) {
        mapEditor.loadMap(loadFileFromExternalStorage(fileName));
    }

    public String loadFileFromExternalStorage(String filename){
        String mapInString = null;
        if(isExternalStorageAvailable()){
            String directory = "trf/";
            if(isExternalDirectoryExists(directory)){
                mapInString = getMap(directory + filename);
            }
        }
        return mapInString;
    }


    public boolean isExternalStorageAvailable(){
        return Gdx.files.isExternalStorageAvailable();
    }

    public boolean isExternalDirectoryExists(String directory){
        if(Gdx.files.external(directory).isDirectory()){
            return true;
        }
        else{
            return false;
        }
    }

    public String getMap(String filePath){
        String mapInString = null;
        FileHandle fileHandle = Gdx.files.external(filePath);
        if(fileHandle != null){
            mapInString = fileHandle.readString();
        }
        return mapInString;
    }

    /**
     * If MEDIA_MOUNTED: checks if app's folder exists (if not - creates it);
     * writes file into this folder.
     * @param object
     * @param filename
     */
    /*public static void writeFileIntoExternalStorage(Object object, String filename){
        MapEditorScreen mapEditorScreen = null;
        if(object != null){
            if(object instanceof MapEditorScreen){
                mapEditorScreen = (MapEditorScreen) object;
            }
        }

        String state = Environment.getExternalStorageState();

        if(!state.equals(Environment.MEDIA_MOUNTED)){
        }
        else{
            String folder_main = "trf"; //directory for saving levels.

            File externalDir = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!externalDir.exists()) {  //create new directory if not exists.
                externalDir.mkdirs();
            }
            File textFile = new File(externalDir.getAbsolutePath() + File.separator + filename);

            try{
                writeTextFile(textFile, getMapInText(mapEditorScreen));
                //String text = readTextFile(textFile);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }*/




    /**
     * Gets array list of filename strings inside app's dir.
     * @return
     */
    /*public static ArrayList<String> getlistFiles(){
        String state = Environment.getExternalStorageState();

        if(!state.equals(Environment.MEDIA_MOUNTED)){
            return null;
        }
        else{
            String folder_main = "trf"; //directory for saving levels.

            File externalDir = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!externalDir.exists()) {
                return null;
            }
            else{
                File[] listFiles = externalDir.listFiles(); //get list of current dir files...
                if(listFiles != null){ //...if there are any files inside dir...
                    ArrayList<String> fileNames = new ArrayList<String>(); //...create new array list...
                    for(int fileNum = 0; fileNum < listFiles.length; fileNum++){ //...put file names into this array list.
                        fileNames.add(listFiles[fileNum].getName());
                    }
                    return fileNames;
                }
            }
        }
        return null;
    }


    private static String getMapInText(MapEditorScreen mapEditorScreen){
        MapTer[][] level1MapTer = mapEditorScreen.getMapEditor().getLevel1Ter();
        MapTer[][] level2MapTer = mapEditorScreen.getMapEditor().getLevel2Ter();
        MapTer[][] level3MapTer = mapEditorScreen.getMapEditor().getLevel3Ter();
        Spawn[][] spawnBots = mapEditorScreen.getMapEditor().getSpawns();

        String map = null;
        StringBuilder stringBuilder = new StringBuilder();
        if(mapEditorScreen == null) return map;

        if(!mapEditorScreen.getMapEditor().isHorizontal()){
            stringBuilder.append(":");
            stringBuilder.append("vertical");
            stringBuilder.append("\n");
        }

        if(level1MapTer != null){
            stringBuilder.append(":");
            stringBuilder.append("level1MapTer");
            stringBuilder.append(":");
            stringBuilder.append("lev1"); //name of the asset with tileset.
            stringBuilder.append(":");
            stringBuilder.append("\n");
            setLevelMap(stringBuilder, level1MapTer, spawnBots);
        }

        if(level2MapTer != null){
            stringBuilder.append(":");
            stringBuilder.append("level2MapTer");
            stringBuilder.append(":");
            stringBuilder.append("lev2"); //name of the asset with tileset.
            stringBuilder.append(":");
            stringBuilder.append("\n");
            setLevelMap(stringBuilder, level2MapTer, null);
        }

        if(level3MapTer != null){
            stringBuilder.append(":");
            stringBuilder.append("level3MapTer");
            stringBuilder.append(":");
            stringBuilder.append("lev3"); //name of the asset with tileset.
            stringBuilder.append(":");
            stringBuilder.append("\n");
            setLevelMap(stringBuilder, level3MapTer, null);
        }

        map = stringBuilder.toString();
        return map;
    }

    private static void setLevelMap(StringBuilder stringBuilder, MapTer[][] mapTer, Spawn[][] spawns){
        for(int row = 0; row < mapTer.length; row++){
            for(int col = 0; col < mapTer[0].length; col++){
                stringBuilder.append("row");
                stringBuilder.append(":");
                stringBuilder.append(row);
                stringBuilder.append(":");
                stringBuilder.append("col");
                stringBuilder.append(":");
                stringBuilder.append(col);
                stringBuilder.append(":");
                if(mapTer[row][col].getMapObject() != null){
                    stringBuilder.append(mapTer[row][col].getMapObject().getAssetY());
                    stringBuilder.append(":");
                    stringBuilder.append(mapTer[row][col].getMapObject().getAssetX());
                }
                else{
                    stringBuilder.append(-1);
                    stringBuilder.append(":");
                    stringBuilder.append(-1);
                }
                if(mapTer[row][col].isLadder()){
                    stringBuilder.append(":");
                    stringBuilder.append("ladder");
                    stringBuilder.append(":");
                    stringBuilder.append("\n");
                    continue;
                }
                if(spawns != null){
                    if(spawns[row][col] != null){
                        stringBuilder.append(":");
                        stringBuilder.append("enemy");
                        stringBuilder.append(":");
                        stringBuilder.append(spawns[row][col].getBotId());
                        stringBuilder.append(":");
                        stringBuilder.append(spawns[row][col].getDirection());
                        stringBuilder.append(":");
                        stringBuilder.append("\n");
                        continue;
                    }
                }
                if(mapTer[row][col].isBlocked()){
                    stringBuilder.append(":");
                    stringBuilder.append("blocked");
                }
                stringBuilder.append(":");
                stringBuilder.append("\n");
            }
        }
    }


    private static void writeTextFile(File file, String text) throws IOException{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(text);
        bufferedWriter.close();
    }


    /**
     * This method loads file from assets.
     * @param object
     */
    /*public static void loadFile(Object object){
        MapEditorScreen mapEditorScreen = null;
        if(object != null){
            if(object instanceof MapEditorScreen){
                mapEditorScreen = (MapEditorScreen) object;
            }
        }

        InputStream inputStream = null;
        try{
            inputStream = mapEditorScreen.getGame().getFileIO().readAsset("levels/level1.txt");
            mapEditorScreen.getMapEditor().loadMap(loadTextFile(inputStream));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(inputStream != null){
                try{
                    inputStream.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static String loadTextFile(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while((len = inputStream.read(bytes)) > 0)
            byteArrayOutputStream.write(bytes, 0, len);
        return new String(byteArrayOutputStream.toByteArray(), "UTF8");
    }


    /**
     * It is not suitable for reading data. It doesn't give appropriate String results.
     * The String line is not forming correctly.
     */
    /*private static String readTextFile (File file) throws IOException{

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder text = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null){
            text.append(line);
            text.append("/n");
        }
        bufferedReader.close();
        return text.toString();
    }*/
}
