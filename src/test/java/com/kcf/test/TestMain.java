package com.kcf.test;

import org.ansj.domain.Term;
import org.ansj.library.UserDefineLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormat;
import org.elasticsearch.common.joda.time.format.DateTimeFormatter;
import org.elasticsearch.common.joda.time.format.DateTimeFormatterBuilder;

import java.util.List;
import java.util.Map;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-9
 */
public class TestMain {

    private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static long signTime = 1325347200000l;

    public static void main(String[] args){

        System.out.println(calScore(4553, 40,
            DateTime.parse("2014-04-02 15:36:43", format).getMillis(),
            DateTime.parse("2014-03-04 15:35:59", format).getMillis())
        );
    }



    public static double calScore(int viewCount, int replyCount, long lastReplied, long created) {
        double X = 10;
        double B = 1.1d;

        double vScore = viewCount > 0? Math.log10(viewCount) : 0;
        double rScore = replyCount > 0? Math.log10(replyCount) : 0;
        double lScore = B * Math.log10(lastReplied - signTime) / X;
        double cScore = Math.log10(created -signTime) / X;

        return vScore + rScore + lScore + cScore;
    }
}
