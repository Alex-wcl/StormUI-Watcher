package com.weibo.stormUI.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {
	// 查询一个字符串中特定字符串的个数
			public static int getSubtringCount(String string, String subString) {
				String s = String.copyValueOf(string.toCharArray());
				String ToFind = subString;
				int index = 0;
				int count = 0;
				while (index != -1) {
					index = s.indexOf(ToFind);
					if (index == -1) {
						break;
					}
					s = s.substring(index + ToFind.length());
					count++;

				}
				return count;

			}

			// 查询一个字符串中特定字符串第n次出现的索引
			public static int getSubtringIndex(String _string, String subString, int n) {
				String string = String.copyValueOf(_string.toCharArray());
				Matcher slashMatcher = Pattern.compile("\\" + subString).matcher(string);
				int mIdx = 0;
				while (slashMatcher.find()) {
					mIdx++;
					// 当"/"符号第n次出现的位置
					if (mIdx == n) {
						break;
					}
				}
				return slashMatcher.start();
			}
}
