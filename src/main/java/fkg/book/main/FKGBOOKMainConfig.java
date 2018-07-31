package fkg.book.main;

public class FKGBOOKMainConfig {
	public final static int DEFAULT_DEPUTYLEADERID = 110813;
	/** 全CG */
	private boolean useAllcgPatch = false;
	/** 备份masterdata */
	private boolean saveDataForMasterData = false;
	/** 替换副团长 */
	private boolean replaceDeputyLeader = false;
	/** 目标副团长ID */
	private int targetDeputyLeaderId = DEFAULT_DEPUTYLEADERID;
	/** 跳过剧情,总开关 */
	private boolean isSkipEvent = false;
	/** 跳过剧情 */
	private SkipEventConfig skipEventConfig = null;

	public static class SkipEventConfig {
		/** 跳过 表区版Hscene的剧情 */
		private boolean isSkipEventHscene = false;
		/** 跳过 里区版Hscene的剧情 */
		private boolean isSkipEventHsceneR18 = false;
		/** 跳过入手新角色时的介绍剧情 */
		private boolean isSkipEventNew = false;
		/** 跳过某些地图里面战斗中出现的剧情 */
		private boolean isSkipEventPerformance = false;
		/** 跳过地图的剧情 */
		private boolean isSkipEventStory = false;
		/** 跳过引导剧情 */
		private boolean isSkipEventTutorial = false;

		public boolean isSkipEventHscene() {
			return this.isSkipEventHscene;
		}

		public void setSkipEventHscene(boolean skipEventHscene) {
			this.isSkipEventHscene = skipEventHscene;
		}

		public boolean isSkipEventHsceneR18() {
			return this.isSkipEventHsceneR18;
		}

		public void setSkipEventHsceneR18(boolean skipEventHsceneR18) {
			this.isSkipEventHsceneR18 = skipEventHsceneR18;
		}

		public boolean isSkipEventNew() {
			return this.isSkipEventNew;
		}

		public void setSkipEventNew(boolean skipEventNew) {
			this.isSkipEventNew = skipEventNew;
		}

		public boolean isSkipEventPerformance() {
			return this.isSkipEventPerformance;
		}

		public void setSkipEventPerformance(boolean skipEventPerformance) {
			this.isSkipEventPerformance = skipEventPerformance;
		}

		public boolean isSkipEventStory() {
			return this.isSkipEventStory;
		}

		public void setSkipEventStory(boolean skipEventStory) {
			this.isSkipEventStory = skipEventStory;
		}

		public boolean isSkipEventTutorial() {
			return this.isSkipEventTutorial;
		}

		public void setSkipEventTutorial(boolean skipEventTutorial) {
			this.isSkipEventTutorial = skipEventTutorial;
		}
	}

	public boolean isSkipEvent() {
		return this.isSkipEvent;
	}

	public void setSkipEvent(boolean skipEvent) {
		this.isSkipEvent = skipEvent;
	}

	public boolean isUseAllcgPatch() {
		return this.useAllcgPatch;
	}

	public void setUseAllcgPatch(boolean useAllcgPatch) {
		this.useAllcgPatch = useAllcgPatch;
	}

	public boolean isReplaceDeputyLeader() {
		return this.replaceDeputyLeader;
	}

	public void setReplaceDeputyLeader(boolean replaceDeputyLeader) {
		this.replaceDeputyLeader = replaceDeputyLeader;
	}

	public int getTargetDeputyLeaderId() {
		return this.targetDeputyLeaderId;
	}

	public void setTargetDeputyLeaderId(int targetDeputyLeaderId) {
		this.targetDeputyLeaderId = targetDeputyLeaderId;
	}

	public SkipEventConfig getSkipEventConfig() {
		if (this.skipEventConfig == null) {
			this.skipEventConfig = new SkipEventConfig();
		}
		return this.skipEventConfig;
	}

	public void setSkipEventConfig(SkipEventConfig skipEventConfig) {
		this.skipEventConfig = skipEventConfig;
	}

	public boolean isSaveDataForMasterData() {
		return this.saveDataForMasterData;
	}

	public void setSaveDataForMasterData(boolean saveDataForMasterData) {
		this.saveDataForMasterData = saveDataForMasterData;
	}
}
