package com.example.ai_for_data_science;

import java.io.*;

public class IO {

    public static final String DIRECTORY="myData/";

    /**
     * Used to save data, represented as a string, in a file called with name specified.
     *
     * @param fileName the name of the file (including extension)
     * @param data the data to be saved
     * @return whether the data was saved successfully
     */
    public static boolean saveFile(String fileName, String data, boolean append)
    {
        try
        {
            FileOutputStream outS = new FileOutputStream(DIRECTORY + fileName,append);
            PrintWriter pw = new PrintWriter(outS);

            pw.println(data);
            pw.flush();
            outS.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads data from a file as specified by the file name supplied.
     *
     * @param fileName the file to be loaded
     * @return the data contained in the file specified
     */
    public static String loadFile(String fileName)
    {
        StringBuffer data = new StringBuffer();

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DIRECTORY + fileName)));
            String input = br.readLine();

            while(input != null)
            {
                if(!input.equals(""))
                    data.append(input + "\n");

                input = br.readLine();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        return data.toString();
    }
}
