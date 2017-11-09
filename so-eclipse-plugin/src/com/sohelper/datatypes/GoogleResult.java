package com.sohelper.datatypes;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a response from Google search.
 */
@Getter
@Setter
public class GoogleResult {
	private String title;
	private URL url;
	private String source;
}
