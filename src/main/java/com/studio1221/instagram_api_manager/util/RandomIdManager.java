package com.studio1221.instagram_api_manager.util;

import java.util.Random;

/**
 * Created by jo on 2017-11-08.
 */

public class RandomIdManager {
    final static String[] arrChar = new String[]{
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
            , "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    final static String[] arrCharWithNumber = new String[]{
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
            , "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
            , "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    final static Random random = new Random();

    final static String[] arrEmailHost = new String[]{"@gmail.com", "@yahoo.com", "@hotmail.com"};

    public static final String makeRandomWebkitBoundary(){
        String boundary = ""; //----WebKitFormBoundaryEB9YnpfyEH47ht87
        boundary = "----WebKitFormBoundary";
        for(int i = 0; i < 16; i++){
            boundary += arrCharWithNumber[random.nextInt(arrCharWithNumber.length)];
        }

        return boundary;
    }

    public static final String makeRandomEmail(){
        Random r = new Random();

        int maxLength = r.nextInt(6) + 8;

        String result = "";
        int charLength = r.nextInt(maxLength-1)+1;
        int numberLength = maxLength-charLength;
        int dashbarPos = (r.nextInt(3) == 0 ? -1 : r.nextInt(charLength + numberLength-1));

        int globalPos = 0;
        for(int i = 0; i < charLength; i++, globalPos++){
            String c = arrChar[r.nextInt(arrChar.length)];
            result += c;
            if(dashbarPos == globalPos){
                result+="_";
            }
        }

        for(int i = 0; i < numberLength; i++, globalPos++){
            String c = r.nextInt(10) +"";
            result += c;
            if(dashbarPos == globalPos){
                result+="_";
            }
        }

        result += arrEmailHost[r.nextInt(arrEmailHost.length)];

        return result;
    }

    public static String makeRandomId(int charLen, int numberLen){
        String id = "";

        for(int i = 0; i < charLen; i++){
            id += arrChar[random.nextInt(arrChar.length)];
        }

        for(int i = 0; i < numberLen; i++){
            id += random.nextInt(10)+"";
        }

        return id;
    }

    public static String getRandomChar(){
        return arrChar[random.nextInt(arrChar.length)];
    }

    public static String getRandomNumber(){
        return random.nextInt(10) + "";
    }

    final static String[] arrIdType = new String[]{
            "xxx_x_xxx_x", "x_xxx_x_x", "xx_xxx_x_x", "xxxx_x_xx", "x_x_x_xxx"
            , "xxx_n_xx_n_x", "x_nn_x_n_xx", "x_x_x_n_xxx_nn", "xxx_nn_n_xx_x"
            };
    public static String makeRandomIdType2(){
        String idFormat = arrIdType[random.nextInt(arrIdType.length)];
        String id = "";
        for(int i = 0; i < idFormat.length(); i++){
            String c = idFormat.substring(i, i+1);
            if(c.equals("x")){
                id += arrChar[random.nextInt(arrChar.length)];
            }else if(c.equals("n")){
                id += (random.nextInt(10)+"");
            }else{
                id += c;
            }
        }
        return id;
    }

    public static String makeRandomIdFromId(String id){
        String[] arrRandomIds = new String[]{
                makeRandomIdFromIdType1(id)
                , makeRandomIdFromIdType2(id)
                , makeRandomIdFromIdType3(id)
                , makeRandomIdFromIdType4(id)
                , makeRandomIdFromIdType5(id)
                , makeRandomIdFromIdType6(id)
                , makeRandomIdFromIdType7(id)
                , makeRandomIdFromIdType8(id)
                , makeRandomIdFromIdType9(id)
                , makeRandomIdFromIdType10(id)
                , makeRandomIdFromIdType11(id)
        };
        return arrRandomIds[random.nextInt(arrRandomIds.length)];
    }

    public static String makeRandomIdFromIdType1(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 2){
                result += "_";
            }else if(i == 4){
                result += "_";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType2(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 1){
                result += "_";
            }else if(i == 2){
                result += "_";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType3(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 3){
                result += "_";
            }else if(i == 6){
                result += ".";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType4(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 5){
                result += "_";
            }else if(i == 6){
                result += "_";
            }
        }
        result+="_";
        return result;
    }

    public static String makeRandomIdFromIdType5(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 6){
                result += "_";
            }else if(i == 8){
                result += ".";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType6(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 6){
                result += getRandomChar();
            }else if(i == 8){
                result += ".";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType7(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 2){
                result += "." + getRandomChar() + ".";
            }else if(i == 8){
                result += ".";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType8(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 1){
                result += "_" + getRandomNumber() + "_";
            }else if(i == 5){
                result += ".";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType9(String id){
        //짝대기 추가
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 0){
                result += "_" + getRandomChar() + "__";
            }else if(i == 4){
                result += "_";
            }
        }
        return result;
    }

    public static String makeRandomIdFromIdType10(String id){
        //짝대기 추가
        String result = id + "_" + getRandomChar() + "_";
        return result;
    }

    public static String makeRandomIdFromIdType11(String id){
        String result = "";

        for(int i = 0; i < id.length(); i++){
            String c = id.substring(i, i+1);
            result += c;
            if(i == 4){
                result += ".";
            }else if(i == 7){
                result += ".";
            }
        }
        return result;
    }
}
