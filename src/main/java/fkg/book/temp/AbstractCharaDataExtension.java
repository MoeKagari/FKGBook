package fkg.book.temp;

import fkg.book.masterdata.CharacterBook;
import fkg.book.masterdata.CharacterLeaderSkillDescription;
import fkg.book.masterdata.CharacterSkill;
import fkg.book.masterdata.CharacterTextResource;
import fkg.book.masterdata.GetMasterData;

import java.util.Optional;

/**
 * @author MoeKagari
 */
public class AbstractCharaDataExtension {
	private final AbstractCharaData chara;

	public AbstractCharaDataExtension(AbstractCharaData chara) {
		this.chara = chara;
	}

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	private String stateString;
	private int imageId = -1;

	/**
	 * fkgbook中 可否显示
	 */
	public boolean canShowInApplication() {
		return this.chara.isCharacter() &&
				this.chara.getId() < 1_0000_0000//仅有三个角色的特殊换装
				;
	}

	/**
	 * 可以做 副团长
	 */
	public boolean canBeSetDeputyLeader() {
		switch(this.chara.getOeb()) {
			case 1:
			case 2:
				return true;
			case 3:
				return this.chara.isKariBloom();
			case 99:
				return false;
		}
		throw new RuntimeException("不可能的OEB : " + this.chara.getOeb());
	}

	public String getStateString() {
		return this.stateString = Optional.ofNullable(this.stateString).orElseGet(() -> {
			switch(this.chara.getOeb()) {
				case 1:
					return "原始";
				case 2:
					return "进化";
				case 3:
					return "开花" + (this.chara.isKariBloom() ? "(假)" : "");
				case 99:
					return String.format("升华(%s)", this.chara.isBloomChara() ? "开花" : "进化");
			}
			throw new RuntimeException("不可能的OEB : " + this.chara.getOeb());
		});
	}

	/**
	 * 最高进化
	 */
	public boolean isMostLevel() {
		AbstractCharaData[] allChara = this.getAllChara();
		for(int index = allChara.length - 1; index >= 0; index--) {
			if(allChara[index] != null) {
				return allChara[index].getId() == this.chara.getId();
			}
		}
		throw new RuntimeException("不可能");
	}

	public int getImageId() {
		if(this.imageId == -1) {
			this.imageId = this.calculateImageId();
		}
		return this.imageId;
	}

	private int calculateImageId() {
		int id = this.chara.getId();
		switch(this.chara.getOeb()) {
			case 1:
			case 2:
				return id;
			case 3:
				return this.chara.isKariBloom() ? (id - 300000 + 1) : id;
			case 99:
				try {
					switch(this.getAllChara()[0].getRarity()) {
						case 1:
						case 2:
						case 3:
						case 4:
							return this.getAllChara()[1].getId();
						case 5:
							if(this.chara.isBloomChara()) {
								return this.chara.isKariBloom() ? (id - 300000) : (id - 1);
							} else {
								throw new RuntimeException("不可能不为 bloomChara");
							}
						case 6:
						default:
							throw new RuntimeException("不可能的rarity : " + this.chara.getRarity());
					}
				} catch(Exception e) {
					throw new RuntimeException(this.chara.getId() + "", e);
				}
			default:
				throw new RuntimeException("不可能的oeb : " + this.chara.getOeb());
		}
	}

	/*-------------------------------------------------------------------------------------------------------------------------------*/

	private AbstractCharaData[] allChara;
	private CharacterSkill skill;
	private CharacterLeaderSkillDescription ability;
	private String introduction;
	private String flowerLanguage;

	public AbstractCharaData[] getAllChara() {
		return this.allChara = Optional.ofNullable(this.allChara)
		                               .orElseGet(() -> {
			                               int o_id = this.chara.getId();
			                               switch(this.chara.getOeb()) {
				                               case 1:
					                               o_id -= 0;
					                               break;
				                               case 2:
					                               o_id -= 1;
					                               break;
				                               case 3:
					                               o_id -= 300000;
					                               break;
				                               case 99:
					                               if(this.chara.isBloomChara()) {
						                               o_id = o_id - 300000 - 1;
					                               } else {
						                               o_id = o_id - 300000;
					                               }
					                               break;
				                               default:
					                               throw new RuntimeException("不可能的oeb : " + this.chara.getOeb());
			                               }
			                               return new AbstractCharaData[]{
					                               GetMasterData.ALL_CHARA.get(o_id),
					                               GetMasterData.ALL_CHARA.get(o_id + 1),
					                               GetMasterData.ALL_CHARA.get(o_id + 300000),
					                               GetMasterData.ALL_CHARA.get(o_id + 300000 + 1)
			                               };
		                               });
	}

	public CharacterSkill getSkill() {
		if(this.skill == null) {
			Optional<CharacterSkill> skillOptional = GetMasterData.MASTERSKILL
					.stream().filter(ms -> ms.id == this.chara.getSkillNumber())
					.findFirst();
			assert skillOptional.isPresent();
			this.skill = skillOptional.get();
		}
		return this.skill;
	}

	public CharacterLeaderSkillDescription getAbility() {
		if(this.ability == null) {
			Optional<CharacterLeaderSkillDescription> abilityOptional = GetMasterData.MASTERLEADERSKILLDESCRIPTION
					.stream().filter(characterLeaderSkillDescription -> characterLeaderSkillDescription.cid == this.chara.getId())
					.findFirst();
			assert abilityOptional.isPresent();
			this.ability = abilityOptional.get();
		}
		return this.ability;
	}

	public String getIntroduction() {
		if(this.introduction == null) {
			Optional<CharacterTextResource> introductionOptional = GetMasterData.MASTERCHARACTERTEXTRESOURCE
					.stream()
					.filter(CharacterTextResource::isIntroduction)
					.filter(ctr -> ctr.getCharacter() == this.getAllChara()[0].getId())
					.findFirst();
			assert introductionOptional.isPresent();
			this.introduction = introductionOptional.get().getText();
		}
		return this.introduction;
	}

	public String getFlowerLanguage() {
		if(this.flowerLanguage == null) {
			Optional<String> flowerLanguageOptional = GetMasterData.MASTERBOOK
					.stream().filter(cb -> cb.getId() == this.chara.getBid())
					.findFirst().map(CharacterBook::getFlowerLanguage);
			assert flowerLanguageOptional.isPresent();
			this.flowerLanguage = flowerLanguageOptional.get();
		}
		return this.flowerLanguage;
	}
}
