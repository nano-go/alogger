package com.nano.logger.util;

import java.util.ArrayList;

public class PrettyTable {
	
	private static final String BORDER_LEFT_TOP    = "┌" ;
	private static final String BORDER_LEFT_MIDDLE = "├" ;
	private static final String BORDER_LEFT        = "│" ;
	private static final String BORDER_LEFT_BOTTOM = "└" ;
	private static final String DOUBLE_DIVIDER     = "────────────────────────────────────────────────────────────────";
	private static final String DOTTED_DIVIDER     = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";

	private static final String BOTTOM_DIVIDER     = BORDER_LEFT_BOTTOM + DOUBLE_DIVIDER + DOUBLE_DIVIDER ;
	private static final String MIDDLE_DIVIDER     = BORDER_LEFT_MIDDLE + DOTTED_DIVIDER + DOTTED_DIVIDER ;
	private static final String TOP_DIVIDER        = BORDER_LEFT_TOP + DOUBLE_DIVIDER + DOUBLE_DIVIDER ;
	
	private ArrayList<Section> sections ;

	public PrettyTable() {
		sections = new ArrayList<>(3) ;
	}

	public Section addNewSection() {
		Section newSection = new Section() ;
		sections.add(newSection) ;
		return newSection ;
	}

	public Section getSectionAt(int sectionIndex) {
		return sections.get(sectionIndex) ;
	}
	
	public void clear() {
		sections.clear() ;
	}

	public String toString(boolean clear) {
		if(sections.size() == 0) {
			return "" ;
		}
		StringBuilder table = new StringBuilder() ;
		table.append(TOP_DIVIDER) ;
		int iMax = sections.size() - 1 ;
		for(int i = 0; ; i ++) {
			String[] lines = sections.get(i).toString().split(System.lineSeparator()) ;
			for(String line : lines) {
				table.append('\n').append(BORDER_LEFT).append(line) ;
			}
			if(i == iMax) {
				break ;
			}
			table.append('\n').append(MIDDLE_DIVIDER) ;
		}
		table.append('\n').append(BOTTOM_DIVIDER) ;
		if(clear) clear() ;
		return table.toString() ;
	}

	@Override
	public String toString() {
		return toString(false) ;
	}
	
	public static class Section {
		StringBuilder content ;
		public Section() {
			content = new StringBuilder() ;
		}

		public void appendNewLine(CharSequence line) {
			if(content.length() != 0){
				content.append('\n') ;
			}
			content.append(line) ;
		}

		public void append(CharSequence s) {
			content.append(s) ;
		}

		@Override
		public String toString() {
			return content.toString();
		}
	}
}
