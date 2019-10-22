package com.okunev.bookshelf.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ImageHelper {
    private static ArrayList<String> images = new ArrayList<>(Arrays.asList(
            "https://i.livelib.ru/boocover/1002686425/140/daa2/Dzh._K._Rouling__Garri_Potter_i_uznik_Azkabana.jpg"
            , "https://i.livelib.ru/boocover/1002281151/140/60e6/Stiven_King__Zelenaya_milya.jpg"
            , "https://i.livelib.ru/boocover/1000281109/140/914a/Margaret_Mitchell__Unesennye_vetrom.jpg"
            , "https://j.livelib.ru/boocover/1001569364/140/dfe4/Ketrin_Stokett__Prisluga.jpg"
            , "https://j.livelib.ru/boocover/1003012244/140/4d2a/Aleksandr_Dyuma__Graf_MonteKristo.jpg"
            , "https://i.livelib.ru/boocover/1002892359/140/45c1/Dzhordzh_Martin__Burya_mechej.jpg"
            , "https://i.livelib.ru/boocover/1002924319/140/cad9/Dzhon_R._R._Tolkin__Vlastelin_Kolets._Vozvraschenie_korolya.jpg"
            , "https://j.livelib.ru/boocover/1000494838/140/3bad/Mario_Pyuzo__Krestnyj_otets.jpg"
            , "https://i.livelib.ru/boocover/1000529826/140/e593/Artur_Konan_Dojl__Priklyucheniya_Sherloka_Holmsa._Etyud_v_bagrovyh_tonah._Znak_c.jpg"
            , "https://j.livelib.ru/boocover/1000440471/140/d205/Deniel_Kiz__Tsvety_dlya_Eldzhernona.jpg"
            , "https://i.livelib.ru/boocover/1000203893/140/796d/Agata_Kristi__Desyat_negrityat.jpg"
            , "https://j.livelib.ru/boocover/1003164035/140/8dd2/Fredrik_Bakman__Vtoraya_zhizn_Uve.jpg"
            , "https://j.livelib.ru/boocover/1002179956/140/6ed8/Viktor_Dragunskij__Deniskiny_rasskazy_sbornik.jpg"
            , "https://j.livelib.ru/boocover/1001466286/140/ed3a/Dzhejms_Herriot__O_vseh_sozdaniyah__bolshih_i_malyh.jpg"));

    public static String getRandomImageUrl() {
        Collections.shuffle(images);
        return images.get(0);
    }
}
