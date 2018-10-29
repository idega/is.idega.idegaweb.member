package is.idega.idegaweb.member.util;

import com.idega.user.data.GroupTypeConstants;
import com.idega.user.util.ICUserConstants;
import com.idega.util.CoreConstants;

/**
 * A collection of the static variables used in the member system like group type names.
 *
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 *
 */

// uses the constants in GroupTypeConstants and ICUserConstants to declare the constants in this class
public class IWMemberConstants {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	public static final String REQUEST_PARAMETER_SELECTED_GROUP_ID = "r_sel_gr_id";

	public static final String APPLICATION_PARAMETER_ADMINISTRATOR_MAIN_EMAIL = CoreConstants.PROP_SYSTEM_MAIL_FROM_ADDRESS;
	public static final String APPLICATION_PARAMETER_MAIL_SERVER = CoreConstants.PROP_SYSTEM_SMTP_MAILSERVER;

	public static final String GROUP_RELATION_TYPE_PARENT = "GROUP_PARENT";

	public static final String META_DATA_DIVISION_LEAGUE_CONNECTION ="CLUBDIV_CONN";
	public static final String META_DATA_DIVISION_BOARD = "CLUBDIV_BOARD";
	public static final String META_DATA_DIVISION_NUMBER ="CLUBDIV_NUMBER";
	public static final String META_DATA_DIVISION_SSN = "CLUBDIV_SSN";
	public static final String META_DATA_DIVISION_FOUNDED = "CLUBDIV_FOUNDED";

	public static final String META_DATA_CLUB_LEAGUE_CONNECTION = "CLUBINFO_CONN";
	public static final String META_DATA_CLUB_USING_SYSTEM = "CLUBINFO_SYSTEM";
	public static final String META_DATA_CLUB_OPERATION = "CLUBINFO_OPERATION";
	public static final String META_DATA_CLUB_STATUS = "CLUBINFO_STATUS";
	public static final String META_DATA_CLUB_MAKE = "CLUBINFO_MAKE";
	public static final String META_DATA_CLUB_IN_UMFI = "CLUBINFO_MEMBER";
	public static final String META_DATA_CLUB_TYPE = "CLUBINFO_TYPE";
	public static final String META_DATA_CLUB_FOUNDED = "CLUBINFO_FOUNDED";

	public static final String META_DATA_CLUB_SSN = "CLUBINFO_SSN";
	public static final String META_DATA_CLUB_NUMBER = ICUserConstants.META_DATA_GROUP_NUMBER;

	public static final String META_DATA_CLUB_STATUS_MULTI_DIVISION_CLUB = "1";
	public static final String META_DATA_CLUB_STATUS_SINGLE_DIVISION_CLUB = "2";
	public static final String META_DATA_CLUB_STATUS_NO_MEMBERS_CLUB = "3";


	public static final String META_DATA_CLUB_STATE_INACTIVE = "INACTIVE";
	public static final String META_DATA_CLUB_STATE_ACTIVE = "ACTIVE";
	public static final String META_DATA_CLUB_STATE_COMPETITION_BAN = "COMP_BAN";

	public static final String META_DATA_CLUB_USING_NETBOKHALD = "CLUBINFO_NETBOKHALD";


	public static final String META_DATA_USER_CLUB_MEMBER_NUMBER_PREFIX = "CLUB_MEMB_NR_";//suffix with club id (group)

	public static final String GROUP_TYPE_ALIAS = GroupTypeConstants.GROUP_TYPE_ALIAS;

	public static final String GROUP_TYPE_GENERAL = GroupTypeConstants.GROUP_TYPE_GENERAL;
	public static final String GROUP_TYPE_FEDERATION = "iwme_federation";
	public static final String GROUP_TYPE_FEDERATION_STAFF = "iwme_federation_staff";
	public static final String GROUP_TYPE_FEDERATION_COMMITTEE = "iwme_federation_committee";
	public static final String GROUP_TYPE_UNION = "iwme_union";
	public static final String GROUP_TYPE_UNION_STAFF = "iwme_union_staff";
	public static final String GROUP_TYPE_UNION_COMMITTEE = "iwme_union_committee";
	public static final String GROUP_TYPE_REGIONAL_UNION = "iwme_regional_union";
	public static final String GROUP_TYPE_REGIONAL_UNION_STAFF = "iwme_regional_union_staff";
	public static final String GROUP_TYPE_REGIONAL_UNION_COMMITTEE = "iwme_regional_union_committee";
	public static final String GROUP_TYPE_REGIONAL_UNION_COLLECTION = "iwme_regional_union_collection";
	public static final String GROUP_TYPE_LEAGUE = GroupTypeConstants.GROUP_TYPE_LEAGUE;
	public static final String GROUP_TYPE_LEAGUE_STAFF = "iwme_league_staff";
	public static final String GROUP_TYPE_LEAGUE_COMMITTEE = "iwme_league_committee";
	public static final String GROUP_TYPE_LEAGUE_COLLECTION = "iwme_league_collection";
	public static final String GROUP_TYPE_LEAGUE_CLUB_COLLECTION = "iwme_league_club_collection";
	public static final String GROUP_TYPE_LEAGUE_CLUB_DIVISION = "iwme_league_club_division";
	public static final String GROUP_TYPE_CLUB = GroupTypeConstants.GROUP_TYPE_CLUB;
	public static final String GROUP_TYPE_CLUB_MEMBER = "iwme_club_member";
	public static final String GROUP_TYPE_CLUB_PLAYER = "iwme_club_player";
	public static final String GROUP_TYPE_CLUB_PLAYER_TEMPLATE = "iwme_club_player_template";
	public static final String GROUP_TYPE_CLUB_TRAINER = "iwme_club_trainer";
	public static final String GROUP_TYPE_CLUB_STAFF = "iwme_club_staff";
	public static final String GROUP_TYPE_CLUB_COMMITTEE = "iwme_club_committee";
	public static final String GROUP_TYPE_CLUB_COMMITTEE_MAIN = "iwme_club_main_committee";
	public static final String GROUP_TYPE_CLUB_DIVISION = "iwme_club_division";
	public static final String GROUP_TYPE_CLUB_DIVISION_INNER = "iwme_club_division_inner";
	public static final String GROUP_TYPE_GROUP_INNER = "iwme_group_inner";
	public static final String GROUP_TYPE_CLUB_PLAYER_INNER = "iwme_club_player_inner";
	public static final String GROUP_TYPE_UNION_TRAINER = "iwme_union_trainer";
	public static final String GROUP_TYPE_CLUB_DIVISION_TRAINER = "iwme_club_division_trainer";
	public static final String GROUP_TYPE_CLUB_DIVISION_STAFF = "iwme_club_division_staff";
	public static final String GROUP_TYPE_CLUB_DIVISION_COMMITTEE = "iwme_club_division_committee";
	public static final String GROUP_TYPE_CLUB_DIVISION_TEMPLATE = "iwme_club_division_template";
	public static final String GROUP_TYPE_TEMPORARY = "iwme_temporary";
	public static final String GROUP_TYPE_CLUB_PRACTICE_PLAYER = "iwme_club_practice_player";

	//	Board roles
	public static final String MEMBER_BOARD_CHAIR_MAN = "STAT_B_CHAIR"; //"chairman"; //Formaður
	public static final String MEMBER_BOARD_VICE_CHAIRMAN = "STAT_B_VICECHAIR"; //"vice_chairman"; //Varaformaður
	public static final String MEMBER_CASHIER =  "STAT_B_CASH"; //"cashier"; //Gjaldkeri
	public static final String MEMBER_SECRETARY = "STAT_B_SECR"; //"secretary"; //Ritari
	public static final String MEMBER_BOARD_MEMBER = "STAT_B_MAINBOARD"; //"board_member"; //Aðalstjórn
	public static final String MEMBER_EXTRA_BOARD = "STAT_B_EXTRABOARD"; //"extra_board"; //Varastjórn
	public static final String MEMBER_STAND_IN = "STAT_B_EXTRA"; //"stand_in"; //Varamaður
	public static final String MEMBER_CO_CHIEF = "STAT_B_COCHIEF"; //"co-chief"; //Meðstjórnandi
	public static final String MEMBER_CEO = "STAT_B_CEO"; //"ceo"; //Framkvæmdastjóri
	public static final String MEMBER_PRESIDENT = "STAT_B_PRES"; //"president"; //Forseti
	public static final String MEMBER_VICE_PRESIDENT = "STAT_B_VICEPRES"; //"vice_president"; //Varaforseti

	//	Trainer roles
	public static final String STATUS_COACH = "STAT_COACH"; //Þjálfari
	public static final String STATUS_ASSISTANT_COACH = "STAT_ASSCOACH"; //Aðstoðarþjálfari
	public static final String STAT_CHIEF_TRAINER = "STAT_CHIEF_TRAINER"; //Yfirþjálfari

	// Other roles
	public static final String STAT_CHEF_DE_MISSION = "STAT_CHEF_DE_MISSION"; //Aðalfararstjóri
	public static final String STAT_DEPUTY_CDM = "STAT_DEPUTY_CDM"; //Aðstoðarfararstjóri
	public static final String STAT_REF = "STAT_REF"; //Dómari
	public static final String STAT_GUIDE = "STAT_GUIDE"; //Fararstjóri
	public static final String STAT_TEAM_MANAGER = "STAT_TEAM_MANAGER"; //Flokksstjóri
	public static final String STAT_EX_COM = "STAT_EX_COM"; //Framkvæmdastjórn
	public static final String SPORTS_REPRESENTATIVE = "SPORTS_REPRESENTATIVE"; //Íþróttafulltrúi
	public static final String STAT_COMP = "STAT_COMP"; //Keppandi
	public static final String STAT_TEAM_OFFICIAL = "STAT_TEAM_OFFICIAL"; //Liðsstjóri
	public static final String STAT_DOCTOR = "STAT_DOCTOR"; //Læknir
	public static final String STAT_PSYCHOLOG = "STAT_PSYCHOLOG"; //Sálfræðingur
	public static final String STAT_SPORT_MASSAGE = "STAT_SPORT_MASSAGE"; //Sjúkranuddari or Nuddari
	public static final String STAT_PHYSIOTHERAPIST = "STAT_PHYSIOTHERAPIST"; //Sjúkraþjálfari
	public static final String STAT_REGION_MANAGER = "STAT_REGION_MANAGER"; //Sviðsstjóri
	public static final String STAT_PROJECT_MANGAGER = "STAT_PROJECT_MANGAGER"; //Verkefnastjóri
	public static final String STAT_EMPL = "STAT_EMPL"; //Starfsmaður
	public static final String STAT_ASSISTANT = "STAT_ASSISTANT"; //Aðstoðarmaður
	public static final String STAT_GUEST = "STAT_GUEST"; //Gestur
	public static final String STAT_OFFICE_MANAGER = "STAT_OFFICE_MANAGER"; //Skrifstofustjóri
	public static final String STAT_YOUNG_AMBASSADOR = "STAT_YOUNG_AMBASSADOR"; //Ungur sendiherra
	public static final String STAT_YOUNG_CHANGE_MAKER = "STAT_YOUNG_CHANGE_MAKER"; //Ungur áhrifavaldur


	public static final String ORDER_BY_NAME = "name_order";
	public static final String ORDER_BY_GROUP_PATH = "group_path_order";
	public static final String ORDER_BY_GROUP_TYPE = "group_type_order";
	public static final String ORDER_BY_DATE_OF_BIRTH = "date_of_birth_order";
	public static final String ORDER_BY_USER_STATUS = "user_status_order";
	public static final String ORDER_BY_ADDRESS = "address_order";
	public static final String ORDER_BY_POSTAL_ADDRESS = "postal_address_order";
	public static final String ORDER_BY_POSTAL_CODE = "postal_code_order";
	public static final String ORDER_BY_GROUP_NAME = "group_name_order";
	public static final String ORDER_BY_ENTRY_DATE = "entry_date_order";

  public static final String[] STATUS = {MEMBER_BOARD_CHAIR_MAN,MEMBER_BOARD_VICE_CHAIRMAN,MEMBER_CASHIER,MEMBER_SECRETARY,MEMBER_BOARD_MEMBER,MEMBER_EXTRA_BOARD,MEMBER_STAND_IN,MEMBER_CO_CHIEF,MEMBER_CEO,MEMBER_PRESIDENT,MEMBER_VICE_PRESIDENT};
}
