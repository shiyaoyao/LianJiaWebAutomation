package appobjects.web.bases;

public interface IRTEToolbarStrings {

	// Toolbar icon strings
	public static final String UNDO_ICON = "undo";
	public static final String BOLD_ICON = "bold";
	public static final String ITALIC_ICON = "italic";
	public static final String UNDERLINE_ICON = "underline";
	public static final String STRIKETHROUGH_ICON = "strikethrough";
	public static final String BACKGROUNDCOLOR_ICON = "showBkColorPicker";
	public static final String INSERTTABLE_ICON = "insertTable";
	public static final String INDENT_ICON = "indent";
	public static final String OUTDENT_ICON = "outdent";
	public static final String UNORDERDLIST_ICON = "insertunorderedlist";
	public static final String ORDEREDLIST_ICON = "insertorderedlist";
	public static final String HORIZONTALRULE_ICON = "inserthorizontalrule";
	public static final String INSERTIMAGE_ICON = "insertImage";
	public static final String SHOW_INSERTIMAGE_MENU="showImageMenus";
	
	public static final String[] TEXTCOLORS = {"#000000", "#000099", "#0000CC", "#0000FF", 
		"#009900", "#009999", "#0099CC", "#0099FF", 
		"#00CC00", "#00CC99", "#00CCCC", "#00CCFF", 
		"#00FF00", "#00FF99", "#00FFCC", "#00FFFF", 
		"#CCFF00", "#CCFF99", "#CCFFCC", "#CCFFFF", 
		"#999900", "#999999", "#9999CC", "#9999FF",
		"#99CC00", "#99CC99", "#99CCCC", "#99CCFF", 
		"#CC9900", "#CC9999", "#CC99CC", "#CC99FF", 
		"#CCCC00", "#CCCC99", "#CCCCCC", "#CCCCFF", 
		"#990000", "#990099", "#9900CC", "#9900FF", 
		"#CC0000", "#CC0099", "#CC00CC", "#CC00FF", 
		"#FF0000", "#FF3399", "#FF33CC", "#FF00FF", 
		"#FF9900", "#FF9999", "#FF99CC", "#FF99FF", 
		"#FFCC00", "#FFCC99", "#FFCCCC", "#FFCCFF", 
		"#FFFF00", "#FFFF99", "#FFFFCC", "#FFFFFF",
	};
	
	enum FontFamily {
		SANSSERIF ("Sans Serif"),
		SERIF ("Serif"),
		MONOSPACE ("Monospace"),
		ARIAL ("Arial"),
		ARIALBLACK ("Arial Black"),
		GEORGIA ("Georgia"),
		TREBUCHET ("Trebuchet"),
		__UNK ("UNDEFINED");
		
		public final String family;
		
		FontFamily (String label) {
			this.family = label;
		}
		
		public static FontFamily get(String sType) {
			FontFamily[] allTypes = FontFamily.values();
			for(FontFamily e : allTypes) {
				if(e.family.equalsIgnoreCase(sType))
					return e;
			}
			return __UNK;
		}
	}
	
	enum FontSize {
		F8 ("8", "1"),
		F10("10", "2"),
		F12 ("12", "3"),
		F14 ("14", "4"),
		F18 ("18", "5"),
		F24 ("24", "6"),
		F36 ("36", "7"),
		__UNK("UNDEFINE", "-1");
		
		public final String pixels;
		public final String css;
		
		FontSize (String px, String css) {
			this.pixels = px;
			this.css = css;
		}
		
		public static FontSize get(String sSize) {
			FontSize[] allTypes = FontSize.values();
			for(FontSize e : allTypes){
				if(e.pixels.equals(sSize))
					return e;
			}
			return __UNK;
		}
	}
	
	enum Color {
		BLACK ("#000000"),
		WHITE ("#FFFFFF"),
		GRAY ("#999999"),
		AQUA ("#00FFFF"),
		BLUE ("#0000FF"),
		DARKBLUE ("#000099"),
		LIGHTBLUE ("#00CCFF"),
		GREEN ("#00CC00"),
		DARKGREEN ("#009900"),
		LIGHTGREEN ("#00FF00"),
		RED ("#CC0000"),
		DARKRED ("#990000"),
		LIGHTRED ("#FF0000"),
		VIOLET ("#CC99FF"),
		PURPLE ("#CC00CC"),
		DARKPURPLE ("#9900CC"),
		LIGHTPURPLE ("#CC00FF"),
		PINK ("#FF99FF"),
		DARKPINK ("#FF00FF"),
		LIGHTPINK ("#FFCCFF"),
		ORANGE ("#FF9900"),
		LIGHTORANGE ("#FFCC00"),
		YELLOW ("#FFFF00");
		
		public final String hex;
		
		Color (String hexval) {
			this.hex = hexval;
		}
	}
	
	enum Heading {
		H1 ("Heading 1"),
		H2 ("Heading 2"),
		H3 ("Heading 3"),
		H4 ("Heading 4"),
		H5 ("Heading 5"),
		H6 ("Heading 6"),
		__UNK ("UNDEFINED");
		
		public final String size;
		
		Heading (String head) {
			this.size = head;
		}
		
		public static Heading get(String sType) {
			Heading[] allTypes = Heading.values();
			for(Heading e : allTypes) {
				if(e.size.toLowerCase().contains(sType.toLowerCase()))
					return e;
			}
			return __UNK;
		}
	}
	
	enum Align {
		LEFT,
		CENTER,
		RIGHT;
		
		public static Align get(String align) {
			Align[] allTypes = Align.values();
			for(Align e : allTypes) {
				if(e.name().equalsIgnoreCase(align))
					return e;
			}
			return null;
		}
	}
}
