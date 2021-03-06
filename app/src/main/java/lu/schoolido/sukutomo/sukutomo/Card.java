package lu.schoolido.sukutomo.sukutomo;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arukantara on 23/05/15.
 */
public class Card implements Parcelable{
    public static final String FORMAT = "yyyy-MM-dd";
    // General
    private int id = 0;
    private String name = "";
    private String japanese_name = "";
    private String collection = "";
    private String japanese_collection = "";
    private Attribute attribute = Attribute.SMILE;
    private boolean japan_only = false;
    private String card_image = "";
    private String round_card_image = "";
    private Rarity rarity = Rarity.N;
    private Date release_date;

    // IdolCard
    private String event = "";
    private int hp = 0;
    private int[] minimum_statistics = new int[3];
    private int[] non_idolized_maximum_statistics = new int[3];
    private int[] idolized_maximum_statistics = new int[3];
    private String card_idolized_image = "";
    private String video_story = null;
    private String japanese_video_story = null;

    // Support and RareCard
    private String skill = "None";
    private String japanese_skill = "無し";
    private String skill_details = "None";
    private String japanese_skill_details = "無し";

    // RareCard
    private boolean is_promo = false;
    private String promo_item = null;
    private boolean is_special = false;
    private String center_skill = "None";
    private String japanese_center_skill = "無し";
    private String center_skill_details = "None";
    private String japanese_center_skill_details = "無し";
    //private Bitmap bitmap;
    //private ImageView views;

    public Card(JSONObject object) throws JSONException {
        card_image = object.getString("card_image");
        card_idolized_image = object.getString("card_idolized_image");

        id = object.getInt("id");
        JSONObject idolInfo = object.getJSONObject("idol");
        name = idolInfo.getString("name");
        japanese_name = idolInfo.getString("japanese_name");
        //collection = object.getString("collection");
        //japanese_collection = object.getString("japanese_collection");

        String at = object.getString("attribute");
        if(at.equalsIgnoreCase("smile"))
            attribute = Attribute.SMILE;
        else if(at.equalsIgnoreCase("pure"))
            attribute = Attribute.PURE;
        else if(at.equalsIgnoreCase("cool"))
            attribute = Attribute.COOL;
        else
            attribute = Attribute.ALL;
        japan_only = object.getBoolean("japan_only");
        card_image = object.getString("card_image");
        round_card_image = object.getString("round_card_image");
        String r = object.getString("rarity");
        if(r.equalsIgnoreCase("r"))
            rarity = Rarity.R;
        else if(r.equalsIgnoreCase("sr"))
            rarity = Rarity.SR;
        else if(r.equalsIgnoreCase("ur"))
            rarity = Rarity.UR;
        else
            rarity = Rarity.N;

        // IdolCard
        //event = "";new Card(
        hp = object.getInt("hp");
        minimum_statistics[0] = object.getInt("minimum_statistics_smile");
        minimum_statistics[1] = object.getInt("minimum_statistics_pure");
        minimum_statistics[2] = object.getInt("minimum_statistics_cool");
        non_idolized_maximum_statistics[0] = object.getInt("non_idolized_maximum_statistics_smile");
        non_idolized_maximum_statistics[1] = object.getInt("non_idolized_maximum_statistics_pure");
        non_idolized_maximum_statistics[2] = object.getInt("non_idolized_maximum_statistics_cool");
        idolized_maximum_statistics[0] = object.getInt("idolized_maximum_statistics_smile");
        idolized_maximum_statistics[1] = object.getInt("idolized_maximum_statistics_pure");
        idolized_maximum_statistics[2] = object.getInt("idolized_maximum_statistics_cool");
        card_idolized_image = object.getString("card_idolized_image");
        video_story = object.getString("video_story");
        japanese_video_story = object.getString("japanese_video_story");
        DateFormat format = new SimpleDateFormat(FORMAT);
        try {
            release_date = format.parse(object.getString("release_date"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Support and RareCard
        skill = object.getString("skill");
        japanese_skill = object.getString("japanese_skill");
        skill_details = object.getString("skill_details");
        //japanese_skill_details = object.getString("japanese_skill_details");

        // RareCard
        is_promo = object.getBoolean("is_promo");
        promo_item = object.getString("promo_item");
        is_special = object.getBoolean("is_special");
        center_skill = object.getString("center_skill");
        japanese_center_skill = object.getString("japanese_center_skill");
        //center_skill_details = object.getString("center_skill_details");
        //japanese_center_skill_details = object.getString("japanese_center_skill_details");
    }

    public String getImageURL(boolean idolized) {
        if(idolized)
            return card_idolized_image;
        else
            return card_image;
    }

    /**
     * Method used to show the card image
     * @param idolized True if the user wants to see the idolized version.
     * @param view View used to show the card image.
     */
    public void showImage(boolean idolized, ImageView view) {
        Log.d("im", "ID: " + id + "\n URL showImage:" + card_image);
        if(idolized || is_promo)
            new LoadImage(view, false).execute(card_idolized_image, card_image);
        else
            new LoadImage(view, false).execute(card_image, card_idolized_image);
    }

    /**
     * Method used to show the round card image.
     * @param bitmap
     * @param view
     */
    public void showRoundImage(Bitmap bitmap, ImageView view) {
        new LoadImage(view, true).execute(round_card_image, null);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJapanese_name() {
        return japanese_name;
    }

    public String getCollection() {
        return collection;
    }

    public String getJapanese_collection() {
        return japanese_collection;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public boolean isJapan_only() {
        return japan_only;
    }

    public String getCard_image() {
        return card_image;
    }

    public String getRound_card_image() {
        return round_card_image;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public String getEvent() {
        return event;
    }

    public int getHp() {
        return hp;
    }

    public int[] getMinimum_statistics() {
        return minimum_statistics;
    }

    public int[] getNon_idolized_maximum_statistics() {
        return non_idolized_maximum_statistics;
    }

    public int[] getIdolized_maximum_statistics() {
        return idolized_maximum_statistics;
    }

    public String getCard_idolized_image() {
        return card_idolized_image;
    }

    public String getVideo_story() {
        return video_story;
    }

    public String getJapanese_video_story() {
        return japanese_video_story;
    }

    public String getSkill() {
        return skill;
    }

    public String getJapanese_skill() {
        return japanese_skill;
    }

    public String getSkill_details() {
        return skill_details;
    }

    public String getJapanese_skill_details() {
        return japanese_skill_details;
    }

    public boolean is_promo() {
        return is_promo;
    }

    public String getPromo_item() {
        return promo_item;
    }

    public boolean is_special() {
        return is_special;
    }

    public String getCenter_skill() {
        return center_skill;
    }

    public String getJapanese_center_skill() {
        return japanese_center_skill;
    }

    public String getCenter_skill_details() {
        return center_skill_details;
    }

    public String getJapanese_center_skill_details() {
        return japanese_center_skill_details;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public String getRelease_date(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        if(release_date != null)
            return dateFormat.format(release_date);
        else
            return null;
    }

    private static Date recoverDate(String str_date, String format) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(format);
        if(str_date != null)
            return dateFormat.parse(str_date);
        else
            return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(japanese_name);
        dest.writeString(collection);
        dest.writeString(japanese_collection);
        dest.writeInt(attribute.ordinal());
        dest.writeInt(japan_only ? 1 : 0);
        dest.writeString(card_image);
        dest.writeString(round_card_image);
        dest.writeInt(rarity.ordinal());
        dest.writeInt(is_promo ? 1 : 0);

        dest.writeInt(hp);
        dest.writeIntArray(minimum_statistics);
        dest.writeIntArray(non_idolized_maximum_statistics);
        dest.writeIntArray(idolized_maximum_statistics);
        dest.writeString(card_idolized_image);

        dest.writeString(skill);
        dest.writeString(japanese_skill);
        dest.writeString(skill_details);
        dest.writeString(japanese_skill_details);
        dest.writeString(center_skill);
        dest.writeString(japanese_center_skill);
        dest.writeString(center_skill_details);
        dest.writeString(japanese_center_skill_details);
        dest.writeString(getRelease_date(FORMAT));
    }

    public static final Parcelable.Creator<Card> CREATOR
            = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    private Card(Parcel in) {
        id = in.readInt();
        name = in.readString();
        japanese_name = in.readString();
        collection = in.readString();
        japanese_collection = in.readString();
        int at = in.readInt();
        switch(at) {
            case 0:
                attribute = Attribute.SMILE;
                break;
            case 1:
                attribute = Attribute.PURE;
                break;
            case 2:
                attribute = Attribute.COOL;
                break;
            default:
                attribute = Attribute.ALL;
        }
        japan_only = in.readInt() != 0;
        card_image = in.readString();
        round_card_image = in.readString();
        int r = in.readInt();
        switch(r) {
            case 1:
                rarity = Rarity.R;
                break;
            case 2:
                rarity = Rarity.SR;
                break;
            case 3:
                rarity = Rarity.UR;
                break;
            default:
                rarity = Rarity.N;
        }
        is_promo = in.readInt() != 0;

        hp = in.readInt();
        in.readIntArray(minimum_statistics);
        in.readIntArray(non_idolized_maximum_statistics);
        in.readIntArray(idolized_maximum_statistics);
        card_idolized_image = in.readString();

        skill = in.readString();
        japanese_skill = in.readString();
        skill_details = in.readString();
        japanese_skill_details = in.readString();
        center_skill = in.readString();
        japanese_center_skill = in.readString();
        center_skill_details = in.readString();
        japanese_center_skill_details = in.readString();
        try {
            release_date = recoverDate(in.readString(), FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class used to manage the image download for cards.
     */
    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        Bitmap bitmap;
        final WeakReference<ImageView> viewReference;
        // If the image to load is a roundImage we mustn't add a background.
        private boolean isRoundImage;

        protected LoadImage(ImageView view, boolean isRound) {
            viewReference = new WeakReference<>( view );
            isRoundImage = isRound;
        }

        @Override
        protected void onPreExecute() {
            CardBrowser.loadingView.setVisibility(View.VISIBLE);
            CardBrowser.loadingView.setAnimation(CardBrowser.loadAnimation);
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = CardBrowser.getBitmapFromMemCache(args[0]);
                if (bitmap == null) {
                    if(args[1] != null && args[1].length() > 0) {
                        try {
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[1]).getContent());
                            CardBrowser.addBitmapToMemoryCache(args[1], bitmap);
                        } catch (FileNotFoundException f) {
                            Log.d("imError", "Card image not found at specified URL.");
                        }
                    }
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                    CardBrowser.addBitmapToMemoryCache(args[0], bitmap);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(Bitmap image) {

            if(image != null){
                ImageView imageView = viewReference.get();

                if(!isRoundImage) {
                    CardBrowser.setCardBackground(Card.this, imageView);
                    // If the card is an UR, we will use its image blurred as the background
                    if(rarity == Rarity.UR) {
                        ImageEdition ie = new ImageEdition(image, CardBrowser.context);
                        ie.blurBitmap(10);
                        Bitmap newBackground = ie.getOutput();

                        // Using the best method for each version
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN)
                            imageView.setBackground(new BitmapDrawable(null, newBackground));
                        else
                            imageView.setBackgroundDrawable(new BitmapDrawable(newBackground));
                    }
                }
                // Printing card
                if( imageView != null ) {
                    imageView.setImageBitmap(image);
                }
            }
            CardBrowser.loadingView.clearAnimation();
            CardBrowser.loadingView.setVisibility(View.GONE);
        }
    }
}
