package Database;

import android.provider.BaseColumns;

public class UserMasters {
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN1_UN = "username";
        public static final String COLUMN2_EM = "email";
        public static final String COLUMN3_PW = "passwords";
        public static final String COLUMN4_AT = "accountType";

    }
    //uPlan(PlanName,Image,amount,noOfSongsDownload)
    public static class UserPlans implements BaseColumns{
        public static final String TABLE_NAME1="userPlans";
        public static final String COLUMN1_PN="planname";
        public static final String COLUMN2_IM="image";
        public static final String COLUMN3_AM="amount";
        public static final String COLUMN4_DL="downloadlimit";
    }
    public static class ArtistPlans implements BaseColumns{
        public static final String TABLE_NAME2="artistPlans";
        public static final String COLUMN1_PNA="planname";
        public static final String COLUMN2_IMA="image";
        public static final String COLUMN3_AMA="amount";
        public static final String COLUMN4_DLA="downloadlimit";
        public static final String COLUMN5_ULA="uploadlimit";
    }
    public static class PremiumUsers implements BaseColumns{
        public static final String TABLE_NAME3="premiumUsers";
        public static final String COLUMN1_UN="username";
        public static final String COLUMN2_PT="plantype";
    }
    public static class ArtistUsers implements BaseColumns{
        public static final String TABLE_NAME4="artistUsers";
        public static final String COLUMN1_UN="username";
        public static final String COLUMN2_PT="plantype";
    }
    public static  class Payments implements BaseColumns{
        public static final String TABLE_NAME5="payments";
        public static final String COLUMN1_UN="username";
        public static final String COLUMN2_AM="amount";
        public static final String COLUMN3_DT="date";
        public static final String COLUMN4_PN="planname";
    }
}
