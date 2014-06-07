package dev.blacksheep.trif;

public class Consts {
	public static final String BASE_URL = "http://shaunidiot.info/csgoskins/";
	public static final String STEAM_TRADE_URL = "http://steamcommunity.com/tradeoffer/new/?partner=92341746&token=nxFo3Xxo";
	public static final String STEAM_API_KEY = "1163D10860F904250D38289561531CCF";
	public static final String PREFS_NAME = "SharedPreferences";

	public static final String WALLET_ERROR = "wallet_error_calculations";
	public static final String MARKET_ITEM_NOT_FOUND = "market_item_not_found";

	public static final String ITEMS_UPDATE_DATABASE_YES = "UPDATE";
	public static final String ITEMS_UPDATE_DATABASE_NO = "NOUPDATE";
	public static final String ITEMS_UPDATE_DATABASE_LINK = Consts.BASE_URL + "update.php";

	public static final int ITEMS_LOAD_LIMIT = 20;
	public static final String CATEGORY_URL = "http://192.168.2.8/csgobetting_emulator/category.php";
	public static final String DATABASE_URL = Consts.BASE_URL + "db.php";

	public enum ITEM_QUALITY {

		NORMAL("#B2B2B2"), UNIQUE("#FFD700"), VINTAGE("#476291"), GENUINE("#4D7455"), STRANGE("#CF6A32"), UNUSUAL("#8650AC"), HAUNTED("#38F3AB"), COLLECTOR("#AA0000"), COMMUNITY("#70B04A"), SELF_MADE(
				"#70B04A"), VALVE("#A50F79");

		private String type;

		ITEM_QUALITY(String type) {
			this.type = type;
		}

		public String getColor() {
			return type;
		}

	}
}
