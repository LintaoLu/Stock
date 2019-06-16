package llu25.hawk.iit.kchartlib0.chart.formatter;

import java.util.Date;


import llu25.hawk.iit.kchartlib0.chart.base.IDateTimeFormatter;
import llu25.hawk.iit.kchartlib0.utils.DateUtil;

/**
 * 时间格式化器
 * Created by tifezh on 2016/6/21.
 */

public class TimeFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date == null) {
            return "";
        }
        return DateUtil.shortTimeFormat.format(date);
    }
}
