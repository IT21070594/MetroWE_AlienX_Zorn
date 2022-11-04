package Database;

import android.provider.BaseColumns;

public class UserMaster {
    private UserMaster(){

    }
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN1_UN = "username";
        public static final String COLUMN2_PW = "passwords";
        public static final String COLUMN3_AT =  "accounttype";

    }
    public static class PremiumUsers implements BaseColumns {
        public static final String TABLE_NAME = "PremiumUsers";
        public static final String COLUMN1_UN = "username";
        public static final String COLUMN3_PI = "planID";
        public static final String COLUMN4_PN =  "planName";

    }
    public static class Artists implements BaseColumns {
        public static final String TABLE_NAME = "Artists";
        public static final String COLUMN1_UN = "username";
        public static final String COLUMN_UI = "userid";
        public static final String COLUMN2_PI = "planID";
        public static final String COLUMN3_PN =  "planName";

    }
 public static class UserPlan implements BaseColumns {
        public static final String TABLE_NAME = "UserPlans";
        public static final String COLUMN1_PI = "planId";
        public static final String COLUMN2_PN = "planName";
        public static final String COLUMN3_Im = "image";
        public static final String COLUMN4_AM =  "amount";
        public static final String COLUMN5_DL =  "downloadLimit";

    }
    public static class Download implements BaseColumns {
        public static final String TABLE_NAME = "Downloads";
        public static final String COLUMN1_DI = "downloadID";
        public static final String COLUMN2_SI = "songID";
        public static final String COLUMN3_SN = "songName";
        public static final String COLUMN4_UI =  "userID";
        public static final String COLUMN5_UN =  "userName";
    }
    public static class Album implements BaseColumns {
        public static final String TABLE_NAME = "Albums";
        public static final String COLUMN1_AI = "albumID";
        public static final String COLUMN2_AN = "albumName";
        public static final String COLUMN3_IM = "Image";
    }
    public static class AlbumHas implements BaseColumns {
        public static final String TABLE_NAME = "AlbumHas";
        public static final String COLUMN1_AI = "albumID";
        public static final String COLUMN2_AN = "albumName";
        public static final String COLUMN3_SI = "songID";
        public static final String COLUMN4_SN = "SongName";
    }
    public static class Song implements BaseColumns {
        public static final String TABLE_NAME = "Songs";
        public static final String COLUMN1_SI = "songID";
        public static final String COLUMN2_SN = "songName";
        public static final String COLUMN3_Im = "image";
        public static final String COLUMN4_URI = "songURI";
        public static final String COLUMN5_GE = "genre";
        public static final String COLUMN6_LA= "language";
        public static final String COLUMN7_TY = "songType";
        public static final String COLUMN8_DU = "duration";
    }
    public static class Upload implements BaseColumns {
        public static final String TABLE_NAME = "Uploads";
        public static final String COLUMN1_AI = "artistID";
        public static final String COLUMN2_AN="artistName";
        public static final String COLUMN3_SI = "songID";
        public static final String COLUMN4_SN = "songName";
    }
    public static class Playlist implements BaseColumns {
        public static final String TABLE_NAME = "Playlists";
        public static final String COLUMN1_PI = "playlistID";
        public static final String COLUMN2_PN = "playlistName";
        public static final String COLUMN3_UI = "userID";
        public static final String COLUMN4_UN = "userName";
    }
    public static class PlaylistHas implements BaseColumns {
        public static final String TABLE_NAME = "PlaylistHas";
        public static final String COLUMN1_PI = "playlistID";
        public static final String COLUMN2_PN = "playlistName";
        public static final String COLUMN3_SI = "songID";
        public static final String COLUMN4_SN = "songName";
    }
    public static class ArtistPlan implements BaseColumns {
        public static final String TABLE_NAME = "ArtistPlans";
        public static final String COLUMN1_PI = "planId";
        public static final String COLUMN2_PN = "planName";
        public static final String COLUMN3_Im = "image";
        public static final String COLUMN4_AM =  "amount";
        public static final String COLUMN5_DL =  "downloadLimit";
        public static final String COLUMN5_UL =  "uploadLimit";

    }
    public static class Payment implements BaseColumns {
        public static final String TABLE_NAME = "Payment";
        public static final String COLUMN1_PI = "payId";
        public static final String COLUMN2_UI = "userID";
        public static final String COLUMN3_UN = "userName";
        public static final String COLUMN4_AM =  "amount";
        public static final String COLUMN5_DA =  "date";
        public static final String COLUMN5_PN =  "planName";

    }


}
