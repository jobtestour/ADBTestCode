package exemplefortest.adb_test;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ReadJson {

     static String JsonReader(Context context,String data)  {



        try{

        InputStream inputStream=null;

        inputStream = context.openFileInput(data);


        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();
            return stringBuilder.toString();
        }

        } catch (
                IOException e) {
            e.printStackTrace();
        }


        return null;


    }























}
