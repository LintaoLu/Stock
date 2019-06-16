package llu25.hawk.iit.stock.search;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import llu25.hawk.iit.stock.R;

public class SearchItem implements SearchSuggestion
{
    private String item, companyName;

    public SearchItem(String item, String companyName)
    {
        this.item = item;
        this.companyName = companyName;
    }

    public SearchItem(Parcel in){
        this.item = in.readString();
    }

    @Override
    public String getBody()
    {
        return item + "\n" + companyName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.item);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };
}