package fkg.other.home.chara;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fkg.gui.jfx.other.CharacterData;
import fkg.masterdata.CharacterMypageVoiceResource;
import fkg.masterdata.CharacterMypageVoiceResourceGroup;
import fkg.masterdata.GetMasterData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

public class CharacterResource {
	final CharacterData characterData;
	private final List<CharacterMypageVoiceResourceGroup> cmvrgs = new ArrayList<>();
	private final Map<Integer, CharacterMypageVoiceResource> cmvrs = new HashMap<>();

	public CharacterResource(int id) {
		this.characterData = CharacterData.ALL_CHARA.get(id);
		GetMasterData.MASTERCHARACTERMYPAGEVOICERESOURCEGROUP.stream()
				.forEach(cmvrg -> {
					if (cmvrg.characterId == id) {
						this.cmvrgs.add(cmvrg);
					}
				});
		GetMasterData.MASTERCHARACTERMYPAGEVOICERESOURCE.stream()
				.forEach(cmvr -> {
					if (cmvr.characterId == id) {
						this.cmvrs.put(cmvr.id, cmvr);
					}
				});
	}

	public Image getDefaultHomeImage() {
		return this.characterData.getHome(0);
	}

	public RandomHomeCharacter getRandomHomeCharacter(CharacterMypageVoiceResourceGroup old) {
		CharacterMypageVoiceResourceGroup cmvrg;

		int size = this.cmvrgs.size();
		Random random = new Random();
		do {//不重复
			cmvrg = this.cmvrgs.get(random.nextInt(size));
		} while (old != null && old == cmvrg);

		return new RandomHomeCharacter(cmvrg);
	}

	public class RandomHomeCharacter {
		public final CharacterMypageVoiceResourceGroup cmvrg;
		public final ImageView imageView;
		public final AudioClip audio;

		public RandomHomeCharacter(CharacterMypageVoiceResourceGroup cmvrg) {
			this.cmvrg = cmvrg;
			this.imageView = new ImageView(CharacterResource.this.characterData.getHome(this.cmvrg.character_expression_type_id));
			this.audio = CharacterResource.this.characterData.getVoice(CharacterResource.this.cmvrs.get(cmvrg.voice_resource_id).voice_file);
		}
	}
}
