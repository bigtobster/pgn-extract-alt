package com.bigtobster.pgnextractalt.core;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * Prompt for Shell
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("RefusedBequest")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PromptProvider extends DefaultPromptProvider
{

	private static final String PROMPT = "pgn-extract-alt>";

	@SuppressWarnings("MethodReturnAlwaysConstant")
	@Override
	public String getPrompt()
	{
		return PromptProvider.PROMPT;
	}

}
