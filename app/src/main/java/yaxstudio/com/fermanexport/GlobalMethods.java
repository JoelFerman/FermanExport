package yaxstudio.com.fermanexport;

import java.util.Random;

public class GlobalMethods
{
// **************************************** PACKAGE TOKEN ****************************************
// ***********************************************************************************************
    public void RandomizeAlphaNumeric()
    {
        char[] alphNum = "A0B8C6D3E7F0G2H1I2J9K3L1M4N6O8P7Q6R7S3T8U5V2W9X4Y1Z".toCharArray();

        Random rnd = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++)

            sb.append(alphNum[rnd.nextInt(alphNum.length)]);

        String id = sb.toString();

        GlobalVars.GVRandomAlphaNumeric = id;
    }
}
