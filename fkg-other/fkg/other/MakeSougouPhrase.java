package fkg.other;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.stream.Collectors;

import fkg.masterdata.CharacterInformation;
import fkg.masterdata.GetMasterData;
import net.sourceforge.pinyin4j.PinyinHelper;
import tool.function.FunctionUtils;

public class MakeSougouPhrase {
	public static void main(String[] args) {
		try (PrintStream pw = new PrintStream(new FileOutputStream("aaa"))) {
			System.setOut(pw);
			GetMasterData.MASTERCHARACTER.stream()
					.filter(cd -> cd.getOeb() == 1)//
					.flatMap(cd -> Arrays.asList(new Phrase(cd, true), new Phrase(cd, false)).stream())//
					.filter(Phrase::need)//
					.collect(Collectors.groupingBy(p -> p.str)).forEach((str, strResult) -> {
						strResult.sort((a, b) -> {
							int flag = Integer.compare(a.id, b.id);
							if (flag == 0) {
								flag = a.chinese ? -1 : 1;
							}
							return flag;
						});
						FunctionUtils.forEachUseIndex(strResult, (index, phrase) -> {
							System.out.println(String.format("%s,%d=%s", phrase.str, index + 1, phrase.target));
						});
					});;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class Phrase {
		private final String str;
		private final String target;

		private final boolean chinese;
		private final int id;

		public Phrase(CharacterInformation data, boolean chinese) {
			this.chinese = chinese;
			this.id = data.getId();

			this.str = Phrase.parse(data.getChineseName());
			this.target = chinese ? data.getChineseName() : data.getName();
		}

		public boolean need() {
			return this.str != null;
		}

		private static String parse(String chineseName) {
			if (chineseName == null) return null;
			StringBuilder result = new StringBuilder();
			String chineseNameWithoutVersion = FunctionUtils.ifFunction(chineseName.indexOf('('), index -> index != -1, index -> chineseName.substring(0, index), chineseName);
			for (int index = 0; index < chineseNameWithoutVersion.length(); index++) {
				Arrays.stream(PinyinHelper.toHanyuPinyinStringArray(chineseNameWithoutVersion.charAt(index))).limit(1).forEach(pinyin -> {
					result.append(pinyin.charAt(0));
				});
			}
			return result.toString();
		}
	}
}
