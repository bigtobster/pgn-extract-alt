package com.bigtobster.pgnextractalt.core;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * Manages the history of the commands
 *
 * @author Toby Leheup (Bigtobster)
 */
@SuppressWarnings("RefusedBequest")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HistoryFileNameProvider extends DefaultHistoryFileNameProvider
{

	private static final String LOG_FILENAME = "history.log";

	@SuppressWarnings("MethodReturnAlwaysConstant")
	@Override
	public String getHistoryFileName()
	{
		return HistoryFileNameProvider.LOG_FILENAME;
	}

}
