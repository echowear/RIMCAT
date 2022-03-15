package org.echowear.rimcatbeta.data_log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

public class UseGSONapitoConvertJavaOBJtoJASONstring {
    public  static void main(String ID, String[][][] xyTimes, String rootDir) throws IOException {
        Frag frag0 = new Frag(
                xyTimes[0][0][0],
                xyTimes[0][1][0],
                xyTimes[0][2][0]
        );
        Frag frag1 = new Frag(
                xyTimes[1][0][0],
                xyTimes[1][1][0],
                xyTimes[1][2][0]
        );
        Frag frag2 = new Frag(
                xyTimes[2][0][0],
                xyTimes[2][1][0],
                xyTimes[2][2][0]
        );
        Frag frag3 = new Frag(
                xyTimes[3][0][0],
                xyTimes[3][1][0],
                xyTimes[3][2][0]
        );
        Frag frag4 = new Frag(
                xyTimes[4][0][0],
                xyTimes[4][1][0],
                xyTimes[4][2][0]
        );
        Frag frag5 = new Frag(
                xyTimes[5][0][0],
                xyTimes[5][1][0],
                xyTimes[5][2][0]
        );
        Frag frag6 = new Frag(
                xyTimes[6][0][0],
                xyTimes[6][1][0],
                xyTimes[6][2][0]
        );
        Frag frag7 = new Frag(
                xyTimes[7][0][0],
                xyTimes[7][1][0],
                xyTimes[7][2][0]
        );
        Frag frag8 = new Frag(
                xyTimes[8][0][0],
                xyTimes[8][1][0],
                xyTimes[8][2][0]
        );
        Frag frag9 = new Frag( xyTimes[9][0][0], xyTimes[9][1][0], xyTimes[9][2][0]);
        Frag frag10 = new Frag( xyTimes[10][0][0], xyTimes[10][1][0], xyTimes[10][2][0]);
        Frag frag11 = new Frag( xyTimes[11][0][0], xyTimes[11][1][0], xyTimes[11][2][0]);
        Frag frag12 = new Frag( xyTimes[12][0][0], xyTimes[12][1][0], xyTimes[12][2][0]);
        Frag frag13 = new Frag( xyTimes[13][0][0], xyTimes[13][1][0], xyTimes[13][2][0]);
        Frag frag14 = new Frag( xyTimes[14][0][0], xyTimes[14][1][0], xyTimes[14][2][0]);
        Frag frag15 = new Frag( xyTimes[15][0][0], xyTimes[15][1][0], xyTimes[15][2][0]);
        Frag frag16 = new Frag( xyTimes[16][0][0], xyTimes[16][1][0], xyTimes[16][2][0]);
        Frag frag17 = new Frag( xyTimes[17][0][0], xyTimes[17][1][0], xyTimes[17][2][0]);
        Frag frag18 = new Frag( xyTimes[18][0][0], xyTimes[18][1][0], xyTimes[18][2][0]);
        Frag frag19 = new Frag( xyTimes[19][0][0], xyTimes[19][1][0], xyTimes[19][2][0]);
        Frag frag20 = new Frag( xyTimes[20][0][0], xyTimes[20][1][0], xyTimes[20][2][0]);
        Frag frag21 = new Frag( xyTimes[21][0][0], xyTimes[21][1][0], xyTimes[21][2][0]);
        Frag frag22 = new Frag( xyTimes[22][0][0], xyTimes[22][1][0], xyTimes[22][2][0]);
        Frag frag23 = new Frag( xyTimes[23][0][0], xyTimes[23][1][0], xyTimes[23][2][0]);
        Frag frag24 = new Frag( xyTimes[24][0][0], xyTimes[24][1][0], xyTimes[24][2][0]);
        Frag frag25 = new Frag( xyTimes[25][0][0], xyTimes[25][1][0], xyTimes[25][2][0]);
        Frag frag26 = new Frag( xyTimes[26][0][0], xyTimes[26][1][0], xyTimes[26][2][0]);
        Frag frag27 = new Frag( xyTimes[27][0][0], xyTimes[27][1][0], xyTimes[27][2][0]);
        Frag frag28 = new Frag( xyTimes[28][0][0], xyTimes[28][1][0], xyTimes[28][2][0]);
        Frag frag29 = new Frag( xyTimes[29][0][0], xyTimes[29][1][0], xyTimes[29][2][0]);
        Frag frag30 = new Frag( xyTimes[30][0][0], xyTimes[30][1][0], xyTimes[30][2][0]);
        Frag frag31 = new Frag( xyTimes[31][0][0], xyTimes[31][1][0], xyTimes[31][2][0]);
        Frag frag32 = new Frag( xyTimes[32][0][0], xyTimes[32][1][0], xyTimes[32][2][0]);
        Frag frag33 = new Frag( xyTimes[33][0][0], xyTimes[33][1][0], xyTimes[33][2][0]);
        Frag frag34 = new Frag( xyTimes[34][0][0], xyTimes[34][1][0], xyTimes[34][2][0]);
        Frag frag35 = new Frag( xyTimes[35][0][0], xyTimes[35][1][0], xyTimes[35][2][0]);
        Frag frag36 = new Frag( xyTimes[36][0][0], xyTimes[36][1][0], xyTimes[36][2][0]);
        Frag frag37 = new Frag( xyTimes[37][0][0], xyTimes[37][1][0], xyTimes[37][2][0]);
        Frag frag38 = new Frag( xyTimes[38][0][0], xyTimes[38][1][0], xyTimes[38][2][0]);
        Frag frag39 = new Frag( xyTimes[39][0][0], xyTimes[39][1][0], xyTimes[39][2][0]);
        Frag frag40 = new Frag( xyTimes[40][0][0], xyTimes[40][1][0], xyTimes[40][2][0]);
        Frag frag41 = new Frag( xyTimes[41][0][0], xyTimes[41][1][0], xyTimes[41][2][0]);
        Frag frag42 = new Frag( xyTimes[42][0][0], xyTimes[42][1][0], xyTimes[42][2][0]);
        fragment fragm = new fragment(
                frag0,
                frag1,
                frag2,
                frag3,
                frag4,
                frag5,
                frag6,
                frag7,
                frag8,
                frag9,
                frag10,
                frag11,
                frag12,
                frag13,
                frag14,
                frag15,
                frag16,
                frag17,
                frag18,
                frag19,
                frag20,
                frag21,
                frag22,
                frag23,
                frag24,
                frag25,
                frag26,
                frag27,
                frag28,
                frag29,
                frag30,
                frag31,
                frag32,
                frag33,
                frag34,
                frag35,
                frag36,
                frag37,
                frag38,
                frag39,
                frag40,
                frag41,
                frag42
        );
        JsonCoord user = new JsonCoord(
                ID,
                fragm
        );
        Gson gson = new Gson();

        FileWriter writer = new FileWriter(rootDir + "/" + ID + "_" + "_coords.json");
        gson.toJson(user, writer);
        writer.flush();
        writer.close();
//        String json = gson.toJson(user);
//        System.out.println(json);

    }






//    public static void main(String ID, String[][][] xyTimes, String rootDir) throws IOException {
//        JsonCoord user = new JsonCoord(
//                ID, xyTimes
//        );
//
//
//        Gson gson = new Gson();
//        FileWriter writer = new FileWriter(new File(rootDir + "/" + ID + "_" + "_coords.json"));
//        gson.toJson(user, writer);
//        writer.flush();
//        writer.close();
////        String json = gson.toJson(user);
////        System.out.println(json);
//    }
}
