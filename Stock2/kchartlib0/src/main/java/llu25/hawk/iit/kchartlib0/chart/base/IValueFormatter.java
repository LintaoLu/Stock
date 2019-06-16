package llu25.hawk.iit.kchartlib0.chart.base;

/**
 * Value格式化接口
 * Created by tifezh on 2016/6/21.
 */

public interface IValueFormatter {
    /**
     * 格式化value
     *
     * @param value 传入的value值
     * @return 返回字符串
     */
    String format(float value);
}
