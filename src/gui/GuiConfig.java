package gui;

public class GuiConfig {

	private static boolean allCG = false;
	private static boolean tihuan = false;
	private static int futuanzhangID = 169702;

	public static boolean isAllCG() {
		return allCG;
	}

	public static void setAllCG(boolean allCG) {
		GuiConfig.allCG = allCG;
	}

	public static boolean isTihuan() {
		return tihuan;
	}

	public static void setTihuan(boolean tihuan) {
		GuiConfig.tihuan = tihuan;
	}

	public static int getFutuanzhangID() {
		return futuanzhangID;
	}

	public static void setFutuanzhangID(int futuanzhangID) {
		GuiConfig.futuanzhangID = futuanzhangID;
	}

}
