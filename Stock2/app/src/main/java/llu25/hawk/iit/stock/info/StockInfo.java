package llu25.hawk.iit.stock.info;

import java.io.Serializable;

public class StockInfo implements Serializable
{
    private String stockSymbol, companyName, exchangeCenter, stockChange, closeYesterday,
            priceOpen, dayHigh, dayLow, currentPrice = "0", stockRiseRate = "0.0";

    public StockInfo(String stockSymbol, String companyName, String currentPrice, String stockRiseRate,
                     String exchangeCenter, String stockChange, String closeYesterday, String priceOpen,
                     String dayHigh, String dayLow)
    {
        this.stockRiseRate = stockRiseRate;
        this.stockSymbol = stockSymbol;
        this.currentPrice = currentPrice;
        this.companyName = companyName;
        this.exchangeCenter = exchangeCenter;
        this.stockChange = stockChange;
        this.closeYesterday = closeYesterday;
        this.priceOpen = priceOpen;
        this.dayHigh = dayHigh;
        this.dayLow = dayLow;
    }

    public StockInfo(String stockSymbol, String companyName)
    {
        this.stockSymbol = stockSymbol;
        this.companyName = companyName;
    }

    public String getCurrentPrice() { return currentPrice; }

    public String getStockRiseRate() { return stockRiseRate; }

    public String getStockSymbol() { return stockSymbol; }

    public String getCompanyName() { return companyName; }

    public String getCloseYesterday() { return closeYesterday; }

    public String getExchangeCenter() { return exchangeCenter; }

    public String getStockChange() { return stockChange; }

    public String getPriceOpen() { return priceOpen; }

    public String getDayHigh() {
        return dayHigh;
    }

    public String getDayLow() {
        return dayLow;
    }
}
