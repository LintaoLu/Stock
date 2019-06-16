package llu25.hawk.iit.stock.search;

import android.os.Parcelable;

/**
 * An object that represents a single suggestion item
 * in the suggestions drop down generated in response
 * to an entered query in the {@link com.arlib.floatingsearchview.FloatingSearchView}
 */
public interface SearchSuggestion extends Parcelable{

    /**
     * Returns the text that should be displayed
     * for the suggestion represented by this object.
     *
     * @return the text for this suggestion
     */
    String getBody();
}
