package fkg.gui.jfx.other.filter;

import java.util.function.Predicate;

import fkg.gui.jfx.other.CharacterData;

public interface Filter extends Predicate<CharacterData> {
	public boolean filter(CharacterData cd);

	@Override
	default boolean test(CharacterData cd) {
		return filter(cd);
	}
}
